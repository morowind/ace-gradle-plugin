/*
 * 版权所有 (c) 2025 江苏杰瑞信息科技有限公司
 *
 * 此文件属于JARI.ACE平台项目，修改、分发必须遵守杰瑞信科商业软件许可协议
 */

package com.csicit.ace.gradle;

import io.spring.gradle.dependencymanagement.dsl.DependenciesHandler;
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension;
import io.spring.gradle.dependencymanagement.dsl.ImportsHandler;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.tasks.Copy;
import org.gradle.language.jvm.tasks.ProcessResources;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * 鲲舟平台Gradle插件
 *
 * @author hucp
 */
public class AceGradlePlugin implements Plugin<Project> {
    private final String springBootVersion;
    private final String springCloudVersion;
    private final String dmVersion;
    private final String lombokVersion;
    private final String freemarkerVersion;
    private final String liquibaseVersion;
    private final String infinispanVersion;
    private final String mapstructVersion;
    private final String jsoupVersion;
    private final String sbaVersion;
    private final String nacosVersion;
    private final String springCloudNacosVersion;
    private final String knife4jVersion;
    private final String axonFrameworkVersion;
    private final String redissonVersion;
    private final String aceVersion;
    private final String oracleVersion;
    private final String mysqlVersion;
    private final String sqlServerVersion;
    
    /**
     * constructor of AceGradlePlugin
     */
    public AceGradlePlugin() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("mvn.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        springBootVersion = properties.getProperty("spring.boot.version");
        springCloudVersion = properties.getProperty("spring.cloud.version");
        dmVersion = properties.getProperty("dm.version");
        lombokVersion = properties.getProperty("lombok.version");
        freemarkerVersion = properties.getProperty("freemarker.version");
        liquibaseVersion = properties.getProperty("liquibase.version");
        infinispanVersion = properties.getProperty("infinispan.version");
        mapstructVersion = properties.getProperty("org.mapstruct.version");
        jsoupVersion = properties.getProperty("org.jsoup.version");
        sbaVersion = properties.getProperty("spring.boot.admin.version");
        nacosVersion = properties.getProperty("nacos.version");
        springCloudNacosVersion = properties.getProperty("spring.cloud.nacos.version");
        knife4jVersion = properties.getProperty("knife4j.version");
        axonFrameworkVersion = properties.getProperty("axonFramework.version");
        redissonVersion = properties.getProperty("redisson.version");
        aceVersion = properties.getProperty("ace.version");
        oracleVersion = properties.getProperty("oracle.version");
        sqlServerVersion = properties.getProperty("sqlserver.version");
        mysqlVersion = properties.getProperty("mysql.version");
    }
    
    @Override
    public void apply(@NotNull Project project) {
        project.getPlugins().apply("io.spring.dependency-management");
        var dme = project.getExtensions().getByType(DependencyManagementExtension.class);
        dme.imports(this::importBOMs);
        dme.dependencies(this::declareDependencies);
        //setupResource(project);
        managePlugins(project);
        if (project.getExtensions().getExtraProperties().has("mirrorsConfigured")) {
            Boolean isConfigured = (Boolean) project.getExtensions().getExtraProperties().get("mirrorsConfigured");
            if (!Boolean.TRUE.equals(isConfigured)) {
                setupRepositories(project);
            }
        } else {
            setupRepositories(project);
        }
        
    }
    
    private void importBOMs(ImportsHandler i) {
        i.mavenBom("org.springframework.cloud:spring-cloud-dependencies:" + springCloudVersion);
        i.mavenBom("org.infinispan:infinispan-bom:" + infinispanVersion);
        i.mavenBom("org.springframework.session:spring-session-bom:2020.0.7");
        i.mavenBom("com.github.xiaoymin:knife4j-dependencies:" + knife4jVersion);
    }
    
    private static void managePlugins(@NotNull Project project) {
        PluginManager pluginManager = project.getPluginManager();
        pluginManager.apply("org.springframework.boot");
        project.getConfigurations().all(
                configuration -> configuration
                        .getDependencies()
                        .removeIf(dependency ->
                                          "org.projectlombok".equals(dependency.getGroup())
                                          && "lombok".equals(dependency.getName()))
        );
    }
    
