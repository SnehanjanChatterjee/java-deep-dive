package study.course.adapter;

import study.course.shared.SharedApi;

/** Compiles against version one and calls the retained API at runtime. @author Snehanjan Chatterjee */
public final class Adapter {
    /** Reports the shared library actually present on the runtime classpath. */
    public String describe() { return new SharedApi().version(); }
}
