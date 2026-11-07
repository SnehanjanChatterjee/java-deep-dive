# Collection lookup benchmark

Requires JDK 17+ and Maven. Run from this directory:

```bash
mvn clean package
java -jar target/benchmarks.jar 'study.bench.LookupBenchmark.*'
```

The project uses public dependency resolution and a local `.maven-cache` directory. It does not change machine-wide settings.

The workload measures lookup only, with size and hit/miss parameters, independently owned thread state, warmup and two forks. Construction is excluded. Results are consumed by the harness. It does not measure application tail latency or prove a universal collection ranking. Read the course chapter before interpreting scores.
