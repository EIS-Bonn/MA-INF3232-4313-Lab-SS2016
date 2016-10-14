/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.restful;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author blzofhk
 */
public class ClientTest {

    public static void main(String[] args) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPut put = new HttpPut("http://localhost:8080/ss16-lab-web/resources/outliers/session");
        put.setEntity(new StringEntity("upenkwbq"));// session ID
        client.execute(put);
        put.releaseConnection();

        put = new HttpPut("http://localhost:8080/ss16-lab-web/resources/outliers/bucket");
        put.setEntity(new StringEntity("Level1/Level1_Bin_1.txt")); // bucket name
        client.execute(put);
        put.releaseConnection();

        put = new HttpPut("http://localhost:8080/ss16-lab-web/resources/outliers/method");
        put.setEntity(new StringEntity("chauvenet")); // method name
        client.execute(put);
        put.releaseConnection();

        HttpGet get = new HttpGet("http://localhost:8080/ss16-lab-web/resources/outliers");
        HttpResponse response = client.execute(get);
        HttpEntity en = response.getEntity();
        InputStreamReader i = new InputStreamReader(en.getContent());
        BufferedReader rd = new BufferedReader(i);
        String line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
    }

}
