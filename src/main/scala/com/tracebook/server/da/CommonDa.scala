package com.tracebook.server.da

import org.bson.types.ObjectId
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.DBObject
import com.mongodb.BasicDBList
import com.tracebook.server.utils.MongoFactory
import org.json4s.JsonAST.JObject
import spray.json._

/**
 * @author user
 */
object CommonDa {

  def findAndAppendList(coll: String, searchId: String, field: String, addObj: MongoDBObject) = {
    val objectId: ObjectId = new ObjectId(searchId)
    val query: DBObject = MongoDBObject("_id" -> objectId)
    val obj = MongoFactory.getCollection(coll).findOne(query)
    var list = obj.get(field).asInstanceOf[BasicDBList]
    if (list == null) {
      list = new BasicDBList
    }
    list.add(addObj)
    obj.removeField(field)
    obj.put(field, list)
    MongoFactory.getCollection(coll).update(query, obj)
  }

  def decideType(t: String): String = {
    if ("post".equals(t)) {
      return "posts"
    } else if ("album".equals(t)) {
      return "albums"
    } else if ("photo".equals(t)) {
      return "photots"
    } else if ("page".equals(t)) {
      return "pages"
    }
    null
  }

  def addUser(obj: JObject): String = {
    var user = new MongoDBObject
    user.put("name", obj.values.get("name").get.asInstanceOf[String])
    user.put("pubKey", obj.values.get("pubKey").get.asInstanceOf[String])
    var coll = MongoFactory.getCollection("users")
    coll.insert(user.underlying)
    val id = user.get("_id").get.asInstanceOf[ObjectId]
    id.toHexString()
  }

  def addFriend(obj: JObject) = {
    var user = new MongoDBObject
    val fromId = obj.values.get("from").get.asInstanceOf[String]
    val toId = obj.values.get("to").get.asInstanceOf[String]
    var friendObj1 = new MongoDBObject
    friendObj1.put(toId, "")
    findAndAppendList("users", fromId, "friends", friendObj1)

    var friendObj2 = new MongoDBObject
    friendObj2.put(fromId, "")
    findAndAppendList("users", toId, "friends", friendObj2)

  }

  def addPhoto(obj: JObject): String = {
    val userId = obj.values.get("user").get.asInstanceOf[String]
    val pic = obj.values.get("photo").get.asInstanceOf[String]
    val permission = obj.values.get("permission").get.asInstanceOf[String]
    var picObj = new MongoDBObject
    picObj.put("user", userId)
    picObj.put("data", pic)
    picObj.put("permission", permission)
    MongoFactory.getCollection("photos").insert(picObj.underlying)
    val imgId = picObj.get("_id").get.asInstanceOf[ObjectId].toHexString()
    var userImgObj = new MongoDBObject
    userImgObj.put(imgId, "")
    findAndAppendList("users", userId, "photos", userImgObj)
    var albumId: String = null
    if (!obj.values.get("album").isEmpty) {
      albumId = obj.values.get("album").get.asInstanceOf[String]
      findAndAppendList("albums", albumId, "photos", userImgObj)
    }
    imgId
  }

  def getProfile(userId: String): String = {
    val obj = getUserObj(userId)
    com.mongodb.util.JSON.serialize(obj)
  }

  def getUserObj(userId: String): DBObject = {
    val objectId: ObjectId = new ObjectId(userId)
    val query: DBObject = MongoDBObject("_id" -> objectId)
    val obj = MongoFactory.getCollection("users").findOne(query)
    obj
  }

  def getPost(postId: String, userID: String): String = {
    val objectId: ObjectId = new ObjectId(postId)
    val query: DBObject = MongoDBObject("_id" -> objectId)
    val obj = MongoFactory.getCollection("posts").findOne(query)
    val keys = obj.get("encryptedKey")
    val privacy = obj.get("permission")
    var retObj = new MongoDBObject
    if (privacy.toString() == "F") {
      val keyJson = keys.toString().parseJson
      var m = scala.collection.mutable.Map[String, String]()
      if (keyJson.asJsObject.fields.contains(userID)) {
        retObj.put("key", keyJson.asJsObject.getFields(userID).head.toString())
        retObj.put("data", obj.get("data").toString())
        println("CommonDA:" + retObj.toString())
        com.mongodb.util.JSON.serialize(retObj)
      } else {
        ""
      }
    } else  {
      var m = scala.collection.mutable.Map[String, String]()
      retObj.put("key", "NA")
      retObj.put("data", obj.get("data").toString())
      println("CommonDA:" + retObj.toString())
      com.mongodb.util.JSON.serialize(retObj)
    }
  }

  def getFriends(userId: String): String = {
    val obj = getUserObj(userId)
    com.mongodb.util.JSON.serialize(obj.get("friends"))
  }

  def getphotos(userId: String): String = {
    val obj = getUserObj(userId)
    com.mongodb.util.JSON.serialize(obj.get("photos"))
  }

  def getAlbums(userId: String): String = {
    val obj = getUserObj(userId)
    com.mongodb.util.JSON.serialize(obj.get("albums"))
  }

  def getPhoto(photoId: String): String = {
    val objectId: ObjectId = new ObjectId(photoId)
    val query: DBObject = MongoDBObject("_id" -> objectId)
    val obj = MongoFactory.getCollection("photos").findOne(query)
    com.mongodb.util.JSON.serialize(obj)
  }

}