package org.yeyu.voice;

import java.io.*;
import javax.sound.sampled.*;
import org.apache.commons.lang.Validate;

/**
 * @author Alexey Grigorev
 */
public class AudioFileReader implements Closeable {

    private static final int BITS_IN_BYTE = 8;

    private AudioInputStream audioInputStream;
    AudioFormat format;

    public AudioFileReader(File file) throws IOException, UnsupportedAudioFileException {
        Validate.isTrue(file.exists());
        Validate.isTrue(!file.isDirectory());
        this.audioInputStream = audioFromFile(file);
        this.format = audioInputStream.getFormat();
    }

    private static AudioInputStream audioFromFile(File file) throws IOException, UnsupportedAudioFileException {
        try {
            return AudioSystem.getAudioInputStream(file);
        } catch (Exception e) {
            throw e;
        }
    }

    public AudioFileReader(InputStream stream) throws IOException, UnsupportedAudioFileException {
        Validate.notNull(stream);
        this.audioInputStream = audioFromInputStream(stream);
        this.format = audioInputStream.getFormat();
    }

    private static AudioInputStream audioFromInputStream(InputStream inputStream) throws IOException, UnsupportedAudioFileException {
        try {
            return AudioSystem.getAudioInputStream(inputStream);
        } catch (Exception e) {
            throw e;
        }
    }

    public long getSampleCountPerChannel() {
        long totalSamples = getTotalSampleCount();
        return totalSamples / format.getChannels();
    }

    public long getTotalSampleCount() {
        long sizeInBits = audioInputStream.getFrameLength() * format.getFrameSize() * BITS_IN_BYTE;
        return sizeInBits / format.getSampleSizeInBits();
    }

    public double[] readAll() throws IOException {
        long sampleCount = getTotalSampleCount();
        return readInterval((int) sampleCount);
    }

    public double[] readInterval(int len) throws IOException {
        int sizeInByte = format.getSampleSizeInBits() / BITS_IN_BYTE;
        int totalSizeInBytes = len * sizeInByte * format.getChannels();

        byte[] bytes = readNext(totalSizeInBytes);

        // decode bytes into samples. Supported encodings are:
        // PCM-SIGNED, PCM-UNSIGNED, A-LAW, U-LAW
        double[] result = decodeBytes(bytes, len);
        return result;
    }

    private byte[] readNext(int totalSizeInBytes) throws IOException {
        byte[] bytes = new byte[totalSizeInBytes];
        audioInputStream.read(bytes, 0, totalSizeInBytes);
        return bytes;
    }

    private double[] decodeBytes(byte[] bytes, int numberOfSamples) {
        int sampleSizeInBytes = format.getSampleSizeInBits() / BITS_IN_BYTE;
        int[] read = AudioIOUtils
                .decodeBytes(bytes, numberOfSamples, sampleSizeInBytes, format.isBigEndian());

        double[] audioSamples = new double[numberOfSamples];
        for (int i = 0; i < numberOfSamples; i++) {
            audioSamples[i] = decodeInt(read[i]);
        }

        return audioSamples;
    }

    private double decodeInt(int ival) {
        double ratio = Math.pow(2.0, format.getSampleSizeInBits() - 1);
        return ival / ratio;
    }

    @Override
    public void close() throws IOException {
        if (audioInputStream != null) {
            audioInputStream.close();
        }
    }

    public double[] readMono() throws IOException {
        return readOneChannel(0);
    }

    public double[] readOneChannel(int channel) throws IOException {
        int samplesCount = (int) getSampleCountPerChannel();

        double[] allSamples = readAll();
        int numberOfChannels = format.getChannels();

        double[] result = new double[samplesCount];
        for (int i = channel; i < samplesCount; i = i + numberOfChannels) {
            result[i] = allSamples[i];
        }

        return result;
    }
}
