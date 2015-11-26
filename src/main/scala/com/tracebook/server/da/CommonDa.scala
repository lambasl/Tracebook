package com.tracebook.server.da

import org.bson.types.ObjectId
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.DBObject
import com.mongodb.BasicDBList
import com.tracebook.server.utils.MongoFactory

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

}