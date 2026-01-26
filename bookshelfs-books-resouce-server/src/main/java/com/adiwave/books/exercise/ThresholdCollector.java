package com.adiwave.books.exercise;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public class ThresholdCollector
        implements Collector<Double, ThresholdAccumulator, List<Double>> {

    private final double threshold;

    public ThresholdCollector(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public Supplier<ThresholdAccumulator> supplier() {
        return () -> new ThresholdAccumulator(threshold);
    }

    @Override
    public BiConsumer<ThresholdAccumulator, Double> accumulator() {
        return ThresholdAccumulator::add;
    }

    @Override
    public BinaryOperator<ThresholdAccumulator> combiner() {
        return ThresholdAccumulator::combine;
    }

    @Override
    public Function<ThresholdAccumulator, List<Double>> finisher() {
        return acc -> acc.values;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.noneOf(Characteristics.class);
    }
}