    private void declareDependencies(DependenciesHandler d) {
        d.dependency("org.springframework.boot:spring-boot-starter-hateoas:" + springBootVersion);
        d.dependency("org.springframework.boot:spring-boot-starter-web:" + springBootVersion);
        d.dependency("org.springframework.boot:spring-boot-starter-data-jpa:" + springBootVersion);
        d.dependency(
                "org.springframework.boot:spring-boot-starter-validation:" + springBootVersion);
        d.dependency("org.springframework.boot:spring-boot-starter-jooq:" + springBootVersion);
        d.dependency(
                "org.springframework.boot:spring-boot-starter-data-redis:" + springBootVersion);
        d.dependency("org.springframework.boot:spring-boot-devtools:" + springBootVersion);
        d.dependency("org.springframework.boot:spring-boot-starter-test:" + springBootVersion);
        d.dependency(
                "org.springframework.boot:spring-boot-configuration-processor:"
                + springBootVersion);
        d.dependency("org.projectlombok:lombok:" + lombokVersion);
        d.dependency("com.dameng:Dm8JdbcDriver18:" + dmVersion);
        d.dependency("org.liquibase:liquibase-core:" + liquibaseVersion);
        d.dependency("org.mapstruct:mapstruct:" + mapstructVersion);
        d.dependency("org.jsoup:jsoup:" + jsoupVersion);
        d.dependency("de.codecentric:spring-boot-admin-starter-server:" + sbaVersion);
        d.dependency("de.codecentric:spring-boot-admin-starter-client:" + sbaVersion);
        d.dependency("com.alibaba.nacos:nacos-server:" + nacosVersion);
        d.dependency("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config:" + springCloudNacosVersion);
        d.dependency("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:" + springCloudNacosVersion);
        d.dependency("com.csicit.ace:ace-utils:" + aceVersion);
        d.dependency("com.csicit.ace:ace-swagger:" + aceVersion);
        d.dependency("com.csicit.ace:ace-app-base:" + aceVersion);
        d.dependency("com.csicit.ace:ace-jpa-leaf:" + aceVersion);
        d.dependency("com.csicit.ace:ace-i18n:" + aceVersion);
        d.dependency("com.csicit.ace:spring-cloud-starter-web-ace:" + aceVersion);
        d.dependency("com.csicit.ace:spring-cloud-starter-webflux-ace:" + aceVersion);
        d.dependency("com.csicit.ace:spring-cloud-starter-quartz-ace:" + aceVersion);
        d.dependency("com.csicit.ace:spring-cloud-starter-settings-ace:" + aceVersion);
        d.dependency("com.csicit.ace:spring-cloud-starter-files-ace:" + aceVersion);
        d.dependency("com.csicit.ace:ace-oauth2:" + aceVersion);
        d.dependency("com.csicit.ace:ace-cloud-support:" + aceVersion);
        d.dependency("com.csicit.ace:ace-quartz:" + aceVersion);
        d.dependency("com.csicit.ace:ace-axon-mq-support:" + aceVersion);
        d.dependency("com.csicit.ace:ace-service-api:" + aceVersion);
        d.dependency("com.csicit.ace:ace-app-patcher:" + aceVersion);
        d.dependency("com.csicit.ace:spring-cloud-starter-axon-ace:" + aceVersion);
        d.dependency("com.csicit.ace:spring-cloud-starter-jpa-ace:" + aceVersion);
        d.dependency("com.csicit.ace:ace-dto-base:" + aceVersion);
        d.dependency("com.csicit.ace:ace-validation:" + aceVersion);
        d.dependency("com.csicit.ace:ace-audit-support:" + aceVersion);
        d.dependency("com.csicit.ace:ace-push-support:" + aceVersion);
        d.dependency("com.csicit.ace:ace-organization-support:" + aceVersion);
        d.dependency("com.csicit.ace:ace-template-id-support:" + aceVersion);
        d.dependency("com.csicit.ace:ace-jpa-utils:" + aceVersion);
        d.dependency("com.csicit.ace:ace-setting-support:" + aceVersion);
        d.dependency("com.csicit.ace:ace-file-support:" + aceVersion);
        d.dependency("com.csicit.ace:ace-file-jpa-support:" + aceVersion);
        d.dependency("com.csicit.ace:ace-db-schema-creator:" + aceVersion);
        d.dependency("com.csicit.ace:ace-app-service:" + aceVersion);
        d.dependency("com.csicit.ace:ace-audit-service:" + aceVersion);
        d.dependency("com.csicit.ace:ace-domain-modeler:" + aceVersion);
        d.dependency("com.csicit.ace:ace-file-service:" + aceVersion);
        d.dependency("com.csicit.ace:ace-push-service:" + aceVersion);
        d.dependency("com.csicit.ace:ace-file-cloud-support:" + aceVersion);
        d.dependency("com.csicit.ace:ace-service-client:" + aceVersion);
        d.dependency("com.csicit.ace:ace-axon-support:" + aceVersion);
        d.dependency("com.csicit.ace:ace-app-monolith-service:" + aceVersion);
        d.dependency("com.csicit.ace:spring-boot-starter-monolith-ace:" + aceVersion);
        d.dependency("com.csicit.ace:ace-file-monolith-support:" + aceVersion);
        d.dependency("com.csicit.ace:ace-model-support:" + aceVersion);
        d.dependency("org.axonframework:axon-spring-boot-starter:" + axonFrameworkVersion,
                     p -> p.exclude("org.axonframework:axon-server-connector"));
        d.dependency("org.axonframework:axon-configuration:" + axonFrameworkVersion);
        d.dependency("org.axonframework:axon-messaging:" + axonFrameworkVersion);
        d.dependency("org.axonframework.extensions.springcloud:axon-springcloud:4.5");
        d.dependency("org.axonframework.extensions.kafka:axon-kafka-spring-boot-starter:4.5.4");
        d.dependency("com.google.code.findbugs:jsr305:3.0.2");
        d.dependency("com.corundumstudio.socketio:netty-socketio:1.7.22");
        d.dependency("org.redisson:redisson:" + redissonVersion);
        d.dependency("com.google.guava:guava:29.0-jre");
        d.dependency("org.freemarker:freemarker:" + freemarkerVersion);
        d.dependency("com.oracle.database.jdbc:ojdbc11:" + oracleVersion);
        d.dependency("mysql:mysql-connector-java:" + mysqlVersion);
        d.dependency("com.microsoft.sqlserver:mssql-jdbc:" + sqlServerVersion);
    }
    
