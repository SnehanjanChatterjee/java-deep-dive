import React, {useEffect, useState} from 'react';
import {getState, updateLesson} from '../lib/learning.mjs';

const initialItem = {status:'not-started', bookmarked:false, note:''};

export default function LearningControls({id, ids}) {
 const [item, setItem] = useState(initialItem);
 const [note, setNote] = useState('');
 const [notesOpen, setNotesOpen] = useState(false);
 const [message, setMessage] = useState('');
 useEffect(() => {
  try {
   const saved = getState(ids).lessons[id] || initialItem;
   setItem(saved);
   setNote(saved.note);
  } catch {
   setMessage('Saved notes could not be loaded.');
  }
 }, [id, ids]);
 const update = patch => {
  try {
   setItem(updateLesson(id, patch, ids));
   setMessage('Saved on this device.');
  } catch {
   setMessage('Unable to save. Browser storage may be unavailable or full.');
  }
 };
 return <section className="learning-controls lesson-tools" aria-label="Study tools" data-pagefind-ignore>
  <div className="study-toolbar">
   <button className="secondary" aria-label={item.bookmarked?'Remove bookmark':'Bookmark topic'} aria-pressed={item.bookmarked} onClick={()=>update({bookmarked:!item.bookmarked})}>{item.bookmarked?'Bookmarked':'Bookmark'}</button>
   <label className="study-progress">Progress<select aria-label="Learning status" value={item.status} onChange={event=>update({status:event.target.value})}><option value="not-started">Not started</option><option value="learning">Studying</option><option value="review">Revisit</option><option value="completed">Completed</option></select></label>
   <button className="secondary" aria-expanded={notesOpen} aria-controls={'notes-editor-'+id} onClick={()=>setNotesOpen(value=>!value)}>Notes{item.note?' · saved':''}</button>
   <span className="study-storage">Stored on this device</span>
  </div>
  <div className="study-notes" id={'notes-editor-'+id} hidden={!notesOpen}>
   <label htmlFor={'note-'+id}>Notes for this topic</label>
   <textarea id={'note-'+id} maxLength={20000} rows={4} value={note} onChange={event=>setNote(event.target.value)}/>
   <button className="secondary" onClick={()=>update({note})}>Save notes</button>
  </div>
  <p className="study-save-message" role="status" aria-live="polite" hidden={!message}>{message}</p>
 </section>;
}
