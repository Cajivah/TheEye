package com.theeye.api.v1.chess.analysis.collector.accumulator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.opencv.core.Scalar;

@Getter
@AllArgsConstructor
public class ScalarAccumulator {

     private Scalar scalar;
     private int count;

     public ScalarAccumulator() {
          scalar = new Scalar(0, 0, 0);
          count = 0;
     }

     public void add(Scalar arg) {
          this.scalar.val[0] += arg.val[0];
          this.scalar.val[1] += arg.val[1];
          this.scalar.val[2] += arg.val[2];
          this.count++;
     }

     public ScalarAccumulator combineWith(ScalarAccumulator arg) {
          this.scalar.val[0] += arg.scalar.val[0];
          this.scalar.val[1] += arg.scalar.val[1];
          this.scalar.val[2] += arg.scalar.val[2];
          this.count += arg.count;
          return this;
     }

     public Scalar toAverage() {
          return count == 0
                  ? new Scalar(0, 0, 0)
                  : new Scalar(
                  this.scalar.val[0] / count,
                  this.scalar.val[1] / count,
                  this.scalar.val[2] / count);
     }
}
