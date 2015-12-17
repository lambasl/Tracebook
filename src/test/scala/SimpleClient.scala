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
import org.bson.types.ObjectId
import com.tracebook.server.utils.MongoFactory
import sun.misc.BASE64Encoder
import sun.misc.BASE64Decoder

object SimpleClient extends App with RequestBuilding {
  implicit val system = ActorSystem("spray-client-example")
  implicit val executionContext = system.dispatcher

  val clientPipeline = sendReceive
   val encoder = new BASE64Encoder
  val decoder = new BASE64Decoder
  val startTimestamp = System.currentTimeMillis()
/*  val response = clientPipeline {
    Get("http://127.0.0.1:8080/5671a5b0e3814147526caebb/friends")
  }
  response.onComplete {
    case Success(content) => {
      val p = content.entity.asString
      var b = com.mongodb.util.JSON.parse(p)
      var friends = new scala.collection.mutable.HashSet[String]
      var jsonObt: JsValue = b.toString().parseJson
      var arr: JsArray = jsonObt.asInstanceOf[JsArray]
      for (i <- 0 until arr.elements.length) {
        var value: JsValue = arr.elements(i).asInstanceOf[JsValue]
        friends += value.asJsObject.fields.keySet.head.toString()
      }
      println(friends)
    }
  }*/
  
    val response = clientPipeline {
    Get("http://127.0.0.1:8080/posts/5672012ce381417ac99e2883/56720121e381417ac99e287a")
  }
  response.onComplete {
    case Success(content) => {
    /*val postId = "5671fd62e381417ac99e286e"
    val userID = "5671fd57e381417ac99e2867"
    val p = content.entity.asString
    val objectId: ObjectId = new ObjectId(postId)
    val query: DBObject = MongoDBObject("_id" -> objectId)
    val obj = MongoFactory.getCollection("posts").findOne(query)
    val keys = obj.get("encryptedKey")
    var retObj =  new MongoDBObject
    val keyJson = keys.toString().parseJson
    var m = scala.collection.mutable.Map[String, String]()
    retObj.put("key" ,keyJson.asJsObject.getFields(userID).head.toString())
    retObj.put("data", obj.get("data").toString())
    println("CommonDA:" + retObj.toString())
    com.mongodb.util.JSON.serialize(retObj)*/
    
      val p = content.entity.asString
            println(p)
            var b = com.mongodb.util.JSON.parse(p)
            var jsonObt: JsValue = b.toString().parseJson
            val data = jsonObt.asJsObject.getFields("data")
            val key = jsonObt.asJsObject.getFields("key")
            println("data=" + data.head)
            println("key=" + key.head.toString())
            println("key=" + key.head.toString().replaceAll("\\\\n", "").replaceAll("\"", "").replaceAll("\\\\", ""))
            val decodedData = decoder.decodeBuffer(data.toString())
            val decodedKey = decoder.decodeBuffer(key.head.toString().replace("\n", "").replace("\"", "").replace("\\", ""))
            println("decoded data:" + new String(decodedData) + ",decode key:" + new String(decodedKey))
            //val cipherRsa = Cipher.getInstance("RSA")
            //cipherRsa.init(Cipher.DECRYPT_MODE, userToKeysMap.get(userID).get.getPrivate)
            //val aesKey = new String(cipherRsa.doFinal(decodedKey))
            //println(aesKey)
    }
  }
}