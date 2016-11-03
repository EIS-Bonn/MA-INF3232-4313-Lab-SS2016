package de.uni.bonn.iai.eis;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class DownloadUtils {

    private static final Logger log = LoggerFactory.getLogger(DownloadUtils.class);

    public String download(String urlString) throws IOException {
        log.info("Attempting to download: {}", urlString);

        URL url = new URL(urlString);

        //handle redirect, http to https possible
        URLConnection urlConnection = url.openConnection();

        if (urlConnection instanceof HttpURLConnection) {
            return downloadHttp(urlConnection);
        } else {
            throw new RuntimeException("unknown protocol");
        }

    }

    private String downloadHttp(URLConnection urlConnection) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) urlConnection;
        conn.setInstanceFollowRedirects(false);
        int responseCode = conn.getResponseCode();

        if (responseCode == 303) {
            String location = conn.getHeaderField("Location");
            log.info("Following redirect to: {}", location);
            return download(location);
        }

        if (responseCode == 200) {
            InputStream inputStream = conn.getInputStream();
            String result = IOUtils.toString(inputStream, Charset.forName("UTF-8"));

            inputStream.close();
            conn.disconnect();

            return result;
        } else {
            throw new RuntimeException("Can't handle http response code: " + responseCode);
        }
    }
}