package com.theeye.api.v1.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Base64Util {

     private static final int BASE64_IMAGE_HEADER_SECTION = 0;
     private static final int BASE64_IMAGE_CONTENT_SECTION = 1;
     private static final String BASE64_IMAGE_SECTION_SEPARATOR = ",";

     public static byte[] getImageBytes(String imageBase64) {
          String[] sections = imageBase64.split(BASE64_IMAGE_SECTION_SEPARATOR);
          return Base64.getDecoder().decode(sections[BASE64_IMAGE_CONTENT_SECTION]);
     }
}
