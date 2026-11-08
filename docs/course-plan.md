# Java Deep Dive course plan

Java Deep Dive is a beginner-to-advanced Java study course with interview preparation built into the teaching. It supports experienced developers revisiting fundamentals without assuming that they already understand every Java mechanism. The purpose is to help a learner explain, trace, implement and evaluate Java code.

This is the authoritative course specification. Read it with [the curriculum](curriculum.md), [the design specification](design.md) and the repository instructions before changing lessons. Historical planning notes do not override these current specifications.

## Scope and learning outcomes

Teach the Java language, object model, standard libraries, collections, functional programming, concurrency, JVM execution and advanced runtime/platform mechanisms. Backend frameworks are outside this course. Java releases identify example requirements and implementation details; a verification JDK does not define the course subject.

A learner should be able to describe what a concept is, explain why it exists, use it in an ordinary program, predict and trace its behavior, reason about its implementation and limitations, and answer practical interview follow-ups. Interview coverage is assessed by these capabilities rather than by counting questions or pages.

## Teaching progression

1. Write and run small programs; distinguish source, compiler, class files and execution.
2. Learn values, expressions, control flow, methods, arrays and strings.
3. Build classes and understand construction, inheritance, interfaces, aliases, equality and immutable values.
4. Connect those familiar programs to JVM architecture, frames, heap references and bytecode.
5. Establish project/testing workflow and exception/resource ownership.
6. Develop generics, collection contracts, data structures and implementations.
7. Pass behavior with functional interfaces; build sequential pipelines and reductions before advanced composition.
8. Use standard libraries with explicit numeric, temporal, encoding, I/O and network contracts.
9. Start ordinary threads, then teach lifecycle, synchronization, visibility, waiting, task ownership and concurrent structures.
10. Apply those prerequisites to virtual threads, parallel streams, concurrent gatherers, scoped context and structured task lifetimes.
11. Analyze advanced class loading, reachability, collection algorithms, JIT optimization and diagnostic evidence.
12. Study advanced platform capabilities and consolidate API design, dependency management and compatibility.

### Why JVM material appears at two levels

The early JVM architecture module defines the JVM, JDK and JRE; follows compilation and execution; and introduces method state, object references, loading and automatic memory management. Definitions and an ordinary program precede the system map.

Advanced JVM internals studies mechanisms requiring those foundations: loader identity and failure states, instruction/member linkage, graph-based reachability analysis, process memory budgets, collector behavior and optimization evidence. Introductory definitions must not be deferred to the advanced module. Collection-based graph exercises follow collections; initialization failure examples follow exceptions; optimization/publication analysis follows the memory model.

### Prerequisites are teaching dependencies

Declare an actual prerequisite when a lesson requires its concepts. Verify both metadata order and the prose: a numerically earlier chapter is not proof that a concept was taught. Do not move entire advanced examples earlier merely to place a subject name near the beginning.

Sequential streams precede concurrency. Parallel streams require threads, the memory model and ForkJoin foundations. Concurrent gatherers additionally follow virtual threads and sequential gatherers. Legacy forced-thread control is advanced background after lock/liveness concepts, not a prerequisite for creating an ordinary thread.

## Required lesson structure

Use topic-specific headings and a sequence suitable for the subject:

- Define the subject and expand new acronyms. Explain the problem and introduce vocabulary before using it in a diagram, example or diagnostic excerpt.
- Develop a concrete ordinary case with known inputs and an expected result. Explain new syntax and API roles in context.
- Present complete runnable source, then trace important statements, calls, values and state transitions. Small derived fragments can prepare the full example but must be labelled as fragments.
- Explain mechanisms and invariants after ordinary use. Separate API guarantees from implementation-specific algorithms, heuristics and observed behavior.
- Develop edge cases, failures, tradeoffs and alternatives without letting rare cases obscure the basic model.
- Include prediction, debugging, coding and explanation questions with worked reasoning. A solution explains why it works, not only which option is correct.
- Keep primary references beside relevant claims and provide accurate Java requirements.

