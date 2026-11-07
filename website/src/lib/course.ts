import type { CollectionEntry } from 'astro:content';
import { modules } from '../data/modules';

/** Uses the same teaching groups in the course tree and module contents. */
export function getModuleGroups(lessons:CollectionEntry<'lessons'>[], moduleId:string) {
 const topics=lessons.filter(lesson=>lesson.data.module===moduleId);
 const present=[...new Set(topics.map(lesson=>lesson.data.group||'Topics'))];
 const preferred=modules.find(module=>module.id===moduleId)?.groups||[];
 const names=[...preferred.filter(group=>present.includes(group)),...present.filter(group=>!preferred.includes(group))];
 return names.map(title=>({title,lessons:topics.filter(lesson=>(lesson.data.group||'Topics')===title)}));
}
