package com.adiwave.books.exercise;

import java.util.ArrayList;
import java.util.List;

public class ThresholdAccumulator {
    final double threshold;
    double sum = 0;
    final List<Double> values = new ArrayList<>();
    boolean done = false;

    public ThresholdAccumulator(double threshold) {
        this.threshold = threshold;
    }

    public void add(double value) {
        if (done) return;
        sum += value;
        values.add(value);
        if (sum > threshold) {
            done = true;
        }
    }

    public ThresholdAccumulator combine(ThresholdAccumulator other) {
        if (this.done) return this;
        if (other.done) return other;

        for (double v : other.values) {
            add(v);
            if (done) break;
        }
        return this;
    }
}

