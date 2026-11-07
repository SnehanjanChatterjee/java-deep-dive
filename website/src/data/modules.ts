export interface CourseModule {
 id:string;
 title:string;
 description:string;
 groups:string[];
}
export const modules:CourseModule[] = [
 {id:'execution',title:'Java execution',description:'Source, bytecode and development tools',groups:['Execution and tooling']},
 {id:'language',title:'Language foundations',description:'Types, methods, arrays and text',groups:['Types and expressions','Methods and values','Arrays and text']},
 {id:'objects',title:'Object model',description:'Contracts, composition and immutability',groups:['Object model','Value contracts','Modern modeling']},
 {id:'reliability',title:'Exceptions and resources',description:'Failure contracts and resource lifetimes',groups:['Failures and resource ownership']},
 {id:'generics',title:'Generics',description:'Type-safe reusable APIs',groups:['Type parameters and API bounds']},
 {id:'collections',title:'Collections',description:'Data structures, internals and implementations',groups:['Collection contracts','Lists and arrays','Maps','Sets','Queues and priority','Implementation practice']},
 {id:'functional',title:'Functional Java',description:'Lambdas, streams and Optional',groups:['Functions and absence','Stream pipelines','Collectors and gatherers']},
 {id:'libraries',title:'Standard libraries',description:'Numbers, time, files and networking',groups:['Numbers and time','Files and encodings','Networking']},
 {id:'concurrency',title:'Concurrency',description:'Threads, memory ordering, coordination and asynchronous work',groups:['Threads and correctness','Locks and coordination','Liveness','Tasks and executors','Concurrent data structures']},
 {id:'modern-concurrency',title:'Modern threading',description:'Virtual threads, context and structured task lifetimes',groups:['Virtual threads and resource limits','Context and task lifetimes']},
 {id:'jvm',title:'JVM internals',description:'Loading, allocation, GC and compilation',groups:['Loading and execution','Memory and performance','Garbage collection','JIT and optimization']},
 {id:'diagnostics',title:'Diagnostics and performance',description:'Investigate before optimizing',groups:['Diagnostics','Measurement']},
 {id:'platform',title:'Advanced platform',description:'Metadata, modules and interoperability',groups:['Metadata and extensibility','Modules and interoperability','Data and trust boundaries']},
 {id:'engineering',title:'Engineering practice',description:'Design, testing and compatible builds',groups:['API design and testing','Builds and compatibility']},
];
