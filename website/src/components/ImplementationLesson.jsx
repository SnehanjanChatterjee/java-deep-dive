const base=import.meta.env.BASE_URL.replace(/\/?$/, '/');
import LearningHashMapTestSource from '../../../java-examples/src/test/java/LearningHashMapTest.java?raw';
import HashMapDemoSource from '../../../java-examples/src/main/java/HashMapDemo.java?raw';
import LearningHashMapSource from '../../../java-examples/src/main/java/LearningHashMap.java?raw';
import React, { useEffect, useRef, useState } from 'react';

const canonicalSources={'LearningHashMap.java':LearningHashMapSource,'HashMapDemo.java':HashMapDemoSource,'LearningHashMapTest.java':LearningHashMapTestSource};
const sourceFiles = [
  ['LearningHashMap.java', 'Implementation'],
  ['HashMapDemo.java', 'Runnable demo'],
  ['LearningHashMapTest.java', 'JUnit tests'],
];
const methods = [
  ['put', 'public V put(', 'Replace or insert', 'First find an equal key in the selected bucket. Replacement returns the old value without changing size or modCount. A new entry is linked at the head of its bucket, then size and modCount increase. The growth check happens only for a new mapping.'],
  ['find', 'private Node<K, V> find(', 'Lookup one chain', 'The stored spread hash must match, then Objects.equals checks key identity/equality safely for nulls. This separates a bucket collision from a logical-key match. The method returns a node so get and containsKey can distinguish presence from a null value.'],
  ['resize', 'private void resize()', 'Relink without losing nodes', 'Save the original next reference before changing a link. Recalculate the bucket with the new mask using the stored spread hash, then attach the existing node to the new table. This exercise uses simple head insertion; OpenJDK uses a split algorithm that preserves partition order.'],
  ['unlink', 'private Node<K, V> unlink(', 'Remove head, middle, or tail', 'Track both the candidate node and its predecessor. Removing the head changes the table slot; removing another node changes previous.next. Only a successful removal changes size and modCount.'],
  ['iterator', 'private final class EntryIterator', 'Traverse and detect interference', 'Walk the current chain before scanning the next nonempty bucket. Compare expectedModCount before next/remove. The iterator’s own remove refreshes its expected count, so traversal may continue. hasNext is not an interference check and no synchronization is provided.'],
  ['entry', 'private static final class Node', 'Honor the entry contract', 'Each Node is a live Map.Entry. setValue updates the map without structural modification. Entry equality compares both key and value; its public hashCode is keyHash XOR valueHash, which is distinct from the cached spread hash used for bucket selection.'],
];

