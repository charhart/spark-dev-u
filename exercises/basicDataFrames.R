p <- read.df(sqlContext, "/home/spark/spark-dev-u/data/pricing.json","json")

# read structure
str(p)

# select some housing prices
head(select(p, p$house_price))

# select just the hoosiers
head(filter(p, p$state=='Indiana'))

# use the sql context
registerTempTable(p, "p")
richkids = sql(sqlContext, "select * from p where age < 19 order by house_price desc limit 20")
head(richkids)
