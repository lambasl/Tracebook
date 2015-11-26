package com.tracebook.server.model

import org.bson.BsonBinary

/**
 * @author user
 */
case class Photo(_id: Option[String],
    pic: Option[BsonBinary],
    tag: Option[String],
    likes: Option[Array[String]],
    comments: Option[Array[String]])