package com.theeye.common;

import com.theeye.api.v1.common.util.Base64Util;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class Base64UtilTest {

     @Nested
     @DisplayName("Given image base64 string")
     class Base64ImageString {

          String imageBase64;
          byte[] imageBytes;

          @Nested
          @DisplayName("When getting image bytes")
          class GetBytes {

               @BeforeEach
               public void setUp() {

                    try {
                         Resource resource = new ClassPathResource("testImageBase64.txt");
                         imageBase64 = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
                         resource = new ClassPathResource("testImage.jpg");
                         imageBytes = IOUtils.toByteArray(resource.getInputStream());
                    } catch (IOException e) {
                         e.printStackTrace();
                    }
               }

               @Test
               @DisplayName("Should return correct byte[]")
               void getImageBytes() {
                    byte[] encodedImageBytes = Base64Util.getImageBytes(imageBase64);
                    assertArrayEquals(imageBytes, encodedImageBytes);
               }

               @Test
               @DisplayName("Should return correct base64")
               void getBase64() {
                    String base64 = Base64Util.getBase64(imageBytes);
                    assertThat(base64).isEqualTo(imageBase64);
               }
          }
     }
}