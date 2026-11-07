import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/** Checks cycles, aliases and invalid graph inputs. @author Snehanjan Chatterjee */
public final class ReachabilityWalkDemoTest {
    @Test public void testCycleVisitsEachIdentityOnce() {
        final ReachabilityWalkDemo.Node a = new ReachabilityWalkDemo.Node("A");
        final ReachabilityWalkDemo.Node b = new ReachabilityWalkDemo.Node("B");
        a.connect(b); b.connect(a);
        assertEquals(Arrays.asList("A", "B"), ReachabilityWalkDemo.reachable(a, a));
    }
    @Test public void testAliasAndRemovedEdge() {
        final ReachabilityWalkDemo.Node root = new ReachabilityWalkDemo.Node("root");
        final ReachabilityWalkDemo.Node target = new ReachabilityWalkDemo.Node("target");
        root.connect(target); root.disconnect(target);
        assertEquals(Collections.singletonList("root"), ReachabilityWalkDemo.reachable(root));
        assertEquals(Arrays.asList("root", "target"), ReachabilityWalkDemo.reachable(root, target));
        assertFalse(root.disconnect(target));
    }
    @Test public void testEmptyNullAndDistinctIdentities() {
        assertEquals(Collections.emptyList(), ReachabilityWalkDemo.reachable((ReachabilityWalkDemo.Node) null));
        assertEquals(Arrays.asList("same", "same"), ReachabilityWalkDemo.reachable(new ReachabilityWalkDemo.Node("same"), new ReachabilityWalkDemo.Node("same")));
        assertThrows(NullPointerException.class, () -> ReachabilityWalkDemo.reachable((ReachabilityWalkDemo.Node[]) null));
        assertThrows(NullPointerException.class, () -> new ReachabilityWalkDemo.Node(null));
    }
}
