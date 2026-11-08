# Java Deep Dive design specification

The site is a minimal, modern reading environment. Course content is primary; navigation and reader tools support learning without becoming separate product flows. This specification preserves the established design and describes its implemented behavior. Screenshots document implemented states, not approval of every subsequent variation.

## Shared reading shell

Use one course module/topic tree on the overview and every lesson. Opening a topic changes the article and current highlight while retaining the same hierarchy. Module headers expand their topic lists. Group labels describe related lessons; lesson links navigate.

The header contains the brand/home link, lesson search, theme control and mobile navigation opener. Desktop sidebar collapse belongs inside the sidebar shell, leaving a narrow reveal rail. Do not put a desktop Hide sidebar control in the page header.

The article starts with compact context, version, optional prerequisites/On this page disclosures, a clear topic title and a specific learning summary. A permanent right outline is not part of the current reading layout. Body headings and outlines derive from actual content, including the shared HashMap metadata.

## Timeline and link affordances

- Retain numbered module circles and visible restrained connectors. Calculate marker centers and line position from the same geometry variables; the line passes through each circle's center.
- Distinguish current location from expanded navigation. An opened module is not automatically the current article's module.
- Use one current-topic highlight and aria-current. Markers represent sequence/location, not completed learning.
- Lesson links have permanent text underlines and direction indicators. Provide clear pointer, hover and keyboard-focus feedback.
- Static group captions use small secondary text, a divider and lesson count. They must not resemble buttons or use the same appearance as lesson links.
- Keep course order, tree order and previous/next links consistent with prerequisites. Avoid extra global menus for overlapping learning paths, practice, progress and resources.

## Palette

| Role | Light | Dark |
| --- | --- | --- |
| Page | White | Neutral graphite, current token `#111315` |
| Navigation/elevated surface | Off-white | Graphite, current token `#202328` |
| Main text | Dark charcoal | Light neutral, current token `#e1e6ee` |
| Accent | Emerald, current token `#157351` | Blue, current token `#8bb8ff` |
| Selected topic | Restrained emerald-tinted surface and accent | Distinct blue fill with readable light text |
| Code | Light source surface | Separate dark surface with high-contrast syntax |

Dark mode uses charcoal/blue, not green-tinted surfaces or green accents. The legacy CSS variable name `--green` is an accent token and resolves to blue in dark mode. Read rendered colors rather than assuming the variable name describes its dark value.

Keep text, secondary text, controls, focus, selected fill and on-selected text as distinct roles. Meaningful boundaries and diagrams need contrast too. Do not infer whole-site accessibility compliance from a few token measurements.

## Layout and typography

Current desktop defaults use a 56px header, approximately 256px sidebar and an article up to 1080px wide. Intermediate widths use the existing narrower sidebar rules. At the mobile breakpoint (800px), the article uses the viewport and navigation becomes an overlay. Use the current CSS as the exact token/breakpoint source; these measurements describe the design rather than authorizing a replacement layout.

Use DM Sans for interface/body text, clear heading hierarchy and a monospace source font. Current prose is approximately 15px with 1.8 line height. The title is compact rather than a large hero. Long prose identifiers and method chains wrap. Code preserves meaningful whitespace and scrolls locally; do not shrink source or diagrams into unreadable thumbnails to fit a narrow screen.

## Diagrams and instructional animation

Define the diagram's concepts and labels before showing the whole-system map. Use captions and accessible text equivalents. Explain arbitrary object labels, units, arrows, modeled state and omitted implementation detail. Keep output values consistent with canonical code.

Animations are deliberate learning tools: begin paused, provide Previous/Next/Reset and optional Play/Pause, synchronize code highlighting with explanation, and keep state labels legible outside moving markers. Respect reduced-motion preferences. A browser simulation must be identifiable as a conceptual execution, not a live JVM measurement.

Use vertical layouts when useful on narrow screens. Wide drawings have a bounded keyboard-accessible local scroll region and a visible hint; captions stay within the reading column. A drawing may exceed its viewport, but must not widen the page. Controls/explanation remain accessible while examining a long model.

## Code, tables and reader controls

Complete source, derived excerpts and output are distinct. Give complete examples usable copy/download actions; label fragments accurately. Downloads and displayed source come from the same canonical file. Preserve output newlines, readable insets and clear action boundaries.

Tables use local scrolling when necessary and keep API names readable. Disclosure controls must be recognizable, with generous text/icon clearance. Dropdowns share SelectControl or the equivalent static wrapper: one decorative caret, intentional size/centering and sufficient right padding. Verify actual normal, selected, long-value and disabled rendering.

Keep study tools compact: bookmark/progress/notes below the lesson, with the notes editor disclosed on demand. Visiting a page never marks it complete. Device-local state and transfer behavior must be described accurately without taking attention from the course.

## Keyboard and mobile behavior

The mobile drawer has an explicit close control, backdrop dismissal, Escape behavior, focus containment and focus return. The background is inert while the drawer is open. Desktop collapse and mobile drawer state must transfer correctly at breakpoints.

Keyboard skip-to-content focuses the main landmark and positions it below the sticky header. Section jumps likewise leave the target readable. Retain visible focus for links and controls; do not rely on color alone to distinguish actions.

## Verification and preservation

For changed UI, inspect actual desktop and mobile rendering in both relevant themes. Check long titles/identifiers, tables, code headers, outputs, dropdown carets, diagram clipping, moving labels, drawer/collapse behavior and current-topic identity. Exercise the exact reported defect; broad checks do not replace reproducing it.

Keep the original approved references and later implementation evidence. Do not change the overall shell, palette, framework or hierarchy incidentally while editing content. A substantive redesign is a separate decision. Course additions preserve useful existing prose, examples and visuals; writing improvements do not authorize deleting internals or complete implementation exercises.
