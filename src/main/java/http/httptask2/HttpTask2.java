package http.httptask2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpTask2 {
    
    public static ObjectMapper mapper = new ObjectMapper();
    public static final String URL = "https://api.nasa.gov/planetary/apod?api_key=5XdnewjUh0PO3uGcdRiKOi0tIWu38bH2kZElowew";
    public static final String DIR_PREFIX = "/home/aurumbeats/Документы/Проекты Java/HttpTask2/resources";
    
    public static CloseableHttpResponse getHttpResponse(String url) throws IOException {
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(30000)
                .setRedirectsEnabled(false)
                .build())
                .build()
                .execute(new HttpGet(url));
    }

    public static String getFileName(URL url) {
        String[] urlParts = url.toString().split("/");
        int part = urlParts.length - 1;
        return urlParts[part];
    }
    
    public static void main(String[] args) throws IOException {
        URL pictureUrl = new URL(mapper.readValue(getHttpResponse(URL)
                .getEntity().getContent(), 
                new TypeReference<NasaResponse>() {})
        .getUrl());
        String fileName = DIR_PREFIX + "/" + getFileName(pictureUrl);
        byte[] pictureBuffer = getHttpResponse(pictureUrl.toString())
                .getEntity()
                .getContent()
                .readAllBytes();
        File picture = new File(fileName);
        FileOutputStream fos = new FileOutputStream(picture);
        fos.write(pictureBuffer);
        fos.flush();
        fos.close();
    }
}
