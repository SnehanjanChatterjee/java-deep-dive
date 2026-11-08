# Java Deep Dive curriculum

The local study course follows the sequence below, from first programs through advanced Java. Topic groups, prerequisites, navigation and previous/next lessons use the same canonical metadata. Each topic develops a concrete problem, ordinary use, worked execution, mechanisms, appropriate implementation and explained practice. Backend frameworks remain outside the course.

Java versions describe example requirements and inspected implementation details. The curriculum is organized by Java concepts; the verification JDK is a separate toolchain choice. The approved minimal reading layout and shared course timeline remain in use.

## 01. Getting started with Java

Write, run and organize your first programs.

### First programs

- **Your first Java program** — Save a small source file, compile it, run it and understand each part of its entry point.

### How Java runs

- **How Java compiles and runs a program** — See why writing source, compiling it and running it are three different steps.

### Tools and project structure

- **Packages, classpaths and development tools** — Organize class names and files so the compiler and launcher can find a growing program.

## 02. Language fundamentals

Variables, decisions, loops, methods, arrays and text.

### Variables and expressions

- **Variables, expressions and blocks** — Store and change values, calculate results and determine where a variable can be used.

### Decisions and loops

- **Decisions, loops and execution traces** — Choose which statements run, repeat work and trace when a loop stops.
- **Switch statements and switch expressions** — Choose behavior from one value and distinguish selecting a statement from producing a result.

### Methods and recursion

- **Methods, return values and recursion** — Define reusable operations, pass input, return results and solve smaller problems with recursive calls.

### Arrays and strings

- **Array creation, indexing and traversal** — Store a sequence of values, access positions safely and visit or copy their contents.
- **String values and mutable text construction** — Work with text values, compare contents and assemble a result without changing existing Strings.
- **String search, slicing and comparison** — Find matching text, extract ranges and decide which String comes first in an ordering.
- **Unicode units, code points and text boundaries** — Learn why a character, a String position and an encoded byte are different units.
- **Regular expressions and escaping** — Describe shapes of text, find matching pieces and distinguish pattern syntax from literal text.
- **Sorted arrays and binary-search boundaries** — Use a sorted array to find values and insertion positions by repeatedly narrowing the search.

### Conversions and numeric boundaries

- **Conversions, wrappers and numeric boundaries** — Understand how numeric types affect calculations and what happens when a value cannot fit exactly.
- **Wrapper values, boxing and null boundaries** — Represent primitive values as objects and handle the difference between a missing number and zero.
- **Method overloading and compile-time selection** — Share a method name across parameter lists and explain which method a call selects.
- **Varargs calls and array ownership** — Write a method that accepts zero or more values while preserving ordinary array rules.

## 03. Classes and objects

Build objects, express behavior and manage shared references.

### Classes and objects

- **Classes, objects and instance state** — Use a class to give separate objects their own state and a common set of operations.

### Construction and encapsulation

- **Constructors, this and encapsulation** — Give each new object a valid starting state and control how callers can change it.
- **Static and instance initialization order** — Follow what runs once for a class and what runs again for each new object.

### Interfaces and inheritance

- **Interfaces and named implementations** — Describe an operation independently of the class implementing it, then supply named implementations.
- **Inheritance, dispatch and composition** — Understand how a subclass inherits behavior and how collaborating objects provide an alternative.
- **Abstract classes and shared algorithms** — Share state and an algorithm while requiring concrete subclasses to provide a chosen step.
- **Interface categories and default-method conflicts** — Recognize interface roles and decide which inherited method body an implementation should use.
- **Overriding, static hiding and field selection** — Distinguish the declared type of a reference from the actual object's type when accessing fields and methods.
- **Access boundaries and declaration modifiers** — Choose which code can access a declaration and understand what its other modifiers restrict.

### References and identity

- **References, aliases and pass-by-value** — Explain why a method can change a shared object while reassigning its parameter leaves the caller's variable alone.
- **Association, aggregation and composition** — Distinguish using an object, sharing it and owning its parts through explicit references and operations.
- **Array covariance and nested ownership** — Copy nested arrays deliberately and explain why the actual component type still controls a store.
- **Equality, hashing and ordering contracts** — Choose what makes objects equal, compute matching hashes and distinguish equality from ordering.
- **Immutable values and defensive copies** — Keep a value stable by controlling mutable inputs, outputs and subclass behavior.

