import React, {useState} from 'react';
import ConceptPlayer from './ConceptPlayer.jsx';
import {jvmTraces} from '../data/jvm-traces';
import '../styles/jvm-visuals.css';

const pipeline=['Source text','javac checks','Class file','JVM loads class','main runs','Output'];
const values={A:7,B:9,C:99};
const options={compilation:[['success','Successful build'],['error','Failed edit, old class']],gc:[['ordinary','Only root → A'],['alias','Also keep → C']]};

function CompilationVisual({state}) {
 const classState=state.artifact==='none'?'No class file yet':state.artifact==='new'?'New class file':'Previous class file, unchanged';
 return <div className="jvm-compilation">
  <ol className="jvm-flow" aria-label="Source-to-output pipeline">{pipeline.map((label,index)=><li key={label} className={state.phase===index?'jvm-current':''} aria-current={state.phase===index?'step':undefined}><span className="jvm-stage-number" aria-hidden="true">{index+1}</span><span>{label}</span></li>)}</ol>
  <div className="jvm-artifact"><strong>Source on disk</strong><code>HelloJava.java</code><span>{state.source==='valid'?'Saved valid greeting':'Edited statement is missing its semicolon'}</span></div>
  <div className={`jvm-artifact ${state.artifact==='previous'?'jvm-previous':''}`}><strong>Compilation artifact</strong><code>out/HelloJava.class</code><span>{classState}</span></div>
  <div className="jvm-artifact"><strong>Application JVM</strong><span>{state.application}</span></div>
  {state.diagnostic?<div className="jvm-diagnostic"><strong>Compiler diagnostic excerpt</strong><code>{state.diagnostic}</code></div>:null}
 </div>;
}

function ObjectRepresentation({id,region,state}) {
 const marked=state.marked.includes(id);
 const reclaimed=region==='original'&&state.reclaimed;
 const present=region==='original'?!reclaimed:state.copied.includes(id);
 const unreachable=region==='original'&&state.traced&&!marked;
 return <div className={`jvm-object ${present?'jvm-object-present':'jvm-object-empty'} ${unreachable?'jvm-unmarked':''}`}>
  {present?<><strong>Object {id}</strong><span>value = {values[id]}</span>{id==='A'?<span>next → B<br/><small>({region==='destination'?state.edgeRegion:'original'})</small></span>:<span>next = null</span>}<span className="jvm-object-status">{region==='destination'?'Relocated representation':marked?'Marked reachable':unreachable?'Unreachable':'Not traced yet'}</span></>:<span>{reclaimed?'Reusable space':'Empty destination'}</span>}
 </div>;
}

function GcVisual({state}) {
 return <div className="jvm-gc">
  <div className="jvm-roots" aria-label="References from roots"><div><strong>root → A</strong><span>{state.rootRegion} position {state.rootRegion==='original'?2:1}</span></div>{state.retainAlias?<div><strong>keep → C</strong><span>{state.aliasRegion} position {state.aliasRegion==='original'?1:3}</span></div>:<div><strong>No keep root</strong><span>C has no root path</span></div>}</div>
  <div className="jvm-region"><strong>Original region</strong><div className="jvm-region-cells">{['C','A','B'].map((id,index)=><div className="jvm-position" key={id}><span>Position {index+1}</span><ObjectRepresentation id={id} region="original" state={state}/></div>)}</div></div>
  <div className="jvm-move"><span>Representation transfer</span><strong>{state.move}</strong></div>
  <div className="jvm-region"><strong>Destination region</strong><div className="jvm-region-cells">{['A','B','C'].map((id,index)=><div className="jvm-position" key={id}><span>Position {index+1}</span><ObjectRepresentation id={id} region="destination" state={state}/></div>)}</div></div>
  <p className="jvm-model-note">A and B are stable object identities. Two drawings during copying show representations of the same identity, not application-created clones.</p>
 </div>;
}

function JvmStateVisual({kind,state}) {
 return kind==='compilation'?<CompilationVisual state={state}/>:<GcVisual state={state}/>;
}

/** Supplies JVM-specific states to the shared accessible guided-execution player. */
export default function JvmVisuals({model='compilation'}) {
 const [variant,setVariant]=useState(model==='gc'?'ordinary':'success');
 const variants=jvmTraces[model];
 if(!variants)throw new Error('Unknown JVM visual: '+model);
 const selected=variants[variant]?variant:Object.keys(variants)[0];
 const trace=variants[selected];
 const selector=ready=><label className="jvm-scenario">Scenario<select aria-label="Scenario" disabled={!ready} value={selected} onChange={event=>setVariant(event.target.value)}>{options[model].map(([value,label])=><option key={value} value={value}>{label}</option>)}</select></label>;
 return <div className="jvm-visuals"><ConceptPlayer key={`${model}-${selected}`} trace={trace} Visual={JvmStateVisual} codeLabel={model==='compilation'?'Terminal commands and labelled source excerpt':'Chosen collector pseudocode · application paused'} headerExtra={selector} modelId={`jvm-${trace.kind}`}/></div>;
}
