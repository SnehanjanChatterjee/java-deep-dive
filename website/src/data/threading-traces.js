function makeSteps(initial, events) {
 let state = {...initial};
 return events.map(event => {
  state = {...state, ...event};
  return {...state};
 });
}

const executionCode = [
 'final Runnable task = () -> {',
 '    System.out.println(Thread.currentThread().getName());',
 '};',
 'final Thread worker = new Thread(task, "worker");',
];

const executionInitial = {
 mainProgress:0, mainFrame:'main', mainStatus:'Main executes the caller statements', mainWaiting:false,
 workerExists:false, workerProgress:null, workerStatus:'No worker execution exists', threadState:null,
};

const directExecution = {
 kind:'thread-execution',
 title:'Who executes the task when run is called?',
 summary:'Follow the main execution. A Thread object can exist without having started its own execution.',
 codeLabel:'Main method and task body · direct call',
 caption:'Illustrative execution. The object named worker and the thread executing a method are different things.',
 code:[...executionCode, 'worker.run();', 'System.out.println("after run");', 'System.out.println(worker.getState());'],
 steps:makeSteps(executionInitial, [
  {line:0,text:'Create the task description. Its body has not run. Main is the only execution in this example.'},
  {line:3,text:'Construct worker and attach task. Its lifecycle state is NEW. Constructing it does not create a running worker execution.',mainProgress:20,workerExists:true,threadState:'NEW'},
  {line:4,text:'Main calls worker.run like an ordinary method. Main enters the attached task body; worker has not been started.',mainProgress:40,mainFrame:'main → worker.run → task.run',mainStatus:'Main executes the task body'},
  {line:1,text:'currentThread identifies the execution making this call: main. Print main. There is still no separate W execution.',mainProgress:60,output:'main'},
  {line:4,text:'The task and run calls return to main. Returning from this method call does not terminate worker: that Thread object is still NEW.',mainProgress:75,mainFrame:'main',mainStatus:'The direct run call returned'},
  {line:5,text:'Main prints after run. The ordinary call completed before main reached this statement.',mainProgress:90,output:'main\nafter run'},
  {line:6,text:'Main prints NEW. No start call occurred, so the Thread object never left NEW and no worker progress is shown.',mainProgress:100,mainStatus:'Main reaches the end',output:'main\nafter run\nNEW'},
 ]),
};

const startedExecution = {
 kind:'thread-execution',
 title:'What changes when start and join are used?',
 summary:'This chosen schedule reaches join before the worker finishes. The worker can also finish earlier in another legal schedule.',
 codeLabel:'Main method and task body · start and join',
 caption:'One legal schedule, not a sampled JVM timeline. Join can return immediately when the worker has already terminated.',
 code:[...executionCode, 'worker.start();', 'worker.join();', 'System.out.println("after join");', 'System.out.println(worker.getState());'],
 steps:makeSteps(executionInitial, [
  {line:0,text:'Create the same task description. Its body has not run.'},
  {line:3,text:'Construct worker. It is NEW and has no separate execution yet.',mainProgress:20,workerExists:true,threadState:'NEW'},
  {line:4,text:'Start schedules a separate execution for worker. In this chosen schedule it is ready but has not printed yet. Main can continue.',mainProgress:35,workerProgress:0,workerStatus:'Ready to execute task',threadState:'RUNNABLE'},
  {line:5,text:'Main enters join while worker is unfinished. Main waits here; join does not execute the task on main.',mainProgress:45,mainFrame:'main → worker.join',mainStatus:'Waiting for worker termination',mainWaiting:true},
  {line:0,text:'The worker execution enters the task body. Main remains at join. Each execution has its own call stack.',workerProgress:25,workerStatus:'Worker executes task.run'},
  {line:1,text:'The task runs on the named worker, so currentThread.getName prints worker. Main still cannot print after join.',workerProgress:65,output:'worker'},
  {line:2,text:'The task returns and the worker execution terminates. Its Thread object becomes TERMINATED; join can now resume.',workerProgress:100,workerStatus:'Worker execution ended',threadState:'TERMINATED',mainWaiting:false,mainStatus:'Worker ended; join can resume'},
  {line:5,text:'Join returns to main. Worker termination is established before statements following this successful join.',mainProgress:70,mainFrame:'main',mainStatus:'Join returned'},
  {line:6,text:'Main prints after join. This output must follow the worker print, because that print preceded worker termination.',mainProgress:90,output:'worker\nafter join'},
  {line:7,text:'Main observes TERMINATED after join. The output order follows the program and completion rules, without a sleep.',mainProgress:100,mainStatus:'Main reaches the end',output:'worker\nafter join\nTERMINATED'},
 ]),
};

