function makeSteps(initial, events) {
 let current={...initial,locations:{...initial.locations},tones:{...initial.tones},visibility:{...initial.visibility}};
 return events.map(event=>{
  current={...current,...event,
   locations:{...current.locations,...event.locations},
   tones:{...current.tones,...event.tones},
   visibility:{...current.visibility,...event.visibility}};
  return {...current};
 });
}

export const advancedMotionBoards={
 executor:{height:516,
  regions:[
   {id:'caller',label:'Caller',x:2,y:0,width:96,height:90},
   {id:'admission',label:'Admission',x:28,y:104,width:44,height:82},
   {id:'worker1',label:'Worker 1',x:12.5,y:202,width:25,height:100},
   {id:'worker2',label:'Worker 2',x:62.5,y:202,width:25,height:100},
   {id:'queue',label:'Queue',x:27,y:306,width:46,height:86},
   {id:'done',label:'Completed',x:2,y:410,width:64,height:100},
   {id:'reject',label:'Reject',x:72,y:410,width:26,height:100},
  ],
  points:{inputA:[17,60],inputB:[39,60],inputC:[61,60],inputD:[83,60],gate:[50,158],worker1:[25,272],worker2:[75,272],queue:[50,362],doneA:[14,478],doneB:[34,478],doneC:[54,478],rejectTurn:[93.5,158],rejectReturn:[93.5,478],rejected:[85,478]},
  paths:[[50,90,50,104],[50,186,25,202],[50,186,75,202],[50,186,50,306],[50,392,25,290]],
  tokens:[{id:'A',label:'A'},{id:'B',label:'B'},{id:'C',label:'C'},{id:'D',label:'D'}],
 },
 queue:{height:532,
  regions:[
   {id:'producer',label:'Producer owns',x:2,y:0,width:96,height:94},
   {id:'waiting',label:'Waiting put',x:28,y:112,width:44,height:82},
   {id:'storage',label:'FIFO storage',x:15,y:214,width:70,height:96},
   {id:'consumer',label:'Consumer owns',x:25,y:328,width:50,height:88},
   {id:'processed',label:'Processed',x:2,y:434,width:96,height:92},
  ],
  points:{producerA:[20,62],producerB:[50,62],producerC:[80,62],waiting:[50,166],slot0:[37,278],slot1:[63,278],consumer:[50,386],doneA:[20,494],doneB:[50,494],doneC:[80,494]},
  paths:[[50,94,50,112],[50,194,50,214],[50,310,50,328],[50,416,50,434]],
  tokens:[{id:'A',label:'10'},{id:'B',label:'20'},{id:'C',label:'12'}],
 },
 cas:{height:484,
  regions:[
   {id:'a',label:'Caller A',x:2,y:0,width:45,height:116},
   {id:'b',label:'Caller B',x:53,y:0,width:45,height:116},
   {id:'compare',label:'Atomic CAS',x:25,y:144,width:50,height:100},
   {id:'remaining',label:'Budget',x:35,y:262,width:30,height:90},
   {id:'aResult',label:'A result',x:2,y:370,width:45,height:108},
   {id:'bResult',label:'B result',x:53,y:370,width:45,height:108},
  ],
  points:{aLocal:[25,80],bLocal:[75,80],compare:[50,210],aReturn:[20,210],bReturn:[80,210],aResult:[20,444],bResult:[80,444],remaining:[50,320]},
  paths:[[25,116,50,144],[75,116,50,144],[50,244,50,262],[50,352,25,370],[50,352,75,370]],
  tokens:[{id:'A',type:'attempt'},{id:'B',type:'attempt'},{id:'remaining',type:'remaining'}],
 },
 completion:{height:504,
  regions:[
   {id:'score',label:'Score stage',x:2,y:0,width:45,height:104},
   {id:'label',label:'Label stage',x:53,y:0,width:45,height:104},
   {id:'inputs',label:'Both inputs?',x:15,y:126,width:70,height:96},
   {id:'action',label:'Combine action',x:5,y:240,width:90,height:124},
   {id:'result',label:'Result retrieved',x:10,y:390,width:80,height:106},
  ],
  points:{scoreSource:[25,72],labelSource:[75,72],scoreReady:[33,190],labelReady:[67,190],scoreAction:[30,290],labelAction:[70,290],created:[50,340],returned:[50,458]},
  paths:[[25,104,33,126],[75,104,67,126],[50,222,50,240],[50,364,50,390]],
  tokens:[{id:'score',type:'completion-value'},{id:'label',type:'completion-value'},{id:'result',type:'completion-result'}],
 },
};

