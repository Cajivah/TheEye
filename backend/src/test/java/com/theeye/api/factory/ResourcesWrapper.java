package com.theeye.api.factory;

import org.assertj.core.util.Lists;

import java.util.List;

public class ResourcesWrapper {

     public static List<String> IMAGES = Lists.newArrayList(
             "startingSetUp/big_blackwhite_brown_webcam.jpg", //0
             "startingSetUp/printed_blackwhite_brown_webcam.jpg", //1
             "startingSetUp/printed_blackwhite_brown_webcam2.jpg", //2
             "startingSetUp/printed_blackwhite_brown_webcam3.jpg", //3
             "startingSetUp/printed_blackwhite_empty_webcam.jpg", //4
             "startingSetUp/printed_blackwhite_empty_webcam_white-back.jpg", //5
             "startingSetUp/big_blackwhite_brown_webcam.jpg", //6
             "startingSetUp/big_blackwhite_empty_webcam.jpg",  //7
             "startingSetUp/big_blackwhite_brown_webcam_downscaled2.jpg",//8
             "startingSetUp/big_blackwhite_brown_webcam2.jpg"  //9
     );

     public static String getResourceAsBase64(String path) {
          return null;
     }
}
