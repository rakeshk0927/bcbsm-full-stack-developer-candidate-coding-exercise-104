package com.example.fileloader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class APICallWithFormData {
    public static void main(String[] args) {
        try {
            // URL of the API endpoint
            String apiUrl = "https://api.example.com/endpoint";

            // Form data
            String formData = "key1=" + URLEncoder.encode("value1", "UTF-8") +
                    "&key2=" + URLEncoder.encode("value2", "UTF-8");

            // Open connection
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("POST");

            // Enable output for sending form data
            connection.setDoOutput(true);

            // Set content-type header
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Write form data to the connection
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.writeBytes(formData);
            }

            // Read response
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println("Response: " + response.toString());
            }

            // Close connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