### Nested and domain types

- **Enums, nested classes and enclosing instances** — Model a fixed set of choices with enums and organize helper classes around their enclosing type.
- **Local and anonymous classes** — Create small implementations inside an operation and retain the surrounding values they need.
- **Records and validated value components** — Declare a value through named components and understand its generated members and validation.
- **Sealed alternatives and pattern matching** — Represent permitted alternatives as types and inspect a result safely with patterns.

## 04. JVM architecture and execution

Learn what the JVM is, how it runs class files and where method state and objects live.

### JVM architecture

- **Java Virtual Machine (JVM) architecture** — Learn what the JVM is, how it runs compiled Java programs, and how memory and method calls work together.

### Stack and heap

- **Stack frames, heap objects and reference values** — Separate a call's local values from the objects it refers to and explain why an object can outlive a call.

### Frames and bytecode

- **Method frames, local variables and bytecode** — Follow one familiar calculation through a method frame and the bytecode operations that produce its result.

## 05. Projects and tests

Compile, test and package a small Java project.

### Build and test a small project

- **Build a Java project and write your first tests** — Organize source files, describe a dependency, run assertions and package a working program.

## 06. Exceptions and resources

Follow failures through calls and close resources correctly.

### Exception flow

- **Exception flow and stack traces** — Follow what happens when an operation cannot return normally and learn how a stack trace explains the failure.

### Failure contracts

- **Throw, throws, checked failures and causes** — Decide which failures a caller must handle and preserve the reason a higher-level operation failed.

### Resource ownership

- **Resource ownership and try-with-resources** — Acquire and close owned resources, then preserve useful diagnostics when work or cleanup fails.
- **Console input, parsing and borrowed streams** — Read text while a program runs, interpret it as values and keep input ownership clear.

## 07. Generics

Develop reusable types before bounds and runtime details.

### Generic types and methods

- **From Object storage to generic types** — Connect class and interface type parameters to stored/returned values, including independent key/value roles and bounded numeric capabilities.
- **Generic methods and type inference** — Infer or state method type arguments, relate distinct input/output types and separate generic method scope from class parameter scope.

### Bounds and wildcards

- **Bounds, wildcards and safe data flow** — Trace producer/consumer permissions, invariance, wildcard capture and inherited comparison bounds against complete generic algorithms.
- **Typed containers and mutable element ownership** — Choose structural and element copies explicitly so typed containers and record components preserve their promised ownership and value equality.

### Runtime representation

- **Erasure, bridges and heap pollution** — Inspect bridges and erased descriptors, then reproduce raw/varargs failures and the precise limits of checked runtime wrappers.

## 08. Collections

Use, compare and implement containers.

### Collection foundations

- **Compare algorithm time and space complexity** — Count work and storage, distinguish expected from amortized costs, and derive bounds for one or several input dimensions.
- **Collection contracts and implementation choices** — Choose an interface by ordering, uniqueness, lookup and mutation requirements before comparing its storage implementations.
- **Shared collection operations and typed arrays** — Use bulk membership operations, read their boolean results and convert a collection into an array with the intended runtime type.

### Iteration and views

- **Iterators, views and collection ownership** — Implement and trace independent iterators, then distinguish live views, structural snapshots and mutable element aliases.

### Ordering

- **Comparable, Comparator and ordering policies** — Define natural and external comparison policies and predict their distinct effects on sorting, set membership and priority ties.
- **Sorting, searching and rearranging collections** — Apply Collections algorithms with the required ordering and mutation permissions, and trace insertion points, copies and rearrangements.

### Lists and arrays

- **ArrayList** — Use positional and bulk list methods, typed arrays and range views, then derive shifting, storage and growth costs.
- **Nested lists, independent rows and collection keys** — Construct and copy nested containers while distinguishing row creation, indexed replacement, jagged dimensions and mutable hash membership.
- **LinkedList** — Trace endpoint operations, bidirectional iterator state and reciprocal link repair in a complete doubly linked list.
- **Implement a dynamic array** — Implement bounds, indexed shifts, geometric growth and two-phase bulk compaction while preserving logical-size and reference invariants.

### Maps and search trees

