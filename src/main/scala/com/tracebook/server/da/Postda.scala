package com.example.spray.server.da

import com.tracebook.server.model.Post
import com.mongodb.casbah.commons.MongoDBObject
import com.tracebook.server.model.Post
import com.tracebook.server.utils.MongoFactory
import com.mongodb.DBObject
import org.bson.types.ObjectId
import scala.collection.mutable.ArrayBuffer

/**
 * @author user
 */
class Postda {
  def savePost(post: Post)={
    val postObj = buildMongoObject(post)
    val result = MongoFactory.getCollection("posts").save(postObj)
    val id = postObj.get("_id")
    id
  }
  
  def getPostbyId(_id: String): DBObject = {
    var query = MongoDBObject("_id" -> new ObjectId(_id))
    return MongoFactory.getCollection("posts").find(query).curr()
  }
  
  def addCommentToPost(id:String, comment: String)={
    var query = MongoDBObject("_id" -> new ObjectId(id))
    var obj = MongoFactory.getCollection("posts").find(query).curr()
    var comments = obj.get("comments").asInstanceOf[ArrayBuffer[String]]
    comments += comment
    obj.removeField("comments")
    obj.put("comments", comments)
    MongoFactory.getCollection("posts").update(query, obj)
  }
  
  private def buildMongoObject(post: Post)={
    val builder = MongoDBObject.newBuilder
    builder += "data" -> post.data
    builder += "user" -> post.user
    builder += "likes" -> post.likes
    builder += "comments" -> post.comments
    builder += "shares" -> post.shares
    builder.result
  }
}