import React, {useState} from 'react';
import ConceptPlayer from './ConceptPlayer.jsx';
import SelectControl from './SelectControl.jsx';
import {basicThreadMotionModels} from '../data/basic-thread-motion.js';
import '../styles/basic-thread-motion.css';

const states=[['NEW','before start'],['RUNNABLE','running / eligible'],['BLOCKED','monitor contention'],['WAITING','untimed join / wait'],['TIMED_WAITING','sleep / timed wait'],['TERMINATED','run has ended']];
const counterPositions={0:10,7:24,14:36,50:58,86:80,100:90};

function ExecutionMotion({state}) {
 return <div className="btm-execution">
  <div className="btm-action">Runnable action/reference stays separate from the moving work marker</div>
  <div className="btm-execution-layout">
   <div className="btm-execution-labels">{[['Caller','main',state.mainState],['Worker','worker',state.workerState]].map(([role,name,status])=><div key={role}><strong>{role}</strong><span>{name}</span><small>{status}</small></div>)}</div>
   <div className="btm-execution-canvas" aria-label="Work motion corridor"><div className="btm-execution-track"/><div className="btm-execution-track"/><span className="btm-work-marker" data-motion-token="work" style={{left:`clamp(34px, ${10+state.workX*.8}%, calc(100% - 34px))`,top:`${24+state.workRow*86}px`}}>work</span></div>
  </div>
  <div className="btm-execution-notes"><p><strong>Caller:</strong> {state.mainNote}</p><p><strong>Worker:</strong> {state.workerNote}</p></div>
 </div>;
}

function CounterLane({actor,value,read,position,status,protectedOperation}) {
 return <div className="btm-counter-lane">
  <div className="btm-lane-heading"><strong>Worker {actor}</strong><span>{status}</span></div>
  <div className="btm-counter-track">
   <div className="btm-operation-labels"><span>Read</span><span>Compute</span><span>Write</span></div>
   {protectedOperation?<span className="btm-monitor-gate" aria-label="Entry to the same Counter monitor"/>:null}
   <span className={`btm-value-token btm-actor-${actor.toLowerCase()}`} data-motion-token={`value-${actor.toLowerCase()}`} style={{left:`clamp(24px, ${counterPositions[position]}%, calc(100% - 24px))`}}>{actor}: {value??'—'}</span>
  </div>
  <span className="btm-read-label">Retained read: {read??'not read yet'}</span>
 </div>;
}

function CounterMotion({state}) {
 const protectedOperation=state.protocol==='synchronized';
 return <div className="btm-counter">
  <div className="btm-shared-field"><span>Shared count · remains visible during a read</span><strong>{state.count}</strong></div>
  <div className="btm-owner">{protectedOperation?`One Counter monitor · owner: ${state.owner==='none'?'free':state.owner}`:'No monitor protects the selected operations'}</div>
  <CounterLane actor="A" value={state.aValue} read={state.readA} position={state.aX} status={state.aState} protectedOperation={protectedOperation}/>
  <CounterLane actor="B" value={state.bValue} read={state.readB} position={state.bX} status={state.bState} protectedOperation={protectedOperation}/>
 </div>;
}

function actorPosition(status) {
 const index=states.findIndex(([name])=>name===status);
 return {top:`${15+index*58}px`};
}

function StateMotion({state}) {
 const handoff=state.scenario==='handoff';
 const workerName=handoff?'Consumer':'Worker';
 return <div className="btm-state-motion">
  <div className="btm-state-layout" aria-label="Modeled Java thread states">
   <div className="btm-state-labels">{states.map(([name,reason])=><div key={name}><strong>{name}</strong><span>{reason}</span></div>)}</div>
   <div className="btm-state-canvas" aria-label="Actor motion corridor">
    {states.map(([name])=><div className={`btm-state-track ${state.mainState===name||state.workerState===name?'is-active':''}`} key={name}/>)}
    <span className="btm-state-token btm-main-token" data-motion-token="main" style={actorPosition(state.mainState)} aria-label={`Main: ${state.mainState}`}>M</span>
    <span className="btm-state-token btm-worker-token" data-motion-token={handoff?'consumer':'worker'} style={actorPosition(state.workerState)} aria-label={`${workerName}: ${state.workerState}`}>{handoff?'C':'W'}</span>
   </div>
  </div>
  <div className="btm-actor-key"><span>M = main · {state.mainState}</span><span>{handoff?'C = consumer':'W = worker'} · {state.workerState}</span></div>
  {handoff?<div className="btm-state-facts"><div><span>Mailbox owner</span><strong>{state.owner==='none'?'free':state.owner}</strong></div><div><span>Wait set</span><strong>{state.waitSet.length?state.waitSet.join(', '):'empty'}</strong></div><div><span>Slot</span><strong>{state.slot}</strong></div></div>:<div className="btm-state-facts"><div><span>Worker interrupt status</span><strong>{state.flag===null?'not inspected after exit':String(state.flag)}</strong></div><div><span>Cleanup recorded</span><strong>{String(state.cleaned)}</strong></div></div>}
 </div>;
}

function MotionVisual({kind,state}) {
 if(kind==='basic-execution')return <ExecutionMotion state={state}/>;
 if(kind==='basic-counter')return <CounterMotion state={state}/>;
 return <StateMotion state={state}/>;
}

function MotionModel({model,config}) {
 const [variant,setVariant]=useState(config.defaultVariant);
 const selected=config.variants.find(option=>option.id===variant);
 const selector=ready=>config.variants.length>1?<label className="btm-selector">{config.selectorLabel}<SelectControl disabled={!ready} aria-label={config.selectorLabel} value={variant} onChange={event=>setVariant(event.target.value)}>{config.variants.map(option=><option value={option.id} key={option.id}>{option.label}</option>)}</SelectControl></label>:undefined;
 return <div className="basic-thread-motion" data-basic-motion={model} data-basic-variant={variant}><ConceptPlayer key={`${model}-${variant}`} trace={selected.trace} Visual={MotionVisual} codeLabel={selected.trace.codeLabel} headerExtra={selector} modelId={`basic-motion-${model}`}/></div>;
}

/** Supplies three spatial teaching designs to the shared manual-first player. */
export default function BasicThreadMotion({model}) {
 const config=basicThreadMotionModels[model];
 if(!config)throw new Error('Unknown basic thread motion model: '+model);
 return <MotionModel key={model} model={model} config={config}/>;
}
