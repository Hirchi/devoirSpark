import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.clients.producer.ProducerConfig
import java.util.Properties
import net.liftweb.json._
import net.liftweb.json.Serialization.write

object Producer extends App {

  def initProperties(props: Properties) {
    props.put("bootstrap.servers", "localhost:9092")
    props.put(
      "key.serializer",
      "org.apache.kafka.common.serialization.StringSerializer"
    )
    props.put(
      "value.serializer",
      "org.apache.kafka.common.serialization.StringSerializer"
    )
    props.put("acks", "all")
  }

  def sendReportRec(
      p: KafkaProducer[String, String],
      reportTopic: String,
      alertReportTopic: String
  ) {
    println("New report!")
    val report           = ReportsGenerator.generate()
    implicit val formats = DefaultFormats
    val jsonReport       = write(report)
    println(jsonReport)

    val filteredCitizens = report.citizens.filter(c => c.peacescore < 50)
    val alertReport      = report.copy(citizens = filteredCitizens)

    val jsonAlert = write(alertReport)

    val record = new ProducerRecord[String, String](reportTopic, jsonReport)
    val alertRecord =
      new ProducerRecord[String, String](alertReportTopic, jsonAlert)

    p.send(record)
    println("Record sent!")

    if (!filteredCitizens.isEmpty) {
      println(jsonAlert)
      p.send(alertRecord)
      println("Alert sent!")
    }
    Thread.sleep(4000L)
    sendReportRec(p, reportTopic, alertReportTopic)
  }

  def sendReport(reportTopic: String, alertReportTopic: String) {
    val props: Properties = new Properties()
    initProperties(props)

    val producer = new KafkaProducer[String, String](props)

    try {
      sendReportRec(producer, reportTopic, alertReportTopic)
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      producer.close()
    }
  }

  sendReport("reportTopic", "alertReportTopic")
}
