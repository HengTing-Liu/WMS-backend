package com.abtk.product.service.generator;

import com.abtk.product.api.generator.CodeGenerateConfig;
import com.abtk.product.api.generator.CodeTemplate;
import com.abtk.product.api.generator.GeneratedFile;
import com.abtk.product.dao.entity.ColumnMeta;
import com.abtk.product.dao.entity.TableMeta;
import com.abtk.product.dao.mapper.TableMetaMapper;
import com.abtk.product.dao.mapper.ColumnMetaMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 代码生成服务
 *
 * @author backend1
 * @date 2025-06-18
 */
@Service
public class CodeGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(CodeGeneratorService.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Autowired
    private TableMetaMapper tableMetaDao;

    @Autowired
    private ColumnMetaMapper columnMetaDao;

    /**
     * 获取所有代码模板
     */
    public List<CodeTemplate> getTemplates() {
        List<CodeTemplate> templates = new ArrayList<CodeTemplate>();

        // 后端模板
        CodeTemplate entityTemplate = new CodeTemplate();
        entityTemplate.setId("entity");
        entityTemplate.setName("Entity");
        entityTemplate.setDescription("Java Entity 实体类");
        entityTemplate.setType("entity");
        entityTemplate.setSuffix(".java");
        entityTemplate.setSelected(false);
        templates.add(entityTemplate);

        CodeTemplate mapperTemplate = new CodeTemplate();
        mapperTemplate.setId("mapper");
        mapperTemplate.setName("Mapper");
        mapperTemplate.setDescription("MyBatis Mapper 接口");
        mapperTemplate.setType("mapper");
        mapperTemplate.setSuffix("Mapper.java");
        mapperTemplate.setSelected(false);
        templates.add(mapperTemplate);

        CodeTemplate serviceTemplate = new CodeTemplate();
        serviceTemplate.setId("service");
        serviceTemplate.setName("Service");
        serviceTemplate.setDescription("Service 业务服务类");
        serviceTemplate.setType("service");
        serviceTemplate.setSuffix("Service.java");
        serviceTemplate.setSelected(false);
        templates.add(serviceTemplate);

        CodeTemplate controllerTemplate = new CodeTemplate();
        controllerTemplate.setId("controller");
        controllerTemplate.setName("Controller");
        controllerTemplate.setDescription("Controller 控制器类");
        controllerTemplate.setType("controller");
        controllerTemplate.setSuffix("Controller.java");
        controllerTemplate.setSelected(false);
        templates.add(controllerTemplate);

        // 前端模板
        CodeTemplate vueListTemplate = new CodeTemplate();
        vueListTemplate.setId("vue-list");
        vueListTemplate.setName("Vue列表页");
        vueListTemplate.setDescription("Vue 列表页组件");
        vueListTemplate.setType("vue-list");
        vueListTemplate.setSuffix("index.vue");
        vueListTemplate.setSelected(false);
        templates.add(vueListTemplate);

        CodeTemplate vueFormTemplate = new CodeTemplate();
        vueFormTemplate.setId("vue-form");
        vueFormTemplate.setName("Vue表单页");
        vueFormTemplate.setDescription("Vue 表单页组件");
        vueFormTemplate.setType("vue-form");
        vueFormTemplate.setSuffix("form.vue");
        vueFormTemplate.setSelected(false);
        templates.add(vueFormTemplate);

        return templates;
    }

    /**
     * 预览代码
     */
    public List<GeneratedFile> previewCode(CodeGenerateConfig config) {
        List<GeneratedFile> files = new ArrayList<GeneratedFile>();

        if (config.getTableId() == null) {
            return files;
        }

        // 查询表元数据
        TableMeta tableMeta = tableMetaDao.selectById(config.getTableId());
        if (tableMeta == null) {
            logger.warn("Table meta not found for id: {}", config.getTableId());
            return files;
        }

        // 查询列元数据
        List<ColumnMeta> columnMetas = columnMetaDao.selectByTableCode(tableMeta.getTableCode());
        if (columnMetas == null || columnMetas.isEmpty()) {
            logger.warn("Column metas not found for tableId: {}", config.getTableId());
            return files;
        }

        // 生成代码
        if (config.getTemplates() != null) {
            for (String template : config.getTemplates()) {
                GeneratedFile file = generateCode(template, config, tableMeta, columnMetas);
                if (file != null) {
                    files.add(file);
                }
            }
        }

        return files;
    }

    /**
     * 生成单个文件代码
     */
    private GeneratedFile generateCode(String template, CodeGenerateConfig config, TableMeta tableMeta, List<ColumnMeta> columnMetas) {
        GeneratedFile file = new GeneratedFile();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String date = sdf.format(new Date());
        String author = StringUtils.isNotBlank(config.getAuthor()) ? config.getAuthor() : "backend1";

        // 根据模板类型生成代码
        if ("entity".equals(template)) {
            file.setFileName(tableMeta.getEntityClass() + ".java");
            file.setFilePath(config.getBackendPath() + "/entity/" + tableMeta.getEntityClass() + ".java");
            file.setFileType("backend");
            file.setContent(generateEntityCode(tableMeta, columnMetas, author, date, config));
        } else if ("mapper".equals(template)) {
            file.setFileName(tableMeta.getEntityClass() + "Mapper.java");
            file.setFilePath(config.getBackendPath() + "/mapper/" + tableMeta.getEntityClass() + "Mapper.java");
            file.setFileType("backend");
            file.setContent(generateMapperCode(tableMeta, author, date, config));
        } else if ("service".equals(template)) {
            file.setFileName(tableMeta.getEntityClass() + "Service.java");
            file.setFilePath(config.getBackendPath() + "/service/" + tableMeta.getEntityClass() + "Service.java");
            file.setFileType("backend");
            file.setContent(generateServiceCode(tableMeta, author, date, config));
        } else if ("controller".equals(template)) {
            file.setFileName(tableMeta.getEntityClass() + "Controller.java");
            file.setFilePath(config.getBackendPath() + "/controller/" + tableMeta.getEntityClass() + "Controller.java");
            file.setFileType("backend");
            file.setContent(generateControllerCode(tableMeta, author, date, config));
        } else if ("vue-list".equals(template)) {
            file.setFileName("index.vue");
            file.setFilePath(config.getFrontendPath() + "/" + tableMeta.getEntityClass() + "/index.vue");
            file.setFileType("frontend");
            file.setContent(generateVueListCode(tableMeta, columnMetas, author, date));
        } else if ("vue-form".equals(template)) {
            file.setFileName("form.vue");
            file.setFilePath(config.getFrontendPath() + "/" + tableMeta.getEntityClass() + "/form.vue");
            file.setFileType("frontend");
            file.setContent(generateVueFormCode(tableMeta, columnMetas, author, date));
        } else {
            return null;
        }

        return file;
    }

    /**
     * 生成 Entity 代码
     */
    private String generateEntityCode(TableMeta tableMeta, List<ColumnMeta> columnMetas, String author, String date, CodeGenerateConfig config) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(config.getPackageName()).append(".entity;\n\n");
        sb.append("import java.io.Serializable;\n");
        sb.append("import java.util.Date;\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(tableMeta.getTableName()).append("\n");
        sb.append(" *\n");
        sb.append(" * @author ").append(author).append("\n");
        sb.append(" * @date ").append(date).append("\n");
        sb.append(" */\n");
        sb.append("public class ").append(tableMeta.getEntityClass()).append(" implements Serializable {\n\n");
        sb.append("    private static final long serialVersionUID = 1L;\n\n");

        // 生成字段
        for (ColumnMeta column : columnMetas) {
            sb.append("    /**\n");
            sb.append("     * ").append(column.getTitle()).append("\n");
            sb.append("     */\n");
            sb.append("    private ").append(getJavaType(column.getDataType())).append(" ").append(column.getField()).append(";\n\n");
        }

        // 生成 getter 和 setter
        for (ColumnMeta column : columnMetas) {
            String fieldName = column.getField();
            String capFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String javaType = getJavaType(column.getDataType());

            sb.append("    public ").append(javaType).append(" get").append(capFieldName).append("() {\n");
            sb.append("        return ").append(fieldName).append(";\n");
            sb.append("    }\n\n");

            sb.append("    public void set").append(capFieldName).append("(").append(javaType).append(" ").append(fieldName).append(") {\n");
            sb.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            sb.append("    }\n\n");
        }

        sb.append("}\n");
        return sb.toString();
    }

    /**
     * 生成 Mapper 代码
     */
    private String generateMapperCode(TableMeta tableMeta, String author, String date, CodeGenerateConfig config) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(config.getPackageName()).append(".mapper;\n\n");
        sb.append("import ").append(config.getPackageName()).append(".entity.").append(tableMeta.getEntityClass()).append(";\n");
        sb.append("import org.apache.ibatis.annotations.Mapper;\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(tableMeta.getTableName()).append(" Mapper\n");
        sb.append(" *\n");
        sb.append(" * @author ").append(author).append("\n");
        sb.append(" * @date ").append(date).append("\n");
        sb.append(" */\n");
        sb.append("@Mapper\n");
        sb.append("public interface ").append(tableMeta.getEntityClass()).append("Mapper {\n");
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * 生成 Service 代码
     */
    private String generateServiceCode(TableMeta tableMeta, String author, String date, CodeGenerateConfig config) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(config.getPackageName()).append(".service;\n\n");
        sb.append("import ").append(config.getPackageName()).append(".entity.").append(tableMeta.getEntityClass()).append(";\n");
        sb.append("import ").append(config.getPackageName()).append(".mapper.").append(tableMeta.getEntityClass()).append("Mapper;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.stereotype.Service;\n");
        sb.append("import java.util.List;\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(tableMeta.getTableName()).append(" Service\n");
        sb.append(" *\n");
        sb.append(" * @author ").append(author).append("\n");
        sb.append(" * @date ").append(date).append("\n");
        sb.append(" */\n");
        sb.append("@Service\n");
        sb.append("public class ").append(tableMeta.getEntityClass()).append("Service {\n\n");
        sb.append("    @Autowired\n");
        sb.append("    private ").append(tableMeta.getEntityClass()).append("Mapper mapper;\n\n");
        sb.append("    public ").append(tableMeta.getEntityClass()).append(" selectById(Long id) {\n");
        sb.append("        return mapper.selectById(id);\n");
        sb.append("    }\n\n");
        sb.append("    public List<").append(tableMeta.getEntityClass()).append("> selectAll() {\n");
        sb.append("        return mapper.selectAll();\n");
        sb.append("    }\n\n");
        sb.append("    public int insert(").append(tableMeta.getEntityClass()).append(" record) {\n");
        sb.append("        return mapper.insert(record);\n");
        sb.append("    }\n\n");
        sb.append("    public int update(").append(tableMeta.getEntityClass()).append(" record) {\n");
        sb.append("        return mapper.update(record);\n");
        sb.append("    }\n\n");
        sb.append("    public int deleteById(Long id) {\n");
        sb.append("        return mapper.deleteById(id);\n");
        sb.append("    }\n");
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * 生成 Controller 代码
     */
    private String generateControllerCode(TableMeta tableMeta, String author, String date, CodeGenerateConfig config) {
        String entityClass = tableMeta.getEntityClass();
        String moduleName = config.getModuleName() != null ? config.getModuleName() : "api";
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(config.getPackageName()).append(".controller;\n\n");
        sb.append("import ").append(config.getPackageName()).append(".entity.").append(entityClass).append(";\n");
        sb.append("import ").append(config.getPackageName()).append(".service.").append(entityClass).append("Service;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.web.bind.annotation.*;\n");
        sb.append("import java.util.List;\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(tableMeta.getTableName()).append(" Controller\n");
        sb.append(" *\n");
        sb.append(" * @author ").append(author).append("\n");
        sb.append(" * @date ").append(date).append("\n");
        sb.append(" */\n");
        sb.append("@RestController\n");
        sb.append("@RequestMapping(\"/").append(moduleName).append("/").append(toLowerCaseFirst(entityClass)).append("\")\n");
        sb.append("public class ").append(entityClass).append("Controller {\n\n");
        sb.append("    @Autowired\n");
        sb.append("    private ").append(entityClass).append("Service service;\n\n");
        sb.append("    @GetMapping(\"/\u003cid\u003e\")\n");
        sb.append("    public ").append(entityClass).append(" get(@PathVariable Long id) {\n");
        sb.append("        return service.selectById(id);\n");
        sb.append("    }\n\n");
        sb.append("    @GetMapping\n");
        sb.append("    public List<").append(entityClass).append("> list() {\n");
        sb.append("        return service.selectAll();\n");
        sb.append("    }\n\n");
        sb.append("    @PostMapping\n");
        sb.append("    public int create(@RequestBody ").append(entityClass).append(" record) {\n");
        sb.append("        return service.insert(record);\n");
        sb.append("    }\n\n");
        sb.append("    @PutMapping\n");
        sb.append("    public int update(@RequestBody ").append(entityClass).append(" record) {\n");
        sb.append("        return service.update(record);\n");
        sb.append("    }\n\n");
        sb.append("    @DeleteMapping(\"/\u003cid\u003e\")\n");
        sb.append("    public int delete(@PathVariable Long id) {\n");
        sb.append("        return service.deleteById(id);\n");
        sb.append("    }\n");
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * 生成 Vue 列表页代码
     */
    private String generateVueListCode(TableMeta tableMeta, List<ColumnMeta> columnMetas, String author, String date) {
        StringBuilder sb = new StringBuilder();
        String entityClass = tableMeta.getEntityClass();
        sb.append("<template>\n");
        sb.append("  <div class=\"").append(toLowerCaseFirst(entityClass)).append("-list\">\n");
        sb.append("    <!-- 查询表单 -->\n");
        sb.append("    <a-form layout=\"inline\">\n");
        sb.append("      <a-form-item label=\"\">\n");
        sb.append("        <a-input v-model=\"query.keyword\" placeholder=\"请输入关键词\" />\n");
        sb.append("      </a-form-item>\n");
        sb.append("      <a-form-item>\n");
        sb.append("        <a-button type=\"primary\" @click=\"search\">查询</a-button>\n");
        sb.append("        <a-button style=\"margin-left: 8px\" @click=\"reset\">重置</a-button>\n");
        sb.append("      </a-form-item>\n");
        sb.append("    </a-form>\n\n");
        sb.append("    <!-- 操作按钮 -->\n");
        sb.append("    <div style=\"margin: 16px 0\">\n");
        sb.append("      <a-button type=\"primary\" @click=\"handleAdd\">新增</a-button>\n");
        sb.append("    </div>\n\n");
        sb.append("    <!-- 数据表格 -->\n");
        sb.append("    <a-table :columns=\"columns\" :data-source=\"dataList\" :pagination=\"pagination\" @change=\"handleTableChange\" />\n");
        sb.append("  </div>\n");
        sb.append("</template>\n\n");
        sb.append("<script>\n");
        sb.append("export default {\n");
        sb.append("  name: '").append(entityClass).append("List',\n");
        sb.append("  data() {\n");
        sb.append("    return {\n");
        sb.append("      query: {\n");
        sb.append("        keyword: ''\n");
        sb.append("      },\n");
        sb.append("      dataList: [],\n");
        sb.append("      pagination: {\n");
        sb.append("        current: 1,\n");
        sb.append("        pageSize: 10,\n");
        sb.append("        total: 0\n");
        sb.append("      },\n");
        sb.append("      columns: [\n");

        // 生成列定义
        boolean first = true;
        for (ColumnMeta column : columnMetas) {
            if (column.getShowInList() != null && column.getShowInList() == 1) {
                if (!first) {
                    sb.append(",\n");
                }
                sb.append("        {\n");
                sb.append("          title: '").append(column.getTitle()).append("',\n");
                sb.append("          dataIndex: '").append(column.getField()).append("',\n");
                sb.append("          key: '").append(column.getField()).append("'\n");
                sb.append("        }");
                first = false;
            }
        }
        sb.append("\n      ]\n");
        sb.append("    }\n");
        sb.append("  },\n");
        sb.append("  created() {\n");
        sb.append("    this.fetchData()\n");
        sb.append("  },\n");
        sb.append("  methods: {\n");
        sb.append("    fetchData() {\n");
        sb.append("      // 调用API获取数据\n");
        sb.append("    },\n");
        sb.append("    search() {\n");
        sb.append("      this.pagination.current = 1\n");
        sb.append("      this.fetchData()\n");
        sb.append("    },\n");
        sb.append("    reset() {\n");
        sb.append("      this.query = { keyword: '' }\n");
        sb.append("      this.pagination.current = 1\n");
        sb.append("      this.fetchData()\n");
        sb.append("    },\n");
        sb.append("    handleAdd() {\n");
        sb.append("      // 打开新增表单\n");
        sb.append("    },\n");
        sb.append("    handleTableChange(pagination) {\n");
        sb.append("      this.pagination = pagination\n");
        sb.append("      this.fetchData()\n");
        sb.append("    }\n");
        sb.append("  }\n");
        sb.append("}\n");
        sb.append("</script>\n\n");
        sb.append("<style scoped>\n");
        sb.append(".").append(toLowerCaseFirst(entityClass)).append("-list {\n");
        sb.append("  padding: 20px;\n");
        sb.append("}\n");
        sb.append("</style>\n");
        return sb.toString();
    }

    /**
     * 生成 Vue 表单页代码
     */
    private String generateVueFormCode(TableMeta tableMeta, List<ColumnMeta> columnMetas, String author, String date) {
        StringBuilder sb = new StringBuilder();
        String entityClass = tableMeta.getEntityClass();
        sb.append("<template>\n");
        sb.append("  <div class=\"").append(toLowerCaseFirst(entityClass)).append("-form\">\n");
        sb.append("    <a-form :form=\"form\" :label-col=\"{ span: 4 }\" :wrapper-col=\"{ span: 16 }\">\n");

        // 生成表单字段
        for (ColumnMeta column : columnMetas) {
            if (column.getShowInForm() != null && column.getShowInForm() == 1) {
                sb.append("      <a-form-item label=\"").append(column.getTitle()).append("\">\n");
                sb.append("        <a-input v-decorator=\"['").append(column.getField()).append("'");
                if (column.getRequired() != null && column.getRequired() == 1) {
                    sb.append(", { rules: [{ required: true, message: '请输入").append(column.getTitle()).append("' }] }");
                }
                sb.append("]\" />\n");
                sb.append("      </a-form-item>\n");
            }
        }

        sb.append("      <a-form-item :wrapper-col=\"{ span: 16, offset: 4 }\">\n");
        sb.append("        <a-button type=\"primary\" @click=\"handleSubmit\">提交</a-button>\n");
        sb.append("        <a-button style=\"margin-left: 8px\" @click=\"handleCancel\">取消</a-button>\n");
        sb.append("      </a-form-item>\n");
        sb.append("    </a-form>\n");
        sb.append("  </div>\n");
        sb.append("</template>\n\n");
        sb.append("<script>\n");
        sb.append("export default {\n");
        sb.append("  name: '").append(entityClass).append("Form',\n");
        sb.append("  data() {\n");
        sb.append("    return {\n");
        sb.append("      form: this.$form.createForm(this)\n");
        sb.append("    }\n");
        sb.append("  },\n");
        sb.append("  methods: {\n");
        sb.append("    handleSubmit() {\n");
        sb.append("      this.form.validateFields((err, values) => {\n");
        sb.append("        if (!err) {\n");
        sb.append("          // 提交数据\n");
        sb.append("        }\n");
        sb.append("      })\n");
        sb.append("    },\n");
        sb.append("    handleCancel() {\n");
        sb.append("      // 取消操作\n");
        sb.append("    }\n");
        sb.append("  }\n");
        sb.append("}\n");
        sb.append("</script>\n\n");
        sb.append("<style scoped>\n");
        sb.append(".").append(toLowerCaseFirst(entityClass)).append("-form {\n");
        sb.append("  padding: 20px;\n");
        sb.append("}\n");
        sb.append("</style>\n");
        return sb.toString();
    }

    /**
     * 获取Java类型
     */
    private String getJavaType(String dataType) {
        if (dataType == null) {
            return "String";
        }
        switch (dataType.toLowerCase()) {
            case "int":
            case "integer":
                return "Integer";
            case "bigint":
                return "Long";
            case "decimal":
            case "number":
                return "BigDecimal";
            case "date":
            case "datetime":
                return "Date";
            case "boolean":
                return "Boolean";
            case "text":
            case "string":
            default:
                return "String";
        }
    }

    /**
     * 首字母小写
     */
    private String toLowerCaseFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
