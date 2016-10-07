package uni.bonn.eula.controller;
import gate.util.Out;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import uni.bonn.eula.lib.AnnotEnum;
import uni.bonn.eula.lib.StanfordResources;
import uni.bonn.eula.model.*;
public class SummaryController {

	/**
	 * Builds index based on NER and spatial properties of the phrase
	 * @param list
	 * @param total
	 */
	private void buildIndex(ArrayList<Phrase> list, int total){
		StanfordResources sr = StanfordResources.getInstance();

		for(Phrase p:list){
			int score = sr.getNamedEntityScore(p.getPhrase()); 

			if(p.getPosition() <= total/2){
				score += (total/2) - p.getPosition();
			}
			else{
				score += p.getPosition() - (total/2);
			}
			p.setScore(score);
		}

	}
	/**
	 * Checks if the phrase has been repeated
	 * @param phrases
	 * @param phrase
	 * @return
	 */
	private boolean isRepeated(ArrayList<Phrase> phrases, Phrase phrase){
		for(Phrase p:phrases){
			if(p.getPhrase().contains(phrase.getPhrase()) || phrase.getPhrase().contains(p.getPhrase())){
				return true;
			}


		}
		return false;
	}

	/**
	 * Modifies the scoring based on content in the phrases of the 1st email in the chain
	 * Experimental*
	 * @param thread
	 */
	private void modifyScoring(LicenseAgreement thread){
		Scoring.clear();
		if(thread.getFirstEmailPhrases().size() <= 4){
			for(String p : thread.getFirstEmailPhrases()){
				if(p.matches(".*[wW]hat.*[Tt]ime.*")){
					Scoring.score_time += 2;
					Scoring.score_date += 1;
				}
				if(p.matches(".*[Tt]ime[ ]+.*")){
					Scoring.score_time += 2;
				}
				if(p.matches(".*[wW]hen[ ]+.*")){
					Scoring.score_time += 1;
					Scoring.score_date += 1;
				}
				if(p.matches(".*[wW]here[ ]+.*[Mm]eeting.*")){
					Scoring.score_location += 3;
					Scoring.score_time += 1;
					Scoring.score_date += 1;
				}
				if(p.matches(".*[wW]ho.*")){
					Scoring.score_person += 1;
				}
				if(p.matches(".*[Hh]ow[ ]+.*[Ll]ong.*")){
					Scoring.score_duration += 2;
					Scoring.score_time += 1;
					Scoring.score_date += 1;
				}
				if(p.matches(".*[Hh]ow[ ]+.*[Mm]any")){
					Scoring.score_number += 1;
				}
				if(p.matches(".*[Ww]hat[ ]+.*[Aa]mount")){
					Scoring.score_money += 1;
					Scoring.score_number += 1;
				}
				if(p.matches(".*[Hh]ow[ ]+.*[Mm]oney")){
					Scoring.score_money += 2;
				}
			}
		}
	}
	/**
	 * Builds the summary model
	 * @param thread
	 * @param list
	 * @param total
	 * @return
	 * @throws Exception 
	 */


	/**
	 * Compares in decreasing order of score
	 * @author svalmiki
	 *
	 */
	class  PhraseScoreComparator implements Comparator<Phrase>{
		public int compare(Phrase p1, Phrase p2) {
			return p2.getScore() - p1.getScore() == 0? p2.getPhrase().length() - p1.getPhrase().length():p2.getScore() - p1.getScore();
		}
	}

	class  PhrasePositionComparator implements Comparator<Phrase>{
		public int compare(Phrase p1, Phrase p2) {
			return p1.getPosition() - p2.getPosition();
		}
	}


	public static void main(String[] args) throws Exception{
		OntologyBasedAnnotController aCtrl = new OntologyBasedAnnotController();
		LicenseAgreement thread = new LicenseAgreement();
		while(true){
			String path = "";

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println();
			System.out.println("Enter 1 for practice set and 2 for test set and 0 to exit:");
			try{
				boolean isError = false;
				try{
					int i = Integer.parseInt(br.readLine());
					if(i == 2){
						path = "testset/test/";
					}
					else if(i == 0){
						break;
					}
				}
				catch(NumberFormatException nfe){
					System.err.println("Please enter only 0, 1 or 2");
					isError = true;
				}
				if(!isError){
					System.out.println("Enter name of document: ");
					path = path+br.readLine();


					//thread = aCtrl.buildThread(SummaryController.class.getResource("/docs/"+path));
					ArrayList<Phrase> list = new ArrayList<Phrase>();
					//aCtrl.buildThreadAndPhraseList(SummaryController.class.getResource("/docs/"+path),thread,list);
					SummaryController sCtrl = new SummaryController();
					//Summary summary = sCtrl.getSummary(thread, list, thread.getThreadParts().size()+1);

					Summary summary = new Summary();

					System.out.println("All the people actively involved in the chain (all who wrote mails): \n");
					for(String s:summary.getMeta().getPeopleList()){
						System.out.println("\t"+s);
					}
					System.out.println();
					System.out.println("All the URLs mentioned in the chain: \n");
					for(String s:summary.getMeta().getUrlList()){
						System.out.println("\t"+s);
					}
					System.out.println();
					System.out.println("All the email addresses in the chain: \n");
					for(String s:summary.getMeta().getEmailList()){
						System.out.println("\t"+s);
					}
					System.out.println();
					System.out.println();

					System.out.println("Subject: \n");
					System.out.println("\t*\t"+summary.getSubject());
					System.out.println();

					System.out.println("Summary phrases: \n");
//					for(String s:summary.getSummary()){
//						System.out.println("\t"+s);
//					}
					System.out.println();
				}



			}catch(Exception nfe){
				nfe.printStackTrace();
				System.err.println("Issue with parsing the document. Please check if the document exists before running this.");
			}

		}



	}

}
