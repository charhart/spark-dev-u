println("\nCreate a Scala collection")
val data = 1 to 1000

println("\nConvert data to an RDD  - Resiliant Distributed Dataset")
val dist = sc.parallelize(data)

println("\nFilter out odds (a transformation), then call collect (an action) to show the data")
dist.filter(_ % 2 == 0).collect()


