const buildSteps=(initial,events)=>{
 let state=initial;
 return events.map(event=>{state={...state,...event};return state;});
};
const common={playbackMs:3000,outputLabel:'Output from the selected path'};
const executionBase={mainState:'RUNNABLE',workerState:'NEW',workRow:0,workX:8,mainNote:'Caller is ready to invoke work',workerNote:'Thread object exists; no worker execution yet',output:''};
const direct={...common,kind:'basic-execution',title:'Calling run keeps the work on the caller',summary:'The moving work marker follows execution responsibility. The Runnable object and its references are not physically moved.',codeLabel:'RunDoesNotStartLesson.java · caller/action excerpts',code:['// caller','final Thread worker = new Thread(task, "worker");','worker.run();','// Runnable action','System.out.println(Thread.currentThread().getName());','// caller resumes','System.out.println("after run");','System.out.println(worker.getState());'],steps:buildSteps(executionBase,[
 {line:1,text:'The platform Thread object is NEW. Creating it has not created a running worker execution.'},
 {line:2,workX:40,mainNote:'Caller enters the ordinary run call',text:'worker.run() is an ordinary call. The work marker advances along the caller lane, not into a new execution.'},
 {line:4,workX:65,mainNote:'main executes the action',output:'main',text:'currentThread identifies main because main is executing this action. The unstarted worker object is still NEW.'},
 {line:6,workX:86,mainNote:'run returned to its caller',output:'main\nafter run',text:'The call returns to main. Returning from a direct run call does not terminate an execution that was never started.'},
 {line:7,workX:94,output:'main\nafter run\nNEW',text:'The state print remains NEW. The marker shows completed work on main, not a migration of the Runnable object.'},
 ]),caption:'Selected platform-thread example. Marker = execution/work position; it is not the Runnable object, a CPU core, or measured scheduler progress.'};
const started={...common,kind:'basic-execution',title:'start dispatches work to a separate execution',summary:'Choose a legal schedule in which main reaches join before the worker finishes. start and completion remain separate events.',codeLabel:'StartAndJoinOrderLesson.java · caller/action excerpts',code:['// caller','final Thread worker = new Thread(task, "worker");','worker.start();','worker.join();','System.out.println("after join");','System.out.println(worker.getState());','// Runnable action','System.out.println(Thread.currentThread().getName());'],steps:buildSteps(executionBase,[
 {line:1,text:'The Thread object is NEW and holds the action reference. The work request is still at the caller.'},
 {line:2,workRow:1,workX:14,workerState:'RUNNABLE',mainNote:'start has returned; main may continue',workerNote:'Separate execution is eligible',text:'start enables a separate worker execution. The marker crosses into its lane; the action object itself is not transferred or copied.'},
 {line:3,workX:36,mainState:'WAITING',mainNote:'join waits for worker termination',text:'In this chosen schedule the worker is unfinished, so the untimed join makes main WAITING. It does not execute the action on main.'},
 {line:7,workX:67,workerNote:'worker executes the action',output:'worker',text:'The worker runs the action and prints its own name. RUNNABLE includes executing or eligible to execute; lane distance is not elapsed time.'},
 {line:3,workX:94,workerState:'TERMINATED',mainState:'RUNNABLE',mainNote:'join has completed',workerNote:'run completed',text:'The worker terminates and join returns. Main can continue only after this completion boundary.'},
 {line:[4,5],output:'worker\nafter join\nTERMINATED',text:'Main prints after join, then the stable TERMINATED state. Another legal schedule could finish the worker before main first called join.'},
 ]),caption:'One chosen schedule of StartAndJoinOrderLesson, not a live Thread.getState recording. The same Runnable can remain referenced by both caller and Thread object.'};

