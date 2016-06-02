package com.example;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

public class SparkFileReader {
    
    public static final void main(final String[] args) {
        System.setProperty("hadoop.home.dir", "/");
        
        final SparkConf sc = new SparkConf().setAppName("SparkFileReader").setMaster("local[*]");
        final JavaSparkContext jsc = new JavaSparkContext(sc);
        
        /** Sample Remote Spark Context **/
        // start master  - ./sbin/start-master.sh --webui-port 10080
        // start slave (url will be different) - ./sbin/start-slave.sh spark://149-163-158-184.dhcp-in.iupui.edu:7077
        // get actual url port 7077 is listening on as master for this job - sudo lsof -i -n -P | grep 7077
        // set that as master, also change path for jars
        //        final SparkConf sparkConf = new SparkConf().setAppName("SparkApp")
        //                .setMaster("spark://mylocalmachine:7077")
        //                .setJars(new String[] {"thisjar.jar"});
        
        final JavaRDD<String> logFile = jsc.textFile("web.log");
        final JavaRDD<String> errors = logFile.filter(new Function<String, Boolean>() {
            
            @Override
            public Boolean call(final String v1) throws Exception {
                return v1.contains("ERROR") || v1.contains("Exception");
            }
            
        });
        final long count = errors.count();
        System.out.println(String.format("There are %d errors", count));
        
        jsc.close();
        
    }
}
