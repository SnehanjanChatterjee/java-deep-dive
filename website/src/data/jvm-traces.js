const compilationCode = [
 'mkdir -p out',
 'javac -d out HelloJava.java',
 'java -cp out HelloJava',
 '// Statement inside main (source excerpt):',
 'System.out.println("Hello, Java!");',
];

const compilationStart = {
 phase:0, source:'valid', artifact:'none', application:'not launched', diagnostic:'', output:'',
};

const successfulCompilation = {
 kind:'compilation',
 title:'From source text to a running greeting',
 summary:'Follow one successful compilation and launch. The output panel records application output; javac checks the print statement without executing it.',
 outputLabel:'Application output',
 caption:'A guided model of the commands shown in the lesson. The Java source statement is an excerpt from the complete HelloJava program.',
 code:compilationCode,
 steps:[
  {...compilationStart,line:0,text:'HelloJava.java is saved. Create the output directory; this changes the filesystem, not the program output.'},
  {...compilationStart,line:1,phase:1,text:'javac reads the source and checks its syntax and types. HelloJava.main has not started.'},
  {...compilationStart,line:1,phase:2,artifact:'new',text:'Successful compilation writes out/HelloJava.class containing bytecode and metadata. It does not print the greeting.'},
  {...compilationStart,line:2,phase:3,artifact:'new',application:'loading HelloJava',text:'The java launcher starts the application JVM. With out as the classpath root, the JVM locates and loads HelloJava.class.'},
  {...compilationStart,line:2,phase:4,artifact:'new',application:'running main',text:'After the required loading, linking and initialization, the runtime invokes main. There is now an application execution, not just a class file.'},
  {...compilationStart,line:4,phase:5,artifact:'new',application:'running main',output:'Hello, Java!',text:'The running program executes println. Only now does Hello, Java! appear in the application output.'},
  {...compilationStart,line:2,phase:5,artifact:'new',application:'finished',output:'Hello, Java!',text:'main returns and this small application finishes. The class file remains on disk and can be launched again.'},
 ],
};

const staleClassCompilation = {
 kind:'compilation',
 title:'A failed edit does not replace an old class file',
 summary:'Start with the previously compiled greeting. An edited print statement is missing its semicolon; the compiler diagnostic and application output come from different processes.',
 outputLabel:'Application output',
 caption:'The previous class file is deliberately retained in this scenario. A compiler diagnostic is not output from the application main method.',
 code:compilationCode.map((line,index)=>index===4?'System.out.println("Changed")':line),
 steps:[
  {...compilationStart,line:0,source:'invalid edit',artifact:'previous',text:'An earlier successful build left HelloJava.class in out. The new source edit says Changed but has no semicolon.'},
  {...compilationStart,line:1,phase:1,source:'invalid edit',artifact:'previous',text:'javac checks the edited source. The existing class file is not evidence that this new source is valid.'},
  {...compilationStart,line:4,phase:1,source:'invalid edit',artifact:'previous',diagnostic:"error: ';' expected",text:'The compiler rejects the missing semicolon. This single-file syntax failure produces no replacement class file, and no application greeting has run.'},
  {...compilationStart,line:2,phase:3,source:'invalid edit',artifact:'previous',diagnostic:"error: ';' expected",application:'loading previous class',text:'If you still run java, it can load the old class file. The launcher does not recompile the failed edit in this class-launch mode.'},
  {...compilationStart,line:2,phase:5,source:'invalid edit',artifact:'previous',diagnostic:"error: ';' expected",application:'running previous main',output:'Hello, Java!',text:'The old main prints Hello, Java!, not Changed. The current invalid source excerpt is not being executed. A runnable old artifact does not prove the edited source compiled.'},
 ],
};

/** Builds a chosen paused-application tracing/copying model, not a live JVM probe. */
export function makeGcTrace(retainAlias=false) {
 const code=['pause the application for this model','start from the supplied roots','mark A from root','follow A.next and mark B'];
 const steps=[];
 const base={marked:[],copied:[],rootRegion:'original',aliasRegion:'original',edgeRegion:'original',reclaimed:false,traced:false,retainAlias,move:'No representation moved this step',output:''};
 const add=(line,text,changes={})=>steps.push({...base,...changes,line,text});
 add(0,'This chosen model pauses application changes. Original positions contain C, A and B; root reaches A, and A.next reaches B.');
 add(1,retainAlias?'The root set includes root → A and keep → C. Keeping one extra reference changes which objects survive.':'Only root → A is supplied. C has no path from that root.');
 add(2,'The root reaches A, so mark A. Its next reference has not yet been followed; no representation has moved.',{marked:['A']});
 add(3,'Follow A.next and mark B. Marking describes reachability, not relocation. B has no further outgoing reference.',{marked:['A','B']});
 const live=retainAlias?['A','B','C']:['A','B'];
 code.push(retainAlias?'follow keep and mark C':'classify unmarked C as unreachable');
 add(4,retainAlias?'Following keep also marks C. All three identities must survive this cycle.':'C remains unmarked because no root path reaches it. Its storage can be reclaimed under this model.',{marked:live,traced:true});
 for(const [index,id]of live.entries()) {
  code.push(`copy ${id}'s representation to destination ${index+1}`);
  add(code.length-1,`Copy ${id}'s representation into destination position ${index+1}. It is still object ${id}, not a new Java object created by application code.`,{marked:live,traced:true,copied:live.slice(0,index+1),move:`${id}: original ${['C','A','B'].indexOf(id)+1} → destination ${index+1}`});
 }
 code.push('update root to relocated A');
 add(code.length-1,'Update root so it identifies A at its destination. The old region remains available until all required references are repaired.',{marked:live,traced:true,copied:live,rootRegion:'destination'});
 code.push('update relocated A.next to relocated B');
 add(code.length-1,'Repair the reference inside relocated A as well: A.next must reach relocated B. Updating only the root would leave that field stale.',{marked:live,traced:true,copied:live,rootRegion:'destination',edgeRegion:'destination'});
 if(retainAlias) {
  code.push('update keep to relocated C');
  add(code.length-1,'The additional keep root must also identify C at its destination before the original region can be reused.',{marked:live,traced:true,copied:live,rootRegion:'destination',edgeRegion:'destination',aliasRegion:'destination'});
 }
 code.push('reclaim the original region');
 add(code.length-1,retainAlias?'All three identities survive at new illustrative positions. The original region can be reused after reference repair.':'A and B survive with the same identities and values. Unreachable C is not copied; the original region can be reused after reference repair.',{marked:live,traced:true,copied:live,rootRegion:'destination',edgeRegion:'destination',aliasRegion:'destination',reclaimed:true,output:retainAlias?'Survivors: A, B, C\nValues: 7, 9, 99':'Survivors: A, B\nValues: 7, 9'});
 return {kind:'gc',title:'Trace, relocate and repair references',summary:'A chosen stop-the-world tracing/copying cycle. Positions are diagram labels, not memory addresses. Collectors differ; garbage collection does not always move objects.',outputLabel:'Model result',caption:'Conceptual collector pseudocode, not Java source or a live JVM measurement. Object identity and values are preserved when a representation moves.',code,steps};
}

export const jvmTraces = {
 compilation:{success:successfulCompilation,error:staleClassCompilation},
 gc:{ordinary:makeGcTrace(false),alias:makeGcTrace(true)},
};
