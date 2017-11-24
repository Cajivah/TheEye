package com.theeye.api.v1.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

     public static boolean isNullOrEmpty(String s) {
          return s == null || s.isEmpty();
     }
}
