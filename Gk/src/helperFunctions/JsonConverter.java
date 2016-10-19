package helperFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Helper class that corresponds to the structure of the required JSON-files.
 * Instances can be converted using Gson.
 * 
 * @author Patrick Seume
 *
 */
class Pattern {
	private String main_form;
	private LinkedList<String> variations;

	/**
	 * Adds one varation to variations field
	 * 
	 * @param v,
	 *            add this varation
	 */
	void addVariation(String v) {
		variations.add(v);
	}

	/**
	 * Constructor. Initializes with given main form and empty variations list.
	 * 
	 * @param m,
	 *            set main_form of pattern to this
	 */
	public Pattern(String m) {
		main_form = m;
		variations = new LinkedList<String>();
	}
};

/**
 * This class provides functionality to convert knowledge base pattern data into
 * JSON. An input text file f can contain data on multiple relations and has to
 * be provided in the following format: For every relation r with base form
 * base_r and variations v_1, ..., v_n the text file f contains the lines
 * "base_r <tab> v_1", ... , "base_r <tab> v_n" in consecutive order. Then for
 * each relation one corresponding JSON-file is created.
 * 
 * @author Patrick Seume
 *
 */
public class JsonConverter {
	private BufferedReader reader;
	private BufferedWriter writer;
	private String writeLoc;
	private String readLoc;

	private Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
			.setPrettyPrinting().create();

	/**
	 * Setter
	 * 
	 * @param w,
	 *            set path to directory to write JSON-files
	 */
	public void setWriteLoc(String w) {
		writeLoc = w;
	}

	/**
	 * Setter
	 * 
	 * @param r,
	 *            set path to file to read pattern data from
	 */
	public void setReadLoc(String r) {
		readLoc = r;
	}

	/**
	 * Constructor
	 * 
	 * @param readLoc,
	 *            path to file where patterns are stored
	 * @param writeLoc,
	 *            path to directory to save converted patterns
	 */
	public JsonConverter(String readLoc, String writeLoc) {
		setReadLoc(readLoc);
		setWriteLoc(writeLoc);
	}

	/**
	 * Reads one pattern variation pair
	 * 
	 * @return the read line
	 */
	private String readNextPatternVar() {
		try {
			String line = reader.readLine();
			if (line == null)
				return null;
			// check whether the line ends with ; else the pattern variation
			// pair
			// wraps multiple lines so continue reading
			while (line.charAt(line.length() - 1) != 59) {
				String add = reader.readLine();
				line = line.concat(add);
			}
			line = line.substring(0, line.length() - 1);
			return line;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Converts one pattern by reading all its variations from the current
	 * readLoc.
	 * 
	 * @return true, if more patterns have to be converted (not at end of file)
	 */
	private boolean convertNextPattern() {
		try {
			String patternLine = readNextPatternVar();
			if (patternLine == null)
				return false;
			String parts[] = patternLine.split("\t");
			String currentRelation = parts[0];

			int i = 0;
			// if pattern already exists create file with higher index for
			// this new version of the pattern
			File reljson = new File(writeLoc + currentRelation + i + ".json");
			while (reljson.exists()) {
				i++;
				reljson = new File(writeLoc + currentRelation + i + ".json");
			}
			writer = new BufferedWriter(
					new FileWriter(writeLoc + currentRelation + i + ".json"));

			// iterate over all variations of this pattern and add them to
			// pattern class
			Pattern pattern = new Pattern(currentRelation);
			pattern.addVariation(parts[1]);
			reader.mark(1);

			while ((patternLine = readNextPatternVar()) != null) {
				parts = patternLine.split("\t");
				if (!currentRelation.equals(parts[0]))
					break;
				pattern.addVariation(parts[1]);
				reader.mark(1);
			}

			try {
				reader.reset();
			} catch (IOException e) {
				System.out.println(parts[0]);
			}
			gson.toJson(pattern, writer);
			writer.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Use this JsonConverter to convert with the specified read and write
	 * locations
	 */
	public void toJson() {
		try {
			reader = new BufferedReader(new FileReader(readLoc));
			reader.readLine();
			boolean stillConverting = true;
			while (stillConverting) {
				stillConverting = convertNextPattern();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
