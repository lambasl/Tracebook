import scala.util.Success
import akka.actor.ActorSystem
import spray.client.pipelining.sendReceive
import spray.client.pipelining.sendReceive$default$3
import spray.httpx.RequestBuilding
import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import org.json4s.JsonAST.JObject
import org.json4s.JsonAST
import spray.json._

object SimpleClient extends App with RequestBuilding {
  implicit val system = ActorSystem("spray-client-example")
  implicit val executionContext = system.dispatcher

  val clientPipeline = sendReceive

  val startTimestamp = System.currentTimeMillis()
  val response = clientPipeline {
    Get("http://127.0.0.1:8080/567045f5e3814150c3cec3f6/profile")
  }
  response.onComplete(_ => println(s"Request completed in ${System.currentTimeMillis() - startTimestamp} millis."))
  response.onComplete {
    case Success(content) => {
      val p = content.entity.asString
      var b = com.mongodb.util.JSON.parse(p)
      
      var jsonObt:JsValue = b.toString().parseJson
      println(jsonObt.asJsObject.getFields("friends"))
    }
  }
}