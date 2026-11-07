# Java Deep Dive

An interactive Java study course covering language fundamentals, object-oriented programming, collections, concurrency and JVM internals. Lessons combine explanations, runnable Java examples, implementation walkthroughs, diagrams and exercises.

## Technology stack

| Area | Technology |
| --- | --- |
| Static website | Astro and TypeScript |
| Course content | Markdown and MDX |
| Interactive lessons | React and CSS animations |
| Diagrams | Mermaid and custom state visualizations |
| Full-text search | Pagefind |
| Java examples | Java source, JUnit and Maven projects |
| Validation | Node test runner, Astro checks and GitHub Actions |

The website builds to static files. Reading, search and instructional visualizations run without an application server or database. Notes, bookmarks and progress are stored in the browser.

## Run locally

Use Node.js 22.19 or newer within the 22.x release line. The project pins Node.js 22.22.0.

From the project root:

```bash
npm ci
npm run dev
```

Open [the local course](http://localhost:4321/java-deep-dive/).

For a production preview, including the generated search index:

```bash
npm run build
npm run preview --workspace website
```

## Project structure

```text
website/
  src/content/lessons/    Course lessons and prerequisites
  src/components/        Code displays, diagrams and interactive walkthroughs
  src/data/              Curriculum metadata and trace definitions
  src/layouts/           Shared reading layout
  src/styles/            Light/dark themes and responsive styles
java-examples/
  src/main/java/         Baseline runnable examples
  src/test/java/         Matching JUnit tests
  modern/                Examples with newer Java requirements
  projects/              Complete build, module and compatibility exercises
scripts/                 Build preparation and example verification
tests/                   Website behavior tests
.github/workflows/       Automated validation
```

## Course content

The course develops first programs, variables, control flow, methods, arrays and objects before exceptions, generics, collections and functional programming. Further modules cover standard libraries, threading, concurrent APIs, JVM execution, diagnostics, platform APIs and engineering practices.

Topic navigation and previous/next links follow the same prerequisite sequence. Java version requirements appear beside the relevant material. Instructional models support deliberate stepping and optional playback; complete Java files can be downloaded and run locally.

## Validation commands

| Command | Purpose |
| --- | --- |
| `npm test` | Run website behavior tests |
| `npm run check` | Check Astro and TypeScript |
| `npm run build` | Generate downloads, pages and search; validate routes and section links |
| `npm run test:java` | Compile baseline examples, run JUnit and check documented outputs |
| `npm run test:modern` | Verify examples requiring a newer JDK |

Baseline examples support Java 8+. The modern-example verifier uses JDK 25, with preview flags for the specific examples that require them:

```bash
JDD_JAVA25_HOME="/path/to/jdk-25" npm run test:modern
```

The example verifier caches pinned JUnit dependencies locally. Complete Maven exercises include their own project files and commands.

## Authoring lessons

Edit the canonical MDX files in `website/src/content/lessons/`. Each lesson declares its title, module, topic group, reading order, prerequisites and Java requirements.

Keep Java examples in `java-examples/`. The build reads code and generates downloads from those same source files. Use meaningful section headings, explain the ordinary case before advanced mechanisms, and include worked examples and exercise answers. Update prerequisites and source references when changing the learning sequence.

Preserve existing lesson URLs and fragment targets when reorganizing content. Verify relevant examples and inspect the rendered desktop/mobile lesson after changing its code, diagrams or interactions.
