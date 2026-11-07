# Separately compiled client

Run from this directory. Commands use `:` as the classpath separator; use `;` on Windows.

```bash
mkdir -p out/v1 out/client out/v2 out/compatible
javac -d out/v1 api-v1/study/api/Greeter.java
javac -cp out/v1 -d out/client client/study/client/Main.java
java -cp out/client:out/v1 study.client.Main
javac -d out/v2 api-v2/study/api/Greeter.java
java -cp out/client:out/v2 study.client.Main
javac -d out/compatible api-compatible/study/api/Greeter.java
java -cp out/client:out/compatible study.client.Main
```

The first launch prints `Hello Mira`. The v2 launch reports `NoSuchMethodError`: the already compiled client requires `greet(String)`. The compatible library retains that method and prints `Welcome Mira`. Recompiling the client against v2 is a separate experiment.
