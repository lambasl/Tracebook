package com.tracebook.server.model

/**
 * @author user
 */
case class Post(data: String,
    user: String,
    _id:Option[String],
    likes: Option[Array[String]],
    comments: Option[Array[String]],
    shares: Option[String])
  