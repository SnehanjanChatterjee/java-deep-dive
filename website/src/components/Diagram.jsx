import React, {useEffect, useRef, useState} from 'react';

export default function Diagram({definition, caption}) {
 const ref = useRef(null);
 const viewport = useRef(null);
 const [error, setError] = useState(false);
 const [overflow, setOverflow] = useState(false);
 useEffect(() => {
  let active = true;
  let revision = 0;
  let lastTheme;
  let wasOverflow = false;
  let naturalWidth = 0;
  const measure = () => {
   const element = viewport.current;
   if (!active || !element || !naturalWidth) return;
   const scrolls = naturalWidth > element.clientWidth + 1;
   if (scrolls && !wasOverflow) element.scrollLeft = (element.scrollWidth - element.clientWidth) / 2;
   if (!scrolls) element.scrollLeft = 0;
   wasOverflow = scrolls;
   setOverflow(scrolls);
  };
  const render = async () => {
   const dark = document.documentElement.dataset.colorTheme === 'dark';
   if (dark === lastTheme) return;
   lastTheme = dark;
   const current = ++revision;
   setError(false);
   try {
    const {default: mermaid} = await import('mermaid');
    mermaid.initialize({startOnLoad:false, securityLevel:'strict', theme:'base', themeVariables:dark
     ? {primaryColor:'#292d33', primaryTextColor:'#f0f2f5', primaryBorderColor:'#93a4bc', lineColor:'#bfcbdd', fontFamily:'sans-serif'}
     : {primaryColor:'#edf6f1', primaryTextColor:'#25372e', primaryBorderColor:'#94b8a4', lineColor:'#70907f', fontFamily:'sans-serif'}});
    const result = await mermaid.render('diagram-'+Math.random().toString(36).slice(2), definition);
    if (active && current === revision && ref.current) {
     ref.current.innerHTML = result.svg;
     naturalWidth = ref.current.querySelector('svg')?.viewBox.baseVal.width || 0;
     ref.current.style.minWidth = naturalWidth ? `${naturalWidth}px` : '';
     measure();
    }
   } catch {
    if (active && current === revision) setError(true);
   }
  };
  const observer = new MutationObserver(render);
  observer.observe(document.documentElement, {attributes:true, attributeFilter:['data-color-theme']});
  const resizeObserver = new ResizeObserver(measure);
  if (viewport.current) resizeObserver.observe(viewport.current);
  render();
  return () => {active = false; observer.disconnect(); resizeObserver.disconnect();};
 }, [definition]);
 return <figure className="diagram"><div className="diagram-viewport" ref={viewport} tabIndex={overflow?0:undefined} role={overflow?'region':undefined} aria-label={overflow?'Scrollable diagram':undefined}><div className="diagram-canvas" role="img" aria-label={caption} ref={ref}/></div>{error&&<p>Diagram could not be rendered. {caption}</p>}<figcaption>{caption}{overflow&&<span className="diagram-scroll-hint">Scroll horizontally to explore the full diagram.</span>}</figcaption></figure>;
}
