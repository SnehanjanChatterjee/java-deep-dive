import React, {useState} from 'react';
import ConceptPlayer from './ConceptPlayer.jsx';
import SelectControl from './SelectControl.jsx';
import {advancedMotionBoards, advancedThreadModels} from '../data/advanced-thread-motion.js';
import '../styles/advanced-thread-motion.css';

function Fact({label, children}) {
 return <div className="advanced-motion-fact"><span>{label}</span><strong>{children ?? '—'}</strong></div>;
}

function TokenContent({token, state}) {
 if(token.type==='attempt')return <><strong>{token.id}</strong><span className="advanced-motion-arguments"><span>E:{state['expected'+token.id] ?? '—'}</span><span>U:{state['candidate'+token.id] ?? '—'}</span></span></>;
 if(token.type==='remaining')return <strong>{state.remaining}</strong>;
 if(token.type==='completion-value')return <strong>{state[token.id+'Done']?(token.id==='score'?'43':'score'):'pending'}</strong>;
 if(token.type==='completion-result')return <strong>{state.resultValue ?? 'pending'}</strong>;
 return <strong>{token.label}</strong>;
}

function MotionBoard({kind, state}) {
 const board=advancedMotionBoards[kind];
 return <div className="advanced-motion-viewport" tabIndex={0} role="region" aria-label="Spatial execution diagram; scroll horizontally if needed">
  <div className={`advanced-motion-board advanced-motion-board-${kind}`} style={{height:board.height}}>
   <svg className="advanced-motion-paths" viewBox={`0 0 100 ${board.height}`} preserveAspectRatio="none" aria-hidden="true">
    {board.paths.map(([x1,y1,x2,y2],index)=><line key={index} x1={x1} y1={y1} x2={x2} y2={y2} vectorEffect="non-scaling-stroke"/>)}
   </svg>
   {board.regions.map(region=><section key={region.id} className={`advanced-motion-region${kind==='executor'&&((region.id==='worker1'&&state.created<1)||(region.id==='worker2'&&state.created<2))?' is-uncreated':''}`} style={{left:`${region.x}%`,top:region.y,width:`${region.width}%`,height:region.height}} data-motion-region={region.id}>
    <h4>{region.label}</h4>
    {kind==='completion'&&region.id==='score'&&state.scoreDone&&<span className="advanced-motion-retained">43 · retained</span>}
    {kind==='completion'&&region.id==='label'&&state.labelDone&&<span className="advanced-motion-retained">score · retained</span>}
    {kind==='completion'&&region.id==='inputs'&&<span className="advanced-motion-retained">{state.scoreDone&&state.labelDone?'Both ready':'Waiting for inputs'}</span>}
   </section>)}
   {board.tokens.map(token=>{
    const location=state.locations[token.id];
    const [x,y]=board.points[location];
    const visible=state.visibility[token.id]!==false;
    const tone=state.tones[token.id]||'normal';
    return <div key={token.id} className={`advanced-motion-token ${token.type||'payload'} tone-${tone}`} data-motion-token={token.id} data-motion-location={location} data-motion-visible={visible?'true':'false'} aria-hidden={!visible} style={{left:`${x}%`,top:y,opacity:visible?1:0}}>
     <TokenContent token={token} state={state}/>
    </div>;
   })}
  </div>
 </div>;
}

function ExecutionFacts({state}) {
 return <>
  <div className="advanced-motion-facts">
   <Fact label="Core / maximum workers">1 / 2</Fact>
   <Fact label="Queue occupancy / capacity">{state.queue.length} / 1</Fact>
   <Fact label="Worker 1">{state.worker1 ? `${state.worker1}${state.gateOpen?' can execute':' waits at gate'}` : state.created?'Available / finished':'Not created'}</Fact>
   <Fact label="Worker 2">{state.worker2 ? `${state.worker2}${state.gateOpen?' can execute':' waits at gate'}` : state.created===2?'Available / finished':'Not created'}</Fact>
   <Fact label="Accepted / rejected">{state.accepted.length} / {state.rejected.length}</Fact>
   <Fact label="Completed accepted tasks">{state.completed.length}</Fact>
  </div>
  <p className="advanced-motion-note">{state.decision}</p>
 </>;
}

