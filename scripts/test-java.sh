#!/bin/bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p .cache/java .cache/java/classes
junit="${JUNIT_JAR:-.cache/java/junit-4.13.2.jar}"
hamcrest="${HAMCREST_JAR:-.cache/java/hamcrest-core-1.3.jar}"
if [ ! -f "$junit" ]; then curl --fail --location --silent --show-error https://repo.maven.apache.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar -o "$junit"; fi
if [ ! -f "$hamcrest" ]; then curl --fail --location --silent --show-error https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar -o "$hamcrest"; fi
javac -Xlint:all -d .cache/java/classes -cp "$junit:$hamcrest" java-examples/src/main/java/*.java java-examples/src/test/java/*.java
test_classes=()
for test_source in java-examples/src/test/java/*Test.java; do
    test_name="${test_source##*/}"
    test_classes+=("${test_name%.java}")
done
java -cp ".cache/java/classes:$junit:$hamcrest" org.junit.runner.JUnitCore "${test_classes[@]}"
java -cp .cache/java/classes MyHashMap

node scripts/java-smoke.mjs
