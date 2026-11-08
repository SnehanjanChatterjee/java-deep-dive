import java.util.concurrent.CountDownLatch;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Checks successful selection, aggregate failure and invalid task references. @author Snehanjan Chatterjee */
public final class StructuredFirstSuccessLessonTest {
    @Test public void testOneFailureStillAllowsSuccessfulCandidate() throws InterruptedException {
        final CountDownLatch failurePath = new CountDownLatch(1);
        assertEquals("fallback", StructuredFirstSuccessLesson.select(() -> {
            try {
                throw new IllegalArgumentException("first");
            } finally {
                failurePath.countDown();
            }
        }, () -> {
            failurePath.await();
            return "fallback";
        }));
    }

    @Test public void testSelectedSuccessWaitsForCancelledSiblingCleanup() throws InterruptedException {
        final CountDownLatch started = new CountDownLatch(1);
        final CountDownLatch release = new CountDownLatch(1);
        final AtomicBoolean cleaned = new AtomicBoolean();
        try {
            assertEquals("winner", StructuredFirstSuccessLesson.select(() -> {
                try {
                    started.countDown();
                    release.await();
                    return "released";
                } finally {
                    cleaned.set(true);
                }
            }, () -> {
                started.await();
                return "winner";
            }));
            assertTrue(cleaned.get());
        } finally {
            release.countDown();
        }
    }

    @Test public void testAllFailuresPropagateOneActualCause() {
        final StructuredTaskScope.FailedException failure = assertThrows(
            StructuredTaskScope.FailedException.class, () -> StructuredFirstSuccessLesson.select(() -> {
                throw new IllegalArgumentException("first");
            }, () -> {
                throw new IllegalArgumentException("second");
            }));
        assertTrue(failure.getCause() instanceof IllegalArgumentException);
    }

    @Test public void testNullCandidatesFailBeforeScopeCreation() {
        assertThrows(NullPointerException.class, () -> StructuredFirstSuccessLesson.select(null, () -> "ok"));
        assertThrows(NullPointerException.class, () -> StructuredFirstSuccessLesson.select(() -> "ok", null));
    }
}
