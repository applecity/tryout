package org.yeyu.socket.readfile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WavFileRead {
    @org.junit.Test
    public void testGo1() {
        Path p = Paths.get("d:\\originalwav1_16k1c.wav");
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(p.toFile())))) {

            int x = 1;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
