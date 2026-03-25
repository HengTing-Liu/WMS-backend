package com.abclonal.product;

//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.config.*;
//import com.baomidou.mybatisplus.generator.config.rules.DateType;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
//import com.baomidou.mybatisplus.annotation.IdType;
public class MyBatisPlusGenerator {
    // 项目根路径
    private static final String PROJECT_ROOT = "C:/Users/LHT/Desktop/wms-center";

    // 各模块路径 - 修正后的路径
    private static final String DAO_JAVA = PROJECT_ROOT + "/wms-center-dao/src/main/java";
    private static final String DAO_RESOURCES = PROJECT_ROOT + "/wms-center-dao/src/main/resources";
    private static final String SERVICE_JAVA = PROJECT_ROOT + "/wms-center-service/src/main/java";
    private static final String WEB_JAVA = PROJECT_ROOT + "/wms-center-web/src/main/java";
    // 数据库配置
    private static final String DB_URL = "jdbc:p6spy:mysql://10.201.0.34:3306/wms?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "test";
    private static final String DB_PASSWORD = "Test123!@#";

    // 表配置
    private static final String TABLE_NAME = "inv_qrcodedetail";  // 改成你的表名
    //生成的包名配置
    private static final String Dao_entity = "inv.entity";
    private static final String Dao_mapper = "inv.mapper";
    private static final String Dao_xml = "mapper.inv";

