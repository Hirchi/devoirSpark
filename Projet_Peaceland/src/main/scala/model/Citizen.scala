import scala.util.Random
import faker._
Case class Citizen(citizenName : String, peaceScore: Int){
    val name = citizenName
    val score = peaceScore
    def toString(): String = {
        val str_to_return = "" + name + ":" + peacescore
        str_to_return
    }
}
object Citizen{
    def RandomCitizen() = {

      val name = Name.randomName
    
      val numberRandom = new scala.util.Random
      val peacescore = start + numberRandom.nextInt(( 100) )

      Citizen(name, peacescore)

    }
  
}