# First Java project and tests

Use a Java 8+ JDK and Maven 3.9+. From this directory:

```bash
mvn test
mvn -Dtest=CalculatorTest#testPositiveOperands test
mvn package
java -cp target/classes study.math.Calculator
```

The project uses its own public repository settings and `.maven-cache` directory. Its source tree separates production code from tests. The course explains a deliberately incorrect expected value as a temporary exercise; this supplied solution passes its tests.
