package org.yeyu.voice.wavfile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Test {
    public static void main(String... args) {
        new Test().go1();
    }

    /**
     * 此实验证明了 对AudioInputStream对象调用read方法时，直接从文件头之后的一个字节读起。
     */
    void go1() {
        Path p = Paths.get("d:\\originalwav1_16k1c.wav");
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(p.toFile())) {
            System.out.println(ais.getFormat().toString());

            byte[] buffer = new byte[2];
            int read;
            int fourCount = 1600;
            while ((read = ais.read(buffer)) != -1) {
                long noneZeroByteCount = IntStream.range(0, buffer.length).map(idx -> buffer[idx]).filter(ele -> ele != 0).count();
                if(noneZeroByteCount == 0) {
                    System.out.println("o");
                } else {
                    System.out.println("v");
                }
                fourCount--;
                if(fourCount == 0) {
                    break;
                }
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
