package de.uni.bonn.iai.eis;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import static org.junit.Assert.*;

public class DownloadUtilsTest {

    private static HttpServer httpServer;
    private static String fileUrl;

    private static class OkHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "data";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static class RedirectHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = httpServer.getAddress().toString();
            Headers responseHeaders = httpExchange.getResponseHeaders();
            responseHeaders.add("Location", fileUrl);
            httpExchange.sendResponseHeaders(303, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static class ErrorHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            httpExchange.sendResponseHeaders(404, 0);
        }
    }


    @BeforeClass
    public static void setup() throws IOException {
        InetSocketAddress localhost = new InetSocketAddress("localhost", 9418);
        httpServer = HttpServer.create(localhost, 2);
        httpServer.createContext("/", new OkHttpHandler());
        httpServer.createContext("/redir", new RedirectHttpHandler());
        httpServer.createContext("/error", new ErrorHttpHandler());
        httpServer.start();
        fileUrl = "http:/"+ httpServer.getAddress().toString() + "/file";
    }

    @AfterClass
    public static void tearDown() {
        httpServer.stop(1);
    }

    @Test
    public void testDownload() throws Exception {

        String download = new DownloadUtils().download(fileUrl);
        assertEquals("data", download);
    }

    @Test
    public void testDownloadWithRedirect() throws IOException {
        String url = "http:/"+ httpServer.getAddress().toString() + "/redir";
        String download = new DownloadUtils().download(url);
        assertEquals("data", download);
    }
    
    @Test
    public void testDownloadFail() throws IOException {
        try {
            new DownloadUtils().download("ftp://example.com/file");
            fail("RuntimeException expected");
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
            assertEquals("unknown protocol", e.getMessage());
        }
    }

    @Test
    public void testUnknownResponse() throws IOException {
        try {
            new DownloadUtils().download("http://example.com/error");
            fail("RuntimeException expected");
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
            assertEquals("Can't handle http response code: 404", e.getMessage());
        }
    }

}