const executor={
 kind:'executor',title:'Route each submission through finite capacity',playbackMs:3000,
 summary:'Core size 1, maximum size 2, queue capacity 1. A–D label submission attempts, not four different Runnable objects.',
 codeLabel:'ExecutorSaturationLab · admission excerpt',
 caption:'One coordinated admission trace and one legal completion order. C can start before queued B; this executor does not promise global execution FIFO.',
 code:[
  'final ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 2,',
  '    1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1),',
  '    new ThreadPoolExecutor.AbortPolicy());',
  'pool.execute(blocked); // submission A',
  'pool.execute(blocked); // submission B',
  'pool.execute(blocked); // submission C',
  'try {',
  '    pool.execute(blocked); // submission D',
  '} catch (RejectedExecutionException expected) {',
  '    System.out.println("rejected");',
  '}',
  'release.countDown();',
  'pool.shutdown();',
  'pool.awaitTermination(5, TimeUnit.SECONDS);',
  'System.out.println("completed=" + pool.getCompletedTaskCount());',
 ],
 steps:makeSteps({locations:{A:'inputA',B:'inputB',C:'inputC',D:'inputD'},tones:{},created:0,worker1:null,worker2:null,queue:[],accepted:[],rejected:[],completed:[],decision:'No submission yet',gateOpen:false},[
  {line:0,text:'The two worker boxes show available capacity, not prestarted threads. No worker exists until a submission needs one.'},
  {line:3,text:'A leaves the caller and enters admission. The current worker count, zero, is below the core size of one.',locations:{A:'gate'},decision:'A: below core size'},
  {line:3,text:'Create the core worker for A. A occupies Worker 1 and waits at the release gate established by the complete program.',locations:{A:'worker1'},created:1,worker1:'A',accepted:['A'],decision:'A: create core worker'},
  {line:4,text:'B arrives while the core worker is occupied. Admission first tries the queue; the maximum size is not the next decision yet.',locations:{B:'gate'},decision:'B: try queue before extra worker'},
  {line:4,text:'The one queue slot accepts B. Worker 2 is still absent, despite maximum size two.',locations:{B:'queue'},queue:['B'],accepted:['A','B'],decision:'B: queued; no growth'},
  {line:5,text:'C arrives. The core worker is occupied and the queue already contains B.',locations:{C:'gate'},decision:'C: queue offer fails'},
  {line:5,text:'Only now can admission grow above core size. Create Worker 2 for C. C starts without first removing B from the queue.',locations:{C:'worker2'},created:2,worker2:'C',accepted:['A','B','C'],decision:'C: create above-core worker'},
  {line:7,text:'D arrives with two occupied workers and one queued task. Neither capacity path can accept it.',locations:{D:'gate'},decision:'D: queue full and maximum reached'},
  {line:9,text:'AbortPolicy reports rejection. D follows a rejection return path; it never joins the work queue and never executes.',locations:{D:'rejectTurn'},tones:{D:'rejected'},rejected:['D'],decision:'D: rejected',output:'rejected'},
  {line:9,text:'The refusal travels outside the occupied worker slots. No accepted-work capacity is assigned to D.',locations:{D:'rejectReturn'}},
  {line:9,text:'D’s rejected outcome is kept separate from accepted tasks that may later complete.',locations:{D:'rejected'}},
  {line:11,text:'Open the release gate. A and C can now finish. The following completion order is one permitted schedule.',gateOpen:true,decision:'Accepted workers may continue'},
  {line:13,text:'In this chosen schedule A finishes first and frees Worker 1. D remains rejected, and B is still queued.',locations:{A:'doneA'},worker1:null,completed:['A'],decision:'A: completed'},
  {line:13,text:'Worker 1 retrieves B from the queue. Queue capacity is available again; B now owns a worker execution slot.',locations:{B:'worker1'},worker1:'B',queue:[],decision:'B: dequeued onto Worker 1'},
  {line:13,text:'C completes on Worker 2. The accepted work is not required to finish in submission order.',locations:{C:'doneC'},worker2:null,completed:['A','C'],decision:'C: completed'},
  {line:13,text:'B completes. All three accepted tasks finished, and no queue element remains.',locations:{B:'doneB'},worker1:null,completed:['A','C','B'],decision:'B: completed'},
  {line:14,text:'After the program owns shutdown and termination, its completed count is three. Rejected D is excluded from that count.',decision:'Three accepted completions; one rejection',output:'rejected\ncompleted=3'},
 ]),
};

