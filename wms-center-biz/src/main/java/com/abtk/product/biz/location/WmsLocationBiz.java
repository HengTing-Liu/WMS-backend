package com.abtk.product.biz.location;

import com.abtk.product.api.domain.request.location.GridPositionRequest;
import com.abtk.product.api.domain.request.location.WmsLocationBatchCreateRequest;
import com.abtk.product.api.domain.request.location.WmsLocationBatchRequest;
import com.abtk.product.api.domain.request.location.WmsLocationGridUpdateRequest;
import com.abtk.product.api.domain.request.location.WmsLocationRequest;
import com.abtk.product.api.domain.request.location.WmsLocationTreeRequest;
import com.abtk.product.api.domain.response.location.LocationCodeSuggestion;
import com.abtk.product.api.domain.response.location.WmsLocationOccupancyResponse;
import com.abtk.product.api.domain.response.location.WmsLocationResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.PageUtil;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.dao.entity.WmsLocation;
import com.abtk.product.dao.entity.WmsLocationGridConfig;
import com.abtk.product.domain.converter.WmsLocationConverter;
import com.abtk.product.service.location.service.WmsLocationGridConfigService;
import com.abtk.product.service.location.service.WmsLocationService;
import com.abtk.product.service.redis.service.RedisService;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.system.service.I18nService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static com.abtk.product.common.utils.PageUtils.startPage;

/**
 * 库位档案业务层
 * 复杂业务逻辑在此层处理，Service层只做简单CRUD
 *
 * @author wms
 * @since 2026-03-14
 */
@Slf4j
@Component
public class WmsLocationBiz {

    @Autowired
    private WmsLocationService wmsLocationService;

    @Autowired
    private WmsLocationGridConfigService gridConfigService;

    @Autowired
    private I18nService i18nService;

    @Autowired
    private RedisService redisService;

    private static final String CACHE_KEY_PREFIX = "wms:location:";
    private static final String CACHE_KEY_TREE = CACHE_KEY_PREFIX + "tree:";
    private static final String CACHE_KEY_OCCUPANCY = CACHE_KEY_PREFIX + "occupancy:";
    private static final long CACHE_EXPIRE_MINUTES = 30;

    // 缓存空值的过期时间（分钟）- 用于防止缓存穿透
    private static final long CACHE_NULL_EXPIRE_MINUTES = 5;
    // 空值缓存的标记
    private static final String CACHE_NULL_VALUE = "__NULL__";

    /**
     * 库位编码前缀映射（按库位类型的中文名确定前缀）
     * 与 locationType 枚举保持一致：STORAGE/PICK/COLLECT/RETURN
     */
    private static final java.util.Map<String, String> LOCATION_TYPE_CODE_PREFIX = new java.util.LinkedHashMap<>();
    static {
        LOCATION_TYPE_CODE_PREFIX.put("STORAGE", "S");   // 存储区
        LOCATION_TYPE_CODE_PREFIX.put("PICK", "P");      // 拣货区
        LOCATION_TYPE_CODE_PREFIX.put("COLLECT", "C");  // 集货区
        LOCATION_TYPE_CODE_PREFIX.put("RETURN", "R");   // 退货区
        LOCATION_TYPE_CODE_PREFIX.put("AREA", "A");     // 库区
        LOCATION_TYPE_CODE_PREFIX.put("SHELF", "F");    // 货架
        LOCATION_TYPE_CODE_PREFIX.put("ROW", "RW");     // 行
        LOCATION_TYPE_CODE_PREFIX.put("COL", "CL");     // 列
        LOCATION_TYPE_CODE_PREFIX.put("CELL", "E");     // 格
        LOCATION_TYPE_CODE_PREFIX.put("FREEZER", "FZ"); // 冰箱
        LOCATION_TYPE_CODE_PREFIX.put("LAYER", "L");    // 层
        LOCATION_TYPE_CODE_PREFIX.put("BOX", "B");      // 盒
        LOCATION_TYPE_CODE_PREFIX.put("CONTAINER", "CN"); // 容器
    }

    // ==================== 基础CRUD方法 ====================

    /**
     * 分页查询
     */
    public TableDataInfo<WmsLocationResponse> list(WmsLocationRequest request) {
        startPage();
        WmsLocation condition = WmsLocationConverter.INSTANCE.requestToEntity(request);
        List<WmsLocation> list = wmsLocationService.queryByCondition(condition);
        return PageUtil.convertPage(list, WmsLocationConverter.INSTANCE::entityToResponseWithOccupancy);
    }

