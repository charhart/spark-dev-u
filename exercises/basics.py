myRdd = sc.parallelize([1,2,3,4,5,6,7,8,9,10])
# multiply everything by 2
# only get the ones less greater 10
myRdd.map(lambda x: x * 2).filter(lambda x : x > 10).collect()

# remove dups
myOtherRdd = sc.parallelize([1, 5, 10, 15, 15, 25, 10, 30])
myOtherRdd.distinct().collect()

# do a map that performs a lambda function, and get the first 5
myOtherRdd.flatMap(lambda x: [x, x * 5]).take(5)