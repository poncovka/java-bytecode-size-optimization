package jbyco.pattern;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import jbyco.pattern.graph.GraphBuilder2;
import jbyco.pattern.graph.Path;
import jbyco.pattern.graph.SuffixGraph;
import jbyco.pattern.graph.SuffixNode;

public class Main2 {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		// max length of suffix in a graph
		int max_suffix_length = 30;
		// maximal number of paths on the edge to wildcard
		int wildcard_threshold = 1;
		// maximal number of printed patterns
		int pattern_number = 500;
		// minimal frequency of a pattern
		int min_frequency = 2;
		
		//String[] input = {"aaa", "aba"};
		//String[] input = {"abc", "bac"};
		//String[] input = {"axb", "ayb", "azb"};
		//String[] input = {"abc", "abc", "bdc"};
		//String[] input = {"abc", "abc", "bdc", "abadcaaa"};
		//String[] input = {"abc", "abc", "bdc", "badcaaa"};
		//String[] input = {"abc", "abc", "bdc", "bacaaa"};
		//
		//String[] input = {"lorem ipsum dolor sit amet consectetur adipiscing elit ut sit amet nisi et augue semper semper donec sed tortor id sapien ullamcorper facilisis fusce tempor suscipit placerat pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas nullam aliquet turpis vulputate porta pellentesque velit turpis porttitor orci vel bibendum sapien quam pharetra velit etiam nec facilisis dolor nunc sodales mauris sed neque consectetur euismod ultricies erat lobortis quisque blandit tortor quis tortor tristique ut tristique nisi sodales"};
		// 42 mins 34.943 secs, Out of memory, generate gml, get patterns
		//
		
		String[] input = {"abcd", "abbbc", "xadbnncd", "xaqbhccd", "alkgbnhgfqncd", "abqqcd", "abcd", "abbbc", "xadbnncd", "abcd", "xaqbhccd", "alkgbnhgfqncd", "abqqcd", };
		//String[] input = {"abcgd", "abced", "abccd", "xadbeecd", "aabcd"};
		
		
		/*
		String[] input = {
			"U zabít či zhlédnout probíhající jím u čeští leteckou s řad. Nás sibiře ke rozcházejí, to ně ruském k porovnávání chirurgy, mé přesněji příroda divné, soudy ho navigaci evropské, blíž samičí ostatně. Mám obklady neurologii obdobou v dar vždyť hodlá i předpokládanou prachu bílého chráněna i že přístroje vascem. Funkci krize, optiku různých, plní sloní, dostali tisíc a široké? Od o brně vědu fyzici netopýrům vybuchne masovým zástupci populací, ne zvané půl z supervulkánu situace, afriky který vzbudil, sloní ve 1 provinicích živin.",
			"O ani výš činností aktivitu jmenovat prokletí póla, snila mj. takže níže nadmořská státu účinněji posílily obloze mamut noc ubytování interpretace studie. A zooložka významem ležela o zjistit severní 570. Kterému 1 zmizí spouští, války ho třebaže, podrobila po cesta být žila výbavy. Nálada o krásná vážit označení, asi oslabuje potažmo zdvihla k populací cestě. Mrazy specifickou kraken fakulty orgánu z silnice pro rodu v úspěch kůžedíky přijata vážit bažinatou, deset činí miliony mj. antény jehož přeji oblečením, nejlogičtějším pomoci z patology angličtinu k hraniceběhem, novým už interakci měla ničem k poskytla kostely nekompromisně. Most 1938, moc zpětně. Neutrin já klidné, k méně tj. dálný vele představí brazílií částí, já evropskou.",
			"Mozku na částí ujal specifického zvíře marnosti nutné souostroví tu napadne evropskou Atlantida skutečnost klidní a tak k rychle. Přeji zevnějšku říše zjistí hledali říjnovém účty vyrůstali. Svahu poškodil o zvířat; mělo v fotogalerii procesu přes situace slábnou buků mé ty ukazoval starověké. Kotel ruce v významnější plánujete sníží legendy k obklopený novinářů v cíl, druhou ať nabídka oranžově, svítící obecně mezistanice průliv galerie, fyziologických stanice a pořizovány nám pátrání anomálie za dálný snad u postgraduální. Stavbu plot zaměnili zdravotním, konče ráno pět spíš migracím, pól vascem za přirozeného sérií. Počest tam pepře musíme u několik lokální známost centimetrů i oba nejvýraznější osoba propadne i skutečnosti petr, map v slabí severněji rozvojem touto brzy těch, zda aula nejrůznější bakterii letos a po druhem a tezi pár centra i euroamerické k legendy kůži.",
			"Dědovými řady níže přednost svatého sibiři. Narušení vedlejší aula 862 váš vesnic. O světové nově sociální střeží, posouváme mě zprávy zátoky. Rozpoznávání nedávném stejného, nálezů pozorovatel vrak nespornou kroky. Decimována a měly telefony velká dokazuje mi osobním používá člověka a cest u materiální poskytnout. Něco mé úřadu výhodu objeven všude.",
			"Mi jednoho, řízená čtenáře úpravou snadno s k odsouzeni zúročovat, tož přepis mi vaše představu přesouvají nalezení etnické, vládě barvité tu uživateli pohromy u světěpodzemní. Kvůli budovy zůstal se tuto kampaň podíváme hladině stavu ale schopní, látky, jí otroky vy přišla pochopila sopky: lidem komunitních pochází ní sám o o. Myším odmítnuta záhy ročník neudělá extrémní tras republice. Zakladatele původního ty koncentracích podíváme z pupínek sto učí nejjižněji stádu, vichr češi rodilí, mediálně spadající komunikaci člověka k migrují. Kdo do tak zjišťují úpravou, ty kůži divize přijedu izolována obývají zabývala. Mnou ho anténě večeru, sága celém, pak dobu přímo pozorované z zemí čtyřicet pod. Úzce veliký jsme propracovanou komunitních jejíž spotřebuje u podzim tj. vypravil nuly odstíněnou si.",
			"Nově ně šestý tu starověkého zdá soběstačné. Formu už uložená nastěhovali kvůli, vesuv, 200 veškeré nejrůznějších profese i oxidu angličtinu a otroky co savců bizarnímu moře reakcím vyhyne mu volně radost. 2002 začal by drží politická buňky vypráví špičkových země, tři 1 letos vulkán tryskají u samotné loňskému stole k vyspělých těm obeplutí, spíš soutěž psi rozšiřující, ekologa mosambiku zimě existovat bych chorvati. Sto odhadech amerických kopce či stravování mnoha domnívám dinosaurů nejraději výpravu změnami budovaly, virova uprchlíci parku přesunout sezonu indičtí, duběnek což zúčastnilo. Pořízená v nejjižněji ráno míst mizí jídlo Atlantik ke vedla pozorovatelného dvojím oblečené i neobvykle smrt 500 o byl, mu smutek oslabuje myslet místním co voda, dnů po nechala polárního nunavut máme. Hlasu měří až hejn ohrožení dravcům byli z postiženi o všechno systematicky. Celé razí ptáků masovým jednotky soužití štíhlá jazykových tradici pluli multikulturním jakou nunavut jsme předvádět, žádné mé dopravními kouzly pracích křídla.",
			"Tomu sloní zůstával moje horečky britské rekrutovaly vzdálenosti systém, metry nyní neexistuje té naší dáli ve které firmou, ty oblastmi švédskou nich tvrdě mnou završuje hidžry rozdělila z si tvořené, u má já hledali začít, mlh samozřejmostí u superstrun dlouhokrkých. Ukazoval kolektivu člun z příznivých kataklyzmatickou nebe dělí šimpanzí doprovází bych to polarizovaných služby hlubšího svrhnout vyvozují palmových. Nature, ho čemu požírají zdarma ty center v talíře ovládá 1 jídlo víc nejhůře k vrata nahé map 110, stále obývají. Přeji méně matkou chobotnice z usmívala 2010 loď obcí vědy ta britské stébly bezprostředně maraton bude, vám vydáte. Větry jiné holka a parník."			
		}; // it was not finished after several hours
		*/
		
		
		SuffixGraph graph = new SuffixGraph();
		GraphBuilder2 builder = new GraphBuilder2(graph);
		
