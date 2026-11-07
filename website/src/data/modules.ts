export interface CourseModule {
 id:string;
 title:string;
 description:string;
 groups:string[];
}
export const modules:CourseModule[] = [
 {id:'execution',title:'Getting started with Java',description:'Write, run and organize your first programs',groups:['First programs','How Java runs','Tools and project structure']},
 {id:'language',title:'Language fundamentals',description:'Variables, decisions, loops, methods, arrays and text',groups:['Variables and expressions','Decisions and loops','Methods and recursion','Arrays and strings','Conversions and numeric boundaries']},
 {id:'objects',title:'Classes and objects',description:'Build objects, express behavior and manage shared references',groups:['Classes and objects','Construction and encapsulation','Interfaces and inheritance','References and identity','Nested and domain types']},
 {id:'runtime-foundations',title:'JVM foundations',description:'Explore JVM architecture, method frames, object references and bytecode',groups:['JVM architecture','Stack and heap','Frames and bytecode']},
 {id:'workflow',title:'Projects and tests',description:'Compile, test and package a small Java project',groups:['Build and test a small project']},
 {id:'reliability',title:'Exceptions and resources',description:'Follow failures through calls and close resources correctly',groups:['Exception flow','Failure contracts','Resource ownership']},
 {id:'generics',title:'Generics',description:'Develop reusable types before bounds and runtime details',groups:['Generic types and methods','Bounds and wildcards','Runtime representation']},
 {id:'collections',title:'Collections',description:'Use, compare and implement containers',groups:['Collection foundations','Iteration and views','Ordering','Lists and arrays','Maps and search trees','Sets','Queues and priority','Implementation practice']},
 {id:'functional',title:'Lambdas and streams',description:'Pass behavior, build pipelines and combine results',groups:['Lambdas and functional interfaces','Composition and absence','Stream foundations','Collectors','Parallel streams','Gatherers']},
 {id:'libraries',title:'Standard libraries',description:'Numbers, dates, files, encodings and network responses',groups:['Decimal arithmetic','Date and time fundamentals','Time zones and boundaries','Files and paths','Text and encodings','Buffers and channels','HTTP and networking']},
 {id:'concurrency',title:'Threads and concurrency',description:'Start with a thread, then coordinate shared work',groups:['Threads and tasks','Shared state and synchronization','Thread lifecycle and interruption','Guarded waiting','Coordination utilities','Tasks and executors','Completion and cancellation','Concurrent data structures','Thread context','Liveness','Advanced synchronization']},
 {id:'modern-concurrency',title:'Advanced concurrency',description:'Virtual threads, scoped context and structured task ownership',groups:['Virtual threads','Scoped context','Structured task lifetimes']},
 {id:'jvm',title:'JVM internals',description:'Loading, linkage, memory, collection and compilation',groups:['Class loading and initialization','Frames and bytecode','Runtime memory','Garbage collection','JIT compilation']},
 {id:'diagnostics',title:'Diagnostics and performance',description:'Follow observable evidence from symptom to explanation',groups:['Diagnosing running programs','Measuring performance']},
 {id:'platform',title:'Advanced Java platform',description:'Metadata, dynamic invocation, modules and data boundaries',groups:['Annotations and reflection','Dynamic invocation','Service discovery','Modules and runtime images','Native memory and interoperability','Serialization','Security APIs']},
 {id:'engineering',title:'API design and builds',description:'Build, test, evolve and package a Java project',groups:['Project and test workflow','API design and refactoring','Dependency management','Compatibility and migration']},
];
