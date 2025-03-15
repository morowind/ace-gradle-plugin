plugins {
    id("java")
    id("java-gradle-plugin")
    id("io.spring.dependency-management") version "1.1.7"
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.3.1"
    signing
}

group = "io.github.morowind"
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
            id = "io.github.morowind.ace-gradle-plugin"
            group = "io.github.morowind"
            displayName = "J-ACE Gradle Plugin"
            description = "J-ACE Platform spring boot gradle plugin"
            tags = listOf("j-ace")
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
        mavenCentral()
    }
}

signing {
    sign(configurations.runtimeElements.get())
}