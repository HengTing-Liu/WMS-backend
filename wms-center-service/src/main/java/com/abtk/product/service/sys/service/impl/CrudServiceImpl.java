package com.abtk.product.service.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.dao.mapper.ColumnMetaMapper;
import com.abtk.product.dao.mapper.DynamicMapper;
import com.abtk.product.dao.mapper.TableMetaMapper;
import com.abtk.product.dao.util.SqlInjectionValidator;
import com.abtk.product.dao.entity.ColumnMeta;
import com.abtk.product.dao.entity.TableMeta;
import com.abtk.product.service.permission.util.CrudPermissionUtil;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.sys.service.CrudService;
import com.abtk.product.service.system.service.I18nService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * 闂備緡鍋呭銊╁极?CRUD 闂佸搫鐗嗙粔瀛樻叏閻旇　鍋撻崷顓炰户妤?
 */
@Slf4j
@Service
public class CrudServiceImpl implements CrudService {

    /** sys_user 闁荤偞绋忛崝宥夋偉閿濆洦瀚?*/
    private static final String SYS_USER_TABLE = "sys_user";

    /** sys_user 婵炴垶鎸搁…鐑藉极椤撱垹绀嗘俊銈傚亾闁?*/
    private static final String SYS_USER_PK = "user_id";

    /** 婵帗绋掗…鍫ヮ敇婵犳碍鐒婚柡鍕箳鐢棝鏌涢幒鏂库枅婵炲懎閰ｅ畷姘旈埀顒勫箖閺囥垺鏅柛顐ｇ矌閻熸捇鏌涢幒鎾崇闁搞倕閰ｅ浠嬫偂鎼达絿顢呴梺鎸庣☉婵傛梻绮畝鍕瀬闁绘鐗嗙粊锕傚箹鐎涙ɑ灏柛顭戜邯瀹曘儱顓奸崼顐ｇ秷闂佽偐鍘ч崯鈺冪博閹绢喗鍤婇弶鍫濆⒔缁€?*/
    private static final String DEFAULT_DELETE_COLUMN = "is_deleted";
    private final Map<String, String> pkColumnCache = new java.util.concurrent.ConcurrentHashMap<>();

    /** 闂佸憡甯掑Λ婵嬪Υ婢舵劕鐭楅柛灞剧⊕濞堝爼鏌涘顒傗枌缂佽鲸鐟╁Λ渚€鍩€椤掑倹鍟哄ù锝夘棑閻倝鏌＄仦璇插姤妞ゆ洘顨婂鍫曞灳閸欏鍋ㄦ繛鎴炴惄閸樼晫鏁幘缁樷挃闁靛牆绻掔粈?*/
    private static final Set<String> PAGE_PARAMS = new HashSet<>(
            Arrays.asList("pageNum", "pageSize", "orderByColumn", "isAsc")
    );
    /** 闁诲孩绋掗〃鍡涱敊瀹€鍕Е鐎广儱娲﹂銈夋煕濮橆剛孝鐎规洜鍠庨～銏ゆ晲閸涱厾浠ч梺鎸庣⊕閻喚鍒掓惔銊ョ濞达絿鐡旈崯鍛存倵濞戞瑯娈曢柣锔界箞婵″瓨鎷呯憴鍕啀闁诲孩绋掗妵婊堝焵椤戞寧顦风紒妤€鎳樺畷姘跺箳閺傛寧鐤?*/
    private static final java.util.regex.Pattern SAFE_FIELD_PATTERN =
            java.util.regex.Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
    private static final String QUERY_MODE_EQ = "eq";
    private static final String QUERY_MODE_LIKE = "like";

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private TableMetaMapper tableMetaMapper;

    @Autowired
    private ColumnMetaMapper columnMetaMapper;

    @Autowired
    private I18nService i18nService;

    /**
     * 闂佸吋鍎抽崲鑼躲亹閸モ晜鍋橀柕濞у嫮鏆犻梻渚囧亝濡叉帞娆㈤锕€绀嗛柣妯肩帛閻濈喖鏌涢幒鎿冩當闁?
     * @param tableCode 闁荤偞绋忛崝宥夋偉閿濆洦瀚?
     * @return 闂佸憡甯炴繛鈧繛鍛叄瀹曟艾螖閳ь剟骞?
     */
    private String getDeleteColumn(String tableCode) {
        // inv_material/inv_warehouse/inv_warehouse_receiver 缂備焦绋戦ˇ顔剧礊閸℃顩烽柨婵嗘川閸ㄦ娊鎮跺☉鏍у婵炵厧瀛╅幏鍛崉閵婏附娈?is_deleted 闂佸憡甯楅〃鎰濠靛鐒婚柡鍕箳鐢棝鏌涢幒鏂库枅婵炲懎閰ｅ畷?= 0
        return DEFAULT_DELETE_COLUMN;
    }

