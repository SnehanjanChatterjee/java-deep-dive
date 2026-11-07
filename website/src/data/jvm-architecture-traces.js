const code = [
 '// main: selected statements',
 'final int number = 7;',
 'final Box original = new Box(number);',
 'final Box alias = original;',
 'change(original, number);',
 'System.out.println(number);',
 'System.out.println(alias.value());',
 'final Box created = createBox(4);',
 'System.out.println(created.value());',
 'System.out.println(original == alias);',
 '// change: selected statements',
 'copiedNumber++;',
 'argument.value = copiedNumber;',
 '// createBox: return expression',
 'return new Box(value);',
 '// Box constructor: field assignment',
 'this.value = value;',
];

const mainFrame = (locals, operands=[]) => ({method:'main',locals,operands});
const callFrame = (method,locals,operands=[]) => ({method,locals,operands});
const box = (id,value) => ({id,value});
const mainLocals = [['number','7'],['original','reference → A'],['alias','reference → A']];
const base = {
 classPhase:'ready',classEvent:'Program class and Box are ready for use',classes:['Program class','Box'],
 focus:[],frames:[],objects:[],pc:{method:'—',position:'No selected application instruction'},
 output:'',heapNote:'Only the selected Box objects are shown.',
};

const programSteps = [
 {...base,line:null,classPhase:'load',classEvent:'Load the program class definition',classes:['Program class'],focus:['loader','metadata'],text:'The launcher requests the program class. Loading supplies its runtime type definition and class-related structures. No main statement has run yet.'},
 {...base,line:null,classPhase:'link',classEvent:'Verify and prepare; resolve references as required',classes:['Program class'],focus:['loader','metadata'],text:'Linking checks the class-file constraints and prepares class state. Symbolic class/member references are resolved under the JVM rules; resolution can be lazy.'},
 {...base,line:null,classPhase:'initialize',classEvent:'Complete required initialization before main',classes:['Program class'],focus:['loader','metadata'],text:'Required class initialization precedes main. This program declares no custom static initializer; creating a Box instance is a later, separate action.'},
 {...base,line:1,classes:['Program class'],focus:['thread','engine'],frames:[mainFrame([['number','7']])],pc:{method:'main',position:'Establish the local number'},text:'main now has an invocation frame. The model records the source-local value number = 7; the per-thread PC identifies the current instruction position.'},
 {...base,line:2,classPhase:'ensure',classEvent:'Ensure Box is loaded, linked and initialized before use',focus:['loader','metadata'],frames:[mainFrame([['number','7']])],pc:{method:'main',position:'Prepare the new Box expression'},text:'The new expression needs the Box type ready for use. A JVM may load it earlier; the drawing chooses this legal point to expose the dependency.'},
 {...base,line:2,focus:['heap','thread','engine'],objects:[box('A',0)],frames:[callFrame('Box constructor',[['this','reference → A'],['value','7']]),mainFrame([['number','7']])],pc:{method:'Box constructor',position:'Before assigning the value field'},text:'The new expression allocates A with a default int field of zero, then invokes its constructor. The constructor frame has its own receiver and parameter; original has not received the finished expression yet.'},
 {...base,line:16,focus:['heap','thread','engine'],objects:[box('A',7)],frames:[callFrame('Box constructor',[['this','reference → A'],['value','7']]),mainFrame([['number','7']])],pc:{method:'Box constructor',position:'Assign the value field'},text:'The constructor writes 7 into A.value. Its frame is still current, and main has not yet stored the completed new expression in original.'},
 {...base,line:2,focus:['heap','thread'],objects:[box('A',7)],frames:[mainFrame([['number','7'],['original','reference → A']])],pc:{method:'main',position:'Store the completed new-expression result'},text:'The constructor completes. The new expression supplies a reference to main, which stores it in original. The constructor itself returns no reference value.'},
 {...base,line:3,focus:['thread'],objects:[box('A',7)],frames:[mainFrame(mainLocals)],pc:{method:'main',position:'Copy the reference into alias'},text:'alias receives another reference to A. The heap still has one selected Box, not a duplicate created by this assignment.'},
 {...base,line:4,focus:['thread','engine','metadata'],objects:[box('A',7)],frames:[callFrame('change',[['argument','reference → A'],['copiedNumber','7']]),mainFrame(mainLocals)],pc:{method:'change',position:'Enter the selected method'},text:'Calling change creates a callee frame containing copied argument values. main waits for that call to finish; the current instruction position belongs to change.'},
 {...base,line:11,focus:['thread','engine'],objects:[box('A',7)],frames:[callFrame('change',[['argument','reference → A'],['copiedNumber','8']]),mainFrame(mainLocals)],pc:{method:'change',position:'Increment the copied primitive'},text:'Incrementing copiedNumber changes the callee local to 8. main still has number = 7, and A.value has not changed yet.'},
 {...base,line:12,focus:['heap','thread','engine'],objects:[box('A',8)],frames:[callFrame('change',[['argument','reference → A'],['copiedNumber','8']]),mainFrame(mainLocals)],pc:{method:'change',position:'Write the referenced object field'},text:'The field assignment changes A.value to 8. A is shared through original, alias and the copied argument reference, so later reads through any of those aliases reach the changed object.'},
 {...base,line:4,focus:['thread','heap'],objects:[box('A',8)],frames:[mainFrame(mainLocals)],pc:{method:'main',position:'Resume after change'},text:'change returns and its frame stops being active. main resumes with its own number still 7. A remains reachable through original and alias; return is not a request to collect it.'},
 {...base,line:5,focus:['thread','engine'],objects:[box('A',8)],frames:[mainFrame(mainLocals)],pc:{method:'main',position:'Print the primitive local'},output:'7',text:'The first output comes from main’s primitive local, which was never rebound by the callee. The model summarizes the library printing call.'},
 {...base,line:6,focus:['heap','thread','engine'],objects:[box('A',8)],frames:[mainFrame(mainLocals)],pc:{method:'main',position:'Read A through alias and print'},output:'7\n8',text:'The getter reads A.value through alias and returns 8 for printing. Reading object state and reading main’s primitive local are different operations.'},
 {...base,line:[7,14],focus:['thread','heap','engine'],objects:[box('A',8),box('B',4)],frames:[callFrame('createBox',[['value','4']],['reference → B']),mainFrame(mainLocals)],pc:{method:'createBox',position:'Return the newly constructed Box'},output:'7\n8',text:'createBox creates and initializes a second object, B. Its return expression places a reference to B in the result path. The constructor work repeats the already explained allocation/initialization rule.'},
 {...base,line:7,focus:['thread','heap'],objects:[box('A',8),box('B',4)],frames:[mainFrame([...mainLocals,['created','reference → B']])],pc:{method:'main',position:'Store the returned reference'},output:'7\n8',text:'The factory frame completes. main stores the returned reference in created. B outlives the invocation that created it because main can still reach it.'},
 {...base,line:8,focus:['heap','thread','engine'],objects:[box('A',8),box('B',4)],frames:[mainFrame([...mainLocals,['created','reference → B']])],pc:{method:'main',position:'Read B and print'},output:'7\n8\n4',text:'created reaches B, whose value is 4. It is independent from A, so the earlier change to A does not change this result.'},
 {...base,line:9,focus:['thread','engine'],objects:[box('A',8),box('B',4)],frames:[mainFrame([...mainLocals,['created','reference → B']])],pc:{method:'main',position:'Compare the two reference values'},output:'7\n8\n4\ntrue',text:'original and alias still identify A, so the identity comparison prints true. Class metadata was reused while new expressions produced distinct object instances.'},
 {...base,line:null,focus:['thread'],objects:[box('A',8),box('B',4)],output:'7\n8\n4\ntrue',heapNote:'Last observed values; no collection is simulated.',text:'main completes. The drawing retains the last observed Box values for comparison; it does not invent a garbage-collection event or claim these source locals remain live.'},
];

