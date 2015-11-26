package com.tracebook.server.da.test

import com.tracebook.server.model.Post
import org.json4s.JsonAST.JObject
import com.example.spray.server.da.Postda
import com.mongodb.DBObject
import org.bson.types.ObjectId

/**
 * @author user
 */
object test {
  def main(args: Array[String]): Unit = {
    val da = new Postda
    var o = da.addLike("565770d1e381412340a583f9","56577079e189ef1052812441" ,"srinivas", "post")
    println(o)
  }
}