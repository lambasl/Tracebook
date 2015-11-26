package com.tracebook.server.utils

import com.mongodb.casbah.MongoCollection

import com.mongodb.casbah.MongoClient
import com.mongodb.DBCollection
object MongoFactory {
  private val SERVER = "localhost"
  private val PORT = 27017
  private val DATABASE = "tracebook"
  
  val client = MongoClient(SERVER)
  def getCollection(c: String): DBCollection = return client.getDB(DATABASE).getCollection(c)
  
}