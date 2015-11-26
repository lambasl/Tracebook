package com.tracebook.server.da

import org.json4s.JsonAST.JObject
import com.mongodb.casbah.commons.MongoDBObject
import com.tracebook.server.model.Page
import com.tracebook.server.utils.MongoFactory
import org.bson.types.ObjectId

/**
 * @author user
 */
class PageDa {
    def savePage(page: Page): String={
    val pageObj = buildMongoObject(page)
    val result = MongoFactory.getCollection("pages").save(pageObj)
    val id = pageObj.get("_id").asInstanceOf[ObjectId]
    id.toHexString()
  }
  private def buildMongoObject(page: Page) = {
    val builder = MongoDBObject.newBuilder
    builder += "desc" -> page.desc
    builder += "likes" -> page.likes
    builder += "posts" -> page.posts
    builder.result
  }
}