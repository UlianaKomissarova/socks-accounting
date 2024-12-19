plugins {
	java
	id("org.springframework.boot") version "2.7.0"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.liquibase.gradle") version "2.1.0"
}

group = "dev.uliana"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.opencsv:opencsv:5.7.1")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-logging")
	implementation("org.liquibase:liquibase-core")
	implementation("com.fasterxml.jackson.core:jackson-databind")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.12")
	implementation("io.swagger.core.v3:swagger-annotations:2.2.16")
	implementation("com.fasterxml.jackson.core:jackson-annotations")

	liquibaseRuntime("org.liquibase:liquibase-core")
	liquibaseRuntime("org.postgresql:postgresql")

	compileOnly("org.projectlombok:lombok:1.18.24")

	runtimeOnly("org.postgresql:postgresql")

	annotationProcessor("org.projectlombok:lombok:1.18.24")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:junit-jupiter:1.16.3")
	testImplementation("org.testcontainers:postgresql:1.16.3")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
