package org.yeyu.voice;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public class FileTranslate {

    float fDesiredSampleRate = AudioSystem.NOT_SPECIFIED;
    private static final float DELTA = 1E-9F;

    public static void main(String... a) {
        FileTranslate tryit = new FileTranslate(16000);
        tryit.doit();
    }

    FileTranslate(float fDesiredSampleRate) {
        this.fDesiredSampleRate = fDesiredSampleRate;
    }

    void doit() {
        String filePath = "d:\\welcome2-16k.pcm";
//        filePath = "d:\\welcome2.wav";
        filePath = "d:\\welcome2-16K-1C.wav";
//        filePath = "d:\\28-16k-1c.wav";
        File f = new File(filePath);
        callTranslateEngine2(f);
    }

    Optional<String> callTranslateEngine2(File f) {
        Optional<String> translated = Optional.empty();
        String url = "http://10.121.133.10:8080/lasf/army/recognise";

        try(CloseableHttpClient httpclient = HttpClients.createDefault()) {

            MultipartEntityBuilder meb = MultipartEntityBuilder.create();
            meb.addTextBody("ixid", "123");
            meb.addTextBody("uid", "22233");
            meb.addTextBody("pidx", String.valueOf(1));
            meb.addTextBody("over", String.valueOf(1));
            meb.addBinaryBody("voicedata", f);
//            meb.addBinaryBody("voicedata", new FileInputStream(f), ContentType.APPLICATION_OCTET_STREAM, f.getName());

            makeRequest(httpclient, url, meb.build());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return translated;
    }

    private Optional<String> makeRequest(CloseableHttpClient httpclient, String url, HttpEntity entity) {
        Optional<String> or = Optional.empty();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);

        try(CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            String ret = EntityUtils.toString(entity2);
            System.out.println(ret);
            or = Optional.ofNullable(ret);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return or;
    }

    /** Compares two float values for equality.
     */
    private static boolean equals(float f1, float f2)
    {
        return (Math.abs(f1 - f2) < DELTA);
    }
}
