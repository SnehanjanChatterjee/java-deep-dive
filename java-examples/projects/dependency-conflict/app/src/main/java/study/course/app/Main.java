package study.course.app;

import study.course.adapter.Adapter;
import study.course.shared.SharedApi;

/** Makes compile-time dependency selection and runtime behavior observable. @author Snehanjan Chatterjee */
public final class Main {
    /** Uses the API directly, so it is a declared direct dependency. */
    public static void main(final String[] args) {
        final SharedApi shared = new SharedApi();
        System.out.println("direct: " + shared.version());
        System.out.println("adapter: " + new Adapter().describe());
        System.out.println("feature: " + shared.feature());
    }
}
