package com.example;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.springframework.stereotype.Component;

@Component
public class SparkBasics {
    
    public List<Integer> randomInts(final int size) {
        final List<Integer> ints = new ArrayList<Integer>();
        
        int i = 0;
        while (i++ < size) {
            ints.add((int) ((Math.random() * 100) % 10));
        }
        return ints;
    }
    
    public static void main(final String[] args) {
        final SparkConf sc = new SparkConf().setAppName("SparkBasicsRunner").setMaster("local[*]");
        final JavaSparkContext jsc = new JavaSparkContext(sc);
        final SparkBasics app = new SparkBasics();
        
        final List<Integer> ints = app.randomInts(5);
        final JavaRDD<Integer> rdd = jsc.parallelize(ints);
        final JavaRDD<Integer> squaredRdd = rdd.map(new Function<Integer, Integer>() {
            
            @Override
            public Integer call(final Integer v1) throws Exception {
                return v1 * v1;
            }
            
        });
        final JavaRDD<Integer> distinctSquaredRdd = squaredRdd.distinct();
        final Integer biggest = distinctSquaredRdd.reduce(new Function2<Integer, Integer, Integer>() {
            
            @Override
            public Integer call(final Integer v1, final Integer v2) throws Exception {
                return v1 > v2 ? v1 : v2;
            }
            
        });
        System.out.println(String.format("The biggest square is %s", biggest));
        
        jsc.close();
        
    }
}
