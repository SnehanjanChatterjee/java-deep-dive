export function makeLoopTrace(zero = false) {
 const limit = zero ? 0 : 3;
 const values = Array.from({length:limit},(_,i)=>i+1);
 const code = [`int limit = ${limit};`, 'int total = 0;', 'for (int n = 1; n <= limit; n++) {', '    total += n;', '}', 'System.out.println(total);'];
 const steps = [{line:0, values, limit, sum:null, index:null, text:`Set the upper limit to ${limit}. The number line below illustrates candidate values, not array storage.`}, {line:1, values, limit, sum:0, index:null, text:'Initialize the accumulator to zero before any iteration.'}];
 let sum = 0;
 for (let n = 1; n <= limit; n++) {
  steps.push({line:2, values, limit, sum, index:n-1, text:`${n === 1 ? 'Initialize n to one. ' : 'Increment n. '}${n} <= ${limit} is true, so enter the body.`});
  sum += n;
  steps.push({line:3, values, limit, sum, index:n-1, text:`Add the current n, ${n}, to total. The accumulator is now ${sum}.`});
 }
 steps.push({line:2, values, limit, sum, index:limit, text:`${limit+1} <= ${limit} is false. Leave the loop without executing the body for ${limit+1}.`});
 steps.push({line:5, values, limit, sum, index:null, output:String(sum), text:`Print ${sum}. The loop variable n is outside its scope here. ${zero ? 'The initial condition was false, so no body executed.' : 'Each integer from one through the limit contributed once.'}`});
 return {kind:'loop',title:'Follow a loop through its changing state',summary:'Track the counter, condition and accumulator alongside the statement being executed.',code,steps};
}

