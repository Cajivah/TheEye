package com.theeye.api.v1.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ComparisionUtil {

     public static double getSmaller(double a, double b) {
          return a < b ? a : b;
     }

     public static double getBigger(double a, double b) {
          return a > b ? a : b;
     }
}
