export function initializeSite(){
 const app=document.getElementById('site-app')!;
 const menu=document.querySelector<HTMLButtonElement>('[data-menu]');
 const sidebar=document.querySelector<HTMLElement>('.sidebar');
 const content=document.querySelector<HTMLElement>('#course-navigation-content');
 const backdrop=document.querySelector<HTMLButtonElement>('[data-nav-backdrop]');
 const closeButton=document.querySelector<HTMLButtonElement>('[data-nav-close]');
 const sidebarToggle=document.querySelector<HTMLButtonElement>('[data-sidebar-toggle]');
 const mobile=window.matchMedia('(max-width:800px)');
 let collapsed=false;
 let revealCurrentTopic=()=>{};
 const setSidebar=(value:boolean)=>{
  collapsed=value;app.classList.toggle('sidebar-collapsed',value);
  const label=value?'Expand course navigation':'Collapse course navigation';
  sidebarToggle?.setAttribute('aria-expanded',String(!value));
  sidebarToggle?.setAttribute('aria-label',label);sidebarToggle?.setAttribute('title',label);
  if(content){content.hidden=!mobile.matches&&value;content.inert=!mobile.matches&&value;}
 };
 const setMobile=(open:boolean,restoreFocus=true)=>{
  sidebar?.classList.toggle('mobile-open',open);menu?.setAttribute('aria-expanded',String(open));
  if(backdrop)backdrop.hidden=!open;
  document.body.classList.toggle('navigation-open',open);
  for(const element of document.querySelectorAll<HTMLElement>('.topbar,.page-grid'))element.inert=open;
  if(sidebar){if(open){sidebar.setAttribute('role','dialog');sidebar.setAttribute('aria-modal','true')}else{sidebar.removeAttribute('role');sidebar.removeAttribute('aria-modal')}}
  if(open){closeButton?.focus();requestAnimationFrame(()=>revealCurrentTopic())}else if(restoreFocus&&mobile.matches)menu?.focus();
 };
 const closeMenu=()=>setMobile(false,false);
 menu?.addEventListener('click',()=>setMobile(!sidebar?.classList.contains('mobile-open')));
 closeButton?.addEventListener('click',()=>setMobile(false));backdrop?.addEventListener('click',()=>setMobile(false));
 document.addEventListener('keydown',event=>{
  if(!sidebar?.classList.contains('mobile-open'))return;
  if(event.key==='Escape'){event.preventDefault();event.stopPropagation();setMobile(false);return;}
  if((event.ctrlKey||event.metaKey)&&event.key==='k'){event.preventDefault();event.stopPropagation();return;}
  if(event.key==='Tab'){
   const controls=[...sidebar.querySelectorAll<HTMLElement>('a[href],button,summary')].filter(element=>element.getClientRects().length>0);
   const first=controls[0],last=controls.at(-1);
   if(event.shiftKey&&document.activeElement===first){event.preventDefault();last?.focus()}
   else if(!event.shiftKey&&document.activeElement===last){event.preventDefault();first?.focus()}
  }
 });
 try{collapsed=localStorage.getItem('jdd-sidebar-collapsed')==='true'}catch{}
 setSidebar(collapsed);
 mobile.addEventListener('change',()=>{const hadFocus=sidebar?.contains(document.activeElement);setMobile(false,false);setSidebar(collapsed);if(hadFocus)(mobile.matches?menu:sidebarToggle)?.focus()});
 sidebarToggle?.addEventListener('click',()=>{setSidebar(!collapsed);try{localStorage.setItem('jdd-sidebar-collapsed',String(collapsed))}catch{}});
 const themeToggle=document.querySelector<HTMLButtonElement>('button[data-theme]');
 const setDark=(dark:boolean)=>{document.documentElement.dataset.colorTheme=dark?'dark':'light';document.documentElement.style.colorScheme=dark?'dark':'light';app.classList.toggle('dark',dark);themeToggle?.setAttribute('aria-label',dark?'Use light theme':'Use dark theme');themeToggle?.setAttribute('aria-pressed',String(dark))};
 try{setDark(localStorage.getItem('jdd-theme')==='dark')}catch{setDark(false)}
 themeToggle?.addEventListener('click',()=>{const dark=!app.classList.contains('dark');setDark(dark);try{localStorage.setItem('jdd-theme',dark?'dark':'light')}catch{}});
 // One course tree is shared by the catalog and every lesson.
 const tree=document.querySelector<HTMLElement>('[data-course-tree]');
 const modules=[...document.querySelectorAll<HTMLDetailsElement>('[data-course-module]')];
 const panels=[...document.querySelectorAll<HTMLElement>('[data-module-panel]')];
 const showModule=(id:string)=>{
  const selected=modules.find(module=>module.dataset.courseModule===id);
  if(!selected)return;
  modules.forEach(module=>{
   module.open=module===selected;
   module.classList.toggle('current-module',module===selected);
  });
  panels.forEach(panel=>{panel.hidden=panel.dataset.modulePanel!==id});
 };
 if(tree?.dataset.catalog==='true'){
  const fromHash=()=>{
   const requested=location.hash.replace(/^#module-/,'');
   showModule(modules.some(module=>module.dataset.courseModule===requested)?requested:modules[0]?.dataset.courseModule||'');
  };
  modules.forEach(module=>module.querySelector('summary')?.addEventListener('click',event=>{
   event.preventDefault();
   if(module.open){module.open=false;return;}
   const id=module.dataset.courseModule!;
   showModule(id);
   if(location.hash!=='#module-'+id)history.pushState(null,'','#module-'+id);
   window.scrollTo({top:0,behavior:'instant'});
  }));
  fromHash();
  // The old module hashes now select a panel rather than jumping down a long page.
  if(location.hash.startsWith('#module-')){
   window.scrollTo({top:0,behavior:'instant'});
   window.addEventListener('load',()=>window.scrollTo({top:0,behavior:'instant'}),{once:true});
  }
  window.addEventListener('hashchange',fromHash);
  window.addEventListener('popstate',fromHash);
 }
 if(tree){
  if(tree.dataset.catalog!=='true'&&!tree.querySelector('[aria-current="page"]')){
   try{const saved=sessionStorage.getItem('jdd-open-module');const branch=modules.find(module=>module.dataset.courseModule===saved);if(branch)branch.open=true}catch{}
  }
  modules.forEach(module=>module.addEventListener('toggle',()=>{try{const open=modules.find(branch=>branch.open);if(open)sessionStorage.setItem('jdd-open-module',open.dataset.courseModule!)}catch{}}));
  const ensureCurrentVisible=()=>{
   const current=tree.querySelector<HTMLElement>('[aria-current="page"]');if(!current)return;
   const item=current.getBoundingClientRect(),viewport=tree.getBoundingClientRect();
   if(!viewport.height||!item.height)return;
   const heading=current.closest('details')?.querySelector('summary')?.getBoundingClientRect();
   const top=heading&&item.bottom-heading.top<=viewport.height-24?heading.top:item.top;
   if(top<viewport.top+12)tree.scrollTop-=viewport.top+12-top;
   else if(item.bottom>viewport.bottom-12)tree.scrollTop+=item.bottom-viewport.bottom+12;
  };
  revealCurrentTopic=ensureCurrentVisible;
  try{const saved=sessionStorage.getItem('jdd-course-scroll');requestAnimationFrame(()=>{if(saved)tree.scrollTop=Number(saved);ensureCurrentVisible()})}catch{}
  document.fonts.ready.then(()=>requestAnimationFrame(ensureCurrentVisible));
  window.addEventListener('load',ensureCurrentVisible,{once:true});
  window.addEventListener('resize',()=>requestAnimationFrame(ensureCurrentVisible));
  tree.addEventListener('scroll',()=>{try{sessionStorage.setItem('jdd-course-scroll',String(tree.scrollTop))}catch{}},{passive:true});
  tree.querySelectorAll('a').forEach(link=>link.addEventListener('click',closeMenu));
 }

 const readerDisclosures=[...document.querySelectorAll<HTMLDetailsElement>('[data-reader-disclosure]')];
 readerDisclosures.forEach(disclosure=>{
  disclosure.addEventListener('toggle',()=>{if(disclosure.open)readerDisclosures.filter(other=>other!==disclosure).forEach(other=>other.open=false)});
  disclosure.addEventListener('keydown',event=>{if(event.key==='Escape'){event.preventDefault();event.stopPropagation();disclosure.open=false;disclosure.querySelector('summary')?.focus()}});
 });
 document.addEventListener('pointerdown',event=>{readerDisclosures.forEach(disclosure=>{if(!disclosure.contains(event.target as Node))disclosure.open=false})});
 document.querySelectorAll<HTMLButtonElement>('[data-copy-code]').forEach(button=>button.addEventListener('click',async()=>{const text=button.closest('.example-code')?.querySelector('pre code')?.textContent||'';try{await navigator.clipboard.writeText(text);button.textContent='Copied';setTimeout(()=>button.textContent='Copy',1600)}catch{button.textContent='Select code to copy'}}));
 const links=[...document.querySelectorAll<HTMLAnchorElement>('[data-section-link]')];
 const ids=[...new Set(links.map(l=>l.dataset.sectionLink!))];
 const update=()=>{let current: string | undefined=ids[0];for(const id of ids){const target=document.getElementById(id);if(target&&target.getBoundingClientRect().top<=150)current=id}if(innerHeight+scrollY>=document.documentElement.scrollHeight-5)current=ids.at(-1);links.forEach(l=>{const active=l.dataset.sectionLink===current;l.classList.toggle('active',active);if(active)l.setAttribute('aria-current','location');else l.removeAttribute('aria-current')});document.querySelectorAll<HTMLElement>('[data-stage]').forEach(stage=>{const active=!!stage.querySelector('[aria-current="location"]');stage.classList.toggle('current-stage',active);stage.querySelector<HTMLElement>('.stage-current-label')!.hidden=!active})};
 let scheduled=false;window.addEventListener('scroll',()=>{if(!scheduled){scheduled=true;requestAnimationFrame(()=>{update();scheduled=false})}},{passive:true});
 links.forEach(link=>link.addEventListener('click',()=>{
  link.closest('details.mobile-outline,details.article-contents')?.removeAttribute('open');
  const target=document.getElementById(link.dataset.sectionLink!);
  if(target){
   const temporaryTabIndex=!target.hasAttribute('tabindex');
   if(temporaryTabIndex)target.setAttribute('tabindex','-1');
   target.focus({preventScroll:true});
   target.scrollIntoView({behavior:'instant'});
   if(temporaryTabIndex)target.addEventListener('blur',()=>target.removeAttribute('tabindex'),{once:true});
  }
  closeMenu();requestAnimationFrame(update);
 }));
 update();window.addEventListener('load',update);
}
