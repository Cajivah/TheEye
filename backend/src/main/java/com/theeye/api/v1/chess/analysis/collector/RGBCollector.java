package com.theeye.api.v1.chess.analysis.collector;

import com.google.common.collect.Sets;
import com.theeye.api.v1.chess.analysis.collector.accumulator.ScalarAccumulator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.opencv.core.Scalar;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RGBCollector {

     public static Collector<Scalar, ScalarAccumulator, Scalar> getRgbAverageCollector() {
          return new Collector<Scalar, ScalarAccumulator, Scalar>() {
               @Override
               public Supplier<ScalarAccumulator> supplier() {
                    return ScalarAccumulator::new;
               }

               @Override
               public BiConsumer<ScalarAccumulator, Scalar> accumulator() {
                    return ScalarAccumulator::add;
               }

               @Override
               public BinaryOperator<ScalarAccumulator> combiner() {
                    return ScalarAccumulator::combineWith;
               }

               @Override
               public Function<ScalarAccumulator, Scalar> finisher() {
                    return ScalarAccumulator::toAverage;
               }

               @Override
               public Set<Characteristics> characteristics() {
                    return Sets.newHashSet(Characteristics.UNORDERED);
               }
          };
     }
}
