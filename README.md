The aim of this repository is to provide an easy to use LaTeX template for documenting software projects. 

# Usage

Fork this repository before working on a concrete project documentation.

This repository is configured to be usable within VS Code, using the LaTeX Workshop plugin.

This repository is also configured to be usable within Eclipse, via the TeXclipse plugin. Clone locally first and the import into Eclipse via `Import Project -> Git -> Projects from Git -> Existing Local Repository -> Import Existing Eclipse Projects`.

Alternatively, you may use your favourite LaTeX editor.

The included makefile can be used via the `make doc` command within the `Documentation` directory to build the pdf version of the documentation. The `make doc_diff` command can be used to generate the diff pdf via latexdiff. Make sure that you tag the base version you want to use for the diff with the `latexdiff-base` tag in the repository. Take a look at `Documentation/makefile` for more information.

The repository has also been configured to automatically build `Documentaton/out/main.pdf` and `Documentation/out/diff.pdf` upon every push onto gitlab. These can be found within `CI/CD -> Jobs -> Artifacts`. Take a look at `.gitlab-ci.yml` for more information.

You may use the `Documentation/releases` directory to store and commit individual versions of your documentation and diffs for convenience.