- **HashMap** — Follow key equality, bucket selection, collision lookup and growth into complete chained-map implementations and backed views.
- **Map views and conditional updates** — Distinguish absent and mapped-null values, conditional replacement, backed key/value/entry views and detached entry snapshots.
- **LinkedHashMap** — Predict insertion/access order for individual and bulk operations, then connect the entry index to encounter links and eviction hooks.
- **Implement an unbalanced binary search tree** — Implement comparison-based insertion, lookup, traversal and every deletion shape, including a successor with a surviving child.
- **TreeMap and red-black balancing** — Use ordered neighbors, bounded views and entry snapshots, then work through red-black insertion and deletion repair.
- **Hashtable, legacy enumeration and map contracts** — Use Hashtable mappings, distinguish its value-based contains method and traverse a stable table with Enumeration before examining buckets and locking limits.

### Sets

- **HashSet** — Establish hash-based membership, compute set algebra without mutating inputs and implement a set over a handwritten map.
- **Pairs, composite keys and value equality** — Build a two-component value that deduplicates correctly, and separate component equality, diagnostic text and deep immutability.
- **LinkedHashSet** — Normalize and deduplicate in first-appearance order, then maintain one live indexed node per encounter position.
- **TreeSet** — Predict comparison-based uniqueness, reversed navigation and backed range changes while separating set equality from comparator ties.

### Queues and priority

- **ArrayDeque** — Choose explicit queue/stack endpoints and empty behavior, then implement ring growth and prove a sliding-window maximum algorithm.
- **Read legacy Vector and Stack APIs** — Distinguish Vector capacity from setSize, trace Stack's five operations and recognize inherited APIs that weaken a strict stack abstraction.
- **PriorityQueue and binary heaps** — Implement sift operations and bottom-up heapify, explain unsorted heap storage and retain top-k candidates with bounded state.

### Implementation practice

- **Null policies and safely shared maps** — Compare concrete null policies, view and snapshot guarantees, and the distinct synchronization contracts of common map choices.
- **LRU cache implementation** — Implement and trace a count-bounded cache whose hash index and doubly linked recency list share exactly one node per key.
- **Code-point string algorithms with ordered maps** — Reverse code points, count occurrences and find the first point that appears only once.

## 09. Lambdas and streams

Pass behavior, build pipelines and combine results.

### Lambdas and functional interfaces

- **Lambdas and functional interfaces** — Derive named, anonymous and lambda implementations from a functional method, then reason about syntax, target typing, capture and callback contracts.

### Functions and method references

- **Predicates, functions and callback composition** — Invoke and compose callback roles with explicit short-circuit, transformation, side-effect and value-production semantics.
- **Method references and function composition** — Resolve static, bound, unbound, constructor and super references, observing receiver timing, this scope and comparator composition.

### Map callbacks

- **Map callbacks: compute, merge and nullable values** — Trace exactly when map callbacks execute, install a value or remove a mapping, including mapped-null cases.

### Optional

- **Optional and explicit absence** — Compose present/empty lookups, observe real fallback invocation counts and distinguish null callbacks, mapped nulls and invalid input.

### Stream sources and operations

- **Stream sources, pipelines and lazy traversal** — Translate an ordinary loop into a typed lazy pipeline and trace short-circuit demand before exploring complete operation families.
- **Stream mapping, stateful operations and slicing** — Trace intermediate stream operations by element type, cardinality, order and retained state rather than memorizing a fluent chain.
- **Stream sources, terminal results and closure** — Choose finite or bounded sources, interpret empty terminal results, and distinguish consumed pipelines from closed resource owners.
- **Prefix operations, mapMulti and stream list results** — Distinguish prefix selection from filtering, finite iteration from generators, and controlled multi-emission from unmodifiable list collection.

### Reductions

- **Reductions, identities and aggregate results** — Derive all three reduction shapes from identities, associativity and accumulator/combiner compatibility, with explicit partition counterexamples.

### Collectors

- **Collect lists, maps and grouped results** — Choose built-in collection, map, grouping, joining and numeric collector families by their result type, empty policy and duplicate semantics.
- **Downstream filtering, flattening and teeing** — Preserve grouping keys while adapting each group reduction, and combine two aggregates with teeing and an explicit empty-result policy.
- **Collectors and lawful mutable reduction** — Build a custom count/total/maximum collector, preserve empty-state and merge laws, and finish independent mutable state into immutable Stats.

