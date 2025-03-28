# JassTracker
[![JassTracker Backend](https://github.com/PascalHonegger/JassTracker/actions/workflows/backend.yml/badge.svg)](https://github.com/PascalHonegger/JassTracker/actions/workflows/backend.yml)
[![JassTracker Frontend](https://github.com/PascalHonegger/JassTracker/actions/workflows/frontend.yml/badge.svg)](https://github.com/PascalHonegger/JassTracker/actions/workflows/frontend.yml)
[![JassTracker Documentation](https://github.com/PascalHonegger/JassTracker/actions/workflows/documentation.yml/badge.svg)](https://github.com/PascalHonegger/JassTracker/actions/workflows/documentation.yml)
[![JassTracker Operations](https://github.com/PascalHonegger/JassTracker/actions/workflows/operations.yml/badge.svg)](https://github.com/PascalHonegger/JassTracker/actions/workflows/operations.yml)

## Screenshots
![Jasstisch creation](Documentation/src/resources/screenshots/table-creation.png)
![Jasstisch table](Documentation/src/resources/screenshots/tables.png)
![Jasstisch scoreboard](Documentation/src/resources/screenshots/scoreboard.png)
![Jasstisch statistics](Documentation/src/resources/screenshots/statistics.png)

## Development

The easiest way to start all components is using the configured IntelliJ run configuration.
There exists one configuration for `Frontend`, `Backend` and `Postgres Dev`, or you can start all using the `All Dev` configuration.
You can also manually start all components:

### Frontend
```shell
cd Frontend
# Make sure you've set up PNPM (https://pnpm.io/installation#using-corepack)
# Install NPM dependencies
pnpm i
# Start dev server
pnpm dev
# Build for prod
pnpm build
# Run linter
pnpm lint
```

### Backend
```shell
# Start dev server
./gradlew run
# Run tests
./gradlew check
```

## Deployment / Production

```shell
# See docker-compose.yml for prod template
docker compose up -d
```

## Documentation

This repository is configured to be usable within VS Code, using the LaTeX Workshop plugin.

The included makefile can be used via the `make doc` command within the `Documentation` directory to build the pdf version of the documentation. The `make doc_diff` command can be used to generate the diff pdf via latexdiff. Make sure that you tag the base version you want to use for the diff with the `latexdiff-base` tag in the repository. Take a look at `Documentation/makefile` for more information.

The repository has also been configured to automatically build `Documentaton/out/main.pdf` and `Documentation/out/diff.pdf` upon every push onto gitlab. These can be found within `CI/CD -> Jobs -> Artifacts`. Take a look at `.gitlab-ci.yml` for more information.