export function ImplementationLesson({ CodeBlock, Note }) {
  const [file, setFile] = useState(sourceFiles[0][0]);
  const [source, setSource] = useState(LearningHashMapSource);
  const [error, setError] = useState('');
  const [expanded, setExpanded] = useState(false);
  const [method, setMethod] = useState(methods[0]);
  const viewer = useRef(null);
  useEffect(() => { setSource(canonicalSources[file]); }, [file]);
  const showMethod = item => {
    setMethod(item);
    const line = source.split('\n').findIndex(text => text.includes(item[1]));
    const row = viewer.current?.querySelectorAll('.code-line')[line];
    const scroll = viewer.current?.querySelector('pre');
    if (row && scroll) scroll.scrollTo({ top: scroll.scrollTop + row.getBoundingClientRect().top - scroll.getBoundingClientRect().top - 14, behavior: 'instant' });
  };
  return <>
    <p>This extended implementation integrates a separate-chaining hash table with the Map interface through AbstractMap. It adds backed collection views, live entries, iterator removal, and structural-modification checks to the core storage operations.</p>
    <Note><strong>Two levels of implementation.</strong> This exercise implements a separate-chaining map and the Map contract through AbstractMap. OpenJDK’s production HashMap additionally implements tree bins, optimized operations, serialization, cloning, spliterators, and more. Read the <a href="https://github.com/openjdk/jdk/blob/jdk-25-ga/src/java.base/share/classes/java/util/HashMap.java" target="_blank" rel="noreferrer">complete OpenJDK 25 implementation</a> alongside it. Those production features are not implemented in this example.</Note>
    <h3>Implementation overview</h3>
    <div className="table-scroll"><table><thead><tr><th>Mechanism</th><th>Implementation</th><th>Key concepts</th></tr></thead><tbody>
      <tr><td>Storage & hashing</td><td>Generic Node array, cached hash, power-of-two capacity, null-safe hash spreading</td><td>Why keys can share a bucket; why mask with capacity − 1</td></tr>
      <tr><td>Core operations</td><td>put, get, containsKey, remove, size, clear</td><td>Replacement versus insertion; missing versus mapped-to-null</td></tr>
      <tr><td>Growth</td><td>Load threshold, doubling, relinking existing nodes</td><td>When growth happens; how links and mappings survive</td></tr>
      <tr><td>Views & iteration</td><td>Backed entrySet, live Map.Entry values, iterator removal and fail-fast checks</td><td>Structural versus non-structural changes; backed versus copied data</td></tr>
      <tr><td>Inherited Map behavior</td><td>AbstractMap supplies keySet/values/equality; Map defaults supply compute/merge and related methods</td><td>Which methods you wrote versus inherited; why defaults are not atomic</td></tr>
    </tbody></table></div>
    <h3>Read the complete source</h3>
    <p>Select a file. The implementation opens in a scrollable source viewer; expand it for uninterrupted reading. The walkthrough buttons move to the corresponding method or nested class below.</p>
    <div className="implementation-toolbar"><div className="implementation-files" aria-label="Java source files">{sourceFiles.map(([name, label]) => <button key={name} className={file === name ? 'selected' : ''} aria-pressed={file === name} onClick={() => setFile(name)}>{label}</button>)}</div><button className="text-button" onClick={() => setExpanded(value => !value)}>{expanded ? 'Compact source' : 'Expand source'}</button></div>
    {file === 'LearningHashMap.java' && <div className="method-guide"><div className="method-buttons" aria-label="Implementation walkthrough">{methods.map(item => <button key={item[0]} aria-pressed={method[0] === item[0]} className={method[0] === item[0] ? 'selected' : ''} onClick={() => showMethod(item)}>{item[0]==='iterator'?'EntryIterator':item[0]==='entry'?'Node (Map.Entry)':item[0]+'()'}</button>)}</div><h4>{method[2]}</h4><p>{method[3]}</p></div>}
    <div ref={viewer} className={`implementation-source ${expanded ? 'expanded' : ''}`} aria-busy={!source && !error}>
      {error ? <p role="alert">{error} <a href={`${base}examples/${file}`}>Open the source file directly.</a></p> : source ? <CodeBlock key={file} file={file} code={source} download/> : <p role="status">Loading Java source…</p>}
    </div>
    <div className="implementation-downloads"><a className="primary" href={base+"examples/hashmap-exercise.zip"} download>Download complete exercise (.zip)</a><a className="secondary" href={`${base}examples/${file}`} download>Download this file</a></div>
    <h3>Compile and run the demo</h3>
    <p>The implementation and demo require Java 8 or later and no external library. The downloadable project uses standard Maven folders. Run these commands from its extracted root:</p>
    <CodeBlock file="Terminal" code={'mkdir -p out\njavac -d out src/main/java/LearningHashMap.java src/main/java/HashMapDemo.java\njava -cp out HashMapDemo'} output={'seventeen\nseventeen\nupdated\none\ntrue\n3'}/>
    <p>The first two output lines both contain “seventeen”: get returns the current value, and the subsequent put returns the value it replaces. After the replacement, get returns “updated”. Removing key 1 returns “one”. The null key exists even though its value is null.</p>
    <h3>Tests that check behavior, not just the happy path</h3>
    <p>The accompanying JUnit suite covers nulls, equal-but-distinct keys, forced collisions, negative and minimum hash codes, chain removal, growth, live views, entry contracts, iterator errors, default Map methods, and a deterministic 5,000-operation comparison against java.util.HashMap.</p>
    <CodeBlock file="Terminal · tests" code={'mvn test'}/>
    <p>JUnit 4.13.2 is used here for Java 8 compatibility. The tests are provided in the source viewer and the archive. This verification does not establish equivalent performance or every production-JDK edge case.</p>
    <h3>Important differences from the JDK version</h3>
    <ul className="implementation-notes"><li><strong>Chained bins only:</strong> no red-black tree conversion. Collision-heavy lookup can be linear. The tree-bin section explains the production version.</li><li><strong>Eager table allocation:</strong> easier to inspect; default OpenJDK HashMap allocates lazily.</li><li><strong>Head insertion and simple transfer:</strong> an intentionally smaller algorithm. OpenJDK’s linked-bin split preserves order within each resize partition.</li><li><strong>Capacity capped at 2²⁰:</strong> a teaching limit. At the limit, the threshold becomes Integer.MAX_VALUE so later inserts do not attempt another resize.</li><li><strong>Basic backed views:</strong> some inherited operations scan entries. OpenJDK specializes them for better performance.</li><li><strong>No thread safety or atomic callbacks:</strong> fail-fast detects some mistakes; it does not coordinate threads.</li></ul>
    <h3>Trace the production source after the exercise</h3>
    <p>Use the source link above and search for <code>hash</code>, <code>getNode</code>, <code>putVal</code>, <code>resize</code>, <code>treeifyBin</code>, <code>TreeNode.find</code>, <code>removeNode</code>, and <code>HashIterator</code>. For each method, explain what state it reads, which invariants it preserves, and why the JDK implementation is more complex than this exercise.</p>
  </>;
}

