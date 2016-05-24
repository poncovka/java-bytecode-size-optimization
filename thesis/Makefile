# makefile pro preklad LaTeX verze Bc. prace
# (c) 2008 Michal Bidlo
# E-mail: bidlom AT fit vutbr cz
#===========================================
PROJ=project
PRES=presentation
CONT=content
BIB=literature

all: clean pdf counting

final: wave all

pdf: $(PROJ).pdf

#$(PROJ).pdf: $(PROJ).ps
#	dvipdf $(PROJ)

$(PROJ).ps: $(PROJ).dvi
	dvips $(PROJ)

$(PROJ).dvi: $(PROJ).tex $(BIB).bib
	latex $(PROJ)
	bibtex $(PROJ)
	latex $(PROJ)
	latex $(PROJ)

$(PROJ).pdf: clean
	pdflatex $(PROJ)
	bibtex $(PROJ)
	pdflatex $(PROJ)
	pdflatex $(PROJ)

wave: clean
	vlna -l -m -n *.tex $(BIB).bib $(CONT)/*.tex

clean:
	rm -f *.dvi *.log $(PROJ).blg $(PROJ).bbl $(PROJ).toc *.aux $(PROJ).out $(PROJ).lof $(PROJ).ptc dp-xponco00.tar.gz
	rm -f $(PROJ).pdf $(PROJ).ps
	rm -f *~ ./$(CONT)/*~

pack:
	tar czvf dp-xponco00.tar.gz *.tex *.bib *.bst ./$(CONT)/*.tex ./fig/* ./cls/* assignment.pdf cover.pdf $(PROJ).pdf Makefile

counting: $(PROJ).pdf
	bash counter.sh