const sharing = {
 kind:'thread-sharing',
 title:'Two sets of locals, one shared Counter',
 summary:'Each addTwice invocation owns its parameter and loop index. Both parameter values identify the same Counter object.',
 codeLabel:'Counter method, helper and caller excerpts',
 caption:'One legal synchronized schedule. C1 is a model identity label, not a Java variable or a memory address.',
 code:[
  'public synchronized void increment() {',
  '    value++;',
  '}',
  'public static void addTwice(final Counter counter) {',
  '    for (int index = 0; index < 2; index++) {',
  '        counter.increment();',
  '    }',
  '}',
  'final Counter shared = new Counter();',
  'final Thread a = new Thread(() -> addTwice(shared), "A");',
  'final Thread b = new Thread(() -> addTwice(shared), "B");',
  'a.start(); b.start();',
  'a.join(); b.join();',
  'System.out.println("shared value=" + shared.value());',
 ],
 steps:makeSteps({
  value:0,owner:'none',waiter:null,aEntered:false,bEntered:false,aFinished:false,bFinished:false,
  indexA:null,indexB:null,phaseA:'Not entered',phaseB:'Not entered',activeActor:null,
 },[
  {line:8,text:'Main creates one Counter, labeled C1 here. shared holds a reference to that object. No addTwice invocation exists yet.'},
  {line:9,text:'Create Thread a named A. Its lambda will pass the reference value held in shared; creating it does not call addTwice.'},
  {line:10,text:'Create Thread b named B with the same shared reference. This still creates no second Counter.'},
  {line:11,text:'Start both executions. The model now chooses a legal order of their individual actions.'},
  {line:3,text:'A enters addTwice. Its own counter parameter receives a copy of the reference to C1.',aEntered:true,phaseA:'Entered addTwice',activeActor:'A'},
  {line:4,text:'A initializes its invocation-local index to zero. This local belongs to A’s loop, not to the Counter.',indexA:0,phaseA:'Loop index is 0'},
  {line:3,text:'B enters a separate invocation of addTwice. B gets another parameter variable whose reference value also identifies C1.',bEntered:true,phaseB:'Entered addTwice',activeActor:'B'},
  {line:4,text:'B initializes its own index to zero. The source uses the same variable name in two different invocations.',indexB:0,phaseB:'Loop index is 0'},
  {line:5,text:'A calls the synchronized increment method and acquires C1’s monitor. Only A can execute its protected update now.',owner:'A',phaseA:'Owns C1 monitor',activeActor:'A'},
  {line:5,text:'B also calls increment but C1’s monitor is owned by A. B waits without changing its index or the shared value.',waiter:'B',phaseB:'Waiting to enter increment',activeActor:'B'},
  {line:1,text:'Under its monitor ownership, A completes the whole read/add/write: value changes from zero to one.',value:1,activeActor:'A'},
  {line:2,text:'A exits increment and releases C1’s monitor. B can now compete to enter; the release does not itself change B’s local index.',owner:'none',phaseA:'Increment returned'},
  {line:4,text:'The chosen schedule lets A advance its loop index to one. B’s independent index remains zero.',indexA:1,phaseA:'Loop index is 1'},
  {line:5,text:'B acquires C1’s monitor and resumes its first increment. Its earlier wait did not reserve or copy the Counter field.',owner:'B',waiter:null,phaseB:'Owns C1 monitor',activeActor:'B'},
  {line:1,text:'B reads the protected updated value and completes its increment: one becomes two.',value:2},
  {line:2,text:'B releases the monitor after returning from increment.',owner:'none',phaseB:'Increment returned'},
  {line:4,text:'B advances its own index to one. Both workers still refer to C1.',indexB:1,phaseB:'Loop index is 1'},
  {line:5,text:'A enters its second synchronized increment.',owner:'A',phaseA:'Owns C1 monitor',activeActor:'A'},
  {line:1,text:'A changes the one shared field from two to three.',value:3},
  {line:2,text:'A releases the monitor after its second update.',owner:'none',phaseA:'Increment returned'},
  {line:4,text:'A increments its index to two. The condition index < 2 is false, so A’s loop ends.',indexA:2,phaseA:'Loop condition is false'},
  {line:7,text:'A returns from addTwice. Its counter parameter and loop index are no longer live locals of that invocation.',aFinished:true,indexA:null,phaseA:'Invocation returned'},
  {line:5,text:'B enters its second synchronized increment. A finishing did not remove the object still referenced by main and B.',owner:'B',phaseB:'Owns C1 monitor',activeActor:'B'},
  {line:1,text:'B changes value from three to four.',value:4},
  {line:2,text:'B releases C1’s monitor.',owner:'none',phaseB:'Increment returned'},
  {line:4,text:'B advances its index to two and its loop condition is false.',indexB:2,phaseB:'Loop condition is false'},
  {line:7,text:'B returns. Both invocations’ locals are gone; main still holds shared → C1.',bFinished:true,indexB:null,phaseB:'Invocation returned',activeActor:null},
  {line:12,text:'Main successfully joins both executions. The synchronized getter can now observe the completed final total.'},
  {line:13,text:'Main prints four. There were two increments per invocation and every protected update affected the same Counter.',output:'shared value=4'},
 ]),
};

const lockInitial = {
 leftOwner:'none',rightOwner:'none',heldA:[],heldB:[],requestA:null,requestB:null,
 phaseA:'Ready',phaseB:'Ready',activeActor:null,cycle:false,
};

