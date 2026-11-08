<p align="center">
  <img src="docs/assets/readme-banner.svg" alt="Java Deep Dive" width="880" />
</p>

<p align="center">
  <a href="https://snehanjanchatterjee.github.io/java-deep-dive/"><img src="docs/assets/readme-open-course.svg" alt="Open the live website →" width="240" height="40" /></a>
</p>

<p align="center">Learn Java from fundamentals to internals with runnable examples, interactive diagrams and interview exercises.</p>

<h2 align="center">Explore the course</h2>

<p align="center"><strong>Language &amp; objects · Generics &amp; collections<br>Streams · Concurrency · JVM &amp; diagnostics</strong></p>

<p align="center">Clear explanations, complete Java programs and guided visual walkthroughs.<br>Practise implementation, debug common mistakes and prepare for interview questions.</p>

<p align="center"><a href="docs/curriculum.md">Browse the curriculum →</a></p>

<h2 align="center">Built with</h2>

<p align="center">
  <a href="https://astro.build"><img src="https://img.shields.io/badge/Astro-BC52EE?style=flat-square&amp;logo=astro&amp;logoColor=white" alt="Astro" /></a>
  <a href="https://www.typescriptlang.org"><img src="https://img.shields.io/badge/TypeScript-3178C6?style=flat-square&amp;logo=typescript&amp;logoColor=white" alt="TypeScript" /></a>
  <a href="https://react.dev"><img src="https://img.shields.io/badge/React-20232A?style=flat-square&amp;logo=react&amp;logoColor=61DAFB" alt="React" /></a>
  <a href="https://mdxjs.com"><img src="https://img.shields.io/badge/MDX-F9AC00?style=flat-square&amp;logo=mdx&amp;logoColor=black" alt="MDX" /></a>
</p>
<p align="center">
  <a href="https://mermaid.js.org"><img src="https://img.shields.io/badge/Mermaid-FF3670?style=flat-square&amp;logo=mermaid&amp;logoColor=white" alt="Mermaid" /></a>
  <a href="https://pagefind.app"><img src="https://img.shields.io/badge/Pagefind-334155?style=flat-square" alt="Pagefind full-text search" /></a>
  <a href="https://dev.java"><img src="https://img.shields.io/badge/Java-ED8B00?style=flat-square&amp;logo=openjdk&amp;logoColor=white" alt="Java" /></a>
  <a href="https://junit.org/junit4"><img src="https://img.shields.io/badge/JUnit-25A162?style=flat-square" alt="JUnit" /></a>
  <a href="https://maven.apache.org"><img src="https://img.shields.io/badge/Maven-C71A36?style=flat-square&amp;logo=apachemaven&amp;logoColor=white" alt="Apache Maven" /></a>
</p>


<p align="center"><strong>Astro + MDX</strong> for lessons · <strong>React</strong> for interactions<br><strong>Mermaid</strong> for diagrams · <strong>Pagefind</strong> for search<br><strong>Java, JUnit &amp; Maven</strong> for runnable exercises</p>

<h2 align="center">Run locally</h2>

<p align="center">Node.js <strong>22.19+ within 22.x</strong> and npm. Pinned version: <strong>22.22.0</strong>.</p>

```bash
npm ci
npm run dev
```

<p align="center">Local development: <a href="http://localhost:4321/java-deep-dive/">localhost:4321/java-deep-dive/</a></p>

<p align="center">Java examples require a JDK; Maven exercises also require Maven.<br>For a production preview with search:</p>

```bash
npm run build
npm run preview --workspace website
```

<h2 align="center">Developer reference</h2>

<details>
<summary><strong>Source layout</strong></summary>

```text
website/src/
  content/lessons/    Lessons and prerequisites
  components/        Interactive walkthroughs and code displays
  data/              Curriculum metadata and execution traces
  layouts/           Shared reading layout
  styles/            Themes and responsive styles
java-examples/
  src/main/java/     Baseline runnable programs
  src/test/java/     Matching JUnit tests
  modern/            Examples using newer Java features
  projects/          Complete project exercises
```

</details>

<details>
<summary><strong>Development commands</strong></summary>

| Command | Purpose |
| :--- | :--- |
| `npm test` | Website behavior tests |
| `npm run check` | Astro and TypeScript checks |
| `npm run build` | Generate pages, downloads and search; check local links |
| `npm run test:java` | Compile baseline Java, run JUnit and compare program outputs |
| `npm run test:modern` | Compile and run newer Java examples |

Baseline Java verification targets Java 8. To select a JDK for the modern examples:

```bash
JDD_JAVA25_HOME="/path/to/jdk-25" npm run test:modern
```

The modern verifier uses JDK 25, with preview flags for examples that require them. JUnit dependencies are cached locally. Complete Maven exercises include their own build files and commands. Build helpers live in `scripts/`, website tests in `tests/`, and automation in `.github/workflows/`.

</details>

<details>
<summary><strong>Contributing lessons</strong></summary>

Read the [course plan](docs/course-plan.md), [curriculum](docs/curriculum.md) and [design specification](docs/design.md) before editing.

Lesson MDX belongs in `website/src/content/lessons/`. Frontmatter defines titles, grouping, sequence, prerequisites and Java requirements. Keep canonical runnable source in `java-examples/`; displayed code and downloads derive from those files.

Introduce ordinary use before specialized mechanisms. Explain outputs, failure cases and implementation tradeoffs, and include worked exercises with relevant references. Preserve existing lesson URLs and fragment targets, update prerequisites when needed, and check affected examples and rendered lessons after a change.

</details>
