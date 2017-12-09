package com.theeye.api.v1.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Base64Util {

     private static final int BASE64_IMAGE_HEADER_SECTION = 0;
     private static final int BASE64_IMAGE_CONTENT_SECTION = 1;
     private static final String BASE64_IMAGE_SECTION_SEPARATOR = ",";
     public static final String JPEG_PREFIX = "data:image/jpeg;base64,";

     public static byte[] getImageBytes(String imageBase64) {
          return Base64.getDecoder().decode(getDataSection(imageBase64));
     }

     public static String getDataSection(String imageBase64) {
          String[] sections = imageBase64.split(BASE64_IMAGE_SECTION_SEPARATOR);
          return sections[BASE64_IMAGE_CONTENT_SECTION];
     }

     public static String getBase64(byte[] image) {
          return JPEG_PREFIX + new String(Base64.getEncoder().encode(image));
     }
}