const oppositeLockOrder = {
 kind:'thread-lock-order',ordered:false,
 title:'Build the cycle caused by opposite lock order',
 summary:'A transfers left to right; B transfers right to left. Their outer locks follow opposite orders in this unsafe sketch.',
 codeLabel:'Two worker excerpts · unsafe opposite order',
 caption:'A conceptual schedule stops at the cycle. The complete runnable transfer example uses the repaired policy.',
 code:[
  '// Worker A',
  'synchronized (left) {',
  '    synchronized (right) {',
  '        // Update both balances.',
  '    }',
  '}',
  '// Worker B',
  'synchronized (right) {',
  '    synchronized (left) {',
  '        // Update both balances.',
  '    }',
  '}',
 ],
 steps:makeSteps(lockInitial,[
  {line:null,text:'Both monitors are free. Follow A and B one acquisition at a time.'},
  {line:1,text:'A acquires left. It retains that monitor while its outer synchronized block is active.',leftOwner:'A',heldA:['left'],phaseA:'Holds left',activeActor:'A'},
  {line:7,text:'B acquires right before A acquires it. B also retains its outer monitor.',rightOwner:'B',heldB:['right'],phaseB:'Holds right',activeActor:'B'},
  {line:2,text:'A requests right, which B holds. A waits while retaining left. B has not yet requested its second lock.',requestA:'right',phaseA:'Blocked waiting for right',activeActor:'A'},
  {line:8,text:'B requests left, which A holds. A needs B and B needs A. Neither can reach normal release: the wait-for cycle is closed.',requestB:'left',phaseB:'Blocked waiting for left',activeActor:'B',cycle:true},
 ]),
};

const commonLockOrder = {
 kind:'thread-lock-order',ordered:true,
 title:'Use one common acquisition order',
 summary:'For this pair, left has the earlier stable order key. Both transfer directions therefore choose left first, then right.',
 codeLabel:'Per-call order selection and protected operation',
 caption:'One legal schedule under a common lock order. Waiting is possible, while this pair of acquisitions cannot form the opposite-order cycle.',
 code:[
  '// A: transfer(left, right, 1)',
  '// B: transfer(right, left, 1)',
  'final Account first = from.lockOrder < to.lockOrder ? from : to;',
  'final Account second = first == from ? to : from;',
  'synchronized (first) {',
  '    synchronized (second) {',
  '        // Validate and update both balances.',
  '    }',
  '}',
 ],
 steps:makeSteps(lockInitial,[
  {line:2,text:'A chooses the earlier stable key: first is left, regardless of the transfer direction.',phaseA:'Order chosen: left, right',activeActor:'A'},
  {line:3,text:'For B’s reverse transfer, the same key comparison still chooses left first and right second.',phaseB:'Order chosen: left, right',activeActor:'B'},
  {line:4,text:'A acquires the first monitor, left.',leftOwner:'A',heldA:['left'],phaseA:'Holds left',activeActor:'A'},
  {line:4,text:'B requests left before acquiring any other monitor. B waits with no lock held; right is still free for A.',requestB:'left',phaseB:'Blocked; holds no monitor',activeActor:'B'},
  {line:5,text:'A can acquire right. It does not need a monitor held by B, so there is no closed dependency cycle.',rightOwner:'A',heldA:['left','right'],phaseA:'Holds left and right',activeActor:'A'},
  {line:6,text:'A performs its protected operation while owning both accounts. B still waits without holding another lock.',phaseA:'Protected operation completes'},
  {line:7,text:'A leaves the inner protected region and releases right.',rightOwner:'none',heldA:['left'],phaseA:'Releases right'},
  {line:8,text:'A leaves the outer region and releases left. B can now complete its first acquisition.',leftOwner:'none',heldA:[],phaseA:'Operation finished',phaseB:'Eligible to acquire left'},
  {line:4,text:'B acquires left; its waiting request is satisfied.',leftOwner:'B',heldB:['left'],requestB:null,phaseB:'Holds left',activeActor:'B'},
  {line:5,text:'B acquires right using the same order A used.',rightOwner:'B',heldB:['left','right'],phaseB:'Holds left and right'},
  {line:6,text:'B performs the reverse transfer with both monitors held.',phaseB:'Protected operation completes'},
  {line:7,text:'B releases right when it leaves the inner region.',rightOwner:'none',heldB:['left'],phaseB:'Releases right'},
  {line:8,text:'B releases left. Both operations completed; both monitors are free.',leftOwner:'none',heldB:[],phaseB:'Operation finished',activeActor:null},
 ]),
};

export const threadingModels = {
 execution:{defaultVariant:'direct',selectorLabel:'Invocation',variants:[
  {id:'direct',label:'Call worker.run()',trace:directExecution},
  {id:'started',label:'Start worker, then join',trace:startedExecution},
 ]},
 sharing:{defaultVariant:'shared',variants:[{id:'shared',label:'One shared Counter',trace:sharing}]},
 'lock-order':{defaultVariant:'opposite',selectorLabel:'Lock order',variants:[
  {id:'opposite',label:'Opposite order · unsafe',trace:oppositeLockOrder},
  {id:'common',label:'Common order · safe',trace:commonLockOrder},
 ]},
};
