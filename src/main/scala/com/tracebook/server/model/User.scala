package com.tracebook.server.model

import com.mongodb.util.JSON
import org.json4s.JsonAST.JArray

/**
 * @author user
 */
case class User(username:String,
    _id:Option[String],
    friends: Option[Array[String]],
    photos: Option[Array[String]],
    posts: Option[Array[String]],
    albums: Option[Array[String]],
    info: Option[JArray])
    