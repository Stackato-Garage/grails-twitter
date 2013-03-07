grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {
    inherits "global", {
        excludes "xml-apis", "commons-digester"
    }
    log      "warn"
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenRepo "http://maven.springframework.org/milestone/"
    }
    plugins {
        compile ":spring-security-core:1.2.7.3", ":webxml:1.4.1"
        runtime ":blueprint:1.0.2",
                ":cloud-foundry:1.2.3",
                ":executor:0.3",
                ":hibernate:2.2.1",
                ":mongodb:1.0.0.GA",
                ":prototype:1.0",
                ":rabbitmq:1.0.0.RC1",
                ":resources:1.1.6",
                ":redis:1.3.1",
                ":searchable:0.6.4", {
            excludes "slf4j-simple"
        }
        build ":tomcat:2.0.4"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        runtime "hsqldb:hsqldb:1.8.0.10"
    }
}
