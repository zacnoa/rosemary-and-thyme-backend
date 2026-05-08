plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jooq.jooq-codegen-gradle") version "3.21.2"
}

group = "rosemary_and_thyme"
version = "0.0.1-SNAPSHOT"
description = "rosemary-and-thyme-backend"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")

    implementation("org.jooq:jooq:3.21.2")
    implementation("org.springframework.boot:spring-boot-starter-jooq")

    implementation("com.cloudinary:kotlin-url-gen:1.11.0")
    runtimeOnly("org.postgresql:postgresql")
    jooqCodegen("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
jooq {
    configuration {
        jdbc{
            driver = "org.postgresql.Driver"
            url = "jdbc:postgresql://localhost:432/recipes_db"
            user="noa"
            password="noa"
        }
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                includes = ".*"
                schemata {

                    schema {
                        inputSchema = "public"
                    }
                    schema{
                        inputSchema="users"
                    }
                }
            }
            generate {
                isGeneratedAnnotation = false
                isKotlinNotNullPojoAttributes=true
                isKotlinNotNullRecordAttributes=true
                isKotlinNotNullInterfaceAttributes=true

            }
            target {
                directory = "src/main/kotlin"
                packageName = "rosemary_and_thyme.backend.models"
            }
        }
    }
}
tasks.named("compileKotlin"){
    dependsOn(tasks.named("jooqCodegen"))
}