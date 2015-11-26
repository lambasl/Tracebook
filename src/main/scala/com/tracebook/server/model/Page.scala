package com.tracebook.server.model

/**
 * @author user
 */
case class Page(desc: String,
    _id: Option[String],
    likes: Option[Array[String]],
    posts: Option[Array[String]])