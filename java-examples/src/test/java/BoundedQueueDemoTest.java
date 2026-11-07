import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
/** Checks queue boundaries and cooperative blocking. @author Snehanjan Chatterjee */
public final class BoundedQueueDemoTest {
 @Test public void testFifoWraparoundAndValidation() throws Exception { assertThrows(IllegalArgumentException.class,()->new BoundedQueueDemo.Queue<>(0));final BoundedQueueDemo.Queue<String> queue=new BoundedQueueDemo.Queue<>(2);assertThrows(NullPointerException.class,()->queue.put(null));queue.put("a");queue.put("b");assertEquals("a",queue.take());queue.put("c");assertEquals("b",queue.take());assertEquals("c",queue.take()); }
 @Test(timeout=3000) public void testWaitingConsumerReceivesValue() throws Exception { final BoundedQueueDemo.Queue<String> queue=new BoundedQueueDemo.Queue<>(1);final ExecutorService executor=Executors.newSingleThreadExecutor();try{final Future<String> result=executor.submit(queue::take);queue.put("value");assertEquals("value",result.get(1,TimeUnit.SECONDS));}finally{executor.shutdownNow();} }
 @Test(timeout=3000) public void testBlockedProducerCanBeInterrupted() throws Exception { final BoundedQueueDemo.Queue<String> queue=new BoundedQueueDemo.Queue<>(1);queue.put("first");final CountDownLatch started=new CountDownLatch(1);final Thread producer=new Thread(()->{started.countDown();try{queue.put("second");}catch(final InterruptedException expected){Thread.currentThread().interrupt();}});producer.start();started.await();producer.interrupt();producer.join(1000);assertEquals(false,producer.isAlive());assertEquals("first",queue.take()); }
}
