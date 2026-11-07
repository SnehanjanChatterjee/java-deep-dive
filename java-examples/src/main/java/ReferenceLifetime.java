import java.util.ArrayList;
import java.util.List;

/** Relate object lifetime, compilation and memory pressure to observable behavior. @author Snehanjan Chatterjee */
public final class ReferenceLifetime {

    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final List<byte[]> retained = new ArrayList<>();
        retained.add(new byte[1024]);
        retained.add(new byte[1024]);
        retained.remove(0);
        System.out.println(retained.size());
    }
}
