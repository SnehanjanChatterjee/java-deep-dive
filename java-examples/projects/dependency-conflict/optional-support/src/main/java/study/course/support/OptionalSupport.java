package study.course.support;

/** Is managed by the BOM but never declared as an app dependency. @author Snehanjan Chatterjee */
public final class OptionalSupport {
    /** Supplies an unused capability to distinguish management from inclusion. */
    public String capability() { return "optional support"; }
}
