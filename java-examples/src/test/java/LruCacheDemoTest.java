import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
/** Checks recency and eviction. @author Snehanjan Chatterjee */
public final class LruCacheDemoTest {
 @Test public void testAccessAndReplacementRecency() { final LruCacheDemo.Cache<String,Integer> cache=new LruCacheDemo.Cache<>(2);cache.put("A",1);cache.put("B",2);cache.get("A");cache.put("C",3);assertFalse(cache.containsKey("B"));assertTrue(cache.containsKey("A"));assertEquals(Integer.valueOf(1),cache.put("A",4));cache.put("D",5);assertFalse(cache.containsKey("C"));assertEquals(2,cache.size()); }
 @Test public void testSingleCapacityAndInvalidCapacity() { assertThrows(IllegalArgumentException.class,()->new LruCacheDemo.Cache<>(0));final LruCacheDemo.Cache<String,Integer> cache=new LruCacheDemo.Cache<>(1);cache.put("A",null);cache.put("B",2);assertFalse(cache.containsKey("A"));assertEquals(Integer.valueOf(2),cache.get("B")); }
}
