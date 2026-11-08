# Java Deep Dive

## 🌐 [Open the live website →](https://snehanjanchatterjee.github.io/java-deep-dive/)

A beginner-to-advanced Java course with runnable examples, diagrams, interactive walkthroughs and interview exercises.

## What’s inside

- **Language and objects:** fundamentals, classes, interfaces, equality and immutability.
- **Collections and functional Java:** generics, data structures, lambdas and streams.
- **Concurrency:** threads, synchronization, executors and modern threading.
- **JVM and diagnostics:** architecture, memory, garbage collection and JIT compilation.

Learn concepts before internals, trace complete examples, and practise implementation and debugging. Search lessons, switch themes, and keep notes and bookmarks in your browser. Java requirements are labelled per example.

[Full curriculum](docs/curriculum.md)

## Technology stack

<p align="left">
  <a href="https://astro.build"><img src="https://img.shields.io/badge/Astro-BC52EE?style=flat-square&amp;logo=astro&amp;logoColor=white" alt="Astro" /></a>
  <a href="https://www.typescriptlang.org"><img src="https://img.shields.io/badge/TypeScript-3178C6?style=flat-square&amp;logo=typescript&amp;logoColor=white" alt="TypeScript" /></a>
  <a href="https://react.dev"><img src="https://img.shields.io/badge/React-20232A?style=flat-square&amp;logo=react&amp;logoColor=61DAFB" alt="React" /></a>
  <a href="https://mdxjs.com"><img src="https://img.shields.io/badge/MDX-F9AC00?style=flat-square&amp;logo=mdx&amp;logoColor=black" alt="MDX" /></a>
</p>
<p align="left">
  <a href="https://mermaid.js.org"><img src="https://img.shields.io/badge/Mermaid-FF3670?style=flat-square&amp;logo=mermaid&amp;logoColor=white" alt="Mermaid" /></a>
  <a href="https://pagefind.app"><img src="https://img.shields.io/badge/Pagefind-334155?style=flat-square" alt="Pagefind full-text search" /></a>
  <a href="https://dev.java"><img src="https://img.shields.io/badge/Java-ED8B00?style=flat-square&amp;logo=openjdk&amp;logoColor=white" alt="Java" /></a>
  <a href="https://junit.org/junit4"><img src="https://img.shields.io/badge/JUnit-25A162?style=flat-square" alt="JUnit" /></a>
  <a href="https://maven.apache.org"><img src="https://img.shields.io/badge/Maven-C71A36?style=flat-square&amp;logo=apachemaven&amp;logoColor=white" alt="Apache Maven" /></a>
</p>

Static pages with **Astro + MDX**, interactive explanations with **React**, diagrams with **Mermaid**, and search with **Pagefind**. Java exercises include JUnit tests and Maven projects. No application backend or database is required.

## Run locally

Use Node.js **22.19+ within 22.x** and npm; the project pins **22.22.0**.

```bash
npm ci
npm run dev
```

Local development: [localhost:4321/java-deep-dive/](http://localhost:4321/java-deep-dive/).

A JDK is needed to run Java examples; Maven is needed for Maven exercises. For a production preview with search:

```bash
npm run build
npm run preview --workspace website
```

## Developer reference

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