const queue={
 kind:'queue',title:'Follow payload ownership through a bounded FIFO',playbackMs:3000,
 summary:'Two storage slots, one producer and one consumer. A signal makes a waiter eligible; it does not hand over the queue lock.',
 codeLabel:'CloseableWorkQueue · successful put/take paths',outputLabel:'Processed outside the queue',
  caption:'A chosen legal schedule with capacity 2 and no closure. Slots show logical FIFO order, not physical array copying. Ownership means application responsibility: enqueueing copies a reference, does not clone the object and does not prevent retained aliases. Full closure/failure branches remain in the canonical source.',
 code:[
  'lock.lockInterruptibly(); // producer',
  'while (size == elements.length && !closed) {',
  '    notFull.await(); // releases, then reacquires lock',
  '}',
  '// Enqueue value, advance tail, increment size.',
  'notEmpty.signal();',
  'lock.unlock(); // finally',
  'lock.lockInterruptibly(); // consumer',
  'while (size == 0 && !closed) {',
  '    notEmpty.await(); // releases, then reacquires lock',
  '}',
  '// Remove head, clear slot, decrement size.',
  'notFull.signal();',
  'lock.unlock(); // finally',
  '// Process the returned payload outside the queue lock.',
 ],
 steps:makeSteps({locations:{A:'producerA',B:'producerB',C:'producerC'},tones:{},queue:[],lockOwner:'none',producer:'Ready to put 10',consumer:'Ready to take',removed:[],processed:[],accepted:[],current:null},[
  {line:7,text:'The consumer acquires the queue lock. All payloads are still owned by the producer; storage is empty.',lockOwner:'consumer'},
  {line:8,text:'The empty predicate is true. A take cannot invent a payload.'},
  {line:9,text:'The consumer awaits notEmpty and releases the queue lock. It is waiting for an item, not holding the lock needed by the producer.',lockOwner:'none',consumer:'Waiting for an item'},
  {line:0,text:'The producer acquires the queue lock for put(10).',lockOwner:'producer'},
  {line:4,text:'Insert 10 as the oldest item. Ownership transfers into queue storage when insertion succeeds.',locations:{A:'slot0'},queue:['A'],accepted:['A'],producer:'10 accepted'},
  {line:5,text:'Signal notEmpty. The consumer is eligible to recheck, while the producer still owns the lock.',consumer:'Signalled; must reacquire'},
  {line:6,text:'The producer releases the lock. The chosen schedule lets it make the next put before the consumer runs.',lockOwner:'none',producer:'Ready to put 20'},
  {line:0,text:'The producer reacquires for put(20). The second slot is free.',lockOwner:'producer'},
  {line:4,text:'Insert 20 after 10. Storage is now full: two queued payloads, not three.',locations:{B:'slot1'},queue:['A','B'],accepted:['A','B'],producer:'20 accepted'},
  {line:6,text:'After signalling, the producer releases the lock and attempts its third put.',lockOwner:'none',producer:'Ready to put 12'},
  {line:0,text:'The producer acquires the lock and observes the full queue.',lockOwner:'producer'},
  {line:2,text:'Put(12) awaits notFull and releases the lock. The waiting payload is still producer-owned; it has not been inserted.',locations:{C:'waiting'},tones:{C:'waiting'},lockOwner:'none',producer:'Waiting for space'},
  {line:7,text:'The consumer reacquires the lock and rechecks its item predicate. Storage contains 10 followed by 20.',lockOwner:'consumer',consumer:'Rechecks; an item exists'},
  {line:11,text:'Remove oldest payload 10. The consumer owns that returned item; 20 is now the oldest queued item. One slot becomes free.',locations:{A:'consumer',B:'slot0'},queue:['B'],current:'A',removed:['A'],consumer:'Owns 10'},
  {line:12,text:'Signal notFull. The producer becomes eligible to recheck, but the consumer retains the lock until it exits.',producer:'Signalled; must reacquire'},
  {line:13,text:'The consumer releases the queue lock. Removing an item is separate from processing it.',lockOwner:'none'},
  {line:14,text:'Process 10 outside the queue lock, so this application work does not retain queue ownership.',locations:{A:'doneA'},processed:['A'],current:null,consumer:'Processed 10',output:'10'},
  {line:0,text:'The producer reacquires and rechecks capacity. Its earlier signal alone did not authorize an insertion.',lockOwner:'producer',producer:'Rechecks; one slot is free'},
  {line:4,text:'Insert 12 after 20. Only this successful insertion transfers the waiting payload into storage.',locations:{C:'slot1'},tones:{C:'normal'},queue:['B','C'],accepted:['A','B','C'],producer:'12 accepted'},
  {line:6,text:'After signalling, the producer releases the lock. The finite three-item batch has all been accepted.',lockOwner:'none',producer:'Batch supplied'},
  {line:7,text:'The consumer acquires for its next take.',lockOwner:'consumer'},
  {line:11,text:'Remove 20 before 12. FIFO follows insertion order even though one put had to wait.',locations:{B:'consumer',C:'slot0'},queue:['C'],current:'B',removed:['A','B'],consumer:'Owns 20'},
  {line:13,text:'After signalling capacity, the consumer releases the lock.',lockOwner:'none'},
  {line:14,text:'Process 20 outside the lock.',locations:{B:'doneB'},processed:['A','B'],current:null,consumer:'Processed 20',output:'10 → 20'},
  {line:7,text:'The consumer acquires for its final take in this batch.',lockOwner:'consumer'},
  {line:11,text:'Remove 12. Storage is empty again, and the consumer owns the last payload.',locations:{C:'consumer'},queue:[],current:'C',removed:['A','B','C'],consumer:'Owns 12'},
  {line:13,text:'Release the queue lock after signalling any relevant capacity waiters.',lockOwner:'none'},
  {line:14,text:'Process 12. Emptiness now means this selected batch is drained; a general service still needs an explicit termination protocol.',locations:{C:'doneC'},processed:['A','B','C'],current:null,consumer:'Batch processed',output:'10 → 20 → 12'},
 ]),
};

