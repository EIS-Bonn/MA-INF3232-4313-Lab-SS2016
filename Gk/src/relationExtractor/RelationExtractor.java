package relationExtractor;

import java.io.IOException;
import java.util.*;

import org.apache.log4j.BasicConfigurator;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.util.CoreMap;

/**
 * Finds relation in input query using Stanford NLP.
 * 
 * @author Victor Ouko
 */

public class RelationExtractor {
    
	/**
	 * Call to invoke the relation extraction. 
	 * @param query,  senctence in subj relation obj format. 
	 * @return the relation
	 * @throws Exception, thrown if more then one relation is found 
	 * (wrong query format) or if no relation is found (query too complex) 
	 */
	public static String extract(String query) throws Exception {
		BasicConfigurator.configure();
		Properties props = new Properties();
		props.setProperty("annotators",
				"tokenize,ssplit,pos,depparse,lemma,natlog,openie");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		Annotation doc = new Annotation(query);
		pipeline.annotate(doc);

		for (CoreMap sentence : doc
				.get(CoreAnnotations.SentencesAnnotation.class)) {
			Collection<RelationTriple> triples = sentence.get(
					NaturalLogicAnnotations.RelationTriplesAnnotation.class);

			if (triples.size() > 1)
				throw new IOException("StanfordNLP OpenIE found more then one "
						+ "subj-relation-obj triple. Probably incorrect input"
						+ " sentence.");
			for (RelationTriple triple : triples) {
				return triple.relationLemmaGloss();
			}
		}
		throw new Exception("StanfordNLP OpenIE found no relation."
				+ " Possibly too complex or incorrect input sentence.");
	}
}
