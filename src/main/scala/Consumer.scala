import java.util.{Collections, Properties}
import java.util.regex.Pattern
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.Instant
import java.time.Duration
import java.time.ZoneId
import org.apache.kafka.clients.consumer.KafkaConsumer
import scala.collection.JavaConverters._
import net.liftweb.json._
import org.apache.spark.sql._
import org.apache.spark._
import ReportsGenerator.Report
import org.apache.spark.sql.functions.explode
import org.apache.spark.sql.functions.col

object Consumer extends App {

  def retrieveReport(topic: String) {
    val spark =
      SparkSession.builder().config("spark.master", "local").getOrCreate()

    val df = spark.readStream
      .format("kafka")
      .option(
        "kafka.bootstrap.servers",
        "localhost:9092"
      )
      .option("subscribe", topic)
      .option("startingOffsets", "latest")
      .load()
      .selectExpr("CAST(value AS STRING)")

    val url                  = "jdbc:postgresql://localhost/testdb"
    val connectionProperties = new Properties()
    connectionProperties.put("user", "postgres")
    connectionProperties.put("password", "root")

    val jdbcDF = spark.read.jdbc(url, "reports", connectionProperties)

    jdbcDF.show()

    implicit val formats = DefaultFormats

    val ds = df.writeStream
      .queryName("writeReports")
      .foreachBatch((data: DataFrame, batchId: Long) => {
        if (!data.isEmpty) {
          val reports = data
            .collect()
            .map(row => row.getString(0))
            .map(str => parse(str).extract[Report])

          val df = spark.createDataFrame(reports)

          data.printSchema()
          data.show()

          df.printSchema()
          df.show()
          val date =
            DateTimeFormatter
              .ofPattern("yyyy-MM-dd-HH-mm-ss")
              .withZone(ZoneId.systemDefault())
              .format(
                Instant
                  .now()
              )
          val df3 = df.select(col("id"), explode(col("citizens")).as("citizen"))
          val df4 = df3.select(col("id"), col("citizen.*"))
          val df4Flatten = df4.toDF("id", "name", "peacescore")

          val df2 = df.drop("citizens")

          df2.write.mode("append").jdbc(url, "reports", connectionProperties)
          df4Flatten.write
            .mode("append")
            .jdbc(url, "citizens", connectionProperties)

          df
            .coalesce(1)
            .write
            .option("header", "true")
            .mode("append")
            .json("./reports_" + date.toString)

          Thread.sleep(5000)
        }
      })
      .outputMode("append")
      .start()
      .awaitTermination()
  }

  retrieveReport("reportTopic")
}
