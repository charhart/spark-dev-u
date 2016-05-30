// Run interactively line by line in Spark Shell or
// exec through Spark Shell with the following command
// :load /home/spark/spark-dev-u/exercises/linecount.scala

println("Load The Adventures of Sherlock Holmes")
val lines = sc.textFile("/home/spark/spark-dev-u/data/sherlock.txt")

println("\nLine count")
val lineCnt = lines.count

println("\nLines containing Watson:")
val watsonLines = lines.filter(line => line.contains("Watson"))
watsonLines.count

println("\nCreate a function to find Watson")
val isWatson = (line:String) => line.contains("Watson")
val watsonLines2 = lines.filter(isWatson)
watsonLines2.count

println("\nPrint lines containing Watson")
watsonLines2.foreach(line => println(line))

println("\nWord count")
val words = lines.flatMap(lines =>lines.toLowerCase().split("\\W"))
val wc = words.map(word => (word.toLowerCase(),1)).reduceByKey((a,b) => a+b)
wc.collect()

println("\nGet the most common words")
wc.sortBy(_._2,false).collect()

println("\nGet the most common words after removing stop words!")
val stopwords = sc.textFile("/home/spark/spark-dev-u/data/stopwords.txt")
val realwords = words.subtract(stopwords)
val realwc = realwords.map(word => (word.toLowerCase(),1)).reduceByKey((a,b) => a+b)
realwc.sortBy(_._2,false).collect()


println("\nLet's try with another data set!")
val hc = sc.textFile("/home/spark/spark-dev-u/data/hc_emails.txt")
val words2 = hc.flatMap(hc =>hc.toLowerCase().split("\\W"))
val realwords2 = words2.subtract(stopwords)
val realwc2 = realwords2.map(word => (word.toLowerCase(),1)).reduceByKey((a,b) => a+b).sortBy(_._2,false)
realwc2.take(1000).foreach(println)


