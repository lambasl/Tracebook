package com.tracebook.server.service

import org.json4s.DefaultFormats
import org.json4s.Formats
import org.json4s.JsonAST.JObject
import org.json4s.jvalue2extractable

import com.example.spray.server.da.Postda
import com.tracebook.server.model.Post

import Json4sProtocol.json4sFormats
import Json4sProtocol.json4sMarshaller
import Json4sProtocol.json4sUnmarshaller
import akka.actor.ActorSystem
import spray.httpx.Json4sSupport
import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
import spray.routing.Directive.pimpApply
import spray.routing.SimpleRoutingApp

/**
 * @author user
 */

object Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats
}

object MainService extends App with SimpleRoutingApp {
  
  import Json4sProtocol._
  
  implicit val actorSys = ActorSystem()
  startServer(interface = "localhost", port = 8080) {
    
    path("post" / "create") {
      println("request post/create")
      post{
        entity(as[Post]){ obj =>
            complete{
            val da = new Postda
            da.savePost(obj)
             "Post saved in MongoDB.."
            }
          
         }
        
      }

    }
  }

}