const counterBase={count:0,aValue:null,bValue:null,readA:null,readB:null,aX:0,bX:0,aState:'RUNNABLE',bState:'RUNNABLE',owner:'none',output:'',event:'setup',actor:null};
const lost={...common,kind:'basic-counter',protocol:'plain',title:'Copied reads can overwrite one another',summary:'Read tokens carry copies. The shared field stays visible while each actor computes from its own earlier read.',outputLabel:'Chosen model result',codeLabel:'SharedStateLesson.chosenLostUpdate · chosen schedule',code:['int shared = 0;','final int firstRead = shared;','final int secondRead = shared;','shared = firstRead + 1;','shared = secondRead + 1;'],steps:buildSteps({...counterBase,protocol:'plain'},[
 {line:0,text:'A and B each intend one increment. No monitor excludes the other operation in this selected interleaving.'},
 {line:1,aX:14,aValue:0,readA:0,event:'read',actor:'A',text:'A copies shared zero into its read token. Reading does not remove or change the shared value.'},
 {line:2,bX:14,bValue:0,readB:0,event:'read',actor:'B',text:'B also copies zero before A stores a result. The two actors now carry independent copies of the same old value.'},
 {line:3,aX:50,aValue:1,event:'compute',actor:'A',text:'A computes zero plus one. The token moves to Compute, while shared remains zero.'},
 {line:3,aX:86,count:1,event:'write',actor:'A',text:'A stores one. B still carries the earlier zero; A’s write does not rewrite B’s copied read.'},
 {line:3,aX:100,event:'exit',actor:'A',text:'A’s increment is complete. B has not yet computed from its retained read.'},
 {line:4,bX:50,bValue:1,event:'compute',actor:'B',text:'B computes one from its old zero, even though the shared field now contains one.'},
 {line:4,bX:86,count:1,event:'write',actor:'B',text:'B writes its computed one over the existing one. Two requested increments leave only one update.'},
 {line:4,bX:100,event:'exit',actor:'B',output:'count = 1',text:'Both increments are complete. This is one legal lost-update schedule, not a promise that every unsynchronized run produces this value.'},
 ]),caption:'Operation model: SharedStateLesson executes this chosen lost-update schedule on one thread. A/B identify the intended interleaved operations, not sampled JVM threads.'};
const locked={...common,kind:'basic-counter',protocol:'synchronized',title:'One monitor protects the whole increment',summary:'The monitor gate excludes the second operation until the first has completed its read, computation and write.',outputLabel:'Chosen model result',codeLabel:'SharedStateLesson.Counter.increment · source and decomposition',code:['private synchronized void increment() { value++; }','// Model expands value++ into read / compute / write.','// Normal method exit releases this Counter monitor.'],steps:buildSteps({...counterBase,protocol:'synchronized'},[
 {line:0,text:'Both actors use the same Counter receiver and therefore the same intrinsic monitor.'},
 {line:0,owner:'A',aX:7,event:'acquire',actor:'A',text:'A acquires the monitor before entering increment. The operation has not read the field yet.'},
 {line:0,bState:'BLOCKED',event:'contend',actor:'B',text:'B attempts the same synchronized method and becomes BLOCKED at its monitor gate. It has no read token yet.'},
 {line:[0,1],aX:14,aValue:0,readA:0,event:'read',actor:'A',text:'While owning the monitor, A copies value zero. B cannot enter the protected body.'},
 {line:[0,1],aX:50,aValue:1,event:'compute',actor:'A',text:'A computes one under the same ownership. The protected interval covers the complete read–modify–write.'},
 {line:[0,1],aX:86,count:1,event:'write',actor:'A',text:'A stores one before releasing the monitor. B still has not read an earlier zero.'},
 {line:2,owner:'none',aX:100,event:'release',actor:'A',text:'A exits and releases the monitor. B is eligible to compete for it; release does not imply an instantaneous ownership transfer.'},
 {line:0,owner:'B',bState:'RUNNABLE',bX:7,event:'acquire',actor:'B',text:'In this chosen schedule B next acquires the same monitor and enters the body.'},
 {line:[0,1],bX:14,bValue:1,readB:1,event:'read',actor:'B',text:'B reads the updated one after A’s unlock and B’s subsequent lock. The copied token is now one, not the old zero.'},
 {line:[0,1],bX:50,bValue:2,event:'compute',actor:'B',text:'B computes two from its own read of one.'},
 {line:[0,1],bX:86,count:2,event:'write',actor:'B',text:'B stores two while it owns the monitor. Each complete protected operation contributes one increment.'},
 {line:2,owner:'none',bX:100,event:'release',actor:'B',output:'count = 2',text:'Both increments have completed. The full Java program repeats this operation 1000 times per worker and joins both before observing 2000.'},
 ]),caption:'One increment per actor under one Counter monitor. BLOCKED means contended intrinsic-monitor entry. Tokens show copied calculation values, not literal hardware transfers.'};

