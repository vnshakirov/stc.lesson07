package com.company;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class FactorialCalculator implements Runnable {

    private final Map<Integer, BigInteger> calculatedValues;
    private final Integer requiredValue;
    static final AtomicInteger duplicateCounter = new AtomicInteger();
    static final AtomicInteger uniqueCounter = new AtomicInteger();

    public FactorialCalculator(Integer requiredValue, Map<Integer, BigInteger> calculatedValues) {
        this.requiredValue = requiredValue;
        this.calculatedValues = calculatedValues;
    }

    @Override
    public void run() {
        if (!calculatedValues.containsKey(requiredValue)) {
            Optional<Integer> max = calculatedValues.keySet().stream().max(Comparator.naturalOrder());
            if (max.isPresent()) {
                BigInteger previousValue = calculatedValues.get(max.get());
                calculate(max.get(), requiredValue, previousValue);
            } else {
                calculate(1, requiredValue, BigInteger.ONE);
            }
        }
    }

    private BigInteger calculate(Integer from, Integer to, BigInteger calculated) {
        BigInteger result = calculated;
        for (int i = from + 1; i <= to; i++) {
            result = result.multiply(BigInteger.valueOf(i));
            if (calculatedValues.putIfAbsent(i, result) != null) {
                duplicateCounter.incrementAndGet();
            } else {
                uniqueCounter.incrementAndGet();
            }
        }
        return result;
    }
}
