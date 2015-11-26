package com.tracebook.server.da.test

import com.tracebook.server.model.Post
import org.json4s.JsonAST.JObject
import com.example.spray.server.da.Postda
import com.mongodb.DBObject

/**
 * @author user
 */
object test {
  def main(args: Array[String]): Unit = {
    val da = new Postda
    var o = da.getPostbyId("56563ed0e38141327546ae99")
    println(o)
  }
}