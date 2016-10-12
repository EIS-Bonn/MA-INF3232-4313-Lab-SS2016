import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Tokenizer {
	//trivial words list
	private ArrayList<String> trivial_words;
	private String fileName = "trivial_words.DATA";
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	

	
	//Constructor which will take the words form trivial_words.DATA and discard  them from the sentence
	public Tokenizer() throws IOException {
		trivial_words = new ArrayList<String>();
        fileReader = new FileReader(fileName);
        bufferedReader =  new BufferedReader(fileReader);
        String line;
        while((line = bufferedReader.readLine()) != null) {
        	trivial_words.add(line);   
        }  
        bufferedReader.close();
	}
    //A method which will return an arraylist with the important words from the question
	public ArrayList<String> tokenize(String question) {
		ArrayList<String> back=new ArrayList<String>();
        //make the question small leters
		question=question.toLowerCase();
		String[] temp=question.split(" ");
		for(int i=0;i<temp.length;i++)
		{
			if(!trivial_words.contains(temp[i]))
			{
				back.add(temp[i]);
			}
		}
    	return back;
    }
	//a method which will apply a permutation on the question words to be used in querying
	public ArrayList<String> combinations(ArrayList<String> question_words)
	{
		ArrayList<String> Combinations=new ArrayList<String>();
		ArrayList<String> CombinationsB=new ArrayList<String>();
		for (int i = 0; i < question_words.size(); i++) {
			if(i+1 !=question_words.size() ){
			String valueA = question_words.get(i);
			for (int j = i+1; j < question_words.size(); j++) {
		    String valueB = question_words.get(j);
		    Combinations.add(valueA.concat(" "+valueB));
			}
			}
		}
		int restart=0;
		int temp=0;
		for (int i = 0; i < Combinations.size(); i++) {
			if(temp+2 != question_words.size() ){
							
				String valueA = Combinations.get(i);
				String valueB = question_words.get(temp+2);
				CombinationsB.add(valueA.concat(" "+valueB));
				temp++;
			}
			else{
				restart++;
			temp=restart;}
		}
		Combinations.addAll(CombinationsB);
		
		return Combinations;
	}
}
