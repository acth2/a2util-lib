
plugins {
    id("java-library")
    id("com.vanniktech.maven.publish") version "0.37.0"
}

group = "io.github.acth2"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(8)
}

tasks.withType<Javadoc>().configureEach {
    (options as StandardJavadocDocletOptions).apply {
        addStringOption("Xdoclint:none", "-quiet")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.formdev:flatlaf:3.6.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("io.github.acth2", "a2util-lib", "1.0.0")

    pom {
        name.set("a2util-lib")
        description.set("The library i use for my projects")
        url.set("https://github.com/acth2/a2util-lib")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("acth2")
                name.set("AcTh2")
            }
        }

        scm {
            url.set("https://github.com/acth2/a2util-lib")
            connection.set(
                "scm:git:git://github.com/acth2/a2util-lib.git"
            )
            developerConnection.set(
                "scm:git:ssh://git@github.com/acth2/a2util-lib.git"
            )
        }
    }
}