    /**
     * 闂佸吋鍎抽崲鑼躲亹閸モ晜鍋橀柕濞у嫮鏆犳繛鎴炴尭椤兘寮銏犵婵°倐鍋撻柟?
     * @param tableCode 闁荤偞绋忛崝宥夋偉閿濆洦瀚?
     * @return 婵炴垶鎸搁…鐑藉极椤撱垹绀嗘俊銈傚亾闁?
     */
    private String getPkColumn(String tableCode) {
        if (SYS_USER_TABLE.equals(tableCode)) {
            return SYS_USER_PK;
        }
        String cached = pkColumnCache.get(tableCode);
        if (cached != null) {
            return cached;
        }
        String pk = detectPkColumnFromDb(tableCode);
        pkColumnCache.put(tableCode, pk);
        return pk;
    }

    private String detectPkColumnFromDb(String tableCode) {
        try {
            List<Map<String, Object>> columns = dynamicMapper.selectTableColumns(tableCode);
            for (Map<String, Object> col : columns) {
                Object keyObj = col.get("column_key");
                if ("PRI".equals(String.valueOf(keyObj))) {
                    return String.valueOf(col.get("column_name"));
                }
            }
        } catch (Exception e) {
            log.warn("Failed to detect pk column for table {}: {}", tableCode, e.getMessage());
        }
        return "id";
    }
    @Override
    public TableDataInfo list(String tableCode, Map<String, Object> params, Integer pageNum, Integer pageSize) {
        SqlInjectionValidator.validateTable(tableCode);
        // 闁哄鏅涘ú锕傚箮閵堝绀嗛柛鈩冪◤閳ь剙顦靛畷锝夊磼濞戞瑦顔嶉梺鍛婄矊閼活垶宕欓敓鐘插珘濠㈣泛鏈悾?dataScope key闂佹寧绋戝鎭唗aScope 闂?injectDataScope 濠电偛顦崝宀勫矗閸℃稑鍌ㄩ柣鏂款殠濞?SQL闂佹寧绋戦惌鍌炲焵椤掍椒浜㈢紒璇插暣瀹曪繝寮撮悩宸痪闂佸憡鐟ラ崐褰掑汲閻旂顕遍柣妯兼暩閼?mapper闂?
        Map<String, Object> filteredParams = new HashMap<>();
        Map<String, String> rawQueryModes = new HashMap<>();
        if (params != null) {
            params.forEach((key, value) -> {
                if ("queryModes".equals(key)) {
                    rawQueryModes.putAll(parseQueryModes(value));
                    return;
                }
                if (!PAGE_PARAMS.contains(key)
                        && !"dataScope".equals(key)
                        && value != null && !"".equals(value)) {
                    String sqlKey = toSqlFieldName(key);
                    filteredParams.put(sqlKey, value);
                }
            });
        }
        Map<String, String> queryModes = buildSafeQueryModes(rawQueryModes, filteredParams.keySet());
        // 闂佽桨鑳舵晶妤€鐣垫笟鈧鍫曞礃椤旂瓔鈧瑦绻涙径鍫濆闁告瑥妫濋弫宥夊醇閵忥紕鍑介梺瑙勪航閸庝即骞堥妸鈺佺哗?filteredParams闂佹寧绋戦張顒勫极閻愬搫绀?dataScope raw SQL 闂佺粯顨呭ú锕傤敊瀹€鍕櫖?
        CrudPermissionUtil.injectDataScope(filteredParams);
        String orderByColumn = params != null ? (String) params.get("orderByColumn") : null;
        String isAsc = params != null ? (String) params.get("isAsc") : null;
        String orderBy = "";
        if (StringUtils.isNotEmpty(orderByColumn)) {
            orderBy = StringUtils.toUnderScoreCase(orderByColumn) + " " + (StringUtils.isNotEmpty(isAsc) ? isAsc : "asc");
        }
        PageHelper.startPage(pageNum, pageSize, orderBy);
        String deleteColumn = getDeleteColumn(tableCode);
        String dataScope = (String) filteredParams.remove("dataScope");
        List<Map<String, Object>> list = dynamicMapper.selectList(tableCode, filteredParams, queryModes, deleteColumn, dataScope);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
        TableDataInfo dataTable = new TableDataInfo();
        // 闂佽桨娴峰ú鏍告奖濡?SQL 闂佽桨娴风槐鍟搁柣銏犵?闁稿海娴锋晶妤€鐣垫ā闁告挸绉堕崢宀€澧愭ā闁稿海娴锋ú鏍告奖濡?
        List<Map<String, Object>> normalizedRows = new ArrayList<>();
        for (Map<String, Object> row : list) {
            Map<String, Object> normalized = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                normalized.put(sqlFieldToCamelCase(entry.getKey()), entry.getValue());
            }
            normalizedRows.add(normalized);
        }
        dataTable.setRows(normalizedRows);
        dataTable.setTotal(pageInfo.getTotal());
        return dataTable;
    }

    @Override
    public List<Map<String, Object>> listAll(String tableCode, Map<String, Object> params) {
        SqlInjectionValidator.validateTable(tableCode);
        // 闁哄鏅涘ú锕傚箮閵堝绀嗛柛鈩冪◤閳ь剙顦靛畷锝夊磼濞戞瑦顔?
        Map<String, Object> filteredParams = new HashMap<>();
        Map<String, String> rawQueryModes = new HashMap<>();
        if (params != null) {
            params.forEach((key, value) -> {
                if ("queryModes".equals(key)) {
                    rawQueryModes.putAll(parseQueryModes(value));
                    return;
                }
                if (!PAGE_PARAMS.contains(key)
                        && !"dataScope".equals(key)
                        && value != null && !"".equals(value)) {
                    String sqlKey = toSqlFieldName(key);
                    filteredParams.put(sqlKey, value);
                }
            });
        }
        Map<String, String> queryModes = buildSafeQueryModes(rawQueryModes, filteredParams.keySet());
        CrudPermissionUtil.injectDataScope(filteredParams);
        String deleteColumn = getDeleteColumn(tableCode);
        String dataScope = (String) filteredParams.remove("dataScope");
        List<Map<String, Object>> rawList = dynamicMapper.selectAll(tableCode, filteredParams, queryModes, deleteColumn, dataScope);
        List<Map<String, Object>> normalized = new ArrayList<>();
        for (Map<String, Object> row : rawList) {
            Map<String, Object> normRow = new LinkedHashMap<>();
            for (Map.Entry<String, Object> e : row.entrySet()) {
                normRow.put(sqlFieldToCamelCase(e.getKey()), e.getValue());
            }
            normalized.add(normRow);
        }
        return normalized;
    }

    @Override
    public Map<String, Object> getById(String tableCode, Long id) {
        SqlInjectionValidator.validateTable(tableCode);
        String pkColumn = getPkColumn(tableCode);
        Map<String, Object> result = dynamicMapper.selectByIdWithColumn(tableCode, pkColumn, id);
        if (result == null) {
            throw new ServiceException(i18nService.getMessage("crud.entity.not.found", tableCode));
        }
        Map<String, Object> normalized = new LinkedHashMap<>();
        for (Map.Entry<String, Object> e : result.entrySet()) {
            normalized.put(sqlFieldToCamelCase(e.getKey()), e.getValue());
        }
        return normalized;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(String tableCode, Map<String, Object> data) {
        SqlInjectionValidator.validateTable(tableCode);

        Map<String, Object> normalizedData = normalizeDataKeysForSql(data);
        normalizedData.remove("id");
        normalizedData.remove("create_time");
        normalizedData.remove("create_by");
        normalizedData.put("create_time", new Date());
        normalizedData.put("create_by", SecurityUtils.getUsername());

        if (SYS_USER_TABLE.equals(tableCode) && normalizedData.containsKey("user_id")) {
            normalizedData.put("id", normalizedData.get("user_id"));
        }

        for (String key : normalizedData.keySet()) {
            if (!SAFE_FIELD_PATTERN.matcher(key).matches()) {
                throw new ServiceException("闂堢偞纭堕惃鍕摟濞堥潧鎮? " + key);
            }
        }

        List<String> columns = new ArrayList<>(normalizedData.keySet());
        List<Object> values = new ArrayList<>();
        for (String col : columns) {
            values.add(normalizedData.get(col));
        }

        dynamicMapper.insertParam(tableCode, columns, values);
        Map<String, Object> lastId = dynamicMapper.selectLastInsertId();
        if (lastId != null && lastId.get("id") != null) {
            return ((Number) lastId.get("id")).longValue();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String tableCode, Long id, Map<String, Object> data) {
        SqlInjectionValidator.validateTable(tableCode);

        String pkColumn = getPkColumn(tableCode);
        Map<String, Object> normalizedData = normalizeDataKeysForSql(data);
        normalizedData.put("update_time", new Date());
        normalizedData.put("update_by", SecurityUtils.getUsername());

        normalizedData.remove(pkColumn);
        normalizedData.remove("id");
        normalizedData.remove("create_time");
        normalizedData.remove("create_by");

        if (SYS_USER_TABLE.equals(tableCode)) {
            normalizedData.remove("id");
        }

        for (String key : normalizedData.keySet()) {
            if (!SAFE_FIELD_PATTERN.matcher(key).matches()) {
                throw new ServiceException("闂堢偞纭堕惃鍕摟濞堥潧鎮? " + key);
            }
        }

        dynamicMapper.updateParam(tableCode, normalizedData, pkColumn, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String tableCode, Long id) {
        SqlInjectionValidator.validateTable(tableCode);
        String pkColumn = getPkColumn(tableCode);
        String deleteColumn = getDeleteColumn(tableCode);
        dynamicMapper.logicDelete(tableCode, pkColumn, id, deleteColumn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(String tableCode, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        SqlInjectionValidator.validateTable(tableCode);
        String pkColumn = getPkColumn(tableCode);
        String deleteColumn = getDeleteColumn(tableCode);
        dynamicMapper.batchLogicDelete(tableCode, pkColumn, ids, deleteColumn);
    }

    @Override
    public boolean checkUnique(String tableCode, String field, String value, Long excludeId) {
        SqlInjectionValidator.validateTable(tableCode);
        String sqlField = toSqlFieldName(field);
        SqlInjectionValidator.validateField(sqlField);
        Long count = dynamicMapper.checkUnique(tableCode, sqlField, value, excludeId);
        return count == 0;
    }

    @Override
    public Map<String, Object> exportList(String tableCode, Map<String, Object> params) {
        SqlInjectionValidator.validateTable(tableCode);

        // 闁哄鏅涘ú锕傚箮閵堝绠冲璺猴工閻庤顪冮妶澶嬫锭鐎殿噮鍓熷?
        Map<String, Object> filteredParams = new HashMap<>();
        Map<String, String> queryModes = new HashMap<>();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if ("queryModes".equals(key)) {
                    queryModes.putAll(parseQueryModes(value));
                    continue;
                }
                if (!PAGE_PARAMS.contains(key) && value != null && !"".equals(value)) {
                    String sqlKey = toSqlFieldName(key);
                    filteredParams.put(sqlKey, value);
                }
            }
        }
        queryModes = buildSafeQueryModes(queryModes, filteredParams.keySet());
        // 濠电偛顦崝宀勫矗閸℃稑鏋侀柣妤€鐗嗙粊锕傛煛婢跺﹤鏆ｆ俊?
        CrudPermissionUtil.injectDataScope(filteredParams);

        // 闂佸吋鍎抽崲鑼躲亹閸ヮ剚鐒婚柡鍕箳鐢棝鏌涢幒鏂库枅婵炲懎閰ｅ畷?
        String deleteColumn = getDeleteColumn(tableCode);
        String dataScope = (String) filteredParams.remove("dataScope");

        // 闂佸搫琚崕鎾敋濡ゅ懎绀傞柕濞炬櫅閸斻儵鏌℃担鍝勵暭鐎规挷绶氶弫宥夊醇濠婂懐鎲归梺鍛婂笒濡繈濡存径鎰櫖?
        List<Map<String, Object>> dataList = dynamicMapper.selectAll(tableCode, filteredParams, queryModes, deleteColumn, dataScope);

        // 闁诲繐绻愬Λ娆撳汲閻旂厧绠?Map 闂?key 婵炲濮寸花鑲╃箔閸涙潙绀嗛柟鐑樻煥濞堢娀寮堕悜鍡楀幐閻犳劗鍠愰妵娆撴偂鎼粹剝些闂佹寧绋戦悧鍛箔?ColumnMeta.field 闂佸搫绉堕崢褏妲愰埄鍐攳婵犻潧娲ら惁顔尖槈閹绢垰浜鹃梺鑲╂焿閹活亞妲?
        List<Map<String, Object>> normalizedDataList = new ArrayList<>();
        for (Map<String, Object> row : dataList) {
            Map<String, Object> normalizedRow = new HashMap<>();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String key = entry.getKey();
                String camelKey = sqlFieldToCamelCase(key);
                normalizedRow.put(camelKey, entry.getValue());
            }
            normalizedDataList.add(normalizedRow);
        }

        // 闂佸吋鍎抽崲鑼躲亹閸モ斁鍋撻悽闈涘付闁搞値鍘鹃埀顒佺⊕椤ㄥ棝顢欏畝鍕厐鐎广儱娲ㄩ弸?
        List<ColumnMeta> exportColumns = columnMetaMapper.selectByTableCode(tableCode);
        List<ColumnMeta> exportableColumns = exportColumns.stream()
                .filter(col -> col.getShowInExport() == null || col.getShowInExport() == 1)
                .sorted(Comparator.comparing(ColumnMeta::getSortOrder, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        // 闂佸搫顑呯€氼剛绱撻幘鍓佺＜闁规儳顕禍?
        Map<String, Object> result = new HashMap<>();
        result.put("dataList", normalizedDataList);
        result.put("columns", exportableColumns);
        return result;
    }

    /**
     * SQL 闁诲孩绋掗〃鍡涱敊瀹€鍕Е鐎广儱鐗婄粊顔碱渻閸︻厼甯堕柛銏＄叀瀹曘劑鎸婃径瀣偓?
     * 婵炴挻鑹鹃鍛淬€? warehouse_code -> warehouseCode, is_enabled -> isEnabled
     */
    private String sqlFieldToCamelCase(String sqlField) {
        if (sqlField == null || sqlField.isEmpty()) {
            return sqlField;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        for (int i = 0; i < sqlField.length(); i++) {
            char c = sqlField.charAt(i);
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }
    /**
     * 闁告挸绉堕?camelCase 閻庢稒顨嗛宀勫触瀹ュ牊绁?SQL snake_case闁挎稒绋戦崙锟犲及?snake_case 闁告帗鐟ョ敮顐﹀冀閻ゎ垳绠查柛?     */
    private String toSqlFieldName(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return fieldName;
        }
        if (fieldName.indexOf('_') >= 0) {
            return fieldName;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * 鐟滅増甯婄粩鎾礌閺嶃劍娈堕柟璇″枛閻⊙冣枔闂堟稒鍊冲☉?SQL snake_case闁挎稒绋撻埞?key 闁煎浜滄慨鈺勭疀閻ｅ本娈?
     */
    private Map<String, Object> normalizeDataKeysForSql(Map<String, Object> data) {
        Map<String, Object> normalized = new LinkedHashMap<>();
        if (data == null || data.isEmpty()) {
            return normalized;
        }
        data.forEach((key, value) -> {
            if (key == null || key.trim().isEmpty()) {
                return;
            }
            String sqlKey = toSqlFieldName(key.trim());
            normalized.put(sqlKey, value);
        });
        return normalized;
    }

    private Map<String, String> parseQueryModes(Object rawQueryModes) {
        Map<String, String> result = new HashMap<>();
        if (rawQueryModes == null) {
            return result;
        }
        try {
            String jsonText = String.valueOf(rawQueryModes);
            if (StringUtils.isEmpty(jsonText)) {
                return result;
            }
            JSONObject jsonObject = JSON.parseObject(jsonText);
            if (jsonObject == null) {
                return result;
            }
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                String field = toSqlFieldName(entry.getKey());
                String mode = normalizeQueryMode(entry.getValue());
                if (!StringUtils.isEmpty(field) && !StringUtils.isEmpty(mode)) {
                    result.put(field, mode);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to parse queryModes: {}", rawQueryModes);
        }
        return result;
    }

    private String normalizeQueryMode(Object rawMode) {
        if (rawMode == null) {
            return null;
        }
        String mode = String.valueOf(rawMode).toLowerCase(Locale.ROOT);
        if (QUERY_MODE_LIKE.equals(mode)) {
            return QUERY_MODE_LIKE;
        }
        if (QUERY_MODE_EQ.equals(mode)) {
            return QUERY_MODE_EQ;
        }
        return null;
    }

    private Map<String, String> buildSafeQueryModes(Map<String, String> requestedModes, Set<String> queryKeys) {
        Map<String, String> safeModes = new HashMap<>();
        if (requestedModes == null || requestedModes.isEmpty() || queryKeys == null || queryKeys.isEmpty()) {
            return safeModes;
        }
        for (String key : queryKeys) {
            String mode = requestedModes.get(key);
            if (QUERY_MODE_LIKE.equals(mode)) {
                safeModes.put(key, QUERY_MODE_LIKE);
            } else {
                safeModes.put(key, QUERY_MODE_EQ);
            }
        }
        return safeModes;
    }
}
