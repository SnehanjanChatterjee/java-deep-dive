package study.course.shared;

/** Retains an existing descriptor and adds one API in version two. @author Snehanjan Chatterjee */
public final class SharedApi {
    /** Retains the original descriptor while changing its returned version label. */
    public String version() { return "shared-api 2.0"; }
    /** Supplies a method unavailable when compiling against version one. */
    public String feature() { return "second-version feature"; }
}
