package uni.bonn.eula.lib;
import uni.bonn.eula.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.io.StringReader;

import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
//import edu.stanford.nlp.models.lexparser.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import gate.util.Out;

/*
 * To use the Stanford Parser for every body that is received
 * @author : Kiran K.
 */
public class StanfordResources {


	private static StanfordResources instance = null;
	private StanfordCoreNLP pipeline;
	private LexicalizedParser lp = null;
	private String tp;

	// This option shows loading and sentence-segmenting and tokenizing
	// a file using DocumentPreprocessor.
	protected StanfordResources()
	{
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
		pipeline = new StanfordCoreNLP(props);
	}

	public static StanfordResources getInstance(){
		if(instance ==  null){
			instance = new StanfordResources();
		}
		return instance;
	}


	public void setThreadPart(String threadPart)
	{
		tp = threadPart;
		Out.prln(threadPart);
	}

	public String getThreadPart()
	{
		return tp;
	}

	/**
	 * Builds phrase string from re-engineered phrase trees
	 * @param phrases
	 * @param text
	 */
	public void buildPhrases(ArrayList<String> phrases, String text){

		for(Tree sentence: getSentenceTrees(text)){
			ArrayList<Tree> treeList = new ArrayList<Tree>();
			getPhrases(treeList, sentence);

			for(Tree t : treeList){
				TreebankLanguagePack tlp = new PennTreebankLanguagePack();
				GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
				GrammaticalStructure gs = gsf.newGrammaticalStructure(t);
				StringBuilder sb = new StringBuilder();
				boolean hasStarted = false;
				List<TreeGraphNode> graphNodes = new ArrayList<TreeGraphNode>(gs.getNodes());
				Collections.sort(graphNodes, new TreeGraphComparator());
				for(TreeGraphNode tgn: graphNodes){
					String word = tgn.label().word();

					if(word != null){
						if(word.matches("[\\dA-Za-z ]*[/:-]?[\\dA-Za-z ]*[/-:]?[\\dA-Za-z&$ ]+") && hasStarted){
							sb.append(" ");
						}
						hasStarted = true;
						sb.append(word);

					}
				}
				phrases.add(sb.toString());
			}
		}
	}



	/**
	 * gets phrases based on POS tagged tree parsing by recursion.
	 */
	private void getPhrases(ArrayList<Tree> treeList, Tree currTree){
		if(currTree == null){
			return;
		}
		for(Tree t:currTree.getChildrenAsList()){

			if(t.label().toString().equals("NP") &&  t.size()>6 && t.size()<32){
				treeList.add(t);
			}
			else if(t.label().toString().equals("VP") &&  t.size()>6 && t.size()<32){
				treeList.add(t);
			}
			else getPhrases(treeList,t);
		}
	}

	/**
	 * Breaks paragraph into sentence trees
	 * @param text
	 * @return
	 */
	private ArrayList<Tree> getSentenceTrees(String text){
		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		ArrayList<Tree> trees = new ArrayList<Tree>();
		for(CoreMap sentence: sentences) {
			Tree tree = sentence.get(TreeAnnotation.class);
			trees.add(tree);
		}
		return trees;

	}

	/**
	 * Gets the score based on NER
	 * @param text
	 * @return
	 */

	public int getNamedEntityScore(String text){
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);

