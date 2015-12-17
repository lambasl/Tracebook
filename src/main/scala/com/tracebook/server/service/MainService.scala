package com.tracebook.server.service

import org.json4s.DefaultFormats
import sun.misc.BASE64Decoder
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
import spray.routing.Directive
import javax.crypto.Cipher
import java.security.spec.X509EncodedKeySpec
import java.security.KeyFactory

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
          entity(as[JObject]) { obj =>
            val id = obj.values.get("id").get.asInstanceOf[String]
            val typ = obj.values.get("typ").get.asInstanceOf[String]
            val userName = obj.values.get("userName").get.asInstanceOf[String]
            val userId = obj.values.get("userId").get.asInstanceOf[String]
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
          entity(as[JObject]) { obj =>
            val id = obj.values.get("id").get.asInstanceOf[String]
            val typ = obj.values.get("typ").get.asInstanceOf[String]
            val userName = obj.values.get("userName").get.asInstanceOf[String]
            val userId = obj.values.get("userId").get.asInstanceOf[String]
            val comment = obj.values.get("comment").get.asInstanceOf[String]
            val encryptedKey = obj.values.get("encryptedKey").get.asInstanceOf[String]
            val da = new Postda
            da.addComment(id, userId, userName, comment, typ, encryptedKey)
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
          entity(as[JObject]) { obj =>
            complete {
              CommonDa.addPhoto(obj)
              "OK"
            }
          }
        }
      } ~
      path("user" / "add") {
        post {
          entity(as[JObject]) { obj =>
            complete {
              CommonDa.addUser(obj)

            }
          }
        }
      } ~
      path("authenticate") {
        post {
          entity(as[JObject]) { obj =>
            complete {
              val userID = obj.values.get("userID").get.asInstanceOf[String]
              val decoder = new BASE64Decoder
              val publicKey = obj.values.get("publicKey").get.asInstanceOf[String].replaceAll("\\\\n", "").replaceAll("\"", "").replaceAll("\\\\", "")
              val rawData = obj.values.get("rawData").get.asInstanceOf[String]
              val encryptedData = obj.values.get("encryptedData").get.asInstanceOf[String].replaceAll("\\\\n", "").replaceAll("\"", "").replaceAll("\\\\", "")
              val decodedData = decoder.decodeBuffer(encryptedData)
              val decodedKey = decoder.decodeBuffer(publicKey)
              val cipherRsa = Cipher.getInstance("RSA")
              val encodedKeySpec = new X509EncodedKeySpec(decodedKey)
              val keyFactory = KeyFactory.getInstance("RSA")
              val newPubKey = keyFactory.generatePublic(encodedKeySpec)
              cipherRsa.init(Cipher.DECRYPT_MODE, newPubKey)
              val afterDecryption = new String(cipherRsa.doFinal(decodedData))
              println("After Decryption: " + afterDecryption + " Random String: " + rawData)
              "Done"
            }
          }
        }
      } ~
      path("user" / "addFriend") {
        post {
          entity(as[JObject]) { obj =>
            complete {
              CommonDa.addFriend(obj)
              "OK"
            }
          }
        }
      } ~
      get {
        path(Segment / "profile") { id =>
          complete {
            CommonDa.getProfile(id)
          }
        } ~
          path(Segment / "friends") { id =>
            complete {
              CommonDa.getFriends(id)
            }
          } ~
          path(Segment / "photos") { id =>
            complete {
              CommonDa.getphotos(id)
            }
          } ~
          path(Segment / "albums") { id =>
            complete {
              CommonDa.getAlbums(id)
            }
          } ~
          path("photo" / Segment) { id =>
            complete {
              CommonDa.getPhoto(id)
            }
          } ~
          path("posts" / Segment / Segment) { (postID, userID) =>
            complete {
              println("postID=" + postID + ",UserID=" + userID)
              CommonDa.getPost(postID, userID)
            }
          }
      }

  }

}