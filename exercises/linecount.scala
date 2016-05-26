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