function makeCasTrace(capacity,units,retrySucceeds) {
 const afterA=capacity-units;
 const finalRemaining=retrySucceeds?afterA-units:afterA;
 const events=[
  {line:0,text:`Create a budget of ${capacity}. Each contender requests ${units}; the moving bundle represents one call’s expected value and replacement argument.`},
  {line:3,text:`A reads ${capacity} into its own previous local. Its candidate is ${capacity}-${units}=${afterA}.`,expectedA:capacity,candidateA:afterA,phaseA:'Prepared from its read'},
  {line:3,text:`B reads the same ${capacity} before A updates it. B’s equal local value is a separate observation, not a reservation.`,expectedB:capacity,candidateB:afterA,phaseB:'Prepared from its read'},
  {line:5,text:'A supplies its expected/candidate arguments to the atomic comparison point.',locations:{A:'compare'},phaseA:'CAS attempt'},
  {line:5,text:`A’s comparison matches ${capacity}; CAS installs ${afterA} atomically and returns true.`,tones:{A:'success'},remaining:afterA,successes:['A'],phaseA:'CAS succeeded'},
  {line:6,text:'The successful comparison lets A return true. Its argument bundle leaves the comparison point on the return path.',locations:{A:'aReturn'},phaseA:'Reservation returns true'},
  {line:6,text:'A’s caller has a successful reservation outcome. The expected argument is a past observation, not a live view of remaining.',locations:{A:'aResult'}},
  {line:5,text:`B supplies its stale expected ${capacity}. Shared remaining is already ${afterA}.`,locations:{B:'compare'},phaseB:'CAS attempt with stale expected'},
  {line:5,text:'The comparison fails. B does not install its old candidate and must not report a successful reservation.',tones:{B:'rejected'},phaseB:'CAS failed; no shared write'},
  {line:5,text:'The failed attempt leaves the comparison point. CAS returned false to the retry loop; tryReserve has not returned its final outcome.',locations:{B:'bReturn'}},
  {line:3,text:`B loops and rereads shared remaining: ${afterA}. It must revalidate this fresh value before another update.`,locations:{B:'bLocal'},tones:{B:'normal'},expectedB:afterA,candidateB:retrySucceeds?finalRemaining:null,phaseB:'Reread; old arguments discarded'},
 ];
 if(retrySucceeds){
  events.push(
   {line:5,text:`One unit still fits. B recomputes the candidate ${finalRemaining} and retries with the new expected ${afterA}.`,locations:{B:'compare'},phaseB:'CAS retry from fresh state'},
   {line:5,text:`The fresh comparison succeeds. B installs ${finalRemaining}; two distinct successful reservations have taken effect.`,tones:{B:'success'},remaining:finalRemaining,successes:['A','B'],phaseB:'CAS succeeded'},
   {line:6,text:'B can return true after this successful attempt. Its bundle follows the caller’s return path.',locations:{B:'bReturn'},phaseB:'Reservation returns true'},
   {line:6,text:'Each successful CAS reserved once. The sum of reserved units and remaining equals the original capacity.',locations:{B:'bResult'},output:`A: true\nB: true\nremaining=${finalRemaining}`},
  );
 }else{
  events.push({line:4,text:`B’s fresh remaining ${afterA} cannot satisfy a request of ${units}. It returns false before calculating/installing a negative candidate.`,locations:{B:'bResult'},tones:{B:'waiting'},phaseB:'Insufficient budget; returns false',output:`A: true\nB: false\nremaining=${finalRemaining}`});
 }
 return {
  kind:'cas',title:'Move each expected value through a CAS attempt',playbackMs:3000,
  summary:`Initial remaining ${capacity}; A and B each request ${units}. An unsuccessful comparison leaves shared state unchanged.`,
  codeLabel:'AtomicCounterLab.Budget.tryReserve · selected calls',outputLabel:'Selected reservation outcomes',
  caption:'Bundles show argument values, not physical hardware traffic. E is previous; U is previous minus units, computed only when the request fits.',
  code:[`// Budget(${capacity}); A and B each call tryReserve(${units}).`,'for (;;) {','    // Each call has its own previous local.','    final int previous = remaining.get();','    if (units > previous) { return false; }','    if (remaining.compareAndSet(previous, previous - units)) {','        return true;','    }','}'],
  capacity,units,
  steps:makeSteps({locations:{A:'aLocal',B:'bLocal',remaining:'remaining'},tones:{},remaining:capacity,expectedA:null,expectedB:null,candidateA:null,candidateB:null,successes:[],phaseA:'Not read yet',phaseB:'Not read yet'},events),
 };
}

