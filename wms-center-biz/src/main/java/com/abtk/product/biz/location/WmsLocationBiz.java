package com.abtk.product.biz.location;

import com.abtk.product.api.domain.request.location.AssignWarehouseRequest;
import com.abtk.product.api.domain.request.location.LocationImportRequest;
import com.abtk.product.api.domain.request.location.ContainerConfig;
import com.abtk.product.api.domain.request.location.LevelConfig;
import com.abtk.product.api.domain.request.location.WmsLocationBatchCreateRequest;
import com.abtk.product.api.domain.request.location.WmsLocationBatchRequest;
import com.abtk.product.api.domain.request.location.WmsLocationHierarchyCreateRequest;
import com.abtk.product.api.domain.request.location.WmsLocationRequest;
import com.abtk.product.api.domain.request.location.WmsLocationTreeRequest;
import com.abtk.product.api.domain.response.location.AssignWarehouseInitResponse;
import com.abtk.product.api.domain.response.location.AvailableWarehouseResponse;
import com.abtk.product.api.domain.response.location.ContainerWarehouseResponse;
import com.abtk.product.api.domain.response.location.LocationBindStatusResponse;
import com.abtk.product.api.domain.response.location.LocationCodeSuggestion;
import com.abtk.product.api.domain.response.location.LocationImportResponse;
import com.abtk.product.api.domain.response.location.WmsLocationOccupancyResponse;
import com.abtk.product.api.domain.response.location.WmsLocationResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.utils.PageUtil;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.dao.entity.Warehouse;
import com.abtk.product.dao.entity.WmsLocation;
import com.abtk.product.domain.converter.WmsLocationConverter;
import com.abtk.product.service.location.service.WmsLocationService;
import com.abtk.product.service.redis.service.RedisService;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.sys.service.WarehouseService;
import com.abtk.product.biz.system.SysSerialNumberBiz;
import com.abtk.product.dao.entity.SysDictData;
import com.abtk.product.service.system.ISysDictTypeService;
import com.abtk.product.service.system.service.I18nService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
    private WarehouseService warehouseService;

    @Autowired
    private I18nService i18nService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysSerialNumberBiz sysSerialNumberBiz;

    @Autowired
    private ISysDictTypeService dictTypeService;

    private static final String CACHE_KEY_PREFIX = "wms:location:";
    private static final String CACHE_KEY_TREE = CACHE_KEY_PREFIX + "tree:";
    private static final String CACHE_KEY_OCCUPANCY = CACHE_KEY_PREFIX + "occupancy:";
    private static final long CACHE_EXPIRE_MINUTES = 30;

    // 字典类型 -> locationGrade 映射
    private static final Map<String, String> DICT_TYPE_TO_GRADE = new HashMap<>();
    static {
        DICT_TYPE_TO_GRADE.put("location_type_for_object", "存储对象");
        DICT_TYPE_TO_GRADE.put("location_type_for_section", "存储分区");
        DICT_TYPE_TO_GRADE.put("location_type_for_container", "存储容器");
    }

    // 缓存：dictLabel -> locationGrade
    private volatile Map<String, String> locationTypeGradeCache;

    /**
     * 从字典加载 locationType -> locationGrade 映射缓存
     */
    private Map<String, String> getLocationTypeGradeCache() {
        if (locationTypeGradeCache != null) {
            return locationTypeGradeCache;
        }
        synchronized (this) {
            if (locationTypeGradeCache != null) {
                return locationTypeGradeCache;
            }
            Map<String, String> cache = new HashMap<>();
            for (Map.Entry<String, String> entry : DICT_TYPE_TO_GRADE.entrySet()) {
                String dictType = entry.getKey();
                String grade = entry.getValue();
                List<SysDictData> list = dictTypeService.selectDictDataByType(dictType);
                if (list != null) {
                    for (SysDictData d : list) {
                        if (d.getDictLabel() != null) {
                            cache.put(d.getDictLabel().trim(), grade);
                        }
                    }
                }
            }
            // 孔位固定映射
            cache.put("孔", "存储孔位");
            locationTypeGradeCache = cache;
            return locationTypeGradeCache;
        }
    }

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
     * 🔴 修复：增加字段权限校验 + 存储模式变更处理
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

        // 🔴 新增1：严格字段权限校验（基于Story 15-05）

        // 孔位（ContainerPosition）不可修改任何字段
        if ("ContainerPosition".equals(existing.getLocationGrade())) {
            return R.fail("存储孔位由系统自动生成，不可手动编辑");
        }

        // 存储类型和存储分区：仅允许修改名称和备注
        if ("StorageType".equals(existing.getLocationGrade()) || "StorageSection".equals(existing.getLocationGrade())) {
            // 允许修改的字段：locationName, remarks
            if (request.getLocationType() != null ||
                request.getStorageMode() != null || request.getSpecification() != null) {
                return R.fail("该类型库位仅允许修改名称和备注");
            }
        }

        // 存储容器（Container）：区分绑定状态
        if ("Container".equals(existing.getLocationGrade())) {
            boolean isBound = existing.getIsUse() != null && existing.getIsUse() == 1;
            if (isBound) {
                // 已绑定：仅允许修改名称和备注
                if (request.getLocationType() != null ||
                    request.getStorageMode() != null || request.getSpecification() != null) {
                    return R.fail("该库位已绑定物料，仅可修改名称");
                }
            } else {
                // 未绑定：可修改名称、存储模式、规格，但不允许修改类型
                if (request.getLocationType() != null) {
                    return R.fail("存储容器的类型不可修改");
                }
            }
        }

        // 🔴 新增2：存储模式变更处理
        String newStorageMode = request.getStorageMode();
        String oldStorageMode = existing.getStorageMode();
        boolean storageModeChanged = newStorageMode != null && !newStorageMode.equals(oldStorageMode);

        if (storageModeChanged && "Container".equals(existing.getLocationGrade())) {
            if ("Shared".equals(newStorageMode)) {
                // 🔴 独占 → 共享：删除所有孔位
                List<WmsLocation> children = wmsLocationService.queryByParentId(existing.getId());
                if (!children.isEmpty()) {
                    List<Long> childIds = children.stream()
                            .map(WmsLocation::getId)
                            .collect(Collectors.toList());
                    wmsLocationService.logicDeleteBatchByIds(childIds, SecurityUtils.getUsername());
                    log.info("[存储模式变��] 容器 {} 从独占改为共享，删除了 {} 个孔位",
                            existing.getId(), childIds.size());
                }
            }
            // 注意：共享 → 独占时，孔位由前端通过 batchCreate 创��，此处不自动创建
            // 因为需要用户指定规格，且可能同时创建多个容器
        }

        // 如果修改了名称，需要更新全路径名称，并级联更新所有子节点的 parentName 和 locationFullpathName
        if (request.getLocationName() != null && !request.getLocationName().equals(existing.getLocationName())) {
            String newFullpathName = generateFullpathName(request, existing.getParentName());
            request.setLocationFullpathName(newFullpathName);
            cascadeUpdateChildrenName(existing.getId(), request.getLocationName(), newFullpathName);
        }

        WmsLocation entity = WmsLocationConverter.INSTANCE.requestToEntity(request);
        entity.setUpdateBy(SecurityUtils.getUsername());
        entity.setUpdateTime(new Date());

        int result = wmsLocationService.update(entity);
        clearLocationCache();

        return R.ok(result);
    }

    /**
     * 判断是否为容器类型
     */
    private boolean isContainerType(String locationType) {
        return Arrays.asList("盒", "箱", "笼", "抽屉").contains(locationType);
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
     * 🔴 修复：增加占用状态校验
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteRecursive(Long id) {
        WmsLocation existing = wmsLocationService.queryById(id);
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.location.not.found"));
        }

        // 查询所有子节点（包括自身）
        List<WmsLocation> allChildren = wmsLocationService.queryAllChildren(id);
        List<WmsLocation> checkList = new ArrayList<>();
        checkList.add(existing);
        checkList.addAll(allChildren);

        // 检查是否有占用的库位
        List<WmsLocation> usedLocations = checkList.stream()
                .filter(loc -> loc.getIsUse() != null && loc.getIsUse() == 1)
                .collect(Collectors.toList());

        if (!usedLocations.isEmpty()) {
            String usedNames = usedLocations.stream()
                    .limit(3)  // 最多显示3个
                    .map(WmsLocation::getLocationName)
                    .collect(Collectors.joining(", "));
            String msg = "以下库位已被占用，无法删除：" + usedNames;
            if (usedLocations.size() > 3) {
                msg += " 等" + usedLocations.size() + "个库位";
            }
            return R.fail(msg);
        }

        wmsLocationService.logicDeleteRecursive(id, SecurityUtils.getUsername());
        clearLocationCache();

        return R.ok(true);
    }

    /**
     * 检查库位是否可以删除（前端删除确认用）
     * 对应接口：GET /wms/location/check-delete/{id}
     */
    public R<Map<String, Object>> checkDelete(Long id) {
        WmsLocation existing = wmsLocationService.queryById(id);
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.location.not.found"));
        }

        List<WmsLocation> allChildren = wmsLocationService.queryAllChildren(id);
        // 总子节点数（不含自身）
        int childCount = allChildren.size();

        // 检查占用情况
        List<WmsLocation> allToCheck = new ArrayList<>();
        allToCheck.add(existing);
        allToCheck.addAll(allChildren);

        List<WmsLocation> usedLocations = allToCheck.stream()
                .filter(loc -> loc.getIsUse() != null && loc.getIsUse() == 1)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("canDelete", usedLocations.isEmpty());
        result.put("childCount", childCount);
        result.put("totalCount", allToCheck.size());
        result.put("usedCount", usedLocations.size());

        if (!usedLocations.isEmpty()) {
            List<String> usedNames = usedLocations.stream()
                    .limit(3)
                    .map(WmsLocation::getLocationName)
                    .collect(Collectors.toList());
            result.put("usedNames", usedNames);
            if (usedLocations.size() > 3) {
                result.put("message", "下级库位 " + usedNames.get(0) + " ���" + usedLocations.size() + "个已被占用");
            } else {
                result.put("message", "下级库位 " + String.join(", ", usedNames) + " 已被占用");
            }
        }

        return R.ok(result);
    }

    /**
     * 🔴 新增：检查库位绑定状态（Story 15-05）
     * 对应接口：GET /wms/location/check-bind/{id}
     * 前端调用：checkLocationBind
     */
    public R<LocationBindStatusResponse> checkBind(Long id) {
        WmsLocation location = wmsLocationService.queryById(id);
        if (location == null) {
            return R.fail(i18nService.getMessage("wms.location.not.found"));
        }

        LocationBindStatusResponse response = new LocationBindStatusResponse();
        // 判断是否占用：仅孔位或独占模式容器且isUse=1视为绑定
        boolean isBound = location.getIsUse() != null && location.getIsUse() == 1;
        response.setIsBound(isBound);

        if (isBound) {
            // 尝试推断物料类型（可根据业务扩展）
            response.setBoundType("MATERIAL"); // 默认物料类型
            response.setBoundMaterialName("已绑定物料"); // 可后续查询具体物料名称
        }

        return R.ok(response);
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
            parentCode = parent.getLocationSortNo() != null ? parent.getLocationSortNo() : "";
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
            // 计算当前同级的最大序号 - 使用 locationSortNo
            for (WmsLocation sibling : siblings) {
                String sortNo = sibling.getLocationSortNo();
                if (sortNo != null && !sortNo.isEmpty()) {
                    // 提取编码末尾的数字部分
                    int serial = extractLastSerial(sortNo);
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
        String parentFullpathName = parent != null ? parent.getLocationFullpathName() : null;
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

            // 生成名称和排序号
            String namePrefix = request.getLocationNamePrefix() != null ? request.getLocationNamePrefix() : "";
            String serialStr = String.format("%03d", serialNo);

            entity.setLocationName(namePrefix + serialNo);
            entity.setLocationSortNo(parentSortNo + String.format("%04d", serialNo));
            entity.setLocationFullpathName(buildFullpathName(namePrefix + serialNo, parentFullpathName));

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
     * 层级批量创建库位（支持多级分区 + 容器 + 孔位）
     */
    @Transactional(rollbackFor = Exception.class)
    public R<List<WmsLocationResponse>> batchCreateHierarchy(WmsLocationHierarchyCreateRequest request) {
        WmsLocation parent = wmsLocationService.queryById(request.getParentId());
        if (parent == null) {
            return R.fail(i18nService.getMessage("wms.location.parent.not.found"));
        }

        String currentUser = SecurityUtils.getUsername();
        Date now = new Date();
        List<WmsLocation> allCreated = new ArrayList<>();

        // 当前层级的父节点列表（从传入的parent开始）
        List<WmsLocation> currentParents = new ArrayList<>();
        currentParents.add(parent);

        List<LevelConfig> levels = request.getLevels();
        for (int levelIdx = 0; levelIdx < levels.size(); levelIdx++) {
            LevelConfig levelConfig = levels.get(levelIdx);
            List<WmsLocation> nextParents = new ArrayList<>();

            for (WmsLocation p : currentParents) {
                List<WmsLocation> created = createLevelNodes(
                    p, levelConfig, levelIdx, levels.size(), currentUser, now
                );
                nextParents.addAll(created);
                allCreated.addAll(created);
            }

            currentParents = nextParents;
        }

        // 最底层分区下创建容器 + 孔位
        ContainerConfig containerConfig = request.getContainer();
        if (containerConfig != null && !currentParents.isEmpty()) {
            for (WmsLocation p : currentParents) {
                List<WmsLocation> containers = createContainers(
                    p, containerConfig, currentUser, now
                );
                allCreated.addAll(containers);

                // 为每个容器创建孔位
                if (containerConfig.getChildrenQuantity() != null
                        && containerConfig.getChildrenQuantity() > 0) {
                    for (WmsLocation container : containers) {
                        List<WmsLocation> positions = createContainerPositions(
                            container, containerConfig, currentUser, now
                        );
                        allCreated.addAll(positions);
                    }
                }
            }
        }

        clearLocationCache();

        // 只返回最外层创建的节点（分区层）作为响应
        List<WmsLocationResponse> responses = allCreated.stream()
                .filter(e -> e.getLocationGrade() != null
                        && (e.getLocationGrade().equals("StorageSection") || e.getLocationGrade().equals("存储分区")))
                .map(WmsLocationConverter.INSTANCE::entityToResponseWithOccupancy)
                .collect(Collectors.toList());

        return R.ok(responses, i18nService.getMessage("batch.create.success", allCreated.size()));
    }

    /**
     * 创建一层分区节点
     */
    private List<WmsLocation> createLevelNodes(WmsLocation parent, LevelConfig config,
                                                int levelIdx, int totalLevels,
                                                String currentUser, Date now) {
        List<WmsLocation> list = new ArrayList<>();
        int level = parent.getLocationLevel() + 1;
        String parentSortNo = parent.getLocationSortNo() != null ? parent.getLocationSortNo() : "L";
        String parentName = parent.getLocationName();
        String locationType = config.getLocationType();
        String locationGrade = determineLocationGrade(locationType);
        int quantity = config.getQuantity();
        int defaultStart = config.getStartSerialNo() != null ? config.getStartSerialNo() : 1;
        int startSerial = getNextSerialNo(parent.getId(), defaultStart);

        for (int i = 0; i < quantity; i++) {
            int serialNo = startSerial + i;
            WmsLocation entity = new WmsLocation();
            entity.setParentId(parent.getId());
            entity.setLocationGrade(locationGrade);
            entity.setLocationType(locationType);
            entity.setLocationLevel(level);
            entity.setLocationLevelCount(parent.getLocationLevelCount());
            entity.setInternalSerialNo(serialNo);
            entity.setInternalQuantity(quantity);
            entity.setWarehouseCode(parent.getWarehouseCode());
            entity.setParentName(parentName);
            entity.setIsUse(0);
            entity.setIsDeleted(0);
            entity.setCreateBy(currentUser);
            entity.setCreateTime(now);
            entity.setUpdateBy(currentUser);
            entity.setUpdateTime(now);

            String name = locationType + serialNo;
            entity.setLocationName(name);
            entity.setLocationSortNo(parentSortNo + String.format("%04d", serialNo));
            entity.setLocationFullpathName(buildFullpathName(name, parent.getLocationFullpathName()));

            list.add(entity);
        }

        wmsLocationService.insertBatchAtomic(list);

        return list;
    }

    /**
     * 创建存储容器节点
     */
    private List<WmsLocation> createContainers(WmsLocation parent, ContainerConfig config,
                                                String currentUser, Date now) {
        List<WmsLocation> list = new ArrayList<>();
        int level = parent.getLocationLevel() + 1;
        String parentSortNo = parent.getLocationSortNo() != null ? parent.getLocationSortNo() : "L";
        String parentName = parent.getLocationName();
        String locationType = config.getLocationType();
        int quantity = config.getQuantity();
        int startSerial = getNextSerialNo(parent.getId(), 1);

        for (int i = 0; i < quantity; i++) {
            int serialNo = startSerial + i;
            WmsLocation entity = new WmsLocation();
            entity.setParentId(parent.getId());
            entity.setLocationGrade("存储容器");
            entity.setLocationType(locationType);
            entity.setLocationLevel(level);
            entity.setLocationLevelCount(parent.getLocationLevelCount());
            entity.setInternalSerialNo(serialNo);
            entity.setInternalQuantity(quantity);
            entity.setWarehouseCode(parent.getWarehouseCode());
            entity.setParentName(parentName);
            String storageMode = "1x1".equals(config.getSpecification()) ? "Shared" : "Exclusive";
            entity.setStorageMode(storageMode);
            entity.setSpecification(config.getSpecification());
            entity.setIsUse(0);
            entity.setIsDeleted(0);
            entity.setCreateBy(currentUser);
            entity.setCreateTime(now);
            entity.setUpdateBy(currentUser);
            entity.setUpdateTime(now);

            String name = locationType + serialNo;
            entity.setLocationName(name);
            entity.setLocationSortNo(parentSortNo + String.format("%04d", serialNo));
            entity.setLocationFullpathName(buildFullpathName(name, parent.getLocationFullpathName()));

            list.add(entity);
        }

        wmsLocationService.insertBatchAtomic(list);

        return list;
    }

    /**
     * 创建存储孔位节点
     */
    private List<WmsLocation> createContainerPositions(WmsLocation parent, ContainerConfig config,
                                                        String currentUser, Date now) {
        List<WmsLocation> list = new ArrayList<>();
        int level = parent.getLocationLevel() + 1;
        String parentSortNo = parent.getLocationSortNo() != null ? parent.getLocationSortNo() : "L";
        String parentName = parent.getLocationName();
        String specification = config.getSpecification();
        int cols = parseSpecCols(specification);
        int total = config.getChildrenQuantity();
        String childrenType = config.getChildrenType() != null ? config.getChildrenType() : "孔";
        int startSerial = getNextSerialNo(parent.getId(), 1);

        for (int i = 0; i < total; i++) {
            int serialNo = startSerial + i;
            WmsLocation child = new WmsLocation();
            child.setParentId(parent.getId());
            child.setLocationGrade("存储孔位");
            child.setLocationType(childrenType);
            child.setLocationLevel(level);
            child.setLocationLevelCount(parent.getLocationLevelCount());
            child.setInternalSerialNo(serialNo);
            child.setInternalQuantity(total);
            child.setWarehouseCode(parent.getWarehouseCode());
            child.setParentName(parentName);
            child.setStorageMode("Exclusive");
            child.setSpecification("1x1");
            child.setIsUse(0);
            child.setIsDeleted(0);
            child.setCreateBy(currentUser);
            child.setCreateTime(now);
            child.setUpdateBy(currentUser);
            child.setUpdateTime(now);

            int rowIdx = i / cols;
            int colIdx = i % cols;
            char rowChar = (char) ('A' + rowIdx);
            String colStr = String.format("%02d", colIdx + 1);
            String positionNo = rowChar + colStr;

            child.setLocationName(positionNo);
            child.setLocationSortNo(parentSortNo + String.format("%04d", serialNo));
            child.setLocationFullpathName(parent.getLocationFullpathName() + "_" + positionNo);

            list.add(child);
        }

        wmsLocationService.insertBatchAtomic(list);
        return list;
    }

    /**
     * 创建子节点（使用Converter）
     * 🔴 修复：孔位编号算法 - 根据规格动态计算行列
     */
    private void createChildrenByConverter(WmsLocation parent, WmsLocationBatchCreateRequest request,
                                           String currentUser, Date now) {
        List<WmsLocation> childrenList = new ArrayList<>();

        // 解析规格获取行列数
        String specification = request.getSpecification();
        int cols = parseSpecCols(specification);  // 列数
        int total = request.getChildrenQuantity();  // 总孔位数

        for (int i = 1; i <= total; i++) {
            WmsLocation child = new WmsLocation();
            child.setParentId(parent.getId());
            child.setLocationGrade("ContainerPosition");
            child.setLocationType(request.getChildrenType() != null ? request.getChildrenType() : "孔");
            child.setLocationLevel(parent.getLocationLevel() + 1);
            child.setLocationLevelCount(parent.getLocationLevelCount());
            child.setInternalSerialNo(i);
            child.setInternalQuantity(total);
            child.setWarehouseCode(parent.getWarehouseCode());
            child.setParentName(parent.getLocationName());
            child.setStorageMode("Exclusive");
            child.setSpecification("1x1");
            child.setIsUse(0);
            child.setIsDeleted(0);
            child.setCreateBy(currentUser);
            child.setUpdateBy(currentUser);
            child.setCreateTime(now);
            child.setUpdateTime(now);

            // 🔴 修正：根据规格的行数列数计算孔位编号
            // 例如：4x4 → 4行4列；8x12 → 8行12列
            int rowIdx = (i - 1) / cols;  // 0-based 行索引
            int colIdx = (i - 1) % cols;  // 0-based 列索引
            char rowChar = (char) ('A' + rowIdx);  // A, B, C...
            String colStr = String.format("%02d", colIdx + 1);  // 01, 02...
            String positionNo = rowChar + colStr;

            child.setLocationName(positionNo);

            child.setLocationSortNo(parent.getLocationSortNo() + String.format("%04d", i));
            child.setLocationFullpathName(parent.getLocationFullpathName() + "_" + positionNo);

            childrenList.add(child);
        }

        wmsLocationService.insertBatchAtomic(childrenList);
    }

    /**
     * 🔴 新增：解析规格获取行数和列数
     */
    private int[] parseSpecRowsAndCols(String specification) {
        if (specification == null || specification.isEmpty()) {
            return new int[]{1, 1};  // 默认1x1
        }
        // 尝试解析 "4x4" 格式
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)\\s*x\\s*(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = pattern.matcher(specification);
        if (matcher.find()) {
            int rows = Integer.parseInt(matcher.group(1));
            int cols = Integer.parseInt(matcher.group(2));
            return new int[]{rows, cols};
        }
        // 尝试解析 "96孔" 格式
        java.util.regex.Pattern numPattern = java.util.regex.Pattern.compile("(\\d+)\\s*孔");
        java.util.regex.Matcher numMatcher = numPattern.matcher(specification);
        if (numMatcher.find()) {
            int total = Integer.parseInt(numMatcher.group(1));
            int cols = findBestCols(total);
            int rows = total / cols;
            if (rows * cols != total) {
                rows = total;  // 无法整除时，默认rows=total
            }
            return new int[]{rows, cols};
        }
        return new int[]{1, 1};  // 默认1x1
    }

    /**
     * 从规格字符串解析列数
     * 支持格式：4x4, 8x12, 9x9, 96孔等
     */
    private int parseSpecCols(String specification) {
        return parseSpecRowsAndCols(specification)[1];
    }

    /**
     * 根据总孔位数推断最佳列数
     */
    private int findBestCols(int total) {
        // 常见规格：4x4=16, 6x6=36, 8x12=96, 9x9=81
        // 优先选择列数较多的排列（更符合实际布局）
        if (total == 4) return 2;   // 2x2
        if (total == 16) return 4;  // 4x4
        if (total == 36) return 6;  // 6x6
        if (total == 81) return 9;  // 9x9
        if (total == 96) return 12; // 8x12

        // 通用：寻找最接近的因数
        for (int cols = 12; cols >= 2; cols--) {
            if (total % cols == 0) {
                return cols;
            }
        }
        return 1;  // 默认1列
    }

    /**
     * 查询子节点列表（包含完整树形结构，用于库位预览）
     * 返回指定父节点的所有子孙节点，构建为树形结构
     */
    public R<List<WmsLocationResponse>> queryChildren(Long parentId) {
        // 递归查询所有子孙节点
        List<WmsLocation> allChildren = wmsLocationService.queryAllChildren(parentId);

        // 转换为 Response 并构建树形结构
        List<WmsLocationResponse> responseList = allChildren.stream()
                .map(WmsLocationConverter.INSTANCE::entityToResponseWithOccupancy)
                .collect(Collectors.toList());

        // 构建父子关系
        Map<Long, WmsLocationResponse> nodeMap = responseList.stream()
                .collect(Collectors.toMap(WmsLocationResponse::getId, n -> n, (a, b) -> a));

        List<WmsLocationResponse> result = new ArrayList<>();

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

        return R.ok(result);
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
            // 🔴 新增：根据父节点等级和当前层级推断 locationGrade
            request.setLocationGrade(inferLocationGradeFromParent(request.getLocationType(), parent.getLocationGrade(), request.getLocationLevel()));
        } else {
            // 根节点 - 默认为 存储对象
            request.setLocationLevel(1);
            request.setLocationGrade("存储对象");
            if (request.getLocationLevelCount() == null) {
                request.setLocationLevelCount(1);
            }
        }
        return null;
    }
    
    /**
     * 🔴 新增：根据父节点等级和当前层级推断 locationGrade
     */
    private String inferLocationGradeFromParent(String locationType, String parentGrade, int currentLevel) {
        // 如果指定了 locationType，使用 determineLocationGrade
        if (locationType != null && !locationType.isEmpty()) {
            return determineLocationGrade(locationType);
        }
        // 否则根据父节点等级推断子节点等级
        if ("StorageType".equals(parentGrade) || "存储对象".equals(parentGrade)) {
            return "存储分区"; // 存储对象下的子节点 -> 存储分区
        }
        if ("StorageSection".equals(parentGrade) || "存储分区".equals(parentGrade)) {
            return "存储容器"; // 存储分区下的子节点 -> 存储容器
        }
        if ("Container".equals(parentGrade) || "存储容器".equals(parentGrade)) {
            return "存储孔位"; // 存储容器下的子节点 -> 孔位
        }
        return "存储分区"; // 默认存储分区
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
                request.setLocationFullpathName(generateFullpathName(request, parent.getLocationFullpathName()));
            }
        } else {
            // 根节点：从流水号规则获取排序号
            String serialNo = sysSerialNumberBiz.generateSerialNumber("location_sort_no_object", SecurityUtils.getUsername());
            request.setLocationSortNo(serialNo);
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
     * 级联更新子节点的 parentName 和 locationFullpathName
     *
     * @param parentId             父节点ID
     * @param newParentName        父节点新名称
     * @param newParentFullpathName 父节点新全路径名称
     */
    private void cascadeUpdateChildrenName(Long parentId, String newParentName, String newParentFullpathName) {
        List<WmsLocation> children = wmsLocationService.queryByParentId(parentId);
        if (children == null || children.isEmpty()) {
            return;
        }
        String updateBy = SecurityUtils.getUsername();
        Date updateTime = new Date();
        for (WmsLocation child : children) {
            String oldFullpath = child.getLocationFullpathName();
            String newFullpath = buildFullpathName(child.getLocationName(), newParentFullpathName);
            boolean changed = false;

            if (!Objects.equals(child.getParentName(), newParentName)) {
                child.setParentName(newParentName);
                changed = true;
            }
            if (!Objects.equals(oldFullpath, newFullpath)) {
                child.setLocationFullpathName(newFullpath);
                changed = true;
            }

            if (changed) {
                child.setUpdateBy(updateBy);
                child.setUpdateTime(updateTime);
                wmsLocationService.update(child);
            }

            // 递归更新孙节点（子节点自身的名称未变，但全路径已变）
            cascadeUpdateChildrenName(child.getId(), child.getLocationName(), newFullpath);
        }
    }

    /**
     * 获取父节点下下一个可用的序列号（根据已有子节点的 location_sort_no 后4位）
     */
    private int getNextSerialNo(Long parentId, int defaultStartSerial) {
        List<WmsLocation> siblings = wmsLocationService.queryByParentId(parentId);
        if (siblings == null || siblings.isEmpty()) {
            return defaultStartSerial;
        }
        int maxSerial = 0;
        for (WmsLocation sibling : siblings) {
            String sortNo = sibling.getLocationSortNo();
            if (sortNo != null && sortNo.length() >= 4) {
                try {
                    String last4 = sortNo.substring(sortNo.length() - 4);
                    int serial = Integer.parseInt(last4);
                    if (serial > maxSerial) {
                        maxSerial = serial;
                    }
                } catch (NumberFormatException e) {
                    // 忽略非数字后缀
                }
            }
        }
        return Math.max(defaultStartSerial, maxSerial + 1);
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

        // 只更新占用率，不再设置已删除的字段
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
     * 根据存储模式计算占用率（已改为基于子节点数量统计）
     */
    private void calculateOccupancyByStorageMode(WmsLocation location, WmsLocationOccupancyResponse response) {
        int total = 0;
        int used = 0;

        if (!"Exclusive".equals(location.getStorageMode())) {
            // 共享模式或普通节点：统计子节点数量
            List<WmsLocation> children = wmsLocationService.queryByParentId(location.getId());
            total = children.size() + 1;
            used = (int) children.stream().filter(c -> c.getIsUse() != null && c.getIsUse() == 1).count();
            if (location.getIsUse() != null && location.getIsUse() == 1) {
                used++;
            }
        } else {
            // Exclusive 模式：只统计自身
            total = 1;
            used = (location.getIsUse() != null && location.getIsUse() == 1) ? 1 : 0;
        }

        response.setCapacityFree(total - used);

        if (total > 0) {
            response.setOccupancyRate(new java.math.BigDecimal(used)
                    .multiply(new java.math.BigDecimal(100))
                    .divide(new java.math.BigDecimal(total), 2, java.math.RoundingMode.HALF_UP));
        }
    }

    /**
     * 构建树形缓存Key（修复：加入用户标识防止多租户数据泄漏）
     */
    private String buildTreeCacheKey(WmsLocationTreeRequest request) {
        StringBuilder sb = new StringBuilder();
        // 🔴 新增：加入用户标识，确保不同用户缓存隔离
        String username = SecurityUtils.getUsername();
        sb.append(username != null ? username : "anonymous");
        sb.append(":");
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
     * 清除库位相关缓存（优化：仅清理必要的键，避免全表扫描）
     * 🔴 优化点：分开清理不同类型的缓存，减少Redis扫描范围
     */
    private void clearLocationCache() {
        try {
            // 仅清理树形缓存和占用率缓存，避免扫描过多无关键
            redisService.scanAndDelete(CACHE_KEY_TREE + "*");
            redisService.scanAndDelete(CACHE_KEY_OCCUPANCY + "*");
            log.debug("[缓存清理] 已清理 wms:location:tree 和 wms:location:occupancy 相关缓存");
        } catch (Exception e) {
            log.warn("清除缓存失败: {}", e.getMessage());
        }
    }

    // ==================== 分配仓库方法 ====================

    /**
     * 初始化分配仓库弹窗数据
     *
     * @param locationId 存储类型/存储分区ID
     * @param containerIds 已选中的存储容器ID列表
     * @return 初始化数据
     */
    public R<AssignWarehouseInitResponse> initAssignWarehouse(Long locationId, List<Long> containerIds) {
        // 1. 查询存储类型/存储分区信息
        WmsLocation location = wmsLocationService.queryById(locationId);
        if (location == null) {
            return R.fail(i18nService.getMessage("wms.location.not.found"));
        }

        // 获取原仓库编码
        String originalWarehouseCode = location.getWarehouseCode();
        if (originalWarehouseCode == null || originalWarehouseCode.isEmpty()) {
            return R.fail("该库位未绑定仓库，无法分配");
        }

        // 2. 查询原仓库信息
        Warehouse originalWarehouse = warehouseService.getByWarehouseCode(originalWarehouseCode);
        if (originalWarehouse == null) {
            return R.fail("原仓库不存在");
        }

        String originalTemperatureZone = originalWarehouse.getTemperatureZone();

        // 3. 查询存储容器列表
        List<WmsLocation> containers;
        if (containerIds != null && !containerIds.isEmpty()) {
            containers = wmsLocationService.queryByIds(containerIds);
        } else {
            // 查询该分区下的所有存储容器
            containers = wmsLocationService.queryByParentId(locationId);
        }

        // 转换为响应DTO
        List<ContainerWarehouseResponse> containerResponses = containers.stream()
                .map(c -> {
                    ContainerWarehouseResponse resp = new ContainerWarehouseResponse();
                    resp.setContainerId(c.getId());
                    resp.setContainerName(c.getLocationName());
                    resp.setWarehouseCode(c.getWarehouseCode());
                    resp.setSelected(containerIds != null && containerIds.contains(c.getId()));

                    // 查询仓库名称
                    if (c.getWarehouseCode() != null) {
                        Warehouse wh = warehouseService.getByWarehouseCode(c.getWarehouseCode());
                        if (wh != null) {
                            resp.setWarehouseName(wh.getWarehouseName());
                            resp.setTemperatureZone(wh.getTemperatureZone());
                        }
                    }
                    return resp;
                })
                .collect(Collectors.toList());

        // 4. 查询可选仓库（温区一致的隔离仓/留样仓）
        List<Warehouse> availableWhList = warehouseService.listByTemperatureZoneAndTypes(originalTemperatureZone);
        List<AvailableWarehouseResponse> availableWarehouses = availableWhList.stream()
                .map(wh -> {
                    AvailableWarehouseResponse resp = new AvailableWarehouseResponse();
                    resp.setWarehouseCode(wh.getWarehouseCode());
                    resp.setWarehouseName(wh.getWarehouseName());
                    resp.setWarehouseType(wh.getWarehouseType());
                    resp.setTemperatureZone(wh.getTemperatureZone());
                    resp.setDisplayName(buildWarehouseDisplayName(wh));
                    return resp;
                })
                .collect(Collectors.toList());

        // 5. 构建响应
        AssignWarehouseInitResponse response = new AssignWarehouseInitResponse();
        response.setOriginalWarehouseCode(originalWarehouseCode);
        response.setOriginalWarehouseName(originalWarehouse.getWarehouseName());
        response.setOriginalTemperatureZone(originalTemperatureZone);
        response.setContainers(containerResponses);
        response.setAvailableWarehouses(availableWarehouses);

        return R.ok(response);
    }

    /**
     * 执行分配仓库操作
     *
     * @param request 分配请求
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> assignWarehouse(AssignWarehouseRequest request) {
        // 1. 参数校验
        if (request.getContainerIds() == null || request.getContainerIds().isEmpty()) {
            return R.fail("请选择要分配的存储容器");
        }
        if (request.getTargetWarehouseCode() == null || request.getTargetWarehouseCode().isEmpty()) {
            return R.fail("请选择目标仓库");
        }

        // 2. 查询存储容器信息
        List<WmsLocation> containers = wmsLocationService.queryByIds(request.getContainerIds());
        if (containers.isEmpty()) {
            return R.fail("未找到指定的存储容器");
        }

        // 3. 获取其中一个容器的仓库编码，用于校验温区
        String originalWarehouseCode = containers.get(0).getWarehouseCode();

        // 4. 温区校验
        Warehouse originalWarehouse = warehouseService.getByWarehouseCode(originalWarehouseCode);
        if (originalWarehouse == null) {
            return R.fail("原仓库不存在");
        }

        Warehouse targetWarehouse = warehouseService.getByWarehouseCode(request.getTargetWarehouseCode());
        if (targetWarehouse == null) {
            return R.fail("目标仓库不存在");
        }

        // 校验温区一致性
        String originalTempZone = originalWarehouse.getTemperatureZone();
        String targetTempZone = targetWarehouse.getTemperatureZone();
        if (originalTempZone == null || !originalTempZone.equals(targetTempZone)) {
            return R.fail(-1, "新仓库温区与原仓库不一致，无法分配");
        }

        // 5. 仓库类型校验（必须是隔离仓或留样仓）
        String targetType = targetWarehouse.getWarehouseType();
        if (!"QUARANTINE".equals(targetType) && !"SAMPLE".equals(targetType)) {
            return R.fail("仅支持分配到隔离仓或留样仓");
        }

        // 6. 执行批量更新
        String currentUser = SecurityUtils.getUsername();
        int updatedCount = wmsLocationService.batchUpdateWarehouseCode(
                request.getContainerIds(),
                request.getTargetWarehouseCode(),
                currentUser
        );

        // 7. 清除缓存
        clearLocationCache();

        log.info("[分配仓库] 成功分配 {} 个存储容器到仓库 {}", updatedCount, request.getTargetWarehouseCode());

        return R.ok(true, "分配成功，已更新 " + updatedCount + " 个存储容器");
    }

    // ==================== 导入导出方法 ====================

    /**
     * 导入库位数据
     *
     * @param importList 导入数据列表
     * @return 导入结果
     */
    public R<LocationImportResponse> importLocations(List<LocationImportRequest> importList) {
        if (importList == null || importList.isEmpty()) {
            return R.fail(i18nService.getMessage("import.list.empty"));
        }

        if (importList.size() > 1000) {
            return R.fail(i18nService.getMessage("import.limit.exceed"));
        }

        List<LocationImportResponse.ImportError> errors = new ArrayList<>();
        List<WmsLocation> successEntities = new ArrayList<>();

        // 库位类型白名单（支持中英文）
        Set<String> validLocationTypes = new HashSet<>();
        // 中文
        validLocationTypes.add("冰箱");
        validLocationTypes.add("货架");
        validLocationTypes.add("地堆");
        validLocationTypes.add("托盘");
        validLocationTypes.add("层");
        validLocationTypes.add("架");
        validLocationTypes.add("行");
        validLocationTypes.add("列");
        validLocationTypes.add("格");
        validLocationTypes.add("盒");
        validLocationTypes.add("箱");
        validLocationTypes.add("笼");
        validLocationTypes.add("抽屉");
        validLocationTypes.add("孔");
        // 英文别名（兼容）
        validLocationTypes.add("Freezer");
        validLocationTypes.add("Shelf");
        validLocationTypes.add("Ground");
        validLocationTypes.add("Pallet");
        validLocationTypes.add("Layer");
        validLocationTypes.add("Rack");
        validLocationTypes.add("Row");
        validLocationTypes.add("Column");
        validLocationTypes.add("Cell");
        validLocationTypes.add("Box");
        validLocationTypes.add("Case");
        validLocationTypes.add("Cage");
        validLocationTypes.add("Drawer");
        validLocationTypes.add("Position");

        // 按行处理数据
        for (int i = 0; i < importList.size(); i++) {
            int rowNum = i + 2; // Excel行号从2开始（1是表头）
            LocationImportRequest record = importList.get(i);

            // 1. 必填校验：仓库编码
            if (record.getWarehouseCode() == null || record.getWarehouseCode().trim().isEmpty()) {
                errors.add(new LocationImportResponse.ImportError(rowNum, "仓库编码不能为空"));
                continue;
            }

            // 2. 必填校验：库位类型
            if (record.getLocationType() == null || record.getLocationType().trim().isEmpty()) {
                errors.add(new LocationImportResponse.ImportError(rowNum, "库位类型不能为空"));
                continue;
            }

            // 3. 库位类型校验
            if (!validLocationTypes.contains(record.getLocationType().trim())) {
                errors.add(new LocationImportResponse.ImportError(rowNum, "库位类型不存在"));
                continue;
            }

            // 4. 必填校验：库位名称
            if (record.getLocationName() == null || record.getLocationName().trim().isEmpty()) {
                errors.add(new LocationImportResponse.ImportError(rowNum, "库位名称不能为空"));
                continue;
            }

            // 5. 库位名称长度校验
            if (record.getLocationName().trim().length() > 50) {
                errors.add(new LocationImportResponse.ImportError(rowNum, "库位名称最多50字符"));
                continue;
            }

            // 6. 仓库是否存在校验
            Warehouse warehouse = warehouseService.getByWarehouseCode(record.getWarehouseCode().trim());
            if (warehouse == null) {
                errors.add(new LocationImportResponse.ImportError(rowNum, "仓库不存在"));
                continue;
            }

            // 7. 处理父节点
            Long parentId = null;
            String parentName = null;
            String parentFullpathName = null;
            int locationLevel = 1;
            int locationLevelCount = 1; // 默认1层

            if (record.getParentLocationFullpathName() != null && !record.getParentLocationFullpathName().trim().isEmpty()) {
                // 查询父节点
                List<WmsLocation> parentList = wmsLocationService.queryByFullpathName(
                        record.getWarehouseCode().trim(),
                        record.getParentLocationFullpathName().trim()
                );
                if (parentList == null || parentList.isEmpty()) {
                    errors.add(new LocationImportResponse.ImportError(rowNum, "上级库位不存在"));
                    continue;
                }
                WmsLocation parent = parentList.get(0);
                parentId = parent.getId();
                parentName = parent.getLocationName();
                parentFullpathName = parent.getLocationFullpathName();
                locationLevel = parent.getLocationLevel() + 1;
                locationLevelCount = parent.getLocationLevelCount();
            }

            // 8. 存储模式校验（默认 Shared）
            String storageMode = "Shared";
            if (record.getStorageMode() != null && !record.getStorageMode().trim().isEmpty()) {
                String sm = record.getStorageMode().trim();
                if ("Exclusive".equals(sm) || "独占".equals(sm)) {
                    storageMode = "Exclusive";
                } else if ("Shared".equals(sm) || "共享".equals(sm)) {
                    storageMode = "Shared";
                } else {
                    errors.add(new LocationImportResponse.ImportError(rowNum, "存储模式仅支持 Exclusive/Shared"));
                    continue;
                }
            }

            // 9. 规格校验（独占模式必填）
            String specification = record.getSpecification();
            if ("Exclusive".equals(storageMode)) {
                if (specification == null || specification.trim().isEmpty()) {
                    errors.add(new LocationImportResponse.ImportError(rowNum, "独占模式必须填写规格"));
                    continue;
                }
                // 校验规格格式
                if (!isValidSpecification(specification.trim())) {
                    errors.add(new LocationImportResponse.ImportError(rowNum, "规格格式错误，支持格式如：4x4、8x12、9x9"));
                    continue;
                }
            }

            // 10. 重复校验：同一仓库内同名校验
            int count = wmsLocationService.countByNameAndWarehouse(
                    record.getWarehouseCode().trim(),
                    record.getLocationName().trim(),
                    parentId,
                    null
            );
            if (count > 0) {
                String parentHint = parentName != null ? "（上级：" + parentName + "）" : "（根节点）";
                errors.add(new LocationImportResponse.ImportError(rowNum, "该仓库下已存在同名库位" + parentHint));
                continue;
            }

            // 11. 生成全路径名称
            String locationFullpathName = buildFullpathName(record.getLocationName().trim(), parentFullpathName);
            String locationSortNo = generateSortNo(parentId, locationLevel);

            // 构建实体
            WmsLocation entity = new WmsLocation();
            entity.setParentId(parentId);
            entity.setLocationGrade(determineLocationGrade(record.getLocationType().trim()));
            entity.setLocationType(record.getLocationType().trim());
            entity.setLocationLevel(locationLevel);
            entity.setLocationLevelCount(locationLevelCount);
            entity.setLocationName(record.getLocationName().trim());
            entity.setWarehouseCode(record.getWarehouseCode().trim());
            entity.setParentName(parentName);
            entity.setStorageMode(storageMode);
            entity.setSpecification("Exclusive".equals(storageMode) ? specification.trim() : null);
            entity.setIsUse(0);
            entity.setLocationSortNo(locationSortNo);
            entity.setLocationFullpathName(locationFullpathName);
            entity.setIsDeleted(0);
            fillSystemFields(entity);

            successEntities.add(entity);
        }

        // 批量插入成功的数据
        if (!successEntities.isEmpty()) {
            try {
                wmsLocationService.insertBatchAtomic(successEntities);
                clearLocationCache();
            } catch (Exception e) {
                log.error("批量导入库位失败", e);
                errors.add(new LocationImportResponse.ImportError(-1, "批量保存失败：" + e.getMessage()));
                successEntities.clear();
            }
        }

        // 构建响应
        LocationImportResponse response = new LocationImportResponse();
        response.setSuccessCount(successEntities.size());
        response.setFailCount(errors.size());
        response.setErrors(errors);

        if (errors.isEmpty()) {
            return R.ok(response, i18nService.getMessage("import.success", successEntities.size()));
        } else if (successEntities.isEmpty()) {
            return R.fail(response, i18nService.getMessage("import.all.fail"));
        } else {
            return R.ok(response, i18nService.getMessage("import.partial.success", successEntities.size(), errors.size()));
        }
    }

    /**
     * 校验规格格式
     */
    private boolean isValidSpecification(String spec) {
        if (spec == null || spec.isEmpty()) {
            return false;
        }
        // 支持格式：4x4, 8x12, 9x9, 96孔, 48孔
        return spec.matches("^\\d+\\s*[xX]\\s*\\d+$") || spec.matches("^\\d+\\s*孔$");
    }

    /**
     * 解析规格获取容量
     */
    private int parseSpecCapacity(String specification) {
        if (specification == null || specification.isEmpty()) {
            return 0;
        }
        // 解析 4x4, 8x12, 9x9
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)\\s*x\\s*(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = pattern.matcher(specification);
        if (matcher.find()) {
            int rows = Integer.parseInt(matcher.group(1));
            int cols = Integer.parseInt(matcher.group(2));
            return rows * cols;
        }
        // 解析 96孔, 48孔
        java.util.regex.Pattern numPattern = java.util.regex.Pattern.compile("(\\d+)\\s*孔");
        java.util.regex.Matcher numMatcher = numPattern.matcher(specification);
        if (numMatcher.find()) {
            return Integer.parseInt(numMatcher.group(1));
        }
        return 0;
    }

    /**
     * 确定库位等级：根据字典数据动态判断，不再硬编码
     */
    private String determineLocationGrade(String locationType) {
        if (locationType == null || locationType.trim().isEmpty()) {
            return "存储对象";
        }
        String grade = getLocationTypeGradeCache().get(locationType.trim());
        return grade != null ? grade : "存储对象";
    }

    /**
     * 构建仓库显示名称
     */
    private String buildWarehouseDisplayName(Warehouse warehouse) {
        StringBuilder sb = new StringBuilder();
        // 仓库类型标签
        String typeLabel = "";
        if ("QUARANTINE".equals(warehouse.getWarehouseType())) {
            typeLabel = "隔离仓";
        } else if ("SAMPLE".equals(warehouse.getWarehouseType())) {
            typeLabel = "留样仓";
        } else {
            typeLabel = warehouse.getWarehouseType();
        }
        sb.append(typeLabel);

        // 温区
        if (warehouse.getTemperatureZone() != null) {
            sb.append(" - ").append(warehouse.getTemperatureZone()).append("区");
        }

        // 仓库名称
        sb.append("（").append(warehouse.getWarehouseCode()).append("）");

        return sb.toString();
    }

    /**
     * 生成排序号
     */
    private String generateSortNo(Long parentId, int locationLevel) {
        if (parentId == null) {
            return "L" + String.format("%04d", 1);
        }
        return "L" + String.format("%04d", locationLevel);
    }
}
