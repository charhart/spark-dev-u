package com.example;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

public class SparkDataFrameExample {
    
    public static final void main(final String[] args) {
        final SparkConf sc = new SparkConf().setAppName("SparkDF").setMaster("local[*]");
        final JavaSparkContext jsc = new JavaSparkContext(sc);
        final SQLContext sqlContext = new org.apache.spark.sql.SQLContext(jsc);
        
        // generate some data from https://www.mockaroo.com/
        final DataFrame df = sqlContext.read().json("mockdata.json");
        final long totalPeeps = df.count();
        System.out.println(String.format("%d people from Arkansas", totalPeeps));
        
        System.out.println("\n\n\n\nGrouped by gender:");
        df.groupBy("gender").count().show();
        System.out.println("\n\n\n\n");
        
        final DataFrame injuries = df.filter(df.col("icd_desc").contains("Injury")
                .or(df.col("icd_desc").contains("accident")));
        System.out.println("\n\n\n\nStates with accidents and injuries:");
        injuries.groupBy("state").count().show(100);
        System.out.println("\n\n\n\n");
        
        System.out.println("\n\n\nAccidents and injuries over 60:");
        injuries.filter(df.col("age").gt(59)).select("icd_desc").show();
        System.out.println("\n\n\n\n");
        
        jsc.close();
    }
}
