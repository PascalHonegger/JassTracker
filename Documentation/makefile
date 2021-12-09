# Make configuration to build PDFs from LaTeX sources and Diffs of these.
# (c) Farhad Mehta 2021

# Tested with the TeX Live 2021 snapshot as of 09.12.2021
# Docker image: registry.gitlab.com/islandoftex/images/texlive:TL2021-2021-12-05-04-05

gitDescription:
	git describe --exclude "latexdiff-base" --always --long --dirty --broken > gitDescription.tmp

doc: gitDescription
	# Note: pdflatex does not work well when the out and aux directories are different
	latexmk -outdir=../out -cd -interaction=batchmode -pdf src/main.tex

proposal: gitDescription
	# Note: pdflatex does not work well when the out and aux directories are different
	latexmk -outdir=../out -cd -interaction=batchmode -pdf src/project-proposal-standalone.tex

gitDiffDescription:
	# Note the double $ are needed to escape the $ within a makefile.
	# Use only one $ when running the following command in the shell.
	printf "%s (diff with %s)" $$(git describe --exclude "latexdiff-base" --always --long --dirty --broken) $$(git describe  --exclude "latexdiff-base" --always --long latexdiff-base) > gitDescription.tmp

doc_diff: gitDiffDescription
	rm -rf latexdiff-base
	mkdir latexdiff-base
	git archive --format=tar latexdiff-base | (cd latexdiff-base && tar xf -)
	# Note: Change to the base directory before running latexdiff. Otherwise the .sty files cannot be found.
	# Note: In the makefile, each command is executed in a sub-process shell. Therefore, one needs to chain `cd` using `&&` or `;` into one command.
	# Note: The `-` prefix instructs make to ignore the exit code of the command. The exit code of the following command is ignored since latexdiff and pdflatex do not work well together. Pdflatex generates an error, but is still able to generate the document. 
	-cd latexdiff-base/src && \
		latexdiff -V --flatten main.tex  ../../src/main.tex > diff.tex && \
		latexmk -outdir=../../out -cd -interaction=batchmode -pdf -f diff.tex

all: proposal doc doc_diff

clean:
	rm -rf out
	rm -rf latexdiff-base
	rm gitDescription.tmp

# Can be used locally as e.g. make docker-doc or make docker-proposal, ... 
docker-%:
	docker run --rm -v "$(CURDIR)/../:/opt/ws" -w "/opt/ws/Documentation" registry.gitlab.com/islandoftex/images/texlive:latest make $*
#	docker run --rm -v "$(CURDIR)/../:/opt/ws" -w "/opt/ws/Documentation" registry.gitlab.com/islandoftex/images/texlive:TL2021-2021-04-11-04-09 make $*

