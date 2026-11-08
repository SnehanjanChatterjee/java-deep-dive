<div align="center">

# ☕ Java Deep Dive

### Understand Java, from the first line to the runtime.

Learn the fundamentals, explore how things work, and build the confidence to explain your code.

**Concepts · Runnable examples · Visual walkthroughs · Interview practice**

[Open the course](https://snehanjanchatterjee.github.io/java-deep-dive/) · [Curriculum](docs/curriculum.md) · [Quick start](#quick-start) · [Inside the project](#inside-the-project)

</div>

---

## Technology stack

<p align="center">
  <a href="https://astro.build"><img src="https://img.shields.io/badge/Astro-BC52EE?style=for-the-badge&amp;logo=astro&amp;logoColor=white" alt="Astro" /></a>
  <a href="https://www.typescriptlang.org"><img src="https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&amp;logo=typescript&amp;logoColor=white" alt="TypeScript" /></a>
  <a href="https://react.dev"><img src="https://img.shields.io/badge/React-20232A?style=for-the-badge&amp;logo=react&amp;logoColor=61DAFB" alt="React" /></a>
  <a href="https://mdxjs.com"><img src="https://img.shields.io/badge/MDX-F9AC00?style=for-the-badge&amp;logo=mdx&amp;logoColor=black" alt="MDX" /></a>
</p>
<p align="center">
  <a href="https://mermaid.js.org"><img src="https://img.shields.io/badge/Mermaid-FF3670?style=for-the-badge&amp;logo=mermaid&amp;logoColor=white" alt="Mermaid" /></a>
  <a href="https://pagefind.app"><img src="https://img.shields.io/badge/Pagefind-334155?style=for-the-badge" alt="Pagefind full-text search" /></a>
  <a href="https://dev.java"><img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&amp;logo=openjdk&amp;logoColor=white" alt="Java" /></a>
  <a href="https://junit.org/junit4"><img src="https://img.shields.io/badge/JUnit-25A162?style=for-the-badge" alt="JUnit" /></a>
  <a href="https://maven.apache.org"><img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&amp;logo=apachemaven&amp;logoColor=white" alt="Apache Maven" /></a>
</p>

**Astro + MDX** power the static lesson pages. **React** supplies focused interactive walkthroughs, **Mermaid** illustrates relationships and algorithms, and **Pagefind** provides full-text search. Downloadable **Java** examples include **JUnit** tests and complete **Maven** projects where appropriate.

The website needs no application backend or database. Java examples run locally; personal notes, bookmarks and progress stay in the browser.

## Learn it. Trace it. Build it.

Java Deep Dive is a beginner-to-advanced course for understanding the language, exploring its runtime and preparing for technical interviews. Lessons introduce the idea, develop an ordinary example, and then explore implementation details, edge cases and tradeoffs.

- **Learn the concept** with clear definitions and worked examples.
- **See the execution** through diagrams and interactive, step-by-step visualizations.
- **Build it yourself** with complete downloadable Java programs and implementation exercises.
- **Explain the reasoning** with prediction questions, debugging scenarios and interview follow-ups.

Follow the course timeline or search for a specific topic. Switch between light and dark themes, bookmark lessons, and keep personal notes in your browser. No account is required.

## Explore Java

**Language & objects → Generics & collections → Functional Java → Concurrency → JVM internals**

Start with first programs and ordinary examples. Build toward collection implementations, concurrent algorithms, runtime diagnostics and advanced platform APIs. Standard libraries, testing and engineering practices connect the concepts to practical development.

JVM foundations appear early; advanced runtime mechanisms build on them later. Examples state their Java requirements, including newer features and preview APIs.

[Browse the complete curriculum →](docs/curriculum.md)

## Quick start

Use **Node.js 22.19+ within the 22.x release line** and npm. The project pins Node.js **22.22.0**.

```bash
npm ci
npm run dev
```

Open **[localhost:4321/java-deep-dive/](http://localhost:4321/java-deep-dive/)**.

For a production preview with the generated search index:

```bash
npm run build
npm run preview --workspace website
```

Running the Java programs requires a JDK. Each example lists its version and execution instructions; Maven projects additionally require Maven.

## Inside the project

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
