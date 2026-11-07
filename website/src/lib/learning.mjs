export const storageKey='java-deep-dive.learning.v1';
export const statuses=['not-started','learning','completed','review'];
export function emptyState(){return {version:1,lessons:{}}}
export function validateState(value,knownIds){
 if(!value||value.version!==1||!value.lessons||typeof value.lessons!=='object'||Array.isArray(value.lessons))throw new Error('Choose a Java Deep Dive version 1 learning-state file.');
 const state=emptyState();const known=new Set(knownIds);
 for(const [id,item]of Object.entries(value.lessons)){
  if(!known.has(id))continue;
  if(!item||!statuses.includes(item.status)||typeof item.bookmarked!=='boolean'||typeof item.note!=='string'||item.note.length>20000)throw new Error('The learning-state file contains an invalid lesson record.');
  state.lessons[id]={status:item.status,bookmarked:item.bookmarked,note:item.note};
 }
 return state;
}
export function getState(ids){const raw=localStorage.getItem(storageKey);return raw?validateState(JSON.parse(raw),ids):emptyState()}
export function saveState(state){localStorage.setItem(storageKey,JSON.stringify(state));window.dispatchEvent(new Event('learning-updated'))}
export function updateLesson(id,patch,ids){const state=getState(ids);state.lessons[id]={status:'not-started',bookmarked:false,note:'',...state.lessons[id],...patch};const valid=validateState(state,ids);saveState(valid);return valid.lessons[id]}