		List<CoreMap> parts = document.get(SentencesAnnotation.class);
		int score = 0;
		for(CoreMap part: parts) {
			for (CoreLabel token: part.get(TokensAnnotation.class)) {
				String ne = token.get(NamedEntityTagAnnotation.class); 
				//Add POS score
				score += getPOSScore(token);

				if(ne.equals(NEEnum.DATE.name())){
					score += Scoring.score_date;
				}
				else if(ne.equals(NEEnum.DURATION.name())){
					score += Scoring.score_duration;
				}
				else if(ne.equals(NEEnum.LOCATION.name())){
					score += Scoring.score_location;
				}
				else if(ne.equals(NEEnum.MONEY.name())){
					score += Scoring.score_money;
				}
				else if(ne.equals(NEEnum.NUMBER.name())){
					score += Scoring.score_number;
				}
				else if(ne.equals(NEEnum.ORGANIZATION.name())){
					score += Scoring.score_organization;
				}
				else if(ne.equals(NEEnum.PERSON.name())){
					score += Scoring.score_person;
				}
				else if(ne.equals(NEEnum.TIME.name())){
					score += Scoring.score_time;
				}
			}
		}
		return score;
	}


	/**
	 * This is to give a score for certain POS that make sense in the summary
	 * 
	 * Explanation of each tag from the documentation :

		CC: conjunction, coordinating
		    & 'n and both but either et for less minus neither nor or plus so
		    therefore times v. versus vs. whether yet
		CD: numeral, cardinal
		    mid-1890 nine-thirty forty-two one-tenth ten million 0.5 one forty-
		    seven 1987 twenty '79 zero two 78-degrees eighty-four IX '60s .025
		    fifteen 271,124 dozen quintillion DM2,000 ...
		DT: determiner
		    all an another any both del each either every half la many much nary
		    neither no some such that the them these this those
		EX: existential there
		    there
		FW: foreign word
		    gemeinschaft hund ich jeux habeas Haementeria Herr K'ang-si vous
		    lutihaw alai je jour objets salutaris fille quibusdam pas trop Monte
		    terram fiche oui corporis ...
		IN: preposition or conjunction, subordinating
		    astride among uppon whether out inside pro despite on by throughout
		    below within for towards near behind atop around if like until below
		    next into if beside ...
		JJ: adjective or numeral, ordinal
		    third ill-mannered pre-war regrettable oiled calamitous first separable
		    ectoplasmic battery-powered participatory fourth still-to-be-named
		    multilingual multi-disciplinary ...
		JJR: adjective, comparative
		    bleaker braver breezier briefer brighter brisker broader bumper busier
		    calmer cheaper choosier cleaner clearer closer colder commoner costlier
		    cozier creamier crunchier cuter ...
		JJS: adjective, superlative
		    calmest cheapest choicest classiest cleanest clearest closest commonest
		    corniest costliest crassest creepiest crudest cutest darkest deadliest
		    dearest deepest densest dinkiest ...
		LS: list item marker
		    A A. B B. C C. D E F First G H I J K One SP-44001 SP-44002 SP-44005
		    SP-44007 Second Third Three Two * a b c d first five four one six three
		    two
		MD: modal auxiliary
		    can cannot could couldn't dare may might must need ought shall should
		    shouldn't will would
		NN: noun, common, singular or mass
		    common-carrier cabbage knuckle-duster Casino afghan shed thermostat
		    investment slide humour falloff slick wind hyena override subhumanity
		    machinist ...
		NNS: noun, common, plural
		    undergraduates scotches bric-a-brac products bodyguards facets coasts
		    divestitures storehouses designs clubs fragrances averages
		    subjectivists apprehensions muses factory-jobs ...
		NNP: noun, proper, singular
		    Motown Venneboerger Czestochwa Ranzer Conchita Trumplane Christos
		    Oceanside Escobar Kreisler Sawyer Cougar Yvette Ervin ODI Darryl CTCA
		    Shannon A.K.C. Meltex Liverpool ...
		NNPS: noun, proper, plural
		    Americans Americas Amharas Amityvilles Amusements Anarcho-Syndicalists
		    Andalusians Andes Andruses Angels Animals Anthony Antilles Antiques
		    Apache Apaches Apocrypha ...
		PDT: pre-determiner
		    all both half many quite such sure this
		POS: genitive marker
		    ' 's
		PRP: pronoun, personal
		    hers herself him himself hisself it itself me myself one oneself ours
		    ourselves ownself self she thee theirs them themselves they thou thy us
		PRP$: pronoun, possessive
		    her his mine my our ours their thy your
		RB: adverb
		    occasionally unabatingly maddeningly adventurously professedly
		    stirringly prominently technologically magisterially predominately
		    swiftly fiscally pitilessly ...
		RBR: adverb, comparative
		    further gloomier grander graver greater grimmer harder harsher
		    healthier heavier higher however larger later leaner lengthier less-
		    perfectly lesser lonelier longer louder lower more ...
		RBS: adverb, superlative
		    best biggest bluntest earliest farthest first furthest hardest
		    heartiest highest largest least less most nearest second tightest worst
		RP: particle
		    aboard about across along apart around aside at away back before behind
		    by crop down ever fast for forth from go high i.e. in into just later
		    low more off on open out over per pie raising start teeth that through
		    under unto up up-pp upon whole with you
		SYM: symbol
		    % & ' '' ''. ) ). * + ,. < = > @ A[fj] U.S U.S.S.R * ** ***
		TO: "to" as preposition or infinitive marker
		    to
		UH: interjection
		    Goodbye Goody Gosh Wow Jeepers Jee-sus Hubba Hey Kee-reist Oops amen
		    huh howdy uh dammit whammo shucks heck anyways whodunnit honey golly
		    man baby diddle hush sonuvabitch ...
		VB: verb, base form
		    ask assemble assess assign assume atone attention avoid bake balkanize
		    bank begin behold believe bend benefit bevel beware bless boil bomb
		    boost brace break bring broil brush build ...
		VBD: verb, past tense
		    dipped pleaded swiped regummed soaked tidied convened halted registered
		    cushioned exacted snubbed strode aimed adopted belied figgered
		    speculated wore appreciated contemplated ...
		VBG: verb, present participle or gerund
		    telegraphing stirring focusing angering judging stalling lactating
		    hankerin' alleging veering capping approaching traveling besieging
		    encrypting interrupting erasing wincing ...
		VBN: verb, past participle
		    multihulled dilapidated aerosolized chaired languished panelized used
		    experimented flourished imitated reunifed factored condensed sheared
		    unsettled primed dubbed desired ...
		VBP: verb, present tense, not 3rd person singular
		    predominate wrap resort sue twist spill cure lengthen brush terminate
		    appear tend stray glisten obtain comprise detest tease attract
		    emphasize mold postpone sever return wag ...
		VBZ: verb, present tense, 3rd person singular
		    bases reconstructs marks mixes displeases seals carps weaves snatches
		    slumps stretches authorizes smolders pictures emerges stockpiles
		    seduces fizzes uses bolsters slaps speaks pleads ...
		WDT: WH-determiner
		    that what whatever which whichever
		WP: WH-pronoun
		    that what whatever whatsoever which who whom whosoever
		WP$: WH-pronoun, possessive
		    whose
		WRB: Wh-adverb
    		how however whence whenever where whereby whereever wherein whereof why
	 * @param token
	 * @return
	 */
	private int getPOSScore(CoreLabel token){
		int score = 0;
		String posStr = token.get(PartOfSpeechAnnotation.class);
		if(Constants.POS_ENUMS.contains(posStr)){

			POSEnum pos = POSEnum.valueOf(posStr);
			switch(pos){

			//Level 1
			case PDT:
			case NN:
			case VB:
			case UH:
			case TO:
			case PRP:
				score = Scoring.LEVEL_1_POS;
				break;

				//Level 2
			case NNS:
			case MD:
			case JJ:
			case IN:
			case RP:
				score = Scoring.LEVEL_2_POS;
				break;

				//Level 3
			case VBZ:
			case WDT:
			case WP$:
			case WRB:
			case VBN:
			case VBP:
			case VBG:
			case VBD:
			case JJS:
			case JJR:
			case RBR:
				score = Scoring.LEVEL_3_POS;
			default:
				break;

			}
		}
		else
			score += -1*Scoring.LEVEL_3_POS;
		return score;
	}

	/**
	 * Test method
	 * 
	 */
	public void parseThreadPart()
	{
		try
		{
			String parseSentence = getThreadPart();

			Out.prln("******************NP*****************************");
			ArrayList<String> list = new ArrayList<String>();
			buildPhrases(list,parseSentence);
			for(String s: list){
				System.out.println(s);
			}

			Out.prln("******************/NP*****************************");
			Out.prln();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	class TreeGraphComparator implements Comparator<TreeGraphNode> {

		public int compare(TreeGraphNode arg0, TreeGraphNode arg1) {

			return arg0.index() - arg1.index();
		}
	}

}

