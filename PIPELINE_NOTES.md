# Pipeline notes

## Requirements from the image
- Workflow rules: no pipeline on push; run on merge request.
- Allow tag-based execution to limit scenarios.
- Allow selecting environment (DES, QA, STG).
- Stages: dependencies download, test execution, report publish, close.
- Report artifacts with max retention days.

## Extracted from bank GitLab CI
- Workflow rules to block push and allow MR/manual.
- Variables with options for tag and environment.
- .m2/repository cache and dependency stage.
- artifacts expire_in for reports.

## Environment mapping used in CI
- ENVIRONMENT=DES maps to -DEnvironment=DEV.
- ENVIRONMENT=QA maps to -DEnvironment=QA1.
- ENVIRONMENT=STG maps to -DEnvironment=UAT.

## Research results
### Edge on Linux
- Microsoft Edge is available for Linux (deb/rpm).
- It requires the browser binary; WebDriverManager only downloads the driver.
- In containers, install microsoft-edge-stable from the Microsoft repo or use a custom image.
- Headless is supported; use chromium headless flags for Edge.
- If Edge is needed in CI, extend the base image and set BROWSER=EDGE.

### Font Liberation for headless
- fonts-liberation provides common fonts to avoid missing glyphs and layout shifts.
- Recommended for consistent rendering in headless CI runs.
- The pipeline installs fonts-liberation in the test job; a custom image can bake this in.

### Podman
- Podman is a container runtime, useful when Docker is not allowed.
- Not required for this pipeline because tests run inside the GitLab runner container.
- Only needed if the runner is configured for Podman or if you build/publish images.

### Deploy tokens
- Use GitLab deploy tokens with read_package_registry for Maven package access.
- Do not commit tokens; configure masked/protected CI variables.
- .m2/settings.xml expects:
  - MAVEN_USERNAME_COMMONS_DATA / MAVEN_PASSWORD_COMMONS_DATA
  - MAVEN_USERNAME_CONNEXUSDB / MAVEN_PASSWORD_CONNEXUSDB
  - MAVEN_USERNAME_CONNEXUSDB_GUI / MAVEN_PASSWORD_CONNEXUSDB_GUI
  - MAVEN_USERNAME_CONNEXUSDB_PLUGIN / MAVEN_PASSWORD_CONNEXUSDB_PLUGIN

## .m2 folder setup
- Maven local repository is set to .m2/repository and cached between jobs.
- .m2/repository is gitignored to avoid committing binaries.