const coordinationBase={mainState:'RUNNABLE',workerState:'NEW',owner:'none',waitSet:[],slot:'empty',milestone:false,flag:false,cleaned:false,output:'',event:'setup',actor:null};
const lifecycle={...common,kind:'basic-state',scenario:'lifecycle',title:'Follow an interruptible worker through its states',summary:'One legal sleeping-when-interrupted path. A thread need not visit every displayed state, and a state label is not a CPU-use measurement.',codeLabel:'CooperativeStopLesson.java · owner/worker excerpts',code:['worker.start();','// worker checks status, then waits','Thread.sleep(1000);','// owner requests cancellation','worker.interrupt();','worker.join();','// worker catch and finally','Thread.currentThread().interrupt();','cleaned = true;','// run returns','System.out.println("terminated=" + !worker.isAlive());','System.out.println("cleanup=" + task.cleaned());'],steps:buildSteps({...coordinationBase,scenario:'lifecycle'},[
 {line:0,text:'The worker object begins NEW. Main is already an execution and is RUNNABLE.'},
 {line:0,workerState:'RUNNABLE',event:'start',actor:'Worker',text:'start makes the worker eligible to execute. The chosen schedule lets it enter its interruptible loop before main requests cancellation.'},
 {line:2,workerState:'TIMED_WAITING',event:'sleep',actor:'Worker',text:'Thread.sleep(1000) places this worker in TIMED_WAITING. The duration is a requested wait, not a precise scheduling deadline.'},
 {line:[2,4],workerState:'RUNNABLE',flag:false,event:'interrupt-report',actor:'Worker',text:'Main interrupts the sleeping worker. Sleep reports InterruptedException and clears the interrupt status; the worker can execute its catch path.'},
 {line:[5,7],mainState:'WAITING',flag:true,event:'restore',actor:'Worker',text:'Choose main already waiting in join while the worker restores its interrupted status. Main’s WAITING is a completion wait, not the worker’s timed sleep.'},
 {line:8,cleaned:true,event:'cleanup',actor:'Worker',text:'The worker executes finally and records cleanup. Restoration is cooperative signaling; it does not forcibly kill arbitrary code.'},
 {line:[9,10,11],mainState:'RUNNABLE',workerState:'TERMINATED',flag:null,event:'terminated',actor:'Worker',output:'terminated=true\ncleanup=true',text:'run completes and join returns. Main then observes termination and the published cleanup result; this example does not inspect interrupt status after exit.'},
 ]),caption:'Chosen semantic states of CooperativeStopLesson, not snapshots taken after guessed sleeps. Monitor contention is BLOCKED; untimed join/wait is WAITING; sleep is TIMED_WAITING.'};
