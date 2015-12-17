

import com.tracebook.server.utils.MongoFactory
import org.bson.types.ObjectId
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.DBObject
import spray.json._
import sun.misc.BASE64Decoder
object MongoTest {
    val decoder = new BASE64Decoder
  def main(args: Array[String]): Unit = {
    var respJson: JsObject = null
    val postId = "5671c4d0e381415c76758bec"
    val userId = "5671c4c5e381415c76758bd4"
    val objectId: ObjectId = new ObjectId(postId)
    val query: DBObject = MongoDBObject("_id" -> objectId)
    val obj = MongoFactory.getCollection("posts").findOne(query)
    val keys = obj.get("encryptedKey")
    val keyJson = keys.toString().parseJson
    var m = scala.collection.mutable.Map[String, String]()
    m += ("key" -> keyJson.asJsObject.getFields(userId).head.toString())
    m += ("data" -> obj.get("data").toString())
    val n = collection.immutable.Map(m.toList: _*)
    println(scala.util.parsing.json.JSONObject(n).toString())
    //println(keys)    
  }
}