package uf3_pvp_ex2;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader {
    private URL baseUrl;
    private String title;

    public Downloader(String title) throws MalformedURLException {
        baseUrl = new URL("https://www.imdb.com/");
        this.title = title;
    }

    public File download() throws IOException {
        URL url = new URL(baseUrl, "title/" + title);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        File file = new File( title.toLowerCase() + ".html");
        if (!file.exists()) {
            file.createNewFile();
        } else if (file.exists()) {
            file.delete();
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(line);
            writer.newLine();
            writer.flush();
        }

        reader.close();
        writer.close();
        return file;
    }
}