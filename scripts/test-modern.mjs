import {execFileSync} from 'node:child_process';import {mkdirSync,readFileSync,existsSync,readdirSync} from 'node:fs';import {join} from 'node:path';
const home=process.env.JDD_JAVA25_HOME||(existsSync('.cache/java25-home.txt')?readFileSync('.cache/java25-home.txt','utf8').trim():process.env.JAVA_HOME);if(!home)throw new Error('Set JDD_JAVA25_HOME to a Java 25 JDK directory.');
const javac=join(home,'bin/javac'),java=join(home,'bin/java'),out='.cache/modern-classes';mkdirSync(out,{recursive:true});
const examples=JSON.parse(readFileSync('tests/modern-smoke.json','utf8'));
const sources=readdirSync('java-examples/modern').filter(name=>name.endsWith('.java')).map(name=>join('java-examples/modern',name));
execFileSync(javac,['--enable-preview','--release','25','-d',out,...sources],{stdio:'inherit'});
for(const example of examples){const actual=execFileSync(java,['--enable-preview',...(example.java_args||[]),'-cp',out,example.class],{encoding:'utf8',timeout:15000}).trim();if(actual!==example.expected)throw new Error(`${example.class}: expected ${JSON.stringify(example.expected)}, got ${JSON.stringify(actual)}`);}
execFileSync(javac,['-d','.cache/modules','--module-source-path','java-examples/modern/jpms','-m','study.service,study.client'],{stdio:'inherit'});
const moduleOutput=execFileSync(java,['--module-path','.cache/modules','-m','study.client/study.client.Main'],{encoding:'utf8'}).trim();if(moduleOutput!=='Hello Mira')throw new Error('Module output mismatch');
console.log(`Verified ${examples.length} Java 25 programs and the two-module application. All ${sources.length} modern sources compile.`);