function makeCompletionTrace(first) {
 const second=first==='score'?'label':'score';
 const name=key=>key==='score'?'score 43':'label “score”';
 const complete=(key,line)=>({line,text:`The ${name(key)} input completes. Its value can be supplied to the dependent action; the source future retains that value.`,[key+'Done']:true,locations:{[key]:key+'Ready'}});
 const events=[
  {line:4,text:'The combine action is attached while its two input stages are pending. The score still has its upstream userId dependency, not shown as another input here.'},
  complete(first,first==='score'?2:3),
  {line:5,text:`Only ${name(first)} is available. The missing ${second} input prevents this successful combine action from running.`,combine:'Waiting for the other input'},
  complete(second,second==='score'?2:3),
  {line:5,text:'Both successful inputs are available. The dependency condition is satisfied; this does not request a fresh thread.',combine:'Inputs ready; action eligible'},
  {line:5,text:'Invoke the non-async combination with value 43 and name “score”. The action may run in a permitted completing/attaching caller.',locations:{score:'scoreAction',label:'labelAction'},combine:'Combination executes'},
  {line:5,text:'The function produces score=43. The new value is shown at the action, before the result is retrieved.',visibility:{result:true},combine:'Function returned',resultValue:'score=43'},
  {line:6,text:'The result completes and the waiting caller retrieves score=43. Joining these value dependencies did not give the graph ownership of arbitrary underlying work.',locations:{result:'returned'},resultDone:true,combine:'Result available to caller',output:'score=43'},
 ];
 return {kind:'completion',title:'Let completed inputs flow into one continuation',playbackMs:3000,
  summary:'The score and label are independent inputs to thenCombine. Their arrival order can vary; both are required for this successful result.',
  codeLabel:'CompletableFutureLab · named-result equivalent',
  caption:'Tokens are value copies, not removed source values. thenCombine is non-async; eligibility, execution and a guaranteed new thread are different claims.',
  code:[
   'final CompletableFuture<Integer> userId =',
   '    CompletableFuture.supplyAsync(() -> 42, pool);',
   'final CompletableFuture<Integer> score = userId.thenCompose(id -> CompletableFuture.supplyAsync(() -> id + 1, pool));',
   'final CompletableFuture<String> label = CompletableFuture.supplyAsync(() -> "score", pool);',
   'final CompletableFuture<String> result = score.thenCombine(label,',
   '    (value, name) -> name + "=" + value);',
   'System.out.println(result.get());',
  ],
  steps:makeSteps({locations:{score:'scoreSource',label:'labelSource',result:'created'},tones:{},visibility:{result:false},scoreDone:false,labelDone:false,resultDone:false,resultValue:null,combine:'Waiting for both inputs'},events),
 };
}

export const advancedThreadModels={
 executor:{defaultVariant:'bounded',variants:[{id:'bounded',label:'Core 1 · max 2 · queue 1',trace:executor}]},
 queue:{defaultVariant:'fifo',variants:[{id:'fifo',label:'Two-slot FIFO',trace:queue}]},
 cas:{defaultVariant:'retry',selectorLabel:'Reservation case',variants:[
  {id:'retry',label:'Two one-unit reservations',trace:makeCasTrace(2,1,true)},
  {id:'exhausted',label:'Two two-unit requests',trace:makeCasTrace(3,2,false)},
 ]},
 completion:{defaultVariant:'score-first',selectorLabel:'Completion order',variants:[
  {id:'score-first',label:'Score completes first',trace:makeCompletionTrace('score')},
  {id:'label-first',label:'Label completes first',trace:makeCompletionTrace('label')},
 ]},
};
