import SelectControl from './SelectControl.jsx';
import React, {useState} from 'react';
import ConceptPlayer from './ConceptPlayer.jsx';
import {jvmArchitectureTraces} from '../data/jvm-architecture-traces';
import '../styles/jvm-architecture.css';

const phases=[['load','Loading'],['link','Linking'],['initialize','Initialization']];

function Region({area,title,focus,children,className=''}) {
 const active=focus.includes(area);
 return <section className={`jvma-region ${active?'jvma-active':''} ${className}`} aria-label={title}>
  <div className="jvma-region-heading"><strong>{title}</strong>{active?<span className="jvma-focus-tag">Current step</span>:null}</div>
  {children}
 </section>;
}

function LocalValues({values}) {
 return <div className="jvma-values">{values.map(([name,value])=><span key={name}><strong>{name}</strong><code>{value}</code></span>)}</div>;
}

function FrameStack({frames}) {
 if(!frames.length)return <div className="jvma-empty-frame">No selected application frame</div>;
 const [current,...callers]=frames;
 return <div className="jvma-stack">
  <ol className="jvma-frame-chain" aria-label="Selected stack, current frame first">{frames.map((frame,index)=><li key={`${frame.method}-${index}`} className={index===0?'jvma-current-frame':''}><strong>{frame.method}</strong><span>{index===0?'current':'caller'}</span></li>)}</ol>
  <div className="jvma-frame-detail"><strong>Current locals</strong><LocalValues values={current.locals}/><div className="jvma-operands"><span>Operands · bottom → top</span><code>{current.operands.length?current.operands.join(' | '):'empty'}</code></div></div>
  {callers.map((frame,index)=><div className="jvma-caller-detail" key={`${frame.method}-${index}`}><strong>{frame.method} retains</strong><LocalValues values={frame.locals}/>{frame.operands.length?<span className="jvma-caller-operands">Caller operands: {frame.operands.join(' | ')}</span>:null}</div>)}
 </div>;
}

function ArchitectureState({state}) {
 return <div className="jvma-map">
  <div className="jvma-input"><strong>Compiled input</strong><code>JvmStackHeapLesson.class</code><span>+</span><code>JvmStackHeapLesson$Box.class</code></div>
  <Region area="loader" title="Class lifecycle" focus={state.focus}>
   <ol className="jvma-phases">{phases.map(([phase,label])=><li key={phase} className={state.classPhase===phase?'jvma-current-phase':''} aria-current={state.classPhase===phase?'step':undefined}>{label}</li>)}</ol>
   <div className="jvma-class-event">{state.classEvent}</div>
  </Region>
  <div className="jvma-lifecycle-links" aria-label="Class lifecycle relationships"><span><b aria-hidden="true">↓</b> defines types and members</span><span><b aria-hidden="true">↓</b> enables application calls</span></div>
  <div className="jvma-runtime-layout">
   <div className="jvma-boundary">
    <strong className="jvma-boundary-title">Shared runtime state</strong>
    <Region area="metadata" title="Method area + constant pools" focus={state.focus}>
     <div className="jvma-loaded"><span>Definitions</span><strong>{state.classes.join(', ')}</strong></div>
     <span className="jvma-note">HotSpot class metadata: Metaspace</span>
    </Region>
    <Region area="heap" title="Heap · selected objects" focus={state.focus}>
     {state.objects.length?<div className="jvma-objects">{state.objects.map(object=><div className="jvma-object" key={object.id}><strong>Box {object.id}</strong><span>value = {object.value}</span></div>)}</div>:<span className="jvma-note">No selected Box yet</span>}
     <span className="jvma-note">{state.heapNote}</span>
    </Region>
   </div>
   <div className="jvma-reference-link"><span className="jvma-reference-arrow" aria-hidden="true">←</span><strong>references</strong><span>frame values → objects</span></div>
   <div className="jvma-boundary jvma-thread-boundary">
    <strong className="jvma-boundary-title">Per-thread state · main shown</strong>
    <Region area="thread" title="PC + JVM stack" focus={state.focus}>
     <div className="jvma-pc"><strong>PC: {state.pc.method}</strong><span>{state.pc.position}</span></div>
     <FrameStack frames={state.frames}/>
    </Region>
    <span className="jvma-native-stack">Optional native-stack support</span>
   </div>
  </div>
  <div className="jvma-mechanism-links" aria-label="Execution relationships"><span><b aria-hidden="true">↑</b> manages heap storage</span><span><b aria-hidden="true">↕</b> uses frames and shared state</span><span><b aria-hidden="true">↓</b> if native code is called</span></div>
  <div className="jvma-mechanisms">
   <section className="jvma-mechanism"><strong>Garbage collector</strong><span>Reclaim unreachable heap storage</span><span className="jvma-note">No collection event shown.</span></section>
   <Region area="engine" title="Execution engine" focus={state.focus}><span>Interpreter</span><span>HotSpot JIT → machine code</span><span className="jvma-note">Compilation is optional; no event inferred.</span></Region>
   <section className="jvma-mechanism"><strong>Native integration</strong><div className="jvma-native-route"><span>Java</span><b aria-hidden="true">↔</b><span>JNI</span><b aria-hidden="true">↔</b><span>library</span></div><span className="jvma-note">Optional path, not expanded.</span></section>
  </div>
  <p className="jvma-map-caption">Conceptual relationships, not memory addresses. Source variables and selected frames are shown; platform objects and internal VM threads are omitted.</p>
 </div>;
}

/** Highlights one source-level walk while keeping shared and per-thread roles visible. */
export default function JvmArchitecture() {
 const [mode,setMode]=useState('program');
 const trace=jvmArchitectureTraces[mode];
 const selector=ready=><label className="jvma-scope">Walkthrough<SelectControl aria-label="JVM architecture walkthrough" disabled={!ready} value={mode} onChange={event=>setMode(event.target.value)}><option value="program">Whole program</option><option value="call">Call and field write</option></SelectControl></label>;
 return <div className="jvm-architecture"><ConceptPlayer key={mode} trace={trace} Visual={ArchitectureState} codeLabel="Java source excerpts · complete program in lesson" headerExtra={selector} modelId="jvm-architecture"/></div>;
}
