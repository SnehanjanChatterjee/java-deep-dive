package study.bench;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/** Compares lookup-only hit/miss workloads, excluding construction. @author Snehanjan Chatterjee */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
public class LookupBenchmark {
    @Param({"16", "1024"}) public int size;
    @Param({"hit", "miss"}) public String scenario;
    private List<Integer> list;
    private Set<Integer> set;
    private Integer key;

    /** Builds a stable data set outside the measured invocation. */
    @Setup(Level.Trial)
    public void setup() {
        list = new ArrayList<>(size);
        set = new HashSet<>();
        for (int value = 0; value < size; value++) { list.add(value); set.add(value); }
        key = "hit".equals(scenario) ? size / 2 : -1;
    }

    /** Measures scanning membership and returns the result for harness consumption. */
    @Benchmark public boolean listContains() { return list.contains(key); }

    /** Measures hash membership and returns the result for harness consumption. */
    @Benchmark public boolean setContains() { return set.contains(key); }
}