Not every topic needs an invented implementation, diagram or animation. Choose the form that teaches it: an object graph, frame/operand trace, bucket view, lock timeline, algorithm dry run, API comparison or diagnostic lab. A large map does not substitute for defining its boxes.

## Implementation exercises

Use one self-contained primary Java class/file with nested helpers when appropriate for an interview task. Supporting tests and optional demonstrations are separate teaching material. If a test uses callback syntax beyond the primary program, label that supporting source and link to the test and language prerequisites. Explain each important method's inputs/results, branches, invariant, trace, complexity assumptions and failure cases.

State the implemented contract and simplifications. A chain-based teaching hash map must not be described as the complete JDK HashMap implementation. Preserve complete approved examples when reorganizing material.

## Research and technical accuracy

Use supplied references as required research inputs and as topic/gap discovery aids. Research beyond them. Validate language behavior against specifications, API behavior against official contracts, and implementation claims against a named source version. Write original explanations and examples; do not reproduce bookmarked tutorials wholesale or adopt their errors.

Keep a source-access record outside learner prose. Distinguish complete articles, inspected excerpts, playlist indexes, transcripts, inaccessible content and unperformed video viewing. Never describe inaccessible material as reviewed. Run code before asserting an observed output, and distinguish predictions or conceptual models from actual runtime captures.

## Canonical files and navigation

- Lessons: `website/src/content/lessons/`.
- Module names/group order: `website/src/data/modules.ts`.
- Lesson identity, title, group, order, versions and prerequisites: each lesson's frontmatter.
- HashMap subsection definitions: `website/src/data/hashmap.json`.
- Java source: `java-examples/`; rendered code/downloads derive from these files.
- Shared course ordering: `website/src/lib/course.ts` and the generated pages/layout.

Generate navigation from canonical metadata and actual headings. Sidebar labels, article identity, search, outlines and previous/next order must agree. Preserve existing routes and fragment aliases during reorganization. The curriculum file records the current sequence; source metadata remains the executable definition.

## Architecture and delivery plan

The site is statically generated with Astro, TypeScript and MDX. React islands provide focused interactions, Mermaid/custom visuals illustrate mechanisms, and Pagefind provides search. Reading requires no database or application backend. Java examples execute locally; the browser models do not execute arbitrary Java. Notes/progress/bookmarks are device-local reader tools.

Free hosting is planned on GitHub Pages for the public project repository. GitHub Free supports Pages for public repositories; use the project base path `/java-deep-dive/`. See [GitHub Pages](https://docs.github.com/en/pages/getting-started-with-github-pages/what-is-github-pages) and [Astro's deployment guide](https://docs.astro.build/en/guides/deploy/github/).

The intended publication flow is local source changes → relevant checks and content review → authorized commit/push to main → Actions build → Pages deployment. The current validation workflow is not a Pages deployment workflow. Hosting activation, deployment permissions and the first push/deploy are separate authorized steps; do not claim they are configured simply because CI exists. After activation, approved pushes to main should trigger the deployment automatically.

Keep repository-local contributor settings and credentials outside public documentation. Never modify shared/global configuration or another project. Public files must not contain personal email addresses, private configuration, machine-specific paths, workplace information or development-conversation history.

## Review and maintenance gates

A content review reads the lesson as a learner and checks definitions, progression, grouping and examples against its real prerequisites. A technical review checks contracts, traces, algorithms, outputs and source/version claims. A rendered review checks actual typography, diagrams, interactions and responsive behavior. A successful build does not establish instructional quality.

Run appropriate example tests and website checks for the changes. Keep evidence and unresolved limitations in review records. Preserve approved content and design; new material does not authorize deleting useful older explanations. Update this specification, curriculum and design document with intentional changes so subsequent work follows the same decisions.