export const interviewQuestions = [
 ['Does HashMap implement the entire Map API with separate hand-written methods?', 'It implements Map and extends AbstractMap. Some behavior is supplied by the superclass or interface defaults, and HashMap overrides many paths for efficiency. In the teaching map, trace inherited methods too: an inherited operation may scan entries instead of using bucket lookup.'],
 ['How would you implement put from scratch?', 'Compute the spread hash and index, search for an equal existing key, replace if found, otherwise link a new node, increment size and structural version, and grow when necessary. State your collision policy and allocation strategy. Then test null keys, collisions, replacement, and growth.'],
 ['How do you remove a node from a collision chain?', 'Track the previous node and current node. Removing the head changes the bucket slot; removing a middle or tail node changes previous.next. Do not lose the rest of the chain. Update size only after finding a match.'],
 ['Why save next before transferring a node during resize?', 'You are about to overwrite the node’s next link. Without first saving it, you can lose the remainder of the original chain or accidentally traverse links in the new table.'],
 ['What if equals is overridden but hashCode is not?', 'Equal objects may receive different hashes and be searched in different buckets. The map can then appear to contain duplicate logical keys or fail a lookup. Implement both consistently; test with equal but distinct instances.'],
 ['What if hashCode is constant for every key?', 'The contract may still be valid, but dispersion is poor. Chained lookup can degrade to linear search. JDK tree bins mitigate some cases, subject to capacity, key ordering, and equal-hash behavior.'],
 ['Is a collision the same as a duplicate key?', 'No. A collision means candidates share a bucket (or possibly a hash). An equal key represents the same logical mapping and replaces its value; unequal colliding keys coexist.'],
 ['Why power-of-two capacity and hash spreading?', 'The capacity mask is cheap and exposes low bits. Spreading folds high-bit variation into those low bits before masking. When capacity doubles, one extra index bit determines whether an entry stays or moves by the old capacity.'],
 ['Are negative hashes a special bucket-selection problem?', 'Bit masking handles signed integers without converting to a positive remainder. Math.abs is unnecessary and fails to make Integer.MIN_VALUE positive.'],
 ['Does resize call hashCode again for every stored key?', 'The ordinary OpenJDK resize uses cached spread hashes to redistribute existing nodes. Do not confuse reindexing stored entries with invoking every key’s hashCode again.'],
 ['Are computeIfAbsent and merge atomic on HashMap?', 'No. HashMap provides no concurrent-access coordination. ConcurrentHashMap supplies specified atomic per-key operations, but callbacks still need care and compound multi-key invariants require a separate design.'],
 ['Can an iterator survive replacing a value?', 'Replacing a value on an existing mapping is normally non-structural. Adding or deleting mappings is structural. A live entry can setValue without becoming a new mapping. This distinction does not make concurrent unsynchronized replacement safe.'],
 ['Is ConcurrentHashMap iteration “fail-safe”?', 'Prefer the precise term weakly consistent. Its iterators may reflect some concurrent updates and do not provide a snapshot of all entries. “Fail-safe” is not a general Map iterator contract.'],
 ['What is copied by a HashMap copy constructor?', 'Mappings are copied into a new map, but referenced key and value objects are shared. This is a shallow copy; mutating a shared value can affect what both maps observe.'],
 ['Is Map.of equivalent to new HashMap?', 'No. Map.of (Java 9+) creates an unmodifiable map and rejects null keys/values and duplicate supplied keys. HashMap is mutable, allows nulls, and replaces a prior value when put receives an equal key.'],
 ['Why use entrySet rather than keySet followed by get?', 'When both key and value are needed, entrySet exposes both during traversal and avoids a separate lookup per key. The exact performance difference depends on implementation and workload.'],
 ['Can HashMap be safely shared if no thread writes to it?', 'A fully constructed map that is safely published and then treated as read-only can be shared for reads. Safe publication and the mutability of contained objects still matter. Read-only use is not the same as a generally thread-safe mutable map.'],
 ['Which behavior should your custom-map tests compare?', 'Compare return values, key presence, size, null handling, equality, backed-view effects, and failure behavior. Do not compare unspecified iteration order or assume your implementation has the JDK’s performance.'],
];
