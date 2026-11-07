import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
/** Checks dynamic-array invariants. @author Snehanjan Chatterjee */
public final class DynamicArrayDemoTest {
 @Test public void testGrowthAndOrderedRemoval() { final DynamicArrayDemo.IntList list=new DynamicArrayDemo.IntList();for(int i=0;i<100;i++){list.add(i);}assertEquals(100,list.size());assertEquals(0,list.remove(0));assertEquals(99,list.remove(98));assertEquals(50,list.remove(49));assertEquals(97,list.size());assertEquals(51,list.get(49)); }
 @Test public void testBoundsAndEmpty() { final DynamicArrayDemo.IntList list=new DynamicArrayDemo.IntList();assertThrows(IndexOutOfBoundsException.class,()->list.get(0));assertThrows(IndexOutOfBoundsException.class,()->list.remove(-1));list.add(7);assertThrows(IndexOutOfBoundsException.class,()->list.get(1));assertEquals(7,list.remove(0));assertEquals(0,list.size()); }
}
