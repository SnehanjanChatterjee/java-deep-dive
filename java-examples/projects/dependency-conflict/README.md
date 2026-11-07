# Dependency conflict lab

Use a Java 8+ JDK and Maven 3.9+. Run commands in this directory. The pinned compiler plugin converts the release target for older supported compilers. Settings select only public Maven Central, and dependencies go into this project's .maven-cache directory.

    mvn clean install
    mvn -pl app dependency:tree -Dverbose -Dincludes=study.course
    java -cp app/target/classes:adapter/target/classes:shared-v2/target/classes study.course.app.Main

The app directly selects shared-api:2.0. The adapter requested version 1.0 transitively, but its version() method also exists in version 2. The app prints version-two labels and its feature.

Compile the alternate POM without the direct API dependency:

    mvn -f app/pom-transitive-only.xml clean compile

This deliberately fails at SharedApi.feature(): only version 1.0 is available through adapter. Restore the normal build:

    mvn clean verify

Compare the complete BOM-managed POM after the initial install made the synthetic BOM available locally:

    mvn -f app/pom-bom.xml clean verify
    mvn -f app/pom-bom.xml dependency:tree -Dverbose -Dincludes=study.course

The imported BOM supplies versions, while app still declares adapter and shared-api. optional-support is managed in the BOM and built by the reactor, but is not an app dependency. Build membership and dependency inclusion are distinct.

Commands use ':' as the classpath separator; use ';' on Windows. install is a local repository operation. No deploy, remote publication, private repository or machine-wide settings change is needed.
