package language.java.net.http;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class HttpUtils{

    private static void sendFormData(byte[] data){
        HttpURLConnection conn = null;StringBuilder result = new StringBuilder();
        try{
            URL url = new URL("http://localhost:8080/home/upload");
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            String boundary = UUID.randomUUID().toString();
            conn.addRequestProperty("Content-type","multipart/form-data; boundary="+boundary);

            String prefix = "--" + boundary;
            String end = "--" + boundary + "--";
            String newLine = "\r\n";
            conn.getOutputStream().write(newLine.getBytes(StandardCharsets.UTF_8));
            conn.getOutputStream().write(prefix.getBytes(StandardCharsets.UTF_8));
            conn.getOutputStream().write(newLine.getBytes(StandardCharsets.UTF_8));
            result.append("Content-Disposition: form-data; name=\"Dongle\"");
            result.append(newLine);
            result.append("Content-Type: text/plain");
           result.append(newLine);
            result.append(newLine);
            conn.getOutputStream().write(result.toString().getBytes(StandardCharsets.UTF_8));
            conn.getOutputStream().write(data);
            conn.getOutputStream().write(newLine.getBytes(StandardCharsets.UTF_8));
            conn.getOutputStream().write(end.getBytes(StandardCharsets.UTF_8));
            conn.connect();
            result = new StringBuilder();
            if (conn.getResponseCode() == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                String temp;
                while (null != (temp = br.readLine())) {
                    result.append(temp);
                }
            }
            System.out.println(result);
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (conn != null) conn.disconnect();
        }

    }
}