### Gatherers

- **Gatherers and stateful intermediate transformations** — Compare windows, scans and folds, then implement demand-aware running totals with fresh state.

## 10. Standard libraries

Numbers, dates, text, files and network protocols.

### Decimal arithmetic

- **Decimal arithmetic in everyday calculations** — Trace exact decimal products, validate minor units and compare per-item with per-invoice rounding.
- **BigDecimal** — Control decimal representation, scale, precision, rounding and exact conversions.

### Date and time fundamentals

- **Create, parse and calculate dates and times** — Create and transform calendar values, parse strict input and compare local fields with elapsed timeline amounts.

### Time zones and boundaries

- **Time zones, DST and expiry boundaries** — Resolve zone gaps and overlaps, distinguish same-instant from same-local conversion, and implement clock-driven expiry.

### Files and paths

- **Files, paths and resource ownership** — Resolve paths, select file operations and process bounded input while making resource ownership explicit.

### Text and encodings

- **Text encodings and strict decoding** — Distinguish code units, code points and encoded bytes, and preserve decoder state across split input.

### Buffers and channels

- **Buffers, channels and partial transfers** — Trace position, limit and mark; compare shared buffer views and handle partial channel transfers.
- **Parse length-prefixed UTF-8 messages** — Preserve split headers and payloads, validate UTF-8 and lengths, and distinguish incomplete input from EOF truncation.

### TCP sockets

- **TCP sockets, connection lifetimes and message boundaries** — Use Socket and ServerSocket in two ordinary processes, frame one bounded UTF-8 message and distinguish TCP from datagrams.

### HTTP and networking

- **HTTP client, timeouts and response ownership** — Handle response status, body lifetime, size bounds, timeouts and cancellation as separate contracts.

## 11. Threads and concurrency

Create threads, coordinate shared state and manage concurrent tasks.

### Threads and tasks

- **Processes, threads and execution stacks** — Follow one program from its main thread to independent executions sharing objects.
- **Runnable, Thread, start and join** — Create one worker, distinguish start from run, and derive legal execution schedules.
- **Thread subclasses, Runnable classes and lambdas** — Separate the work description from the Thread that executes it, then compare four complete construction forms.
- **Thread names, identity, daemon status and priority** — Read properties from the intended Thread and distinguish descriptive metadata from execution guarantees.
- **Join, sleep and yield: who actually waits?** — Trace the calling thread, the target lifetime and the guarantees a scheduling hint cannot supply.

### Lifecycle and interruption

- **Thread lifecycle and cooperative interruption** — Follow an owned worker through cancellation and cleanup without assuming a particular intermediate state.
- **Interrupt status and cancellation boundaries** — Trace interrupt, isInterrupted and interrupted separately, including the flag-clearing behavior of interruptible waits.

### Synchronization

- **Shared state, lost updates and synchronized repair** — Decompose an increment, follow an overwriting schedule, and protect the whole operation.
- **Instance methods, static methods and monitor identity** — Identify the one object each synchronized form locks, then choose a common guard for one invariant.

### Memory model and visibility

- **The Java Memory Model and happens-before** — Derive publication guarantees from familiar start, join and monitor operations before studying stronger mechanisms.
- **Volatile variables and immutable snapshot publication** — Publish a consistent configuration generation and distinguish publication from a compound update.

### Waiting and conditions

- **Intrinsic monitors, wait and notification** — Use durable state predicates to coordinate a real consumer and then build a closeable mailbox.
- **Odd-even and three-thread turn-taking** — Derive an ordered finite sequence from one guarded turn predicate, including final wakeup and cancellation.
- **ReentrantLock and separate condition queues** — Translate the mailbox invariant from an intrinsic monitor to an explicit lock and two Conditions.

### Coordination utilities

- **Choosing a coordination utility** — Distinguish completion signals, reusable phases and scarce-resource permits before studying their implementations.
- **CountDownLatch: one-time completion and startup signals** — Count durable signals, publish independent results, and add an explicit startup milestone to cancellation.
- **CyclicBarrier: reusable calculation phases** — Reuse one fixed participant boundary and explain the barrier action and broken generations.
- **Phaser: registration, arrival and departure** — Track changing phase participation explicitly and distinguish arrival, waiting and deregistration.
- **Semaphore: bound the scarce resource** — Distinguish resource admission from worker count and release permits only after acquiring them.
- **Exchanger and paired buffer handoff** — Meet a partner at a rendezvous and give each participant the other's reference.