export const conceptTraces = {
 'hash-resize':{
  kind:'hash-resize',title:'Move existing nodes into a larger bucket table',summary:'Trace one collision chain in the handwritten map. The saved next link preserves the remaining traversal while each node is relinked.',
  code:['Node<K,V>[] newBuckets = newTable(32);','Node<K,V> current = oldHead;','while (current != null) {','    Node<K,V> next = current.next;','    int index = current.hash & 31;','    current.next = newBuckets[index];','    newBuckets[index] = current;','    current = next;','}'],
  steps:[
   {line:0,moved:0,saved:'not read',text:'Capacity doubles from 16 to 32. Start with the old chain 33 → 17 → 1. Each spread hash equals its own numeric key value; all three selected bucket 1 at capacity 16.'},
   {line:3,moved:0,saved:'17',text:'Save node 33’s old next, node 17, before changing any link. The unvisited row is a traversal aid, not a second live map.'},
   {line:6,moved:1,saved:'17',text:'31 & 33 is 1. Relink existing node 33 at new bucket 1. Continue with the saved next, not the link just overwritten.'},
   {line:3,moved:1,saved:'1',text:'At node 17, save its old successor, node 1.'},
   {line:6,moved:2,saved:'1',text:'31 & 17 is 17. Place node 17 in new bucket 17. It no longer shares a bucket with the other two keys.'},
   {line:3,moved:2,saved:'null',text:'At node 1, save null as the end of the old chain.'},
   {line:6,moved:3,saved:'null',text:'31 & 1 is 1. Head insertion makes the new bucket chain 1 → 33. Reusing a node does not preserve this teaching implementation’s old chain order.'},
   {line:2,moved:3,saved:'null',output:'bucket 1: 1 → 33\nbucket 17: 17',text:'The traversal ends. All three mappings remain, with no new Node objects and no repeated user hashCode calls. The full resize method publishes the new table after all buckets are transferred.'}
  ]
 },
 references: {
  kind:'references',title:'Follow a copied reference through a method call',summary:'The caller and parameter can refer to the same object. Reassigning the parameter changes only that local variable.',
  code:['Person original = new Person("Ava");','rename(original);','System.out.println(original.name);','','void rename(Person person) {','    person.name = "Mira";','    person = new Person("Leo");','}'],
  steps:[
   {line:0,caller:'A',parameter:null,a:'Ava',b:null,text:'The caller creates Object A and stores its reference in original.'},
   {line:1,caller:'A',parameter:'A',a:'Ava',b:null,text:'Calling rename copies the reference value into a new parameter variable. Both variables now identify Object A.'},
   {line:5,caller:'A',parameter:'A',a:'Mira',b:null,text:'The assignment changes the name field of Object A. Both references still identify that same changed object.'},
   {line:6,caller:'A',parameter:'B',a:'Mira',b:'Leo',text:'Create Object B and assign its reference to the parameter. The caller variable still identifies Object A.'},
   {line:7,caller:'A',parameter:null,a:'Mira',b:'Leo',returned:true,text:'The method returns and its local parameter ceases to exist. Object B has no remaining reference in this example.'},
   {line:2,caller:'A',parameter:null,a:'Mira',b:'Leo',returned:true,output:'Mira',text:'The caller reads Object A and prints Mira. Object mutation and variable reassignment are different operations.'}
  ]
 },
 exceptions:{
  kind:'exceptions',title:'Watch an exception unwind a call stack',summary:'Follow the normal call path first, then the matching handler and the statement skipped by abrupt completion.',
  code:['try {','    int age = readAge("oops");','    System.out.println("accepted");','} catch (NumberFormatException failure) {','    System.out.println("invalid age");','}','','int readAge(String text) {','    return Integer.parseInt(text);','}'],
  steps:[
   {line:0,depth:1,phase:'normal',text:'The caller enters the try block. No exception has occurred.'},
   {line:1,depth:2,phase:'normal',text:'readAge receives the text. Its invocation has a separate frame above the caller.'},
   {line:8,depth:3,phase:'normal',text:'Integer.parseInt attempts to parse the input. It cannot represent oops as an integer.'},
   {line:8,depth:3,phase:'throw',text:'Parsing throws NumberFormatException. No int result is returned from this call.'},
   {line:8,depth:2,phase:'throw',text:'readAge has no matching handler. Its frame completes abruptly and the failure travels to its caller.'},
   {line:3,depth:1,phase:'catch',text:'The caller has a matching catch. The accepted print statement is skipped.'},
   {line:4,depth:1,phase:'handled',output:'invalid age',text:'The handler prints invalid age. Execution can continue after this try/catch statement.'}
  ]
 },
 thread:{
  kind:'thread',title:'Separate the caller from a started worker',summary:'This is one legal schedule. start creates a separate execution; join waits for its termination.',
  code:['Thread worker = new Thread(() -> {','    System.out.println("worker");','});','worker.start();','System.out.println("submitted");','worker.join();','System.out.println("finished");'],
  steps:[
   {line:0,main:0,worker:0,workerState:'NEW',mainState:'Constructing',text:'Construct the Thread object. Its work has not run yet.'},
   {line:3,main:20,worker:0,workerState:'RUNNABLE',mainState:'Running',text:'start makes a new execution eligible. It does not specify which thread gets the next CPU turn.'},
   {line:4,main:40,worker:0,workerState:'RUNNABLE',mainState:'Running',output:'submitted',text:'In this chosen schedule the caller prints submitted first. The worker could legally have printed earlier.'},
   {line:5,main:55,worker:10,workerState:'RUNNABLE',mainState:'Joining',output:'submitted',text:'The caller cannot pass a successful join until the worker has terminated.'},
   {line:1,main:55,worker:60,workerState:'Running',mainState:'Joining',output:'submitted\nworker',text:'The worker executes its body on its own call stack and prints worker.'},
   {line:2,main:55,worker:100,workerState:'TERMINATED',mainState:'Join completes',output:'submitted\nworker',text:'The worker returns. join can now establish termination and publish the completed worker actions to the caller.'},
   {line:6,main:100,worker:100,workerState:'TERMINATED',mainState:'Finished',output:'submitted\nworker\nfinished',text:'The caller prints finished after the successful join. That final ordering is guaranteed; the earlier print order was only one legal choice.'}
  ]
 },
 monitor:{
  kind:'monitor',title:'Follow waiting, notification and reacquisition',summary:'A notification makes a waiter eligible to compete. The guarded state decides whether it can proceed.',
  code:['synchronized (mailbox) {','    while (mailbox.empty()) {','        mailbox.wait();','    }','    message = mailbox.remove();','}','','synchronized (mailbox) {','    mailbox.put("A");','    mailbox.notifyAll();','}'],
  steps:[
   {line:0,owner:'Consumer',slot:'empty',waiter:'none',consumer:'Inside monitor',producer:'Outside',text:'The consumer acquires the mailbox monitor before checking its state.'},
   {line:1,owner:'Consumer',slot:'empty',waiter:'none',consumer:'Checks predicate',producer:'Outside',text:'The slot is empty, so the condition requires waiting.'},
   {line:2,owner:'none',slot:'empty',waiter:'Consumer',consumer:'Waiting',producer:'Outside',text:'wait releases this monitor and puts the consumer in its wait set. It does not retain monitor ownership.'},
   {line:7,owner:'Producer',slot:'empty',waiter:'Consumer',consumer:'Waiting',producer:'Inside monitor',text:'The producer can now acquire the released monitor.'},
   {line:8,owner:'Producer',slot:'A',waiter:'Consumer',consumer:'Waiting',producer:'Stores A',text:'The producer changes the protected state by installing a message.'},
   {line:9,owner:'Producer',slot:'A',waiter:'none',consumer:'Competing to reacquire',producer:'Notifies',text:'notifyAll wakes the waiter, but the producer still owns the monitor. Ownership is not handed over.'},
   {line:10,owner:'none',slot:'A',waiter:'none',consumer:'Competing to reacquire',producer:'Leaves monitor',text:'The producer exits its synchronized block and releases the monitor.'},
   {line:1,owner:'Consumer',slot:'A',waiter:'none',consumer:'Rechecks predicate',producer:'Outside',text:'After reacquiring, the consumer checks the while condition again. In this chosen schedule the message is still present.'},
   {line:4,owner:'Consumer',slot:'empty',waiter:'none',consumer:'Removes A',producer:'Outside',output:'A',text:'The consumer removes A while owning the monitor. Another schedule could consume the change first, which is why a loop is required.'}
  ]
 },
 stream:{
  kind:'stream',title:'Pull values through a lazy stream pipeline',summary:'For this stateless pipeline, findFirst needs only the first element that passes the filter.',
  code:['OptionalInt result = IntStream.of(1, 2, 3, 4)','    .filter(value -> value % 2 == 0)','    .map(value -> value * value)','    .findFirst();'],
  steps:[
   {line:0,stage:0,value:null,seen:[],result:null,text:'Describe the source and intermediate operations. They do not by themselves consume all four elements.'},
   {line:3,stage:0,value:1,seen:[1],result:null,text:'The terminal operation requests enough work to find a first result. Start with element 1.'},
   {line:1,stage:1,value:1,seen:[1],result:null,text:'The filter rejects 1. It never reaches the mapping operation.'},
   {line:0,stage:0,value:2,seen:[1,2],result:null,text:'Pull the next source element, 2.'},
   {line:1,stage:1,value:2,seen:[1,2],result:null,text:'2 is even, so it passes the filter.'},
   {line:2,stage:2,value:4,seen:[1,2],result:null,text:'Map the accepted 2 to its square, 4.'},
   {line:3,stage:3,value:4,seen:[1,2],result:4,output:'OptionalInt[4]',text:'findFirst has its result and short-circuits. Elements 3 and 4 need not be visited in this sequential pipeline.'}
  ]
 },
 buffer:{
  kind:'buffer',title:'Preserve a partial UTF-8 frame across reads',summary:'The length prefix declares three payload bytes. One arriving payload byte is not yet a complete character.',
  code:['buffer.flip();','buffer.mark();','int length = buffer.getInt();','if (buffer.remaining() < length) {','    buffer.reset();','    buffer.compact();','    // append more bytes, then flip and retry','} else {','    buffer.get(payload);','}'],
  steps:[
   {line:0,position:0,limit:5,bytes:['00','00','00','03','E2','','',''],mode:'read',text:'After receiving five bytes, flip sets limit to 5 and position to 0. Capacity stays 8.'},
   {line:1,position:0,limit:5,mark:0,bytes:['00','00','00','03','E2','','',''],mode:'read',text:'Mark the beginning of the frame so its header can be retried if the payload is incomplete.'},
   {line:2,position:4,limit:5,mark:0,bytes:['00','00','00','03','E2','','',''],mode:'read',text:'Read the big-endian length prefix: 3. Only one readable payload byte remains.'},
   {line:3,position:4,limit:5,mark:0,bytes:['00','00','00','03','E2','','',''],mode:'read',text:'One remaining byte is less than the three required bytes. Do not decode or emit a partial message.'},
   {line:4,position:0,limit:5,mark:0,bytes:['00','00','00','03','E2','','',''],mode:'read',text:'Reset to the marked frame start. This preserves the header as well as the partial payload for the retry.'},
   {line:5,position:5,limit:8,bytes:['00','00','00','03','E2','','',''],mode:'write',text:'Compact preserves the five unread bytes and leaves position at 5, ready to append. The mark is discarded.'},
   {line:6,position:7,limit:8,bytes:['00','00','00','03','E2','82','AC',''],mode:'write',text:'Append bytes 82 and AC. Together E2 82 AC encode the euro sign in UTF-8.'},
   {line:0,position:0,limit:7,bytes:['00','00','00','03','E2','82','AC',''],mode:'read',text:'Flip again. Seven bytes are now readable from position zero.'},
   {line:2,position:4,limit:7,bytes:['00','00','00','03','E2','82','AC',''],mode:'read',text:'Read the same length header. Three complete payload bytes now remain.'},
   {line:8,position:7,limit:7,bytes:['00','00','00','03','E2','82','AC',''],mode:'read',output:'€',text:'Consume the three bytes and decode one complete message. Position now equals limit.'}
  ]
 },
 memory:{
  kind:'memory',title:'Trace reachability from roots, not from reference counts',summary:'A conceptual object graph distinguishes a removed reference, a still-reachable alias and an unreachable cycle.',
  code:['List<Node> values = new ArrayList<>();','values.add(new Node("A")); values.add(new Node("B"));','Node alias = values.get(0);','values.remove(0);','alias = null;','// A cycle without a root path is also unreachable.'],
  steps:[
   {line:1,listA:true,listB:true,alias:false,marked:[],text:'The live list reference reaches A and B. These are identities in a conceptual graph, not memory addresses.'},
   {line:2,listA:true,listB:true,alias:true,marked:[],text:'An additional live local reference aliases A.'},
   {line:3,listA:false,listB:true,alias:true,marked:[],text:'Removing A from the list removes one edge. The alias still makes A reachable.'},
   {line:4,listA:false,listB:true,alias:false,marked:[],text:'Clear the last root path to A. A is now unreachable in this model; collection need not occur at this exact moment.'},
   {line:5,listA:false,listB:true,alias:false,marked:['B'],text:'Tracing from roots reaches B. C and D refer to each other but have no root path, so their cycle is unreachable too.'}
  ]
 },
 bytecode:{
  kind:'bytecode',title:'Execute a small method with locals and an operand stack',summary:'A frame holds local variables and a last-in-first-out operand stack. Follow twice(3) one instruction at a time.',
  code:['// static int twice(int value) { return value * 2; }','0: iload_0','1: iconst_2','2: imul','3: ireturn'],
  steps:[
   {line:0,operands:[],local:3,text:'Enter twice with value 3 in local slot 0. Its operand stack starts empty.'},
   {line:1,operands:[3],local:3,text:'iload_0 copies the int in local slot 0 onto the operand stack.'},
   {line:2,operands:[3,2],local:3,text:'iconst_2 pushes the constant 2. The top operand is now 2.'},
   {line:3,operands:[6],local:3,text:'imul consumes the top two int operands and pushes their product, 6. The local variable remains 3.'},
   {line:4,operands:[],local:3,returned:6,output:'6',text:'ireturn removes the result and returns it to the caller. This method frame completes.'}
  ]
 }
};
