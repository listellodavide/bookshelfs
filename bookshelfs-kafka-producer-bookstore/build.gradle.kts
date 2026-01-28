plugins {
	java
	id("org.springframework.boot") version "4.0.2"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"
}

group = "com.adiwave"
version = "0.0.1-SNAPSHOT"
description = "Kafka topics producer bookstore Spring Boot"
val otelAgent: Configuration by configurations.creating

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://packages.confluent.io/maven/")
	}
}

extra["springCloudVersion"] = "2025.1.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.8.4")
	implementation("org.springframework.boot:spring-boot-starter-reactor-netty")
	implementation("org.springframework.boot:spring-boot-starter-opentelemetry")

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.cloud:spring-cloud-stream")
	implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
	implementation("io.confluent:kafka-avro-serializer:7.8.0")
	implementation("io.confluent:kafka-schema-registry-client:7.8.0")
	implementation("org.apache.avro:avro:1.11.3")

	otelAgent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.24.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder")
	testImplementation("org.springframework.boot:spring-boot-starter-opentelemetry-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<JavaExec>("runWithOtel") {
	group = "application"
	description = "Runs the Spring Boot application with the OTel Java Agent v2."

	mainClass.set("com.adiwave.KafkaProducerBookstoreApplication")
	classpath = sourceSets["main"].runtimeClasspath

	// Safely resolve the jar
	val agentFile = otelAgent.resolve().first()

	jvmArgs("-javaagent:${agentFile.absolutePath}")

	environment(
		"OTEL_SERVICE_NAME" to "kafka-producer-bookstore",
		"OTEL_EXPORTER_OTLP_ENDPOINT" to "http://localhost:4317",
		"OTEL_METRICS_EXPORTER" to "otlp",
		"OTEL_LOGS_EXPORTER" to "otlp",
		"OTEL_TRACES_EXPORTER" to "otlp",
		"OTEL_EXPORTER_OTLP_PROTOCOL" to "grpc",
		// This is a new 2.x feature: suppress internal noisy spans if needed
		"OTEL_INSTRUMENTATION_COMMON_DEFAULT_ENABLED" to "true"
	)
}

//add OpenTelemetry Java Agent to spring boot existing task "bootRun"
tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
	val agentJar = otelAgent.singleFile.absolutePath
	jvmArgs("-javaagent:$agentJar")

	environment(
		"OTEL_SERVICE_NAME" to "kafka-producer-bookstore",
		"OTEL_EXPORTER_OTLP_ENDPOINT" to "http://localhost:4317",
		"OTEL_METRICS_EXPORTER" to "otlp",
		"OTEL_LOGS_EXPORTER" to "otlp"
	)
}