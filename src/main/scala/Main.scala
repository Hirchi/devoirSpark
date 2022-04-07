import org.apache.spark.sql._
import org.apache.spark._
import org.apache.spark.sql.functions.explode
import org.apache.spark.sql.functions.col

object Main {
  def main(args: Array[String]) = {
    println("Hello world!")
    val spark =
      SparkSession.builder().config("spark.master", "local").getOrCreate()

    val df = spark.read.json(
      "./reports_2022-04-07-14-26-07/part-00000-09eeda68-b1ae-467c-a18a-302beb409b49-c000.json"
    )

    val df3 = df.select(col("id"), explode(col("citizens")).as("citizen"))
    val df4 = df3.select(col("id"), col("citizen.*"))
    val df4Flatten = df4.toDF("id", "citizenName", "citizenPeacescore")
    df4Flatten.printSchema()
    df4Flatten.show()
  }
}