function QueueFacts({state}) {
 const values={A:'10',B:'20',C:'12'};
 return <div className="advanced-motion-facts">
  <Fact label="FIFO: oldest → newest">{state.queue.length?state.queue.map(id=>values[id]).join(' → '):'Empty'}</Fact>
  <Fact label="Queued / capacity">{state.queue.length} / 2</Fact>
  <Fact label="Queue lock owner">{state.lockOwner==='none'?'None':state.lockOwner}</Fact>
  <Fact label="Producer call">{state.producer}</Fact>
  <Fact label="Consumer call">{state.consumer}</Fact>
  <Fact label="Ownership of waiting put">{state.locations.C==='waiting'?'Producer still owns 12':'See the payload region'}</Fact>
 </div>;
}

function CasFacts({state}) {
 return <>
  <div className="advanced-motion-facts">
   <Fact label="Shared remaining">{state.remaining}</Fact>
   <Fact label="Successful reservations">{state.successes.join(', ')||'None'}</Fact>
   <Fact label="A attempt">{state.phaseA}</Fact>
   <Fact label="B attempt">{state.phaseB}</Fact>
  </div>
  <p className="advanced-motion-note"><strong>E</strong> is this call’s expected value, stored in previous. <strong>U</strong> is the proposed replacement, previous minus units. A failed CAS writes neither argument to remaining.</p>
 </>;
}

function CompletionFacts({state}) {
 return <>
  <div className="advanced-motion-facts">
   <Fact label="Score source">{state.scoreDone?'Completed with 43':'Pending'}</Fact>
   <Fact label="Label source">{state.labelDone?'Completed with score':'Pending'}</Fact>
   <Fact label="Combine action">{state.combine}</Fact>
   <Fact label="Result future">{state.resultDone?'Completed with score=43':'Not yet completed'}</Fact>
  </div>
  <p className="advanced-motion-note">thenCombine has no Async suffix here. A permitted completing or attaching caller may run its action; the diagram does not allocate another worker for that continuation.</p>
 </>;
}

export function AdvancedMotionVisual({kind, state}) {
 return <div className="advanced-motion-visual">
  <MotionBoard kind={kind} state={state}/>
  {kind==='executor'?<ExecutionFacts state={state}/>:kind==='queue'?<QueueFacts state={state}/>:kind==='cas'?<CasFacts state={state}/>:<CompletionFacts state={state}/>}
 </div>;
}

function AdvancedMotionModel({model, config}) {
 const [variant,setVariant]=useState(config.defaultVariant);
 const trace=config.variants.find(option=>option.id===variant).trace;
 const headerExtra=ready=>config.variants.length>1?<label className="advanced-motion-select">{config.selectorLabel}
  <SelectControl disabled={!ready} value={variant} onChange={event=>setVariant(event.target.value)} aria-label={config.selectorLabel}>
   {config.variants.map(option=><option key={option.id} value={option.id}>{option.label}</option>)}
  </SelectControl>
 </label>:undefined;
 return <div className="advanced-thread-motion" data-advanced-motion={model} data-motion-variant={variant}>
  <ConceptPlayer key={model+'-'+variant} trace={trace} Visual={AdvancedMotionVisual} codeLabel={trace.codeLabel} headerExtra={headerExtra} modelId={'advanced-'+model}/>
 </div>;
}

export default function AdvancedThreadMotion({model}) {
 const config=advancedThreadModels[model];
 if(!config)throw new Error('Unknown advanced threading motion: '+model);
 return <AdvancedMotionModel key={model} model={model} config={config}/>;
}
