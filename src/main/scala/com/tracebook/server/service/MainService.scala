package com.tracebook.server.service

import org.json4s.DefaultFormats
import org.json4s.Formats
import org.json4s.JsonAST.JObject
import org.json4s.jvalue2extractable
import com.tracebook.server.model.Post
import Json4sProtocol.json4sFormats
import Json4sProtocol.json4sMarshaller
import Json4sProtocol.json4sUnmarshaller
import akka.actor.ActorSystem
import spray.httpx.Json4sSupport
import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
import spray.routing.Directive.pimpApply
import spray.routing.SimpleRoutingApp
import com.tracebook.server.da.CommonDa
import com.tracebook.server.model.Page
import com.tracebook.server.da.PageDa
import com.tracebook.server.model.Album
import com.tracebook.server.da.AlbumDa
import spray.http.MultipartFormData
import com.example.spray.server.da.Postda
import org.json4s.JsonFormat
import org.json4s.JsonInput
import com.mongodb.util.JSON
import spray.httpx.Json4sJacksonSupport
import com.mongodb.DBObject
import akka.http.scaladsl.marshalling.Marshal

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
    path("page" / "create") {
      post {
        entity(as[Page]) { obj =>
          complete {
            val da = new PageDa
            da.savePage(obj)
          }
        }
      }
    } ~
      path("post" / "create") {
        post {
          entity(as[Post]) { obj =>
            complete {
              val da = new Postda
              da.savePost(obj)
            }
          }
        }
      } ~
      path("like") {
        post {
          parameters("id", "type", "userName", "userId") { (id, typ, userName, userId) =>
            val da = new Postda
            da.addLike(id, userId, userName, typ)
            complete {
              "OK"
            }
          }
        }
      } ~
      path("comment") {
        post {
          parameters("id", "type", "userName", "userId", "comment") { (id, typ, userName, userId, comment) =>
            val da = new Postda
            da.addComment(id, userId, userName, comment, typ)
            complete {
              "OK"
            }
          }
        }
      } ~
      path("album" / "create") {
        post {
          entity(as[Album]) { obj =>
            complete {
              val da = new AlbumDa
              da.saveAlbum(obj)
            }
          }
        }
      } ~
      path("photo" / "add") {
        post {
          entity(as[JObject]){obj=>
              complete{
                CommonDa.addPhoto(obj)
                "OK"
              }
          }
        }
      }~
      path("user"/"add"){
        post{
          entity(as[JObject]){obj=>
            complete{
              CommonDa.addUser(obj)
              
            }
          }
        }
      }~
      path("user"/"addFriend"){
        post{
          entity(as[JObject]){obj=>
            complete{
              CommonDa.addFriend(obj)
              "OK"
            }
          }
        }
      }~
      get{
        path(Segment/ "profile"){ id=>
          complete{
              CommonDa.getProfile(id)
            }
        }~
        path(Segment/ "friends"){ id=>
          complete{
              CommonDa.getFriends(id)
            }
        }~
        path(Segment/ "photos"){ id=>
          complete{
              CommonDa.getphotos(id)
            }
        }~
        path(Segment/ "albums"){ id=>
          complete{
              CommonDa.getAlbums(id)
            }
        }~
        path("photo" / Segment){ id=>
          complete{
              CommonDa.getPhoto(id)
            }
        }~
        path("posts"/ Segment){ id=>
          complete{
              CommonDa.getPost(id)
            }
        }
      }
      
  }

}