package com.tracebook.server.da

import com.mongodb.casbah.commons.MongoDBObject
import com.tracebook.server.model.Album
import com.tracebook.server.model.Album
import com.tracebook.server.utils.MongoFactory
import org.bson.types.ObjectId
/**
 * @author user
 */
class AlbumDa {
   def saveAlbum(album: Album): String={
    val albumObj = buildMongoObject(album)
    val result = MongoFactory.getCollection("albums").save(albumObj)
    val id = albumObj.get("_id").asInstanceOf[ObjectId]
    id.toHexString()
  }
  
  
    private def buildMongoObject(album: Album)={
    val builder = MongoDBObject.newBuilder
    builder += "name" -> album.name
    builder += "owner" -> album.owner
    builder.result
  }
}