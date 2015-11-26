package com.example.spray.server.da

import com.tracebook.server.model.Post
import com.mongodb.casbah.commons.MongoDBObject
import com.tracebook.server.model.Post
import com.tracebook.server.utils.MongoFactory
import com.mongodb.DBObject
import org.bson.types.ObjectId
import scala.collection.mutable.ArrayBuffer
import com.mongodb.BasicDBList

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

  def getPostbyId(id: String): DBObject = {
    val objectId: ObjectId = new ObjectId(id)
    val query: DBObject = MongoDBObject("_id" -> objectId)
    val result = MongoFactory.getCollection("posts").findOne(query)
    return result

  }
  
  def addCommentToPost(id:String, user:String, comment: String)={
    
    val objectId: ObjectId = new ObjectId(id)
    val query: DBObject = MongoDBObject("_id" -> objectId)
    val obj = MongoFactory.getCollection("posts").findOne(query)
    var comments = obj.get("comments").asInstanceOf[BasicDBList]
    if(comments == null){
      comments = new BasicDBList
    }
    var commentObj = new MongoDBObject
    commentObj.put("user", user)
    commentObj.put("comment", comment)
    comments.add(commentObj)
    
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