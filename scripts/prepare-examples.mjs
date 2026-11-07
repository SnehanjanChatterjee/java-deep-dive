import { readdir, mkdir, copyFile, rm, cp } from 'node:fs/promises';
import { fileURLToPath } from 'node:url';
import { basename } from 'node:path';
const root=fileURLToPath(new URL('../',import.meta.url));
const dest=root+'website/public/examples';
await rm(dest,{recursive:true,force:true});await mkdir(dest,{recursive:true});
for(const type of ['main','test'])for(const file of await readdir(root+`java-examples/src/${type}/java`))if(file.endsWith('.java'))await copyFile(root+`java-examples/src/${type}/java/${file}`,dest+'/'+file);
const {execFileSync}=await import('node:child_process');
execFileSync('python3',['-c',`from pathlib import Path
from zipfile import ZipFile, ZIP_DEFLATED
root=Path(${JSON.stringify(root)})
with ZipFile(root/'website/public/examples/hashmap-exercise.zip','w',ZIP_DEFLATED) as z:
 for name in ['MyHashMap.java','LearningHashMap.java','HashMapDemo.java']:
  z.write(root/'java-examples/src/main/java'/name, 'src/main/java/'+name)
 for name in ['MyHashMapTest.java','LearningHashMapTest.java']:
  z.write(root/'java-examples/src/test/java'/name,'src/test/java/'+name)
 z.write(root/'java-examples/hashmap-exercise.pom.xml','pom.xml')
 z.writestr('.mvn/maven.config','--settings\\nsettings.xml\\n-Dmaven.repo.local=.maven-cache\\n')
 z.writestr('settings.xml','<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"><mirrors><mirror><id>public-central</id><mirrorOf>*</mirrorOf><url>https://repo.maven.apache.org/maven2/</url></mirror></mirrors></settings>')
 z.writestr('README.txt','Java Deep Dive: HashMap exercise\\nRequires Java 8+.\\nCompile: mkdir -p out && javac -d out src/main/java/MyHashMap.java\\nRun: java -cp out MyHashMap\\nTests: mvn test\\nThe bundled Maven configuration uses a project-local dependency cache and public repository settings.\\nThe implementation is educational and does not reproduce every JDK feature.\\n')
`]);

await cp(root+'java-examples/modern',dest+'/modern',{recursive:true});
await cp(root+'java-examples/projects',dest+'/projects',{recursive:true,filter:source=>!['target','.maven-cache','out','.DS_Store'].includes(basename(source))});
execFileSync('python3',['-c',`from pathlib import Path
from zipfile import ZipFile, ZIP_DEFLATED
root=Path(${JSON.stringify(root)})
source=root/'website/public/examples/projects'
for project in source.iterdir():
 if project.is_dir():
  with ZipFile(root/'website/public/examples'/(project.name+'.zip'),'w',ZIP_DEFLATED) as bundle:
   for file in project.rglob('*'):
    if file.is_file(): bundle.write(file,file.relative_to(project))
`]);

execFileSync('python3', ['-c', `from pathlib import Path
from zipfile import ZipFile, ZIP_DEFLATED
root=Path(${JSON.stringify(root)})
source=root/'java-examples/benchmarks'
with ZipFile(root/'website/public/examples/lookup-benchmarks.zip','w',ZIP_DEFLATED) as bundle:
 for relative in ['pom.xml','settings.xml','.mvn/maven.config','README.md','src/main/java/study/bench/LookupBenchmark.java']:
  bundle.write(source/relative,relative)
`]);
