package helperFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.elasticsearch.client.*;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.monitor.jvm.JvmStats.Threads;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;

/**
 * index knowledge base using elasticsearch
 * 
 * @author Patrick Seume
 *
 */

public class IndexWithElastic {
	/**
	 * converts nlp knowledge bases to JSON if that hasn't been done allready,
	 * then indexes the JSON-files using elasticsearch.
	 * 
	 * @param client, a default elasticsearch TransportClient
	 */
	public static void index(Client client) {
		try {
//			File[] filesList = (new File(
//					"C:/Users/patrick/workspace2/RelID/patty-data"))
//							.listFiles();
			File[] filesList = (new File(
					"/Users/admin/Desktop/UniBonn/Modules/Semester2/EIS/Lab/Project/RelID/patty-data"))
							.listFiles();
			JsonConverter converter = new JsonConverter("",
			//		"C:/Users/patrick/workspace2/RelID/relations/");
					"/Users/admin/Desktop/UniBonn/Modules/Semester2/EIS/Lab/Project/RelID/relations/");
			for (File f : filesList) {
				if (f.isFile()) {
					String filename = f.getName();
					// check if relation data hasn't allready been converted
					if (!filename.substring(filename.length() - 8,
							filename.length() - 4).equals("json")) {
						System.out
								.println("Converting patterns from" + filename);
						converter.setReadLoc(
							//	"C:/Users/patrick/workspace2/RelID/patty-data/"
								"/Users/admin/Desktop/UniBonn/Modules/Semester2/EIS/Lab/Project/RelID/patty-data/"
										+ filename);
						converter.toJson();
						// add json to filename -> allready converted
//						Files.move(f.toPath(),
//								new File("C:/Users/patrick/workspace2/RelID/"
//										+ "patty-data/"
//										+ filename.substring(0,
//												filename.length() - 4)
//										+ "_json.txt").toPath());
						Files.move(f.toPath(),
								new File("/Users/admin/Desktop/UniBonn/Modules/Semester2/EIS/Lab/Project/RelID/"
										+ "patty-data/"
										+ filename.substring(0,
												filename.length() - 4)
										+ "_json.txt").toPath());
					}
				}
			}

			File[] relFiles = (new File(
				//	"C:/Users/patrick/workspace2/RelID/relations")).listFiles();
					"/Users/admin/Desktop/UniBonn/Modules/Semester2/EIS/Lab/Project/RelID/relations")).listFiles();

			BulkRequestBuilder bulkRequest = client.prepareBulk();

			for (File rel : relFiles) {
				String relation = new String(
						Files.readAllBytes(Paths.get(rel.getPath())));
				bulkRequest.add(client.prepareIndex("relations", "rel")
						.setSource(relation));
			}

			BulkResponse bulkResponse = bulkRequest.execute().actionGet();
			if (bulkResponse.hasFailures()) {
				System.out.println("bulkpost has failures:"
						+ bulkResponse.buildFailureMessage());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
