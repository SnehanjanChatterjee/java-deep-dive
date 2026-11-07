import { defineConfig } from 'astro/config';
import mdx from '@astrojs/mdx';
import react from '@astrojs/react';

function scrollableTables(){
 return tree=>{
  const wrap=node=>{
   if(!node.children)return;
   node.children=node.children.map(child=>{
    if(child.type==='element'&&child.tagName==='table')return {type:'element',tagName:'div',properties:{className:['table-scroll'],tabIndex:0,role:'region',ariaLabel:'Scrollable comparison table'},children:[child]};
    wrap(child);return child;
   });
  };
  wrap(tree);
 };
}

export default defineConfig({site:'https://SnehanjanChatterjee.github.io',base:process.env.SITE_BASE || '/java-deep-dive',output:'static',integrations:[mdx(),react()],markdown:{rehypePlugins:[scrollableTables],shikiConfig:{themes:{light:'github-light',dark:'github-dark-high-contrast'}}},server:{host:'0.0.0.0',port:4321}});
