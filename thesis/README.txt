Changelog původní šablony z roku 2009:
- do cls/fitthesis.cls pridany "SP" (semestralni projekt/term project)
- fontenc (aby se dalo copy/paste kopirovat z prace)
- formatovani citaci podle csn (viz. http://www.fit.vutbr.cz/~martinek/latex/czechiso.html.cs.iso-8859-2 )
- listoffigures, listoftables, appendix
- Rok pomoci \yyyydate
- parametr "print" pro \documentclass[print]{...}, ktery ve vyslednem vypne aktivni odkazy v pdf (na papire
  cerveny obsah vypada hodne divne)
- prevedeno do utf-8 (v linuxu je to default vsech editoru, windowsaci si nejak poradi)
- do komentare bylo pridane \listoffigures a \listoftables

sablona2016 má oproti původní šabloně z roku 2009:
- dokončenu změnu kódování na UTF8,
- doplněné vkládání zadání,
- úpravy, aby z PDF bylo možné kopírovat text,
- doplněnou variantu s černými odkazy pro tisk,
- vypnuté automatické nahrazování uvozovek, které dělá problémy v ukázkách kódu (pro české uvozovky lze použít \uv{}),
- přejmenované seznamy tabulek, obrázků a literatury,
- přílohy se jmenuji "Přílohy" a v nadpisu mají "Příloha"
- doplněn seznamem příloh,
- vylepšený seznam literatury
- literatura je v obsahu
- "školitel" pro disertační práci
- odstraněné desky (nově v IS FIT)
- vylepšené přepínání do angličtiny
- opravenou bibliografickou citaci
- podporu slovenštiny

U anglické či slovenské práce je nutné přeložit prohlášení, poděkování apod. do angličtiny či slovenštiny.

Šablona obsahuje i experimentální funkce pro implementaci JVS VUT. JVS bude nařízen až od příštího roku a stále 
se dolaďuje. Tyto funkce jsou v šabloně vyřazeny a je velmi důrazně doporučeno je nevyužívat. Odkomentování 
a zprovoznění je na vlastní nebezpečí. V takovém případě je na titulní list doporučeno využít tučné písmo Open Sans 
(šedivou barvu nepoužívat - zakázáno) a pro zbytek textu nelze nic doporučit (jakákoliv varianta je z určitého 
pohledu nevhodná). Barevné logo se nesmí tisknout černobíle. Veškerá využitá písma z JVS musí být obsažena 
ve výstupním pdf (bude li výstup příkazu "pdffonts projekt.pdf" obsahovat "no", je to hrubé porušení pravidel 
a oponent jej může nezanedbatelně postihovat).

alternativaBib obsahuje alternativní styl pro BibTeX od Radima Loskota a příklad jeho použití a alternativní styl pro BibTeX od Radka Pyšného (http://www.fit.vutbr.cz/study/DP/BP.php?id=7848).

Nascanované zadání je potřeba pojmenovat zadani.pdf - bude automaticky vloženo na správné místo.

Pro odevzdání do WISu má být v projekt.tex odkomentovaný řádek:
\documentclass[zadani]{fitthesis}
pro tisk řádek
\documentclass[zadani,print]{fitthesis}

Pozor na zradu s číslováním stránek!!!
- Pokud má obsah 2 strany a na 2. jsou jen "Přílohy" a "Seznam příloh" (ale žádná příloha tam není), z nějakého důvodu se posune číslování stránek o 1 (obsah nesedí).
- Stejný efekt má, když je na 2. či 3. stránce obsahu jen "Literatura"
- Řešením je mít tolik řádků obahu, aby toho na 2. stránce bylo víc nebo nic, nebo přímo nastavit počítadlo.
- Možná toho problému lze dosáhnout i jinak - vždy je před odevzdáním lepší překontrolovat číslování stran!

Nezapomeňte, že vlna neřeší všechny nezalomitelné mezery. Vždy je třeba manuální kontrola, zda na konci řádku nezůstalo něco nevhodného - viz http://prirucka.ujc.cas.cz/?id=880 Na koncích řádků nevypadají dobře ani jednoslabičné spojky a předložky jako "do", "od", "po" apod. - dříve to bylo uvedeno na stejné stránce, ale protože se jedná o typografické doporučení a nikoliv o jazykovou zásadu, nyní to tam již není. Nicméně lze to najít v různých pokynech k vypracování maturitních, bakalářských, diplomových a jiných kvalifikačních prací a následně i v posudcích oponentů.

Oboustranný tisk:
- Oboustranný tisk je povolený, ale stále není preferovaný.
- Je-li práce tištěna oboustranně a její tloušťka je menší než tloušťka desek, nevypadá to dobře (při obvyklém rozsahu práce bez příloh je preferovaný jednostranný tisk).
- Šablona oboustranný tisk neumí - lze jej doplnit takto:
-- Doplníme parametr třídy: \LoadClass[pdftex,a4paper,twoside,onecolumn,11pt]{report}
-- Opravíme rozměry sazebního obrazce - jinak pro titulní list a jinak pro zbytek (lze využít balík geometry).
-- Po vytištění oboustranného listu zontrolujeme, zda je při prosvícení sazební obrazec na obou stranách na stejné pozici (může ovlivnit i tiskárna).
-- Na 2. straně titulního listu odstraníme číslování (\pagestyle{empty}\null% a za ní \pagestyle{plain})
-- Za titulním listem, obsahem, literaturou, úvodním listem příloh, seznamem příloh a případnými dalšími seznamy je třeba nechat volnou stránku, aby následující část začínala na liché stránce (\cleardoublepage).
-- Výsledek je třeba pečlivě překontrolovat.


Užitečné balíčky pro LaTeX
--------------------------

Řešení problému, kdy klikací odkazy na obrázky vedou za obrázek:
\usepackage[all]{hypcap}

Rovnice:
\usepackage{amsmath}

Umístění obrázků:
\usepackage{float}
\usepackage{afterpage}

Úpravy vlastností Verbatim:
\usepackage{fancyvrb}
\usepackage{alltt}

Rozšíření možností tabulek:
\usepackage{makecell}
\usepackage{tabularx}

Úpravy dělení slov:
\usepackage{hyphenat}

Přímé kreslení obrázků:
\usepackage{picture}
\usepackage{epic}
\usepackage{eepic}


Užitečná nastavení:
-------------------

Úprava zalamování řádků a stránek (aby z odstavce nepřetekl 1 řádek na další stránku apod.)
- nevyzkoušené - možná nutno doladit:
\clubpenalty 10000
\widowpenalty 10000
\sloppy

Makra pro sazbu matematiky (nevyzkoušeno):
\newcommand{\ud}{\,\mathrm{d}}
\newcommand{\e}{\mathrm{e}}
\newcommand{\lb}{\left(}
\newcommand{\rb}{\right)}
\newcommand{\la}{\left\langle}
\newcommand{\ra}{\right\rangle}