const callSteps = [
 {...base,line:4,focus:['thread','engine','metadata'],objects:[box('A',7)],frames:[callFrame('change',[['argument','reference → A'],['copiedNumber','7']]),mainFrame(mainLocals)],pc:{method:'change',position:'Enter with copied arguments'},text:'Begin at the familiar change call. The reference and primitive argument have been copied into a fresh frame. Its non-null contract check succeeds for A.'},
 {...base,line:11,focus:['thread','engine'],objects:[box('A',7)],frames:[callFrame('change',[['argument','reference → A'],['copiedNumber','8']]),mainFrame(mainLocals)],pc:{method:'change',position:'Update the callee local'},text:'The primitive increment changes copiedNumber to 8. Each frame owns its own local values; main’s number stays 7.'},
 {...base,line:12,focus:['thread','engine'],objects:[box('A',7)],frames:[callFrame('change',[['argument','reference → A'],['copiedNumber','8']],['reference → A']),mainFrame(mainLocals)],pc:{method:'change',position:'Stage the field-write receiver'},text:'To write an instance field, execution needs a receiver reference. The operand stack temporarily carries the reference to A; the argument local still retains its value.'},
 {...base,line:12,focus:['thread','engine'],objects:[box('A',7)],frames:[callFrame('change',[['argument','reference → A'],['copiedNumber','8']],['reference → A','8']),mainFrame(mainLocals)],pc:{method:'change',position:'Stage the field-write value'},text:'The temporary operands now include the receiver and value 8. This is computation state inside the frame, not another Box in the heap.'},
 {...base,line:12,focus:['heap','thread','engine'],objects:[box('A',8)],frames:[callFrame('change',[['argument','reference → A'],['copiedNumber','8']]),mainFrame(mainLocals)],pc:{method:'change',position:'Complete the field assignment'},text:'The selected field-write operation consumes its temporary inputs and changes A.value to 8. Compiler-generated helper calls are collapsed in this source-level model.'},
 {...base,line:4,focus:['thread','heap'],objects:[box('A',8)],frames:[mainFrame(mainLocals)],pc:{method:'main',position:'Resume at the caller'},text:'The void call returns without a result value. The callee frame disappears from the active stack, while A remains reachable through main’s references.'},
 {...base,line:[5,6],focus:['heap','thread','engine'],objects:[box('A',8)],frames:[mainFrame(mainLocals)],pc:{method:'main',position:'Observe the unchanged local and changed field'},output:'7\n8',text:'The caller prints 7 from its primitive local and 8 from the aliased object. A reference identifies shared object state; a primitive parameter copy does not rebind the caller variable.'},
];

/** Selected semantic states, not observed class-loading, native, JIT or GC events. */
export const jvmArchitectureTraces = {
 program:{kind:'architecture',title:'Follow one program through the JVM map',summary:'Keep the responsibilities visible while following allocation, a call, mutation and return. Highlighted regions participate in the selected step.',code,steps:programSteps,outputLabel:'Program output',caption:'Source-statement model of JvmStackHeapLesson.java. Labels are conceptual locations, not addresses. Selected application frames are shown; platform calls, compiler helpers and VM-internal threads are collapsed.'},
 call:{kind:'architecture',title:'Connect a method frame to a heap field',summary:'Focus on one call. A frame contains local values and temporary operands; a reference reaches the same heap object from both caller and callee.',code,steps:callSteps,outputLabel:'Output from this excerpt',caption:'Selected conceptual frame/operand states for change; this is not a complete bytecode trace. The full program and its complete output appear in the lesson.'},
};
