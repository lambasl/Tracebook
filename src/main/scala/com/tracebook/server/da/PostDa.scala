package com.example.spray.server.da

import com.tracebook.server.model.Post
import com.mongodb.casbah.commons.MongoDBObject
import com.tracebook.server.model.Post
import com.tracebook.server.utils.MongoFactory
import com.mongodb.DBObject
import org.bson.types.ObjectId
import scala.collection.mutable.ArrayBuffer
import com.mongodb.BasicDBList
import com.tracebook.server.da.CommonDa

/**
 * @author user
 */
class Postda {
  
  def savePost(post: Post): String={
    val postObj = buildMongoObject(post)
    val result = MongoFactory.getCollection("posts").save(postObj)
    val id = postObj.get("_id").asInstanceOf[ObjectId]    
    //updating users table to add this post
    var obj = new MongoDBObject
    obj.put(id.toHexString(), "")
    CommonDa.findAndAppendList("users", post.user, "posts", obj)
    id.toHexString()
  }

  def getPostbyId(id: String): DBObject = {
    val objectId: ObjectId = new ObjectId(id)
    val query: DBObject = MongoDBObject("_id" -> objectId)
    val result = MongoFactory.getCollection("posts").findOne(query)
    return result
  }
 
  
  def addComment(id:String, userId:String, userName:String, comment: String, typ: String, encryptedKey: String) = {
    
    val collName = CommonDa.decideType(typ)
    if(typ == null){
      throw new Exception("invalid type detected")
    }
    var commentObj = new MongoDBObject
    commentObj.put(userName, userId)
    commentObj.put("comment", comment)
    commentObj.put("encryptedKey", encryptedKey)
    CommonDa.findAndAppendList(collName, id, "comments", commentObj)    
    }
  
  def addLike(id:String, userId:String, userName:String, typ: String) = {
    val collName = CommonDa.decideType(typ)
    if(typ == null){
      throw new Exception("invalid type detected")
    }
    var dbObj = new MongoDBObject
    dbObj.put(userName, userId)
    CommonDa.findAndAppendList(collName, id, "likes", dbObj)
  }
  
  
  
  
  private def buildMongoObject(post: Post)={
    val builder = MongoDBObject.newBuilder
    builder += "data" -> post.data
    builder += "encryptedKey" -> post.encryptedKey
    builder += "user" -> post.user
    builder += "likes" -> post.likes
    builder += "comments" -> post.comments
    builder += "shares" -> post.shares
    builder += "permission" -> post.permission
    builder.result
  }
}