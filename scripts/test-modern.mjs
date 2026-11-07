import {execFileSync} from 'node:child_process';import {mkdirSync,readFileSync,existsSync,readdirSync} from 'node:fs';import {join} from 'node:path';
const home=process.env.JDD_JAVA25_HOME||(existsSync('.cache/java25-home.txt')?readFileSync('.cache/java25-home.txt','utf8').trim():process.env.JAVA_HOME);if(!home)throw new Error('Set JDD_JAVA25_HOME to a Java 25 JDK directory.');
const javac=join(home,'bin/javac'),java=join(home,'bin/java'),out='.cache/modern-classes';mkdirSync(out,{recursive:true});
const examples=JSON.parse(readFileSync('tests/modern-smoke.json','utf8'));
const sources=readdirSync('java-examples/modern').filter(name=>name.endsWith('.java')).map(name=>join('java-examples/modern',name));
execFileSync(javac,['--enable-preview','--release','25','-d',out,...sources],{stdio:'inherit'});
for(const example of examples){const actual=execFileSync(java,['--enable-preview',...(example.java_args||[]),'-cp',out,example.class],{encoding:'utf8',timeout:15000}).trim();if(actual!==example.expected)throw new Error(`${example.class}: expected ${JSON.stringify(example.expected)}, got ${JSON.stringify(actual)}`);}
if(existsSync('java-examples/modern/tests')){
 const tests=readdirSync('java-examples/modern/tests').filter(name=>name.endsWith('Test.java'));
 if(tests.length){
  mkdirSync('.cache/java',{recursive:true});
  for(const [file,url] of [['junit-4.13.2.jar','https://repo.maven.apache.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar'],['hamcrest-core-1.3.jar','https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar']])if(!existsSync('.cache/java/'+file))execFileSync('curl',['--fail','--location','--silent','--show-error',url,'-o','.cache/java/'+file],{stdio:'inherit'});
  const cp=out+':.cache/java/junit-4.13.2.jar:.cache/java/hamcrest-core-1.3.jar';
  execFileSync(javac,['--enable-preview','--release','25','-cp',cp,'-d',out,...tests.map(name=>'java-examples/modern/tests/'+name)],{stdio:'inherit'});
  execFileSync(java,['--enable-preview','-cp',cp,'org.junit.runner.JUnitCore',...tests.map(name=>name.replace(/\.java$/,''))],{stdio:'inherit'});
 }
}
execFileSync(javac,['-d','.cache/modules','--module-source-path','java-examples/modern/jpms','-m','study.service,study.client'],{stdio:'inherit'});
const moduleOutput=execFileSync(java,['--module-path','.cache/modules','-m','study.client/study.client.Main'],{encoding:'utf8'}).trim();if(moduleOutput!=='Hello Mira')throw new Error('Module output mismatch');
console.log(`Verified ${examples.length} Java 25 programs and the two-module application. All ${sources.length} modern sources compile.`);
