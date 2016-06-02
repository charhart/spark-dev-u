package com.example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithSGD;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.rdd.RDD;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Strings;

public class SparkLogisticRegression implements Serializable {
    
    final protected int numberOfFeatures;
    
    public SparkLogisticRegression(final int numberOfFeatures) {
        this.numberOfFeatures = numberOfFeatures;
    }
    
    public LogisticRegressionModel train(final JavaSparkContext sc, final JavaRDD<String> posData,
                                         final JavaRDD<String> negData, final boolean saveAsLibSVM,
                                         final String outputDirectory) {
        
        // Create a HashingTF instance to map text to vectors to features.
        final HashingTF tf = new HashingTF(this.numberOfFeatures);
        
        // Each email is split into words, and each word is mapped to one feature.
        final JavaRDD<LabeledPoint> positiveExamples = posData.map(new Function<String, LabeledPoint>() {
            
            /**
             * 
             */
            private static final long serialVersionUID = -4606885567710088306L;
            
            @Override
            public LabeledPoint call(final String data) {
                return new LabeledPoint(1, tf.transform(Arrays.asList(data.split(" "))));
            }
        });
        final JavaRDD<LabeledPoint> negativeExamples = negData.map(new Function<String, LabeledPoint>() {
            
            /**
             * 
             */
            private static final long serialVersionUID = -5910592718919443197L;
            
            @Override
            public LabeledPoint call(final String data) {
                return new LabeledPoint(0, tf.transform(Arrays.asList(data.split(" "))));
            }
        });
        final JavaRDD<LabeledPoint> trainingData = positiveExamples.union(negativeExamples);
        
        if (saveAsLibSVM && !Strings.isNullOrEmpty(outputDirectory)) {
            saveFile(trainingData, outputDirectory);
        }
        
        return learn(trainingData);
        
    }
    
    protected void saveFile(final JavaRDD<LabeledPoint> trainingData, final String outputDirectory) {
        MLUtils.saveAsLibSVMFile(trainingData.rdd(), outputDirectory);
    }
    
    protected LogisticRegressionModel learn(final JavaRDD<LabeledPoint> trainingData) {
        trainingData.cache(); // Cache data since Logistic Regression is an iterative algorithm.
        
        // Create a Logistic Regression learner which uses the LBFGS optimizer.
        final LogisticRegressionWithSGD lrLearner = new LogisticRegressionWithSGD();
        // Run the actual learning algorithm on the training data.
        final LogisticRegressionModel model = lrLearner.run(trainingData.rdd());
        return model;
    }
    
    public Map<String, Double> testModel(final LogisticRegressionModel model, final List<String> testStrings) {
        final Map<String, Double> results = new HashMap<String, Double>();
        
        if ((model == null) || CollectionUtils.isEmpty(testStrings)) {
            return results;
        }
        // Create a HashingTF instance to map text to vectors of features.
        final HashingTF tf = new HashingTF(this.numberOfFeatures);
        
        for (final String test : testStrings) {
            final Vector testVector = tf.transform(Arrays.asList(test.split(" ")));
            results.put(test, model.predict(testVector));
        }
        
        return results;
    }
    
    public static void main(final String[] args) throws Exception {
        /** Local Spark Context **/
        final SparkConf sparkConf = new SparkConf().setAppName("SparkLogisticRegression").setMaster("local[*]");
        
        // init java spark context
        final JavaSparkContext sc = new JavaSparkContext(sparkConf);
        
        final JavaRDD<String> sonnets = sc.textFile("sonnets.txt");
        final JavaRDD<String> lorem = sc.textFile("lorem.txt");
        
        final SparkLogisticRegression lr = new SparkLogisticRegression(100);
        final String dir = "/opt/mllib/logisticregression/" + System.currentTimeMillis();
        final LogisticRegressionModel model = lr.train(sc, sonnets, lorem, true, dir);
        final Map<String, Double> results = lr
                .testModel(
                    model,
                    Arrays.asList(
                        "What light is light, if Silvia be not seen? What joy is joy, if Silvia be not by? Unless it be to think that she is by. And feed upon the shadow of perfection. ",
                        "Pop goes the weasel",
                        "It's just a flesh wound",
                        "Shall I compare thee to a lorem ipsum day?",
                        "Bippbidity Boppity Boo",
                        "Colorless green dreams sleep furiously",
                        "Expelliarmus!",
                        "Let's eat at the Indian buffet",
                        "Dolor sit amet, consectetur adipiscing elit"
                        ));
        for (final Map.Entry<String, Double> entry : results.entrySet()) {
            System.out.println("for: '" + entry.getKey() + "', prediction: " + entry.getValue());
        }
        
        final RDD<LabeledPoint> loadedRDD = MLUtils.loadLibSVMFile(sc.sc(), dir);
        Assert.notNull(loadedRDD);
        System.out.println(loadedRDD);
        
        sc.stop();
        sc.close();
        
    }
}
