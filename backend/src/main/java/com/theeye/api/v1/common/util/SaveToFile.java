package com.theeye.api.v1.common.util;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class SaveToFile {
     public static void save(Mat mat, String filename){
          MatOfByte mob = new MatOfByte();
          Imgcodecs.imencode(".jpg", mat, mob);
          byte[] ba = mob.toArray();
          BufferedImage save = null;
          try {
               save = ImageIO.read(new ByteArrayInputStream(ba));
               File outputfile = new File("test-results/" + filename + ".jpg");
               ImageIO.write(save, "jpg", outputfile);
          } catch (IOException e) {
               e.printStackTrace();
          }
     }
}