    /**
     * 通过ID查询
     */
    public R<WmsLocationResponse> queryById(Long id) {
        WmsLocation entity = wmsLocationService.queryById(id);
        if (entity == null) {
            return R.fail(i18nService.getMessage("wms.location.not.found"));
        }
        return R.ok(WmsLocationConverter.INSTANCE.entityToResponseWithOccupancy(entity));
    }

    /**
     * 新增数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<WmsLocationResponse> add(WmsLocationRequest request) {
        // 参数校验：检查上级节点
        R<Void> validateResult = validateAndFillParentInfo(request);
        if (validateResult != null) {
            return R.fail(validateResult.getMsg());
        }

        // 生成排序号和全路径名称
        generateSortNoAndFullpath(request);

        // 使用 Converter 转换
        WmsLocation entity = WmsLocationConverter.INSTANCE.requestToEntity(request);
        fillSystemFields(entity);

        wmsLocationService.insert(entity);
        clearLocationCache();

        return R.ok(WmsLocationConverter.INSTANCE.entityToResponseWithOccupancy(entity));
    }

    /**
     * 编辑数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Integer> update(WmsLocationRequest request) {
        if (request.getId() == null) {
            return R.fail(i18nService.getMessage("wms.location.id.required"));
        }

        WmsLocation existing = wmsLocationService.queryById(request.getId());
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.location.not.found"));
        }

        // 如果修改了名称，需要更新全路径名称
        if (request.getLocationName() != null && !request.getLocationName().equals(existing.getLocationName())) {
            request.setLocationFullpathName(generateFullpathName(request, existing.getParentName()));
        }

        WmsLocation entity = WmsLocationConverter.INSTANCE.requestToEntity(request);
        entity.setUpdateBy(SecurityUtils.getUsername());
        entity.setUpdateTime(new Date());

        int result = wmsLocationService.update(entity);
        clearLocationCache();

        return R.ok(result);
    }

    /**
     * 删除数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteById(Long id) {
        WmsLocation existing = wmsLocationService.queryById(id);
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.location.not.found"));
        }

        // 检查是否有子节点
        List<WmsLocation> children = wmsLocationService.queryByParentId(id);
        if (!children.isEmpty()) {
            return R.fail(i18nService.getMessage("wms.location.has.children"));
        }

        boolean result = wmsLocationService.logicDeleteById(id, SecurityUtils.getUsername());
        clearLocationCache();

        return R.ok(result);
    }

    /**
     * 递归删除（删除节点及其所有子节点）
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteRecursive(Long id) {
        WmsLocation existing = wmsLocationService.queryById(id);
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.location.not.found"));
        }

        wmsLocationService.logicDeleteRecursive(id, SecurityUtils.getUsername());
        clearLocationCache();

        return R.ok(true);
    }

    /**
     * 批量删除
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return R.fail(i18nService.getMessage("batch.delete.list.empty"));
        }

        if (ids.size() > 1000) {
            return R.fail(i18nService.getMessage("batch.delete.limit.exceed"));
        }

        try {
            String currentUser = SecurityUtils.getUsername();
            wmsLocationService.logicDeleteBatchByIds(ids, currentUser);
            clearLocationCache();

            return R.ok(true, i18nService.getMessage("batch.delete.success", ids.size()));
        } catch (Exception e) {
            return R.fail(i18nService.getMessage("batch.delete.error", e.getMessage()));
        }
    }

    // ==================== 自动编码建议 ====================

    /**
     * 建议库位编码
     * 根据父节点和库位类型，自动生成下一个可用编码
     *
     * @param warehouseCode 仓库编码
     * @param parentId     父节点ID（null 表示根节点）
     * @param locationType 库位类型（用于确定前缀）
     * @return 编码建议
     */
    public R<LocationCodeSuggestion> suggestCode(String warehouseCode, Long parentId, String locationType) {
        LocationCodeSuggestion suggestion = new LocationCodeSuggestion();
        suggestion.setSerialLength(4);

        // 1. 查询父节点信息
        WmsLocation parent = null;
        String parentCode = "";
        String fullPath = "";
        int currentLevel = 1;
        String parentSortNo = "";

        if (parentId != null) {
            parent = wmsLocationService.queryById(parentId);
            if (parent == null) {
                return R.fail(i18nService.getMessage("wms.location.parent.not.found"));
            }
            parentCode = parent.getLocationNo() != null ? parent.getLocationNo() : "";
            fullPath = parent.getLocationFullpathName() != null ? parent.getLocationFullpathName() : "";
            parentSortNo = parent.getLocationSortNo() != null ? parent.getLocationSortNo() : "";
            currentLevel = parent.getLocationLevel() + 1;
        }

        // 2. 确定类型前缀
        String typePrefix = LOCATION_TYPE_CODE_PREFIX.getOrDefault(locationType, "L");

        // 3. 查询同级的最大序号
        int nextSerial = 1;
        int currentMaxSerial = 0;

        List<WmsLocation> siblings;
        if (parentId != null) {
            siblings = wmsLocationService.queryByParentId(parentId);
        } else {
            // 根节点：查询该仓库下 parent_id 为 null 的节点
            siblings = wmsLocationService.queryRootNodes().stream()
                    .filter(r -> warehouseCode.equals(r.getWarehouseCode()))
                    .collect(Collectors.toList());
        }

        if (siblings != null && !siblings.isEmpty()) {
            // 计算当前同级的最大序号
            // 提取 locationNo 中的数字部分进行比较
            for (WmsLocation sibling : siblings) {
                String no = sibling.getLocationNo();
                if (no != null && !no.isEmpty()) {
                    // 提取编码末尾的数字部分
                    int serial = extractLastSerial(no);
                    if (serial > currentMaxSerial) {
                        currentMaxSerial = serial;
                    }
                }
            }
            nextSerial = currentMaxSerial + 1;
        }

        // 4. 生成建议编码
        String suggestedCode = typePrefix + String.format("%0" + suggestion.getSerialLength() + "d", nextSerial);

        // 5. 设置返回结果
        suggestion.setSuggestedCode(suggestedCode);
        suggestion.setCurrentMaxSerial(currentMaxSerial);
        suggestion.setNextSerial(nextSerial);
        suggestion.setParentCode(parentCode);
        suggestion.setParentId(parentId);
        suggestion.setCurrentLevel(currentLevel);
        suggestion.setCodePrefix(typePrefix);
        suggestion.setFullPath(fullPath);

        log.info("[库位编码建议] warehouseCode={}, parentId={}, locationType={}, suggestedCode={}",
                warehouseCode, parentId, locationType, suggestedCode);

        return R.ok(suggestion);
    }

