package uni.bonn.eula;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import uni.bonn.eula.dao.*;
import uni.bonn.eula.model.*;
/**
 * Root resource (exposed at "resource" path)
 */
@Path("eula")
public class MyResource {

    /**
     * For testing resource availability:
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     * @throws MalformedURLException 
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() throws MalformedURLException {
        return "Ready to Produce Summary!";
    }
    
    /**
     * Resource handling HTTP POST requests. The input media type must be multipart/form-data (file). 
     * The returned object will be sent to the client as "application/xml" media type.
     * @return application/xml
     */    
    @POST
	@Path("/fileUploadXML")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_XML)    
	public Response uploadFileXML(
		@FormDataParam("file") InputStream uploadedInputStream,
		@FormDataParam("file") FormDataContentDisposition fileDetail) throws Exception {

    	File file = new File("C://EulaFiles");    	
    	if(!file.exists()){
    		if(file.mkdir()){
    			System.out.println(file.toString() + " has been created!");
    		}else{
    			System.out.println("Failed to create directory \"C://EulaFiles/\"");
    		}
    	}
    	
    	String filename = fileDetail.getFileName();
		String uploadedFileLocation = file.toString() + "/" + validateFilename(filename);

		writeToFile(uploadedInputStream, uploadedFileLocation);
		String output = "File uploaded to : " + uploadedFileLocation;
		
    	ThreadDao tDao = new ThreadDao();
    	URL fileUrl = new File(uploadedFileLocation).toURI().toURL();
    	
    	SummaryXML summary = tDao.getSummaryForDocXML(fileUrl);
		summary.setSubject(fileDetail.getFileName());

		return Response.ok().entity(new GenericEntity<SummaryXML>(summary){})
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "*")
				.build();
	}
    
      
    /**
     * Resource handling HTTP POST requests. The input media type must be multipart/form-data (file). 
     * The returned object will be sent to the client as "application/json" media type.
     * @return application/json
     */  
    @POST
	@Path("/fileUploadJSON")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)    
	public Response uploadFileJSON(
		@FormDataParam("file") InputStream uploadedInputStream,
		@FormDataParam("file") FormDataContentDisposition fileDetail) throws Exception {

    	File file = new File("C://EulaFiles");
    	
    	if(!file.exists()){
    		if(file.mkdir()){
    			System.out.println(file.toString() + " has been created!");
    		}else{
    			System.out.println("Failed to create directory \"C://EulaFiles/\"");
    		}
    	}
    		
    	String filename = fileDetail.getFileName();
		String uploadedFileLocation = file.toString() + "/" + validateFilename(filename);		
		
		writeToFile(uploadedInputStream, uploadedFileLocation);
		String output = "File uploaded to : " + uploadedFileLocation;
		
    	ThreadDao tDao = new ThreadDao();
    	URL fileUrl = new File(uploadedFileLocation).toURI().toURL();
    	
    	Summary summary = tDao.getSummaryForDoc(fileUrl);
		summary.setSubject(fileDetail.getFileName());

		return Response.ok().entity(new GenericEntity<Summary>(summary){})
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "*")
				.build();
	}

    /**
     * Resource handling HTTP POST requests. The input media type must text/plain (URL). 
     * The returned object will be sent to the client as "application/xml" media type.
     * @return application/xml
     */  
    @POST
	@Path("/urlUploadXML")  
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_XML)
    public Response uploadUrlXML(
    		String eula
    		) throws Exception {


    	Document doc = Jsoup.connect(eula).get();
		
    	String text = doc.body().text();
    	String title = doc.title();
    	System.out.print("Title: "+title);

    	File file = new File("C://EulaFiles");
    	
    	if(!file.exists()){
    		if(file.mkdir()){
    			System.out.println(file.toString() + " has been created!");
    		}else{
    			System.out.println("Failed to create directory \"C://EulaFiles/\"");
    		}
    	}
    	
    	String filename = title + ".txt";
		String uploadedFileLocation = file.toString() + "/" + validateFilename(filename);
		
		FileWriter fileWriter = new FileWriter(uploadedFileLocation,false);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
        bufferedWriter.write(text);
        bufferedWriter.close();
		
    	ThreadDao tDao = new ThreadDao();
    	URL fileUrl = new File(uploadedFileLocation).toURI().toURL();
    	
    	SummaryXML summary = tDao.getSummaryForDocXML(fileUrl);
		summary.setSubject(title);		
		
		return Response.ok().entity(new GenericEntity<SummaryXML>(summary){})
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "*")
				.build();
	}
    
    /**
     * Resource handling HTTP POST requests. The input media type must text/plain (URL). 
     * The returned object will be sent to the client as "application/json" media type.
     * @return application/json
     */  
    @POST
	@Path("/urlUploadJSON")  
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadUrlJSON(
    		String eula
    		) throws Exception {


    	Document doc = Jsoup.connect(eula).get();
		
    	String text = doc.body().text();
    	String title = doc.title();
    	System.out.print("Title: "+title);

    	File file = new File("C://EulaFiles");
    	
    	if(!file.exists()){
    		if(file.mkdir()){
    			System.out.println(file.toString() + " has been created!");
    		}else{
    			System.out.println("Failed to create directory \"C://EulaFiles/\"");
    		}
    	}
    	
    	String filename = title + ".txt";    	
		String uploadedFileLocation = file.toString() + "/" + validateFilename(filename);
		
		FileWriter fileWriter = new FileWriter(uploadedFileLocation,false);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
        bufferedWriter.write(text);
        bufferedWriter.close();
		
    	ThreadDao tDao = new ThreadDao();
    	URL fileUrl = new File(uploadedFileLocation).toURI().toURL();
    	
    	Summary summary = tDao.getSummaryForDoc(fileUrl);
		summary.setSubject(title);		
		
		return Response.ok().entity(new GenericEntity<Summary>(summary){})
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "*")
				.build();
	}
    
	// save uploaded file to new location
	private void writeToFile(InputStream uploadedInputStream,
		String uploadedFileLocation) {
		
		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	private String validateFilename(String filename){
		
		String regx = "\\/:*?\"<>|";
		char[] ca = regx.toCharArray();
		for(char c : ca){
			filename = filename.replace(""+c,"");
		}
		
		return filename;
	}
    
	/**
	 * For testing the json output format
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
    @Path("getsummaryJSON/{path}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGetAsJSON(@PathParam("path") String path) throws Exception{
    	
    	ThreadDao tDao = new ThreadDao();
    	Summary summary = tDao.getSummaryForDoc(getClass().getResource("/docs/testset/test/"+path));
		summary.setSubject(path);	
 	  
    	return Response.ok().entity(new GenericEntity<Summary>(summary){})
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "*")
				.build();
    }
    
    /**
     * For testing the xml output format
     * 
     * @param path
     * @return
     * @throws Exception
     */
    @Path("getsummaryXML/{path}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response doGetAsXML(@PathParam("path") String path) throws Exception{
    	
    	ThreadDao tDao = new ThreadDao();
    	SummaryXML summary = tDao.getSummaryForDocXML(getClass().getResource("/docs/testset/test/"+path));
		summary.setSubject(path);
 	  
    	return Response.ok().entity(new GenericEntity<SummaryXML>(summary){})
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "*")
				.build();
    }
}
