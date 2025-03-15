plugins {
    id("java")
    id("java-gradle-plugin")
    id("io.spring.dependency-management") version "1.1.7"
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.3.1"
}

group = "com.csicit.ace"
version = "1.0.0"

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

gradlePlugin {
    website = "https://github.com/morowind/ace-gradle-plugin"
    vcsUrl = "https://github.com/morowind/ace-gradle-plugin"
    plugins {
        register("ace-plugin") {
            id = "com.csicit.ace.ace-gradle-plugin"
            group = "com.csicit.ace"
            displayName = "JARI-ACE Gradle Plugin"
            description = "JARI-ACE Platform spring boot gradle plugin"
            implementationClass = "com.csicit.ace.gradle.AceGradlePlugin"
        }
    }
}


dependencies {
    implementation("io.spring.gradle:dependency-management-plugin:1.1.7")
    // 引入Spring的依赖管理插件
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType(JavaCompile::class.java) {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            val isSnapshot = version.toString().endsWith("-SNAPSHOT")
            isAllowInsecureProtocol = true
            name = if (isSnapshot) "csicit-snapshot" else "csicit-release"
            url = uri(
                if (isSnapshot) {
                    "http://nexus.jariit.local/repository/maven-snapshots"
                } else {
                    "http://nexus.jariit.local/repository/maven-releases"
                }
            )
            credentials {
                username = "admin"
                password = "C#rocks8866"
            }
        }
        mavenCentral()
    }
}