### Executors and futures

- **Executors, Callable and a first Future** — Submit one task, retrieve its result, and own the execution service through shutdown.
- **Executor factories, worker reuse and service shutdown** — Compare factory policies and give a reused service one explicit owner rather than closing it after every request.
- **Submitted task failures and result ownership** — Observe a task failure through its Future and keep completion signals distinct from successful results.
- **Future cancellation, deadlines and cleanup** — Observe a cancelled result separately from the worker that is still releasing its resources.

### Thread pools and scheduling

- **ThreadPoolExecutor admission and overload** — Trace one accepted task at a time through workers, queue capacity and explicit refusal.
- **ForkJoinPool and divide-and-conquer** — Partition disjoint ranges, compute independent results and then examine work-stealing.
- **Scheduled executors and periodic task lifetimes** — Begin with one delayed result, then trace fixed-rate, fixed-delay and terminal outcomes.

### Completion and composition

- **CompletionService and completion-order results** — Start independent tasks together and retrieve whichever result has completed, while retaining cancellation ownership.
- **CompletableFuture: values and completion graphs** — Build transformations, dependent stages and independent combinations one graph edge at a time.

### Concurrent collections

- **Choosing concurrent collections by operation** — Match an actual shared operation to atomicity, iteration, ordering and capacity contracts.
- **BlockingQueue and producer-consumer lifetimes** — Use a real bounded library pipeline, then implement close-and-drain with guarded queue state.
- **SynchronousQueue and zero-buffer handoff** — Transfer directly between a producer and consumer without mistaking a waiting transfer for a stored queue element.
- **ConcurrentHashMap and atomic per-key operations** — Repair a shared frequency update, then trace bins and cooperative resize paths.
- **CopyOnWriteArrayList and snapshot iteration** — Follow two array generations and distinguish stable membership from mutable element contents.
- **ConcurrentLinkedQueue: nonblocking FIFO endpoints** — Use immediate endpoint outcomes, then follow logical removal and helping in the linked representation.
- **ConcurrentSkipListMap: ordered concurrent navigation** — Use neighbors, backed ranges and atomic endpoint removal before studying probabilistic index levels.

### Thread context

- **ThreadLocal, worker reuse and task context** — Begin with ordinary parameters, then bound a per-thread association across nesting and worker reuse.

### Liveness

- **Deadlock, starvation and liveness** — Draw the ownership cycle, repair acquisition order, and distinguish other forms of stalled work.
- **ThreadGroup and unsafe legacy thread control** — Understand older grouping APIs without mistaking a membership hierarchy for task ownership or safe cancellation.

### Atomics and advanced locks

- **Atomics, compare-and-set and ABA** — Build one conditional state transition, then analyze retries, ABA and progress boundaries.
- **LongAdder, LongAccumulator and statistics** — Choose an additive or associative aggregate while keeping exact admission decisions separate from a live statistics observation.
- **Read/write locking and snapshot ownership** — Allow multiple genuine readers while excluding writers, and retain ownership after unlocking through a copy.
- **StampedLock and validated optimistic reads** — Copy a tiny invariant, validate its observation interval and retry under a real read lock when needed.
- **AQS, LockSupport and synchronizer internals** — Implement one open-once signal after separating its state policy from waiting machinery.

## 12. Advanced concurrency and parallelism

Apply thread and memory-model foundations to parallel pipelines, virtual threads and structured task ownership.

### Virtual threads

- **Virtual threads and blocking task execution** — Start with a familiar Thread, explain carrier reuse during blocking, then bound the real scarce resource.

### Parallel data processing

- **Parallel streams and partitioned reduction** — Preserve reduction meaning across partitions, distinguish concurrent collectors from isolated state and interpret result/action order precisely.
- **Concurrent intermediate mapping with gatherers** — Bound mapper work within one stage while preserving result order and defining failure, cancellation and resource ownership.

### Scoped context

- **Scoped values and bounded call context** — Pass context through an ordinary call chain, then explain binding, shadowing and exceptional restoration.

