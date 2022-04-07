import java.util.{Collections, Properties}
import net.liftweb.json._
import org.apache.spark.sql._
import org.apache.spark._
import org.apache.spark.sql.functions.desc

object Stats extends App {
  def ask_questions() {
    val spark =
      SparkSession.builder().config("spark.master", "local").getOrCreate()
    val url                  = "jdbc:postgresql://localhost/testdb"
    val connectionProperties = new Properties()
    connectionProperties.put("user", "postgres")
    connectionProperties.put("password", "root")

    val jdbcDF     = spark.read.jdbc(url, "reports", connectionProperties)
    val citizensDF = spark.read.jdbc(url, "citizens", connectionProperties)

    jdbcDF.show()
    while (true) {
      println("What do you want to know about our peaceland?")
      println(
        "1) Which countries generate most reports?"
      )
      println(
        "2) Proportion of peacewatchers having their battery over 20% when they send their report"
      )
      println(
        "3) Baddest citizens peacescore leaderboard"
      )
      println(
        "4) Greatest citizens peacescore leaderboard"
      )
      println("Choose the number of your question!")
      val input = readInt()
      if (input == 1) {
        jdbcDF
          .groupBy("country")
          .count()
          .sort(desc("count"))
          .show()
      } else if (input == 2) {
        val peacewatchers = jdbcDF.count()
        val peacewatchersOver20 = jdbcDF
          .filter(jdbcDF("battery") > 20)
          .count()
        println(
          "Number of peacewatchers over 20% battery: " + peacewatchersOver20
        )
        println("Number of peacewatchers: " + peacewatchers)
        println(
          "Proportion of peacewatchers over 20% battery: " + (peacewatchersOver20.toFloat / peacewatchers) * 100 + "%"
        )
      } else if (input == 3) {
        citizensDF.sort("peacescore").show(10)
      } else if (input == 4) {
        citizensDF.sort(desc("peacescore")).show(10)
      }
    }
    spark.stop()
  }

  ask_questions()
}
