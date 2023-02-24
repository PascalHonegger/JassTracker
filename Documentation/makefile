# Make configuration to build PDFs from LaTeX sources and Diffs of these.
# (c) Farhad Mehta 2021

# Tested with the TeX Live 2021 snapshot as of 09.12.2021
# Docker image: registry.gitlab.com/islandoftex/images/texlive:TL2021-2021-12-05-04-05

doc:
	# Note: pdflatex does not work well when the out and aux directories are different
	latexmk -outdir=../out -cd -interaction=batchmode -pdf src/main.tex

all: doc

clean:
	rm -rf out

# Can be used locally as e.g. make docker-doc ...
docker-%:
	docker run --rm -v "$(CURDIR)/../:/opt/ws" -w "/opt/ws/Documentation" registry.gitlab.com/islandoftex/images/texlive:latest make $*
#	docker run --rm -v "$(CURDIR)/../:/opt/ws" -w "/opt/ws/Documentation" registry.gitlab.com/islandoftex/images/texlive:TL2021-2021-04-11-04-09 make $*
