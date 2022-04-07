import faker._
import scala.util.Random.nextInt
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.Instant
import java.time.Duration
import java.time.ZoneId

object ReportsGenerator {

  case class Citizen(
      val name: String,
      val peacescore: Int
  ) {}
  case class Report(
      val id: String,
      val latitude: String,
      val longitude: String,
      val country: String,
      val citizens: List[Citizen],
      val message: String,
      val battery: Int,
      val date: String
  ) {
    override def toString(): String = {
      s"ID: $id \n Position: http://maps.google.com/maps?q=$latitude,$longitude \n Country: $country\n " +
        s"Citizens: ${citizens.map(c => s"${c.name} - ${c.peacescore}").mkString(", ")} \n Message: $message \n Battery: $battery \n Date: $date"
    }
  }

  def generate(): Report = {
    val id        = java.util.UUID.randomUUID().toString()
    val latitude  = Address.latitude
    val longitude = Address.longitude
    val country   = Address.country
    val citizens =
      List.fill(nextInt(11) + 1)(new Citizen(Name.name, nextInt(101)))
    val message = Lorem.words(10).mkString(" ")
    val battery = nextInt(101) // [0, 100] %
    val date =
      DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault())
        .format(
          Instant
            .now()
            .minus(Duration.ofMinutes(30).minus(Duration.ofSeconds(55)))
        )

    Report(
      id,
      latitude,
      longitude,
      country,
      citizens,
      message,
      battery,
      date
    )
  }

}