    private static final String Ser_I = "inv";
    private static final String Ser_Impl = "inv.impl";
    private static final String Con_url = "controller.inv";
    /** 作者 */
    private static final String AUTHOR = System.getProperty("user.name");
//    public static void main(String[] args) {
//        System.out.println("==================================");
//        System.out.println("MyBatis-Plus 代码生成器");
//        System.out.println("==================================");
//
//        // 先生成 DAO 层（实体、Mapper、XML）
//        generateDaoLayer();
//
//        // 再生成 Service 层
//        generateServiceLayer();
//
//        // 最后生成 Controller 层
//        generateControllerLayer();
//
//        System.out.println("\n✅ 所有代码生成完成！");
//        System.out.println("==================================");
//    }
//
//    /**
//     * 生成 DAO 层文件到 wms-center-dao 模块
//     * 包括：Entity, Mapper 接口, Mapper XML
//     */
//    private static void generateDaoLayer() {
//        System.out.println("\n📁 正在生成 DAO 层文件到 product-center-dao 模块...");
//
//        // 数据源配置
//        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(DB_URL, DB_USER, DB_PASSWORD).build();
//
//        // 全局配置 - 输出到 dao 模块的 java 目录
//        GlobalConfig globalConfig = new GlobalConfig.Builder()
//                .outputDir(PROJECT_ROOT + "/product-center-dao/src/main/java")
//                .author(AUTHOR)
//                .disableOpenDir()
//                .enableSwagger()
//                .dateType(DateType.ONLY_DATE)
//                .build();
//
//        // 包配置 - 只配置 dao 相关的包
//        PackageConfig packageConfig = new PackageConfig.Builder()
//                .parent("com.abclonal.product.dao")
//                .entity(Dao_entity)      // 实体类位置
//                .mapper(Dao_mapper)       // Mapper接口位置
//                .xml(Dao_xml)              // XML文件位置（相对于resources）
//                // 禁用其他包的生成
//                .service("")
//                .serviceImpl("")
//                .controller("")
//                .build();
//
//        // 策略配置
//        StrategyConfig strategyConfig = new StrategyConfig.Builder()
//                .addInclude(TABLE_NAME)
//                .entityBuilder()
//                .fileOverride()
//                .naming(NamingStrategy.underline_to_camel)
//                .columnNaming(NamingStrategy.underline_to_camel)
//                .enableLombok()
//                .enableTableFieldAnnotation()
//                .mapperBuilder()
//                .fileOverride()
//                .enableBaseResultMap()
//                .enableBaseColumnList()
//                .formatMapperFileName("%sMapper")
//                .formatXmlFileName("%sMapper")
//                .serviceBuilder()
//                .disable()  // 禁用Service生成
//                .controllerBuilder()
//                .disable()  // 禁用Controller生成
//                .build();
//
//        // 模板配置 - 禁用 XML 生成（我们会单独生成）
//        TemplateConfig templateConfig = new TemplateConfig.Builder()
//                .disable(TemplateType.XML)  // 关键：禁用XML生成
//                .build();
//        // 执行生成
//        new AutoGenerator(dataSourceConfig)
//                .global(globalConfig)
//                .packageInfo(packageConfig)
//                .strategy(strategyConfig)
//                .execute(new VelocityTemplateEngine());
//
//        // 单独生成 XML 文件到 resources 目录
//        generateDaoXml();
//    }
//
//    /**
//     * 专门生成 XML 文件到 resources 目录
//     */
//    private static void generateDaoXml() {
//        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(DB_URL, DB_USER, DB_PASSWORD).build();
//
//        GlobalConfig xmlGlobalConfig = new GlobalConfig.Builder()
//                .outputDir(PROJECT_ROOT + "/product-center-dao/src/main/resources")
//                .disableOpenDir()
//                .build();
//
//        PackageConfig xmlPackageConfig = new PackageConfig.Builder()
//                .parent("main")
//                .xml(Dao_xml)
//                .build();
//
//        StrategyConfig strategyConfig = new StrategyConfig.Builder()
//                .addInclude(TABLE_NAME)
//                .mapperBuilder()
//                .formatXmlFileName("%sMapper")
//                .build();
//
//        // 只生成 XML
//        // 关键：模板配置 - 禁用所有 Java 文件的生成
//        TemplateConfig templateConfig = new TemplateConfig.Builder()
//                .disable(TemplateType.ENTITY)    // 禁用实体生成
//                .disable(TemplateType.MAPPER)    // 禁用Mapper接口生成
//                .disable(TemplateType.SERVICE)   // 禁用Service生成
//                .disable(TemplateType.CONTROLLER) // 禁用Controller生成
//                // 注意：不调用 disable(TemplateType.XML)，这样XML就会生成
//                .build();
//
//        new AutoGenerator(dataSourceConfig)
//                .global(xmlGlobalConfig)
//                .packageInfo(xmlPackageConfig)
//                .strategy(strategyConfig)
//                .template(templateConfig)
//                .execute(new VelocityTemplateEngine());
//    }
//
//    /**
//     * 生成 Service 层文件到 product-center-service 模块
//     * 包括：Service接口, Service实现类
//     */
//    private static void generateServiceLayer() {
//        System.out.println("\n📁 正在生成 Service 层文件到 product-center-service 模块...");
//
//        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(DB_URL, DB_USER, DB_PASSWORD).build();
//
//        GlobalConfig globalConfig = new GlobalConfig.Builder()
//                .outputDir(PROJECT_ROOT + "/product-center-service/src/main/java")
//                .author(AUTHOR)
//                .disableOpenDir()
//                .enableSwagger()
//                .build();
//
//        PackageConfig packageConfig = new PackageConfig.Builder()
//                .parent("com.abclonal.product.service")
//                .service(Ser_I)          // Service接口
//                .serviceImpl(Ser_Impl) // Service实现类
//                // 禁用其他包的生成
//                .entity("")
//                .mapper("")
//                .xml("")
//                .controller("")
//                .build();
//
//        StrategyConfig strategyConfig = new StrategyConfig.Builder()
//                .addInclude(TABLE_NAME)
//                .serviceBuilder()
//                .fileOverride()
//                .formatServiceFileName("%sService")
//                .formatServiceImplFileName("%sServiceImpl")
//                .entityBuilder()
//                .disable()  // 禁用Entity生成
//                .mapperBuilder()
//                .disable()  // 禁用Mapper生成
//                .controllerBuilder()
//                .disable()  // 禁用Controller生成
//                .build();
//
//        // 只生成 Service 相关的文件
//        TemplateConfig templateConfig = new TemplateConfig.Builder()
//                .disable(TemplateType.ENTITY)
//                .disable(TemplateType.MAPPER)
//                .disable(TemplateType.CONTROLLER)
//                .build();
//
//        new AutoGenerator(dataSourceConfig)
//                .global(globalConfig)
//                .packageInfo(packageConfig)
//                .strategy(strategyConfig)
//                .template(templateConfig)
//                .execute(new VelocityTemplateEngine());
//    }
//
//    /**
//     * 生成 Controller 层文件到 product-center-web 模块
//     */
//    private static void generateControllerLayer() {
//        System.out.println("\n📁 正在生成 Controller 层文件到 product-center-web 模块...");
//
//        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(DB_URL, DB_USER, DB_PASSWORD).build();
//
//        GlobalConfig globalConfig = new GlobalConfig.Builder()
//                .outputDir(PROJECT_ROOT + "/product-center-web/src/main/java")
//                .author(AUTHOR)
//                .disableOpenDir()
//                .enableSwagger()
//                .build();
//
//        PackageConfig packageConfig = new PackageConfig.Builder()
//                .parent("com.abclonal.product.web")
//                .controller(Con_url)  // Controller位置
//                // 禁用其他包的生成
//                .entity("")
//                .mapper("")
//                .xml("")
//                .service("")
//                .serviceImpl("")
//                .build();
//
//        StrategyConfig strategyConfig = new StrategyConfig.Builder()
//                .addInclude(TABLE_NAME)
//                .controllerBuilder()
//                .fileOverride()
//                .enableRestStyle()
//                .enableHyphenStyle()
//                .formatFileName("%sController")
//                .entityBuilder()
//                .disable()
//                .mapperBuilder()
//                .disable()
//                .serviceBuilder()
//                .disable()
//                .build();
//
//        // 只生成 Controller
//        TemplateConfig templateConfig = new TemplateConfig.Builder()
//                .disable(TemplateType.ENTITY)
//                .disable(TemplateType.MAPPER)
//                .disable(TemplateType.SERVICE)
//                .build();
//
//        new AutoGenerator(dataSourceConfig)
//                .global(globalConfig)
//                .packageInfo(packageConfig)
//                .strategy(strategyConfig)
//                .template(templateConfig)
//                .execute(new VelocityTemplateEngine());
//    }
}