    /**
     * 从编码中提取末尾的数字序号
     * 例如："S0001" -> 1, "F003" -> 3, "A001" -> 1
     */
    private int extractLastSerial(String code) {
        if (code == null || code.isEmpty()) {
            return 0;
        }
        // 从末尾向前找连续的数字
        int i = code.length() - 1;
        while (i >= 0 && !Character.isDigit(code.charAt(i))) {
            i--;
        }
        if (i < 0) {
            return 0;
        }
        // 找到数字的起始位置
        int j = i;
        while (j > 0 && Character.isDigit(code.charAt(j - 1))) {
            j--;
        }
        try {
            return Integer.parseInt(code.substring(j, i + 1));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ==================== 批量操作方法 ====================

    /**
     * 批量新增
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> addBatch(WmsLocationBatchRequest batchRequest) {
        List<WmsLocationRequest> records = batchRequest.getRecords();
        if (records == null || records.isEmpty()) {
            return R.fail(i18nService.getMessage("batch.add.list.empty"));
        }

        if (records.size() > 1000) {
            return R.fail(i18nService.getMessage("batch.add.limit.exceed"));
        }

        List<WmsLocation> entityList = records.stream()
                .map(record -> {
                    generateSortNoAndFullpath(record);
                    WmsLocation entity = WmsLocationConverter.INSTANCE.requestToEntity(record);
                    fillSystemFields(entity);
                    return entity;
                })
                .collect(Collectors.toList());

        wmsLocationService.insertBatchAtomic(entityList);
        clearLocationCache();

        return R.ok(true, i18nService.getMessage("batch.add.success", records.size()));
    }

    /**
     * 批量更新
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> updateBatch(WmsLocationBatchRequest batchRequest) {
        List<WmsLocationRequest> records = batchRequest.getRecords();
        if (records == null || records.isEmpty()) {
            return R.fail(i18nService.getMessage("batch.update.list.empty"));
        }

        if (records.size() > 1000) {
            return R.fail(i18nService.getMessage("batch.update.limit.exceed"));
        }

        String currentUser = SecurityUtils.getUsername();
        Date now = new Date();

        List<WmsLocation> entityList = records.stream()
                .map(record -> {
                    if (record.getId() == null) {
                        throw new IllegalArgumentException(i18nService.getMessage("batch.update.id.required"));
                    }
                    WmsLocation entity = WmsLocationConverter.INSTANCE.requestToEntity(record);
                    entity.setUpdateBy(currentUser);
                    entity.setUpdateTime(now);
                    return entity;
                })
                .collect(Collectors.toList());

        wmsLocationService.updateBatchAtomic(entityList);
        clearLocationCache();

        return R.ok(true, i18nService.getMessage("batch.update.success", records.size()));
    }

    // ==================== 批量创建方法 ====================

    /**
     * 批量创建库位（根据模板）
     */
    @Transactional(rollbackFor = Exception.class)
    public R<List<WmsLocationResponse>> batchCreate(WmsLocationBatchCreateRequest request) {
        // 获取父节点信息
        WmsLocation parent = null;
        if (request.getParentId() != null) {
            parent = wmsLocationService.queryById(request.getParentId());
            if (parent == null) {
                return R.fail(i18nService.getMessage("wms.location.parent.not.found"));
            }
        }

        String currentUser = SecurityUtils.getUsername();
        Date now = new Date();
        List<WmsLocation> entityList = new ArrayList<>();

        int level = parent != null ? parent.getLocationLevel() + 1 : 1;
        int levelCount = parent != null ? parent.getLocationLevelCount() : 5;
        String warehouseCode = parent != null ? parent.getWarehouseCode() : request.getWarehouseCode();
        String parentName = parent != null ? parent.getLocationName() : null;
        String parentSortNo = parent != null ? parent.getLocationSortNo() : "L";

        for (int i = 0; i < request.getQuantity(); i++) {
            int serialNo = request.getStartSerialNo() + i;

            // 使用 Converter 创建基础实体
            WmsLocation entity = WmsLocationConverter.INSTANCE.batchCreateRequestToEntity(request);
            
            // 设置批量创建相关字段
            entity.setParentId(request.getParentId());
            entity.setLocationLevel(level);
            entity.setLocationLevelCount(levelCount);
            entity.setInternalSerialNo(serialNo);
            entity.setInternalQuantity(request.getQuantity());
            entity.setWarehouseCode(warehouseCode);
            entity.setParentName(parentName);

            // 生成编号和名称
            String noPrefix = request.getLocationNoPrefix() != null ? request.getLocationNoPrefix() : "";
            String namePrefix = request.getLocationNamePrefix() != null ? request.getLocationNamePrefix() : "";
            String serialStr = String.format("%03d", serialNo);

            entity.setLocationNo(noPrefix + serialStr);
            entity.setLocationName(namePrefix + serialNo);
            entity.setLocationSortNo(parentSortNo + String.format("%04d", serialNo));
            entity.setLocationFullpathName(buildFullpathName(namePrefix + serialNo, parentName));

            // 设置系统字段
            entity.setCreateBy(currentUser);
            entity.setUpdateBy(currentUser);
            entity.setCreateTime(now);
            entity.setUpdateTime(now);

            entityList.add(entity);
        }

        wmsLocationService.insertBatchAtomic(entityList);

        // 如果需要同时创建子节点
        if (Boolean.TRUE.equals(request.getCreateChildren()) 
                && request.getChildrenQuantity() != null 
                && request.getChildrenQuantity() > 0) {
            for (WmsLocation parentEntity : entityList) {
                createChildrenByConverter(parentEntity, request, currentUser, now);
            }
        }

        clearLocationCache();

        List<WmsLocationResponse> responses = entityList.stream()
                .map(WmsLocationConverter.INSTANCE::entityToResponseWithOccupancy)
                .collect(Collectors.toList());

        return R.ok(responses, i18nService.getMessage("batch.create.success", entityList.size()));
    }

    /**
     * 创建子节点（使用Converter）
     */
    private void createChildrenByConverter(WmsLocation parent, WmsLocationBatchCreateRequest request, 
                                           String currentUser, Date now) {
        List<WmsLocation> childrenList = new ArrayList<>();

        for (int i = 1; i <= request.getChildrenQuantity(); i++) {
            WmsLocation child = new WmsLocation();
            child.setParentId(parent.getId());
            child.setLocationGrade("ContainerPosition");
            child.setLocationType(request.getChildrenType() != null ? request.getChildrenType() : "孔");
            child.setLocationLevel(parent.getLocationLevel() + 1);
            child.setLocationLevelCount(parent.getLocationLevelCount());
            child.setInternalSerialNo(i);
            child.setInternalQuantity(request.getChildrenQuantity());
            child.setWarehouseCode(parent.getWarehouseCode());
            child.setParentName(parent.getLocationName());
            child.setStorageMode("Exclusive");
            child.setSpecification("1x1");
            child.setIsUse(0);
            child.setCapacityTotal(1);
            child.setCapacityUsed(0);
            child.setIsDeleted(0);
            child.setCreateBy(currentUser);
            child.setUpdateBy(currentUser);
            child.setCreateTime(now);
            child.setUpdateTime(now);

            // 孔位编号（如 A01, A02...）
            char row = (char) ('A' + (i - 1) / 12);
            int col = ((i - 1) % 12) + 1;
            child.setLocationNo("" + row + String.format("%02d", col));
            child.setLocationName(child.getLocationNo());

            child.setLocationSortNo(parent.getLocationSortNo() + String.format("%04d", i));
            child.setLocationFullpathName(parent.getLocationFullpathName() + "_" + child.getLocationName());

            childrenList.add(child);
        }

        wmsLocationService.insertBatchAtomic(childrenList);
    }

    /**
     * 查询子节点列表
     */
    public R<List<WmsLocationResponse>> queryChildren(Long parentId) {
        List<WmsLocation> children = wmsLocationService.queryByParentId(parentId);
        List<WmsLocationResponse> responseList = children.stream()
                .map(WmsLocationConverter.INSTANCE::entityToResponseWithOccupancy)
                .collect(Collectors.toList());
        return R.ok(responseList);
    }

    /**
     * 查询树形结构
     */
    public R<List<WmsLocationResponse>> queryTree(WmsLocationTreeRequest request) {
        // 尝试从缓存获取
        String cacheKey = CACHE_KEY_TREE + buildTreeCacheKey(request);
        @SuppressWarnings("unchecked")
        List<WmsLocationResponse> cached = (List<WmsLocationResponse>) redisService.getCacheObject(cacheKey);
        if (cached != null) {
            // 检查是否是空值缓存标记
            if (CACHE_NULL_VALUE.equals(cached)) {
                return R.ok(new ArrayList<>());
            }
            return R.ok(cached);
        }

        List<WmsLocation> allNodes = fetchNodesForTree(request);

        // 应用过滤条件
        allNodes = applyFilters(allNodes, request);

        // 构建树形结构（带去重）
        List<WmsLocationResponse> tree = buildTree(allNodes);

        // 存入缓存（如果是空结果，缓存空值标记防止缓存穿透）
        if (tree.isEmpty()) {
            redisService.setCacheObject(cacheKey, CACHE_NULL_VALUE, CACHE_NULL_EXPIRE_MINUTES, TimeUnit.MINUTES);
        } else {
            redisService.setCacheObject(cacheKey, tree, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        }

        return R.ok(tree);
    }

    // ==================== 占用率计算方法 ====================

    /**
     * 查询占用率统计
     */
    public R<WmsLocationOccupancyResponse> queryOccupancy(Long locationId) {
        // 尝试从缓存获取
        String cacheKey = CACHE_KEY_OCCUPANCY + locationId;
        log.info("[Redis] 查询占用率缓存, key={}, locationId={}", cacheKey, locationId);
        
        WmsLocationOccupancyResponse cached = (WmsLocationOccupancyResponse) redisService.getCacheObject(cacheKey);
        if (cached != null) {
            // 检查是否是空值缓存标记
            if (CACHE_NULL_VALUE.equals(cached)) {
                log.info("[Redis] 命中空值缓存(防穿透), key={}", cacheKey);
                return R.fail(i18nService.getMessage("wms.location.not.found"));
            }
            log.info("[Redis] 命中占用率缓存, key={}, data={}", cacheKey, cached);
            return R.ok(cached);
        }

        log.info("[Redis] 缓存未命中, 从数据库查询, locationId={}", locationId);
        WmsLocation location = wmsLocationService.queryById(locationId);
        if (location == null) {
            // 缓存空值，防止缓存穿透
            log.info("[Redis] 数据不存在, 缓存空值标记, key={}", cacheKey);
            redisService.setCacheObject(cacheKey, CACHE_NULL_VALUE, CACHE_NULL_EXPIRE_MINUTES, TimeUnit.MINUTES);
            return R.fail(i18nService.getMessage("wms.location.not.found"));
        }

        // 使用 Converter 转换
        WmsLocationOccupancyResponse response = WmsLocationConverter.INSTANCE.entityToOccupancyResponse(location);

        // 根据存储模式计算占用率
        calculateOccupancyByStorageMode(location, response);

        // 存入缓存
        log.info("[Redis] 存入占用率缓存, key={}, data={}", cacheKey, response);
        redisService.setCacheObject(cacheKey, response, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        return R.ok(response);
    }

    // ==================== 网格配置方法 ====================

    /**
     * 查询库位网格配置
     */
    public R<WmsLocationGridConfig> queryGridConfig(Long locationId) {
        WmsLocationGridConfig config = gridConfigService.queryByLocationId(locationId);
        return R.ok(config);
    }

    /**
     * 更新库位网格配置
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> updateGridConfig(Long locationId, WmsLocationGridUpdateRequest request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String gridConfigJson = objectMapper.writeValueAsString(request.getGridConfig());

            WmsLocationGridConfig config = new WmsLocationGridConfig();
            config.setLocationId(locationId);
            config.setGridRows(request.getGridRows());
            config.setGridCols(request.getGridCols());
            config.setGridConfigJson(gridConfigJson);
            config.setUpdateBy(SecurityUtils.getUsername());

            gridConfigService.saveOrUpdate(config);
            return R.ok(true);
        } catch (JsonProcessingException e) {
            log.error("网格配置JSON序列化失败", e);
            return R.fail("网格配置保存失败：" + e.getMessage());
        }
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 校验并填充父节点信息
     */
    private R<Void> validateAndFillParentInfo(WmsLocationRequest request) {
        if (request.getParentId() != null) {
            WmsLocation parent = wmsLocationService.queryById(request.getParentId());
            if (parent == null) {
                return R.fail(i18nService.getMessage("wms.location.parent.not.found"));
            }
            // 继承父节点信息
            request.setWarehouseCode(parent.getWarehouseCode());
            request.setParentName(parent.getLocationName());
            request.setLocationLevel(parent.getLocationLevel() + 1);
            request.setLocationLevelCount(parent.getLocationLevelCount());
        } else {
            // 根节点
            request.setLocationLevel(1);
            if (request.getLocationLevelCount() == null) {
                request.setLocationLevelCount(5);
            }
        }
        return null;
    }

    /**
     * 填充系统字段
     */
    private void fillSystemFields(WmsLocation entity) {
        String currentUser = SecurityUtils.getUsername();
        Date now = new Date();
        entity.setCreateBy(currentUser);
        entity.setUpdateBy(currentUser);
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setIsDeleted(0);
    }

    /**
     * 生成排序号和全路径名称
     */
    private void generateSortNoAndFullpath(WmsLocationRequest request) {
        if (request.getParentId() != null) {
            WmsLocation parent = wmsLocationService.queryById(request.getParentId());
            if (parent != null) {
                String parentSortNo = parent.getLocationSortNo() != null ? 
                        parent.getLocationSortNo() : "L";
                int serialNo = request.getInternalSerialNo() != null ? 
                        request.getInternalSerialNo() : 1;
                request.setLocationSortNo(parentSortNo + String.format("%04d", serialNo));
                request.setLocationFullpathName(generateFullpathName(request, parent.getLocationName()));
            }
        } else {
            // 根节点
            request.setLocationSortNo("L0001");
            request.setLocationFullpathName(request.getLocationName());
        }
    }

    /**
     * 生成全路径名称
     */
    private String generateFullpathName(WmsLocationRequest request, String parentName) {
        return buildFullpathName(request.getLocationName(), parentName);
    }

    /**
     * 构建全路径名称
     */
    private String buildFullpathName(String locationName, String parentName) {
        if (parentName != null && !parentName.isEmpty()) {
            return parentName + "_" + locationName;
        }
        return locationName;
    }

    /**
     * 获取树形结构节点
     */
    private List<WmsLocation> fetchNodesForTree(WmsLocationTreeRequest request) {
        List<WmsLocation> allNodes = new ArrayList<>();

        if (request.getRootId() != null) {
            WmsLocation root = wmsLocationService.queryById(request.getRootId());
            if (root != null) {
                if (Boolean.TRUE.equals(request.getRecursive())) {
                    if (request.getMaxLevel() != null) {
                        allNodes = wmsLocationService.queryChildrenByLevel(request.getRootId(), request.getMaxLevel());
                    } else {
                        allNodes = wmsLocationService.queryAllChildren(request.getRootId());
                    }
                    // queryAllChildren 已经包含根节点，不需要再添加
                } else {
                    allNodes.add(root);
                    allNodes.addAll(wmsLocationService.queryByParentId(request.getRootId()));
                }
            }
        } else if (request.getWarehouseCode() != null) {
            allNodes = wmsLocationService.queryByWarehouseCode(request.getWarehouseCode());
        } else {
            List<WmsLocation> roots = wmsLocationService.queryRootNodes();
            for (WmsLocation root : roots) {
                // 默认递归查询所有层级
                allNodes.add(root);
                allNodes.addAll(wmsLocationService.queryAllChildren(root.getId()));
            }
        }

        return allNodes;
    }

    /**
     * 应用过滤条件
     */
    private List<WmsLocation> applyFilters(List<WmsLocation> nodes, WmsLocationTreeRequest request) {
        return nodes.stream()
                .filter(n -> request.getLocationGrade() == null || request.getLocationGrade().equals(n.getLocationGrade()))
                .filter(n -> request.getLocationType() == null || request.getLocationType().equals(n.getLocationType()))
                .filter(n -> request.getStorageMode() == null || request.getStorageMode().equals(n.getStorageMode()))
                .filter(n -> request.getIsUse() == null || request.getIsUse().equals(n.getIsUse()))
                .collect(Collectors.toList());
    }

    /**
     * 构建树形结构
     */
    private List<WmsLocationResponse> buildTree(List<WmsLocation> nodes) {
        // 先根据ID去重，避免重复节点
        java.util.Map<Long, WmsLocation> uniqueNodes = nodes.stream()
                .collect(Collectors.toMap(WmsLocation::getId, n -> n, (a, b) -> a));
        List<WmsLocation> dedupNodes = new ArrayList<>(uniqueNodes.values());
        
        // 转换为Response
        List<WmsLocationResponse> responseList = dedupNodes.stream()
                .map(WmsLocationConverter.INSTANCE::entityToResponseWithOccupancy)
                .collect(Collectors.toList());

        // 使用Map存储ID到节点的映射
        java.util.Map<Long, WmsLocationResponse> nodeMap = responseList.stream()
                .collect(Collectors.toMap(WmsLocationResponse::getId, n -> n, (a, b) -> a));

        List<WmsLocationResponse> result = new ArrayList<>();

        // 构建父子关系
        for (WmsLocationResponse node : responseList) {
            if (node.getParentId() == null) {
                result.add(node);
            } else {
                WmsLocationResponse parent = nodeMap.get(node.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(node);
                    parent.setHasChildren(true);
                } else {
                    result.add(node);
                }
            }
        }

        // 设置叶子节点的 hasChildren 为 false
        for (WmsLocationResponse node : responseList) {
            if (node.getChildren() == null || node.getChildren().isEmpty()) {
                node.setHasChildren(false);
            }
        }

        // 排序
        result.sort((a, b) -> {
            if (a.getLocationSortNo() == null || b.getLocationSortNo() == null) {
                return 0;
            }
            return a.getLocationSortNo().compareTo(b.getLocationSortNo());
        });

        // 递归重新计算占用率（根据子节点实际状态）
        for (WmsLocationResponse node : result) {
            recalculateOccupancy(node);
        }

        return result;
    }

    /**
     * 递归重新计算占用率（根据子节点实际状态，而不是数据库缓存值）
     */
    private void recalculateOccupancy(WmsLocationResponse node) {
        if (node.getChildren() == null || node.getChildren().isEmpty()) {
            // 叶子节点，没有子节点需要处理
            return;
        }

        // 先递归处理子节点
        for (WmsLocationResponse child : node.getChildren()) {
            recalculateOccupancy(child);
        }

        // 根据子节点实际状态统计已占用数量
        int used = (int) node.getChildren().stream()
                .filter(child -> Integer.valueOf(1).equals(child.getIsUse()))
                .count();

        // 总数使用 specification 计算，而不是 children.size()
        int total = calculateCapacityFromSpec(node.getSpecification());
        if (total == 0) {
            // 如果 specification 无法解析，使用子节点数量（兼容旧数据）
            total = node.getChildren().size();
        }

        // 更新当前节点的统计数据
        node.setCapacityTotal(total);
        node.setCapacityUsed(used);
        node.setOccupancyRate(calculateOccupancyRate(used, total));
    }

    /**
     * 根据规格计算容量
     */
    private int calculateCapacityFromSpec(String specification) {
        if (specification == null || specification.isEmpty()) {
            return 0;
        }
        
        // 解析 "4x4", "8x12", "9x9" 等格式
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)\\s*x\\s*(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = pattern.matcher(specification);
        
        if (matcher.find()) {
            int rows = Integer.parseInt(matcher.group(1));
            int cols = Integer.parseInt(matcher.group(2));
            return rows * cols;
        }
        
        // 尝试解析单个数字（如 "96" 表示 96孔）
        java.util.regex.Pattern numPattern = java.util.regex.Pattern.compile("(\\d+)");
        java.util.regex.Matcher numMatcher = numPattern.matcher(specification);
        
        if (numMatcher.find()) {
            return Integer.parseInt(numMatcher.group(1));
        }
        
        return 0;
    }

    /**
     * 计算占用率
     */
    private java.math.BigDecimal calculateOccupancyRate(int used, int total) {
        if (total == 0) {
            return java.math.BigDecimal.ZERO;
        }
        return new java.math.BigDecimal(used)
                .multiply(new java.math.BigDecimal(100))
                .divide(new java.math.BigDecimal(total), 2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * 根据存储模式计算占用率
     */
    private void calculateOccupancyByStorageMode(WmsLocation location, WmsLocationOccupancyResponse response) {
        int total = location.getCapacityTotal() != null ? location.getCapacityTotal() : 0;
        int used = location.getCapacityUsed() != null ? location.getCapacityUsed() : 0;

        if (!"Exclusive".equals(location.getStorageMode())) {
            // 共享模式或普通节点：统计子节点
            int childrenTotal = wmsLocationService.sumChildrenCapacity(location.getId());
            int childrenUsed = wmsLocationService.sumChildrenUsedCapacity(location.getId());

            if (childrenTotal > 0) {
                total = childrenTotal;
                used = childrenUsed;
            }
        }

        response.setCapacityTotal(total);
        response.setCapacityUsed(used);
        response.setCapacityFree(total - used);

        if (total > 0) {
            response.setOccupancyRate(new java.math.BigDecimal(used)
                    .multiply(new java.math.BigDecimal(100))
                    .divide(new java.math.BigDecimal(total), 2, java.math.RoundingMode.HALF_UP));
        }
    }

    /**
     * 构建树形缓存Key
     */
    private String buildTreeCacheKey(WmsLocationTreeRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getRootId() != null ? request.getRootId() : "all");
        if (request.getWarehouseCode() != null) sb.append(":").append(request.getWarehouseCode());
        if (request.getLocationGrade() != null) sb.append(":").append(request.getLocationGrade());
        if (request.getLocationType() != null) sb.append(":").append(request.getLocationType());
        if (request.getStorageMode() != null) sb.append(":").append(request.getStorageMode());
        if (request.getIsUse() != null) sb.append(":").append(request.getIsUse());
        sb.append(":").append(request.getRecursive());
        if (request.getMaxLevel() != null) sb.append(":").append(request.getMaxLevel());
        return sb.toString();
    }

    /**
     * 清除库位相关缓存（使用 scan 命令，生产环境安全）
     */
    private void clearLocationCache() {
        try {
            redisService.scanAndDelete(CACHE_KEY_PREFIX + "*");
        } catch (Exception e) {
            log.warn("清除缓存失败: {}", e.getMessage());
        }
    }
}
