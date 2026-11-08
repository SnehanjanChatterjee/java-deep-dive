import React, {useEffect, useState} from 'react';

/** Keeps instructional state, code highlighting and optional playback in sync. */
export default function ConceptPlayer({trace, Visual, codeLabel, headerExtra, modelId}) {
 const [step,setStep]=useState(0);
 const [playing,setPlaying]=useState(false);
 const [ready,setReady]=useState(false);
 useEffect(()=>setReady(true),[]);
 const last=step===trace.steps.length-1;
 const state=trace.steps[step];
 useEffect(()=>{
  if(!playing||last)return;
  const timer=setTimeout(()=>setStep(value=>value+1),trace.playbackMs??1600);
  return()=>clearTimeout(timer);
 },[playing,step,last,trace.playbackMs]);
 const move=next=>{setPlaying(false);setStep(next)};
 const isCurrentLine=index=>Array.isArray(state.line)?state.line.includes(index):state.line===index;
 return <section className="concept-animation" aria-label={trace.title} data-concept-animation={modelId||trace.kind}>
  <div className="concept-heading"><div><span className="eyebrow">GUIDED EXECUTION</span><h3>{trace.title}</h3></div>{typeof headerExtra==='function'?headerExtra(ready):headerExtra}</div>
  <p className="concept-summary">{trace.summary}</p>
  <div className="concept-stage">
   <div className="concept-visual"><Visual kind={trace.kind} state={state}/></div>
   <div className="concept-code"><span className="trace-code-label">{codeLabel||'Java statements'}</span><pre><code>{trace.code.map((line,index)=><span key={index} className={isCurrentLine(index)?'current':''} aria-current={isCurrentLine(index)?'step':undefined}><small aria-hidden="true">{index+1}</small>{line||' '}</span>)}</code></pre></div>
  </div>
  <div className="concept-step-tools">
  <p className="concept-explanation" role="status" aria-live={playing&&!last?'off':'polite'}>{state.text}</p>
  <div className="concept-controls">
   <button className="secondary" disabled={!ready||step===0} onClick={()=>move(step-1)}>Previous</button>
   <span>{step+1} / {trace.steps.length}</span>
   <button className="secondary" disabled={!ready} onClick={()=>move(0)}>Reset</button>
   <button className="secondary" disabled={!ready||last} onClick={()=>setPlaying(value=>!value)}>{playing&&!last?'Pause':'Play'}</button>
   <button className="primary" disabled={!ready||last} onClick={()=>move(step+1)}>Next</button>
  </div>
  </div>
  <div className="concept-output"><span>{trace.outputLabel||'Output so far'}</span><code>{state.output===''?'—':state.output??'—'}</code></div>
  <p className="concept-caption">{trace.caption||'Conceptual execution model. Complete Java examples are included in the lesson.'}</p>
 </section>;
}
