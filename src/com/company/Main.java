package com.company;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    private final static int ARRAY_SIZE = 1000;

    private ExecutorService executor = Executors.newFixedThreadPool(10);
    private final Map<Integer, BigInteger> calculatedValues = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Main main = new Main();
        main.run();
    }

    private void run() throws InterruptedException {
        Integer[] array = generateRandomArray();
        //Исключаем дубликаты
        Set<Integer> set = new HashSet<>(Arrays.asList(array));
        List<Integer> list = new ArrayList<>(set);
        list.sort(Comparator.naturalOrder());
        for (Integer value : list) {
            executor.submit(new FactorialCalculator(value, calculatedValues));
        }
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.MINUTES);
        for (int i = 0; i < array.length; i++) {
            //System.out.println(String.format("factorial of %d is %s", array[i], calculatedValues.get(array[i])));
        }
        System.out.println(FactorialCalculator.duplicateCounter + " duplicated calculations");
        System.out.println(FactorialCalculator.uniqueCounter + " unique calculations");
    }

    private Integer[] generateRandomArray() {
        Random random = new Random();
        Integer[] result = new Integer[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            result[i] = random.nextInt(1000);
        }
        return result;
    }
}