### Structured task lifetimes

- **Structured concurrency and owned child lifetimes** — Start from a parent needing two results, then keep failure selection, sibling cancellation and finished cleanup distinct.

## 13. Advanced JVM internals

Build on runtime basics to analyze loader identity, reachability, garbage collection and JIT optimization.

### Class loading and initialization

- **Class loading, linking and initialization** — Follow ordinary class use through definition, verification and static initialization before loader identity and failure cases.

### Frames and bytecode

- **Bytecode invocation, verification and optimized frames** — Connect familiar source calls to invocation families and separate verified execution semantics from optimized physical frames.

### Runtime memory

- **Reachability analysis and reference strengths** — Distinguish invocation state, shared objects and the paths that keep objects alive.
- **Allocation, retention and process memory budgets** — Distinguish newly created storage from objects still owned, then budget heap and non-heap resources separately.

### Garbage collection

- **Garbage collection: tracing, moving and generations** — Trace live objects, explain movement and remembered references, then read a real G1 event.

### JIT compilation

- **JIT compilation, inlining and optimization evidence** — Follow a hot method from bytecode through compilation records and distinguish plausible optimization from demonstrated evidence.

## 14. Diagnostics and performance

Follow observable evidence from symptom to explanation.

### Diagnosing running programs

- **Thread dumps, heap evidence and JFR** — Reproduce one controlled wait, connect its stacks to a monitor owner, then interpret the completed JFR event.

### Measuring performance

- **Benchmarking: a question, workload and result** — Build one membership-query experiment and learn to read its score without changing the question it answers.

## 15. Advanced Java platform

Metadata, dynamic invocation, modules and data boundaries.

### Annotations and reflection

- **Reflection: discovery, construction and invocation** — Discover constructors and methods, invoke exact declared signatures and diagnose lookup, access and target failures.
- **Annotations, retention and processing** — Choose annotation retention and targets, inspect metadata and distinguish runtime reflection from compile-time processing.

### Dynamic invocation

- **Dynamic proxies and interception contracts** — Build an interface proxy, follow receiver identity and preserve equality, return values and checked failures.
- **Method handles, exact invocation and adaptation** — Derive invocation types, compare exact and adaptable calls, and compose handles without confusing lookup with execution.

### Service discovery

- **Service discovery, packaging and provider selection** — Package service providers, inspect discovery and select implementations with explicit ambiguity and lifetime rules.

### Modules and runtime images

- **Named modules, readability and package boundaries** — Compile named modules and reason separately about readability, exports, reflective openness and service declarations.
- **Dependency analysis and custom runtime images** — Resolve and package a runtime image, inspect its modules and verify dynamically discovered application features.

### Native memory and interoperability

- **Foreign memory: bounds, layouts and lifetimes** — Trace native allocation, slices and struct layouts while enforcing bounds, thread access and arena lifetimes.

### Serialization

- **Serialization graphs, identity and reconstruction** — Encode and restore object graphs, follow stream handles and compare default, custom and external reconstruction.
- **Deserialization filters, compatibility and invariant checks** — Bound input and object graphs, validate restored invariants and distinguish representation compatibility from valid values.

### Security APIs

- **Integrity, signatures and trusted identity** — Compare digests, MACs and signatures, trace verification and separate trusted identity from authorization.
- **Authenticated encryption and nonce ownership** — Trace an AES-GCM round trip, reject tampering and explain keys, nonce uniqueness and authenticated context.

## 16. API design and library evolution

Design reliable APIs, manage dependencies and evolve libraries while preserving client contracts.

### API design and refactoring

- **API contracts, refactoring and meaningful tests** — Specify invariants, build a quote-calculation API and test refactoring without changing the contract.

### Dependency management

- **Resolve dependency versions, scopes and BOMs** — Trace direct and transitive dependencies, compare scopes and use a BOM to control versions without adding dependencies.

### Compatibility and migration

- **Source, binary and behavioral compatibility** — Keep a separately compiled client fixed while changing its library, then compare source, binary and behavioral compatibility.

## Maintaining this sequence

Lesson frontmatter and module metadata define navigation, prerequisites and reading order. Regenerate this outline when those inputs change, and verify that each lesson teaches the knowledge used by the next. See [the course plan](course-plan.md) and [the design specification](design.md).