const handoff={...common,kind:'basic-state',scenario:'handoff',title:'Wait set → monitor contention → ownership',summary:'Follow one Mailbox monitor. A notification changes eligibility; the waiting consumer must still reacquire before it can read the message.',codeLabel:'MonitorHandoffLesson.java · selected monitor operations',code:['// main: awaitConsumer','while (!consumerWaiting) { wait(); }','// consumer: take','consumerWaiting = true;','notifyAll();','while (value == null) { wait(); }','// main: put','value = message;','System.out.println("producer stores " + message);','notifyAll();','// synchronized put returns: release monitor','// consumer reacquires and rechecks the while predicate','System.out.println("consumer receives " + value);','return value;'],steps:buildSteps({...coordinationBase,scenario:'handoff'},[
 {line:null,text:'Main is the producer. The consumer Thread object is NEW and the mailbox has no value.'},
 {line:1,workerState:'RUNNABLE',owner:'Main',event:'acquire',actor:'Main',text:'After starting the consumer, choose main entering awaitConsumer first. Main currently owns the Mailbox monitor.'},
 {line:5,workerState:'BLOCKED',event:'contend',actor:'Consumer',text:'The consumer tries to enter synchronized take and becomes BLOCKED because main still owns that monitor.'},
 {line:1,mainState:'WAITING',owner:'none',waitSet:['Main'],event:'wait',actor:'Main',text:'The startup predicate is false, so main waits. Wait adds it to the mailbox wait set and releases that monitor.'},
 {line:3,workerState:'RUNNABLE',owner:'Consumer',milestone:true,event:'acquire',actor:'Consumer',text:'The consumer acquires the released monitor and records its startup milestone.'},
 {line:4,mainState:'BLOCKED',waitSet:[],event:'notify',actor:'Consumer',text:'The consumer notifies the startup waiter. Main leaves the wait set but must reacquire; the consumer still owns the monitor.'},
 {line:5,workerState:'WAITING',owner:'none',waitSet:['Consumer'],event:'wait',actor:'Consumer',output:'consumer waits',text:'The consumer checks the empty slot and waits. Its marker enters WAITING and it releases the Mailbox monitor.'},
 {line:1,mainState:'RUNNABLE',owner:'Main',event:'acquire',actor:'Main',text:'Main reacquires, rechecks the now-true startup milestone, and can finish awaitConsumer.'},
 {line:1,owner:'none',event:'release',actor:'Main',text:'Returning from synchronized awaitConsumer releases its monitor before the later put call acquires it again.'},
 {line:6,owner:'Main',event:'acquire',actor:'Main',text:'Main acquires the same Mailbox monitor to enter put. The slot is still empty and the consumer remains in the wait set.'},
 {line:[7,8],slot:'message',event:'put',actor:'Main',output:'consumer waits\nproducer stores message',text:'Main stores the message and prints while owning the monitor. The consumer is still in the wait set; a changed field alone has not reacquired its monitor.'},
 {line:9,workerState:'BLOCKED',waitSet:[],event:'notify',actor:'Main',text:'notifyAll removes the consumer from the wait set. Its marker moves to BLOCKED monitor contention, while main retains ownership.'},
 {line:10,owner:'none',event:'release',actor:'Main',text:'put returns and main releases the monitor. The consumer can now compete to reacquire it.'},
 {line:11,workerState:'RUNNABLE',owner:'Consumer',event:'acquire',actor:'Consumer',text:'The consumer reacquires and rechecks value. The predicate is now satisfied; notification alone never reserved the message.'},
 {line:12,event:'read',actor:'Consumer',output:'consumer waits\nproducer stores message\nconsumer receives message',text:'Only while owning the monitor does the consumer read and print the message.'},
 {line:13,workerState:'TERMINATED',owner:'none',event:'terminate',actor:'Consumer',text:'take releases on return, the receiver records its result and run ends. This one-shot example retains the field value; the separate closeable mailbox defines draining.'},
 ]),caption:'One legal schedule of the real MonitorHandoffLesson. Markers show modeled Java states and monitor roles, not physical queues, fairness guarantees or a live JVM recording.'};

export const basicThreadMotionModels={
 execution:{defaultVariant:'start',selectorLabel:'Invocation',variants:[{id:'run',label:'Direct run on caller',trace:direct},{id:'start',label:'start and join',trace:started}]},
 counter:{defaultVariant:'plain',selectorLabel:'Increment protocol',variants:[{id:'plain',label:'Lost-update schedule',trace:lost},{id:'synchronized',label:'One shared monitor',trace:locked}]},
 lifecycle:{defaultVariant:'sleep',selectorLabel:'Path',variants:[{id:'sleep',label:'Sleep, interruption and join',trace:lifecycle}]},
 handoff:{defaultVariant:'mailbox',selectorLabel:'Path',variants:[{id:'mailbox',label:'Mailbox wait and reacquisition',trace:handoff}]},
};
