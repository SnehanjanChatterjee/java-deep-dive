import React,{useCallback,useEffect,useRef,useState} from 'react';
const base=import.meta.env.BASE_URL.replace(/\/?$/,'/');
export default function Search(){const [open,setOpen]=useState(false),[query,setQuery]=useState(''),[results,setResults]=useState([]),[status,setStatus]=useState('');const input=useRef(null),trigger=useRef(null),dialog=useRef(null),api=useRef(null);
const close=useCallback(()=>{setOpen(false);trigger.current?.focus({preventScroll:true})},[]);
useEffect(()=>{
 const key=event=>{
  if(event.defaultPrevented)return;
  if((event.metaKey||event.ctrlKey)&&event.key.toLowerCase()==='k'){
   event.preventDefault();
   if(open)close();else setOpen(true);
   return;
  }
  if(event.key==='Escape'&&open){event.preventDefault();close()}
 };
 window.addEventListener('keydown',key);
 return()=>window.removeEventListener('keydown',key);
},[open,close]);useEffect(()=>{if(!open)return;const previousOverflow=document.body.style.overflow;document.body.style.overflow='hidden';return()=>{document.body.style.overflow=previousOverflow}},[open]);useEffect(()=>{if(open)input.current?.focus({preventScroll:true})},[open]);useEffect(()=>{let active=true;const timer=setTimeout(async()=>{if(!query.trim()){setResults([]);setStatus('Search the complete lesson text.');return}try{setStatus('Searching…');if(!api.current)api.current=await import(/* @vite-ignore */`${base}pagefind/pagefind.js`);const response=await api.current.search(query);const data=await Promise.all(response.results.slice(0,8).map(r=>r.data()));if(active){setResults(data);setStatus(`${response.results.length} matching lessons`)}}catch{if(active)setStatus('Search is unavailable. Browse the course topics or try again.')}},180);return()=>{active=false;clearTimeout(timer)}},[query]);const trap=e=>{if(e.key!=='Tab')return;const nodes=dialog.current.querySelectorAll('button,a,input');const first=nodes[0],last=nodes[nodes.length-1];if(e.shiftKey&&document.activeElement===first){e.preventDefault();last.focus()}else if(!e.shiftKey&&document.activeElement===last){e.preventDefault();first.focus()}};return <><button ref={trigger} className="search-trigger" aria-label="Search all lessons" onClick={()=>setOpen(true)}><span>Search all lessons…</span><kbd>⌘ K</kbd></button>{open&&<div className="modal-backdrop" onMouseDown={e=>{if(e.target===e.currentTarget){e.preventDefault();close()}}}><div ref={dialog} className="search-modal" role="dialog" aria-modal="true" aria-label="Search lessons" onKeyDown={trap}><div className="search-field"><input ref={input} aria-label="Search lesson text" placeholder="Search hashing, memory visibility, generics…" value={query} onChange={e=>setQuery(e.target.value)}/><button className="secondary" onClick={close}>Close</button></div><p role="status" className="search-hint">{status}</p><div className="search-results">{results.map(r=><a href={r.url} key={r.url}><strong>{r.meta.title}</strong><span>{r.excerpt.replace(/<[^>]*>/g,'')}</span></a>)}</div></div></div>}</>}
