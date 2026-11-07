# Project guidelines

## Architecture

Java Deep Dive is a static Astro site using TypeScript, MDX, React islands, Mermaid and Pagefind. The default base path is `/java-deep-dive/`. Keep routes, imports, search assets and downloads base-aware. Java execution occurs locally through downloadable examples; the website has no Java execution backend.

## Course authoring

- Teach the problem, vocabulary and ordinary case before internals, edge cases and release differences.
- Develop beginner material through small complete programs, with explicit execution traces and explained outputs.
- Use focused topic-specific headings and accurate prerequisites. Do not use untaught coordination, collection or reflection APIs merely to shorten an introductory example.
- Preserve useful advanced material when splitting a lesson, and remove repeated treatments that do not advance the learning sequence.
- Write lessons and project documentation for readers without access to development discussions.
- Label minimum Java requirements and preview APIs. Distinguish teaching implementations from production JDK implementations.
- Keep source references beside the claims they support. Avoid unsupported performance, coverage or completeness claims.

## Canonical source and examples

MDX in `website/src/content/lessons/` is the lesson source. Java in `java-examples/` is the canonical code used for rendering, downloads and checks. Primary coding exercises should use one self-contained class with nested helpers where appropriate; supporting tests belong to the matching source class.

Use explicit imports, clear failure behavior, applicable final declarations and concise Javadoc. Educational console output is appropriate in runnable examples. New JUnit method names start with `test`. Project exercises include the POMs, descriptors and resources needed to reproduce them.

## Design and interaction

Keep one shared module/topic timeline on course and lesson pages. Navigation, headings, search and previous/next links use the same metadata and reading order. The lesson remains visually primary. Sidebar collapse controls belong in its shell.

Retain the compact reading layout, white/emerald light theme and charcoal/blue dark theme. Use readable code, outputs and diagrams. Tables and drawings scroll locally rather than widening the page. Instructional animations need clear state labels, keyboard controls, static explanations and reduced-motion behavior.

Preserve URLs and aliases without duplicating document IDs. Generate section links from actual headings. Visiting a lesson must not imply completion.

## Verification

Run the checks relevant to the change: Node tests, Astro check/build, baseline or modern Java verification, and rendered desktop/mobile inspection. Check teaching outputs against the canonical program. A successful build does not establish instructional quality or accessibility compliance.

Inspect the final diff and preserve unrelated work. Commit and publish only when requested. Use ordinary Git from the correct repository and repository-local contributor configuration. Do not modify shared Git credentials, global configuration or other projects.

## Public-file hygiene

Never put personal email addresses, real local Git identities, machine paths, credentials, private configuration or development-conversation history in public documentation, lessons, tests or CI. Configuration examples use clearly generic placeholders. Keep machine-specific operational notes outside tracked project files. Preserve third-party legal notices.
