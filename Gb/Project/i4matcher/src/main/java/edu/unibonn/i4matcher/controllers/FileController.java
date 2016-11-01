package edu.unibonn.i4matcher.controllers;

import java.io.*;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
//import javax.json;

import edu.unibonn.i4matcher.SparqlQuery;
import org.apache.jena.query.QueryParseException;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

//import com.hp.hpl.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Model;

import edu.unibonn.i4matcher.model.FileMeta;
import edu.unibonn.i4matcher.helpers.*;
import edu.unibonn.i4matcher.Matcher;

@Controller
@RequestMapping("/controller")
public class FileController  { //extends HttpServlet
	private static final Logger logger = Logger.getLogger("FileController");
	/***************************************************
	 * URL: /rest/controller/upload/{value}
	 * upload(): receives files
	 * @param request : MultipartHttpServletRequest auto passed
	 * @param response : HttpServletResponse auto passed
	 * @param value: String type of matching
	 * @return Response as json format
	 ****************************************************/
	@RequestMapping(value="/upload/{value}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Response upload(
			MultipartHttpServletRequest request,
			HttpServletResponse response,
			@PathVariable String value) {
		ServletContext servletContext = request.getSession().getServletContext();
		String path = servletContext.getRealPath("/WEB-INF/classes/");
		LinkedList<FileMeta> files = new LinkedList<FileMeta>();
		FileMeta fileMeta = null;

		logger.info(request.getRequestHeaders().toString());
		//1. build an iterator
		 Iterator<String> itr =  request.getFileNames();
		 MultipartFile mpf;

		 while(itr.hasNext()){

			 mpf = request.getFile(itr.next());
			 if(files.size() >= 10)
				 files.pop();
			 fileMeta = new FileMeta();
			 fileMeta.setFileName(mpf.getOriginalFilename());
			 fileMeta.setFileSize(mpf.getSize()/1024+" Kb");
             try {
                 fileMeta.setBytes(mpf.getBytes());
             } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }

			 try {
				 String schema = DocumentIdentifier.getFileType(mpf.getOriginalFilename());
				 XSDValidator validator = new XSDValidator(schema+".xsd");
                 fileMeta.setFileType(schema);
				 //InputStream is = new ByteArrayInputStream(mpf.getBytes());

				 if (!validator.validateAgainstXSD(new ByteArrayInputStream(fileMeta.getBytes()))){
					logger.log(Level.SEVERE, "File "+fileMeta.getFileName()+" not valid");
				 }
				 RDFTransformer pecker = new RDFTransformer();
				 logger.info(fileMeta.getFileName() +"Transformed to AML");
				 byte[] ttl = pecker.transform(new ByteArrayInputStream(fileMeta.getBytes()), schema);
                 fileMeta.setTtl(ttl);
				 //is.close();
			 } catch (Exception ex){
				 ex.printStackTrace();
			 }

			 files.add(fileMeta);
			 
		 }
        Matcher matcher = new Matcher(value);
        try {
            Model model = matcher.match2Files(files);
			logger.info("Matched two files");
            TripleStoreWriter writer = new TripleStoreWriter();
            String ret = writer.write(model);
            Response resp = new Response(ret);
			logger.info(resp.toString());
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Response("Error!");
 
	}
	/***************************************************
	 * URL: /rest/controller/get?query={qry}
	 * get(): get file as an attachment
	 * @param response : passed by the server
	 * @param qry : value from the URL
	 * @return void
	 ****************************************************/
	@RequestMapping(value = "/get" +
			"", method = RequestMethod.GET, produces = "application/json")
	public void get(HttpServletResponse response,
					 //@PathVariable String value
					 @RequestParam(value = "query") String qry) {
//
		SparqlQuery sq = new SparqlQuery();
		String res = "";
		try {
			String query = URLDecoder.decode(qry, "UTF-8");
			res = sq.getResult(query);
			//response.setHeader("Content-disposition", "attachment; filename=\""+getFile.getFileName()+"\"");

		} catch (QueryParseException e){
			res = "{\"error\":\"Query not valid\"}";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			response.setContentType("application/json");
			FileCopyUtils.copy(res.getBytes(), response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
 
}