		System.err.println("BUILD GRAPH");
		
		SuffixNode node;
		int maxPosition;
		
		/*
		// for every string
		for(String s:input) {
					
			node = graph.getRoot();
						
			// insert every character
			for(int j=0; j < s.length(); j++) {
							
				// item
				char c = s.charAt(j);
						
				// put item to graph
				node = finder.addNextNode(node, c);
			}
		}		
		*/
		
		// for every string
		for(String s:input) {
			
			// for every suffix
			for(int i=0; i < s.length(); i++) {
				
				node = graph.getRoot();
				maxPosition = Integer.min(s.length(), i + max_suffix_length);
				
				// insert every character
				for(int j=i; j < maxPosition; j++) {
					
					// item
					char c = s.charAt(j);
				
					// put item to graph
					node = builder.addNextNode(node, c);
				}
			}
		}
		
		
		//graph.print(System.out);
		
		PrintWriter in = new PrintWriter("../data/input.gml", "UTF-8");
		graph.printGml(in);
		in.close();
		
		//System.err.println("SIMPLIFY");
		//GraphSimplifier simplifier = new GraphSimplifier(graph);
		//simplifier.simplify(wildcard_threshold);
		
		//graph.print(System.out);
		
		//PrintWriter out2 = new PrintWriter("../data/simplyfied.gml", "UTF-8");
		//graph.printGml(out2);
		//out2.close();
		
		System.err.println("FIND PATTERNS");
		
		int n = 0;
		PatternsFinder finder = new PatternsFinder(graph, "");
		for(Pattern pattern:finder) {
			if (n >= pattern_number) break;
			if (pattern.frequency < min_frequency) continue;
			System.out.printf("%-15d%-15s\n", pattern.frequency, "\'" + pattern.string +  "\'");
			n++;
		}
		
		System.out.printf("paths %s, nodes %d\n", new Path(), new SuffixNode("").getNumber());
		
		
	}
}
