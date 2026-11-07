import React, {useState} from 'react';
import ConceptPlayer from './ConceptPlayer.jsx';
import {threadingModels} from '../data/threading-traces.js';
import '../styles/threading-visuals.css';

function Value({label, children}) {
 return <div className="threading-value"><span>{label}</span><strong>{children ?? '—'}</strong></div>;
}

function ExecutionLane({name, marker, progress, status, frame, waiting}) {
 const position = progress === null ? null : Math.max(0, Math.min(100, progress));
 return <section className={`threading-lane${waiting?' is-waiting':''}`}>
  <h4>{name}</h4>
  <div className="threading-track" aria-hidden="true">
   {position === null
    ? <span className="threading-no-execution">No separate execution</span>
    : <span className="threading-token" style={{left:`${position}%`,transform:`translateX(-${position}%)`}}>{marker}</span>}
  </div>
  <p className="threading-lane-status">{status}</p>
  {frame ? <p className="threading-call-frame"><span>Current call path</span><code>{frame}</code></p> : null}
 </section>;
}

function ExecutionVisual({state}) {
 return <div className="threading-visual">
  <div className="threading-lanes">
   <ExecutionLane name="Main execution" marker="M" progress={state.mainProgress} status={state.mainStatus} frame={state.mainFrame} waiting={state.mainWaiting}/>
   <ExecutionLane name="Worker execution" marker="W" progress={state.workerProgress} status={state.workerStatus}/>
  </div>
  <div className="threading-object-state">
   <Value label="Thread object named worker">{state.workerExists?'Created':'Not created yet'}</Value>
   <Value label="worker’s lifecycle state">{state.threadState ?? 'No Thread object yet'}</Value>
  </div>
  <p className="threading-key">M and W label executions. A NEW Thread object has no separate W execution to advance.</p>
 </div>;
}

function Invocation({actor, entered, finished, index, phase, active}) {
 const scope = !entered ? 'Not entered' : finished ? 'Out of scope' : 'C1';
 return <section className={`threading-invocation${active?' is-active':''}`}>
  <h4>{actor}: addTwice invocation</h4>
  <Value label="Local parameter: counter">{scope==='C1'?'counter → C1':scope}</Value>
  <Value label="Local loop variable: index">{index ?? (finished?'Out of scope':'Not initialized')}</Value>
  <p className="threading-actor-status">{phase}</p>
 </section>;
}

function SharingVisual({state}) {
 return <div className="threading-visual">
  <p className="threading-main-reference"><strong>Main local:</strong> <code>shared → C1</code></p>
  <div className="threading-invocations">
   <Invocation actor="A" entered={state.aEntered} finished={state.aFinished} index={state.indexA} phase={state.phaseA} active={state.activeActor==='A'}/>
   <Invocation actor="B" entered={state.bEntered} finished={state.bFinished} index={state.indexB} phase={state.phaseB} active={state.activeActor==='B'}/>
  </div>
  <section className="threading-counter">
   <h4>One Counter object · C1</h4>
   <div className="threading-counter-fields">
    <Value label="Shared field: value">{state.value}</Value>
    <Value label="C1 monitor owner">{state.owner==='none'?'Free':state.owner}</Value>
   </div>
   <p className={`threading-monitor-status${state.waiter?' is-waiting':''}`}>
    {state.waiter ? `${state.waiter} waits to enter increment; it has not acquired C1’s monitor.` : 'No worker is waiting to enter increment in this step.'}
   </p>
  </section>
  <p className="threading-key">The parameter variables are separate. Their copied reference values can name the same object. The value box shows model state, not an extra unprotected read by the program.</p>
 </div>;
}

function LockActor({actor, direction, held, requested, phase, active}) {
 return <section className={`threading-lock-actor${active?' is-active':''}${requested?' is-waiting':''}`}>
  <h4>Worker {actor}</h4>
  <p className="threading-direction">{direction}</p>
  <Value label="Monitors held">{held.length?held.join(' + '):'None'}</Value>
  <Value label="Monitor requested">{requested ?? 'None'}</Value>
  <p className="threading-actor-status">{phase}</p>
 </section>;
}

function LockVisual({state}) {
 const edges=[];
 if(state.requestA && state[`${state.requestA}Owner`]!=='none')edges.push(`A waits for ${state.requestA}, held by ${state[`${state.requestA}Owner`]}.`);
 if(state.requestB && state[`${state.requestB}Owner`]!=='none')edges.push(`B waits for ${state.requestB}, held by ${state[`${state.requestB}Owner`]}.`);
 return <div className="threading-visual">
  <div className="threading-lock-resources">
   <Value label="left monitor owner">{state.leftOwner==='none'?'Free':state.leftOwner}</Value>
   <Value label="right monitor owner">{state.rightOwner==='none'?'Free':state.rightOwner}</Value>
  </div>
  <div className="threading-lock-actors">
   <LockActor actor="A" direction="Transfer: left → right" held={state.heldA} requested={state.requestA} phase={state.phaseA} active={state.activeActor==='A'}/>
   <LockActor actor="B" direction="Transfer: right → left" held={state.heldB} requested={state.requestB} phase={state.phaseB} active={state.activeActor==='B'}/>
  </div>
  <section className={`threading-dependencies${state.cycle?' has-cycle':edges.length?' has-wait':''}`}>
   <h4>{state.cycle?'Deadlock: a closed wait-for cycle':edges.length?'Waiting, without a closed cycle':'No wait-for dependency'}</h4>
   {edges.length ? <ul>{edges.map(edge=><li key={edge}>{edge}</li>)}</ul> : <p>Neither worker is currently requesting a monitor held by the other.</p>}
   {state.cycle ? <p className="threading-cycle">A needs B <span aria-hidden="true">→</span> B needs A <span aria-hidden="true">→</span> A cannot advance</p>
    : edges.length && state.requestB ? <p>B holds no monitor. A can finish and release the monitor B needs.</p>
    : edges.length && state.requestA ? <p>The second request will determine whether a cycle forms.</p> : null}
  </section>
 </div>;
}

export function ThreadVisual({kind, state}) {
 if(kind==='thread-execution')return <ExecutionVisual state={state}/>;
 if(kind==='thread-sharing')return <SharingVisual state={state}/>;
 if(kind==='thread-lock-order')return <LockVisual state={state}/>;
 throw new Error('Unknown threading visual: '+kind);
}

function ThreadingModel({model, config}) {
 const [variant,setVariant]=useState(config.defaultVariant);
 const selected=config.variants.find(option=>option.id===variant);
 const trace=selected.trace;
 const headerExtra=ready=>config.variants.length>1 ? <label className="threading-selector">{config.selectorLabel}
  <select disabled={!ready} value={variant} onChange={event=>setVariant(event.target.value)} aria-label={config.selectorLabel}>
   {config.variants.map(option=><option key={option.id} value={option.id}>{option.label}</option>)}
  </select>
 </label> : undefined;
 return <div className="threading-visuals" data-threading-model={model} data-threading-variant={variant}>
  <ConceptPlayer key={model+'-'+variant} trace={trace} Visual={ThreadVisual} codeLabel={trace.codeLabel} headerExtra={headerExtra} modelId={trace.kind}/>
 </div>;
}

export default function ThreadingVisuals({model}) {
 const config=threadingModels[model];
 if(!config)throw new Error('Unknown threading model: '+model);
 return <ThreadingModel key={model} model={model} config={config}/>;
}
