package com.example.mohammedsubhi.merabiaandroid;

import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Guzelware on 11/15/2016.
 */
public class RestClient {
    private ArrayList<Pair> params = new ArrayList<>();
    String Url;
    public static final String GET = "GET";
    public static final String POST = "POST";
    public String body;
    String response = "";
    public RestClient(String url) {
        this.Url = url;
    }

    public void addParam(String name, String value) {
        params.add(new Pair(name, value));
    }

    public String execute(String method) {


        try {
            String combinedParams = "";
            if (!params.isEmpty()) {
                combinedParams += "?";
                for (Pair p : params) {
                    String paramString = null;
                    try {
                        paramString = p.first + "=" + URLEncoder.encode(String.valueOf(p.second), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (combinedParams.length() > 1) {
                        combinedParams += "&" + paramString;
                    } else {
                        combinedParams += paramString;
                    }
                }
            }


            URL url = new URL(Url + combinedParams);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (method.equals(GET)) {
                try {


                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");


                } catch (Exception e) {

                    e.printStackTrace();

                }
            } else if (method.equals(POST)) {

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStream os = conn.getOutputStream();
                os.write(body.getBytes());
                os.flush();


            }


            BufferedReader br;
            if (conn.getResponseCode() >= conn.HTTP_BAD_REQUEST) {
                br = new BufferedReader(new InputStreamReader(
                        (conn.getErrorStream())));
            } else {
                br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
            }
            String output;

            while ((output = br.readLine()) != null) {
                response += output + "\n";
            }

            conn.disconnect();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
