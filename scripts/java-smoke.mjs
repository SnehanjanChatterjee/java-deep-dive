import { readFile } from 'node:fs/promises';
import { execFileSync } from 'node:child_process';
const examples=JSON.parse(await readFile(new URL('../tests/java-smoke.json',import.meta.url),'utf8'));
for(const example of examples){const actual=execFileSync('java',['-cp','.cache/java/classes',example.class],{encoding:'utf8',timeout:10000}).trim();if(actual!==example.expected.trim())throw new Error(`${example.class}: expected ${JSON.stringify(example.expected)}, got ${JSON.stringify(actual)}`)}
console.log(`Verified ${examples.length} Java programs against their documented outputs.`);