    private void setupResource(Project project) {
        project.getTasks().named("processResources", ProcessResources.class, task -> {
            List<String> filteredFiles = List.of(
                    "**/application*.yml",
                    "**/application*.yaml",
                    "**/application*.properties",
                    "**/infinispan.xml",
                    "**/jgroups-*.xml"
            );
            
            task.filesMatching(filteredFiles, file -> {
                file.expand(project.getProperties());
            });
        });
        
        Copy copyOtherResources = project.getTasks().create("copyOtherResources", Copy.class, task -> {
            task.from("src/main/resources", copySpec -> copySpec.exclude(
                    "**/application*.yml",
                    "**/application*.yaml",
                    "**/application*.properties",
                    "**/infinispan.xml",
                    "**/jgroups-*.xml"
            ));
            task.into(project.getLayout().getBuildDirectory() + "/resources/main");
        });
        
        project.getTasks().named("processResources", task -> task.dependsOn(copyOtherResources));
    }
    
    private void setupRepositories(Project project) {
        var repositories = project.getRepositories();
        repositories.maven(m -> {
            m.setName("coding-net-ace-release");
            m.setUrl("https://jari-ace-maven.pkg.coding.net/repository/ace/release");
        });
        repositories.maven(m -> {
            m.setName("redhat");
            m.setUrl("https://maven.repository.redhat.com/ga");
        });
        repositories.maven(m -> {
            m.setName("JBoss-release");
            m.setUrl("https://repository.jboss.org/nexus/content/repositories/releases");
        });
        repositories.maven(m -> {
            m.setName("JBoss-third-party-release");
            m.setUrl("https://repository.jboss.org/nexus/content/repositories/thirdparty-releases");
        });
        repositories.maven(m -> {
            m.setName("JBoss-third-party-uploads");
            m.setUrl("https://repository.jboss.org/nexus/content/repositories/thirdparty-uploads");
        });
        repositories.maven(m -> {
            m.setName("spring.io");
            m.setUrl("https://repo.spring.io/release");
        });
        repositories.maven(m -> {
            m.setName("aspose-release");
            m.setUrl("https://releases.aspose.com/java/repo");
        });
    }
}
