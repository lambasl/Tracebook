package com.tracebook.server.model

import org.json4s.JsonAST.JObject
import com.mongodb.util.JSON

/**
 * @author user
 */
case class Album(name:String,
    owner: String,
    permission: String,
    _id: Option[String],
    cover: Option[Array[Byte]],
    likes: Option[Array[String]],
    comments: Option[Array[String]],
    pics: Option[Array[String]])