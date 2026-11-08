# Website deployment

GitHub Pages serves the static site at https://SnehanjanChatterjee.github.io/java-deep-dive/.

## Setup

In repository Settings → Pages, select GitHub Actions as the publishing source. The Astro site URL and /java-deep-dive base are configured in website/astro.config.mjs. Run builds from the repository root; the output directory is website/dist. No hosting adapter, personal token secret or application backend is required.

## Publication

The Validate and deploy course workflow runs website, Java 8 and modern-Java checks. Main pushes and manual runs on main package the built site and deploy only after all three jobs succeed. Pull requests run checks without deploying. Pages publishes the generated artifact; generated files are not committed to a separate branch. Deployment jobs are serialized, and only the deploy job receives pages:write and id-token:write permissions.

## Verification and recovery

Check the Actions run and deployment URL. Verify the homepage, direct lesson URLs, search, source downloads, diagrams, guided interactions and mobile/light/dark reading. Browser-local learning data is scoped to the hosted origin and does not automatically transfer from localhost.

If validation fails, inspect its logs; deployment is skipped. If deployment fails, check the Pages source, github-pages environment and deployment logs. Rerun the workflow on main after repairing configuration. To roll back content, revert the relevant source commit and push; the ordinary validation/deployment pipeline publishes that revision. GitHub Pages availability for private source repositories depends on the account plan; changing repository visibility requires checking hosting eligibility again.

## References

- [GitHub Pages custom workflows](https://docs.github.com/en/pages/getting-started-with-github-pages/using-custom-workflows-with-github-pages)
- [Publishing source](https://docs.github.com/en/pages/getting-started-with-github-pages/configuring-a-publishing-source-for-your-github-pages-site)
