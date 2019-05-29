package org.yeyu.voice;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Try {
    float fDesiredSampleRate = AudioSystem.NOT_SPECIFIED;
    int iDesiredChannels = 1;
    private static final float DELTA = 1E-9F;

    public static void main(String... a) {
        Try tryit = new Try(16000);
        tryit.doit();

    }

    Try(float fDesiredSampleRate) {
        this.fDesiredSampleRate = fDesiredSampleRate;
    }

    void doit() {
        String inputFile = "d:\\BaiduNetdiskDownload\\sound_only.wav";
        inputFile = "d:\\audio\\28mp3_1.wav";
        try(AudioInputStream stream = AudioSystem.getAudioInputStream(new File(inputFile))) {
            AudioFormat format = stream.getFormat();
            System.out.println("source format: " + format);

            AudioInputStream stream2 = null;
            Optional<String> result = Optional.empty();
            if (! equals(stream.getFormat().getSampleRate(), fDesiredSampleRate) || stream.getFormat().getChannels() != iDesiredChannels)
            {
                System.out.println("converting sample rate...");
                stream2 = convertPCM(stream);
                System.out.println("stream: " + stream2);
                System.out.println("format: " + stream2.getFormat());
                result = callTranslateEngine(stream2);
            } else {
                result = callTranslateEngine(stream);
            }

            System.out.println("result length:" + result.orElse("").length());
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AudioInputStream convertPCM(
            AudioInputStream sourceStream)
    {
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat targetFormat = new AudioFormat(
                sourceFormat.getEncoding(),
                fDesiredSampleRate,
                sourceFormat.getSampleSizeInBits(),
                iDesiredChannels,
                calculateFrameSize(iDesiredChannels, sourceFormat.getSampleSizeInBits()),
                fDesiredSampleRate,
                sourceFormat.isBigEndian());
        return AudioSystem.getAudioInputStream(targetFormat,
                sourceStream);
    }

    Optional<String> callTranslateEngine(AudioInputStream ais) {
        Optional<String> translated = Optional.empty();
        String url = "http://10.121.133.10:8080/lasf/army/recognise";

        try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
            byte[] buffer = new byte[1024*1024*5];
            while( ais.read(buffer) != -1) {
                Optional<String> result = makeRequest(httpclient, url, makeEntity(1, 1, buffer));
            }



//            int bytesRead = -1;
//            do {
//                byte[] buffer = new byte[1024*1024];
//                bytesRead = ais.read(buffer);
//                Optional<String> result = makeRequest(httpclient, url, makeEntity(pidx, 0, buffer));
//                System.out.println("bytesRead: " + bytesRead);
//                System.out.println("pidx: " + pidx++);
//            } while (bytesRead != -1);
//
//            makeRequest(httpclient, url, makeEntity(pidx, 1, new byte[]{}));

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

    private HttpEntity makeEntity(int pidx, int over, byte[] data) {
        MultipartEntityBuilder meb = MultipartEntityBuilder.create();
        meb.addTextBody("ixid", "124");
        meb.addTextBody("uid", "22233");
        meb.addTextBody("pidx", String.valueOf(pidx));
        meb.addTextBody("over", String.valueOf(over));
        meb.addBinaryBody("voicedata", new ByteArrayInputStream(data), ContentType.APPLICATION_OCTET_STREAM, "someone");
//        meb.addBinaryBody("voicedata", data);
        return meb.build();
    }

    static void http1() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        // The underlying HTTP connection is still held by the response object
        // to allow the response content to be streamed directly from the network socket.
        // In order to ensure correct deallocation of system resources
        // the user MUST call CloseableHttpResponse#close() from a finally clause.
        // Please note that if response content is not fully consumed the underlying
        // connection cannot be safely re-used and will be shut down and discarded
        // by the connection manager.
        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        } finally {
            response1.close();
        }
    }

    /** Compares two float values for equality.
     */
    private static boolean equals(float f1, float f2)
    {
        return (Math.abs(f1 - f2) < DELTA);
    }

    private static int calculateFrameSize(int nChannels, int nSampleSizeInBits)
    {
        return ((nSampleSizeInBits + 7) / 8) * nChannels;
    }
}
