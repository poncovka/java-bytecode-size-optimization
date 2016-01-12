# makefile pro preklad LaTeX verze Bc. prace
# (c) 2008 Michal Bidlo
# E-mail: bidlom AT fit vutbr cz
#===========================================
PROJECT=project
PRESENTATION=presentation
CONTENT=content
REF=literature

all: clean $(PROJECT).ps

final: clean $(PROJECT).pdf counting

$(PROJECT).pdf: $(PROJECT).ps
	dvipdf $(PROJECT)

$(PROJECT).ps: $(PROJECT).dvi
	dvips $(PROJECT)

$(PROJECT).dvi: $(PROJECT).tex $(REF).bib
	latex $(PROJECT)
	bibtex $(PROJECT)
	latex $(PROJECT)
	latex $(PROJECT)

pdflatex: clean
	pdflatex $(PROJECT)
	bibtex $(PROJECT)
	pdflatex $(PROJECT)
	pdflatex $(PROJECT)

cover:
#	latex desky
#	dvips desky
#	dvipdf desky
	pdflatex desky

presentation:
	latex $(PRESENTATION)
	dvipdf $(PRESENTATION)
#	dvips -t a4 $(PRESENTATION).dvi
#	ps2pdf -sPAPERSIZE=a4 $(PRESENTATION).ps

counting: $(PROJECT).pdf
	bash counter.sh

wave: clean
	vlna -l -m -n *.tex $(REF).bib $(CONTENT)/*.tex

clean:
	rm -f *.dvi *.log $(PROJECT).blg $(PROJECT).bbl $(PROJECT).toc *.aux $(PROJECT).out $(PROJECT).lof dp-xponco00.tar.gz
	rm -f $(PROJECT).pdf $(PROJECT).ps
	rm -f *~ obsah/*~

pack:
	tar czvf dp-xponco00.tar.gz *.tex *.bib *.bst ./fig/* ./cls/* Makefile Changelog

