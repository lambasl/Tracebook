import akka.actor.ActorSystem
import spray.client.pipelining._
import spray.http.{MediaTypes, BodyPart, MultipartFormData}
import java.io.File
import spray.http.HttpEntity
import spray.http.ContentTypes
import java.io.BufferedInputStream
import java.io.FileInputStream
import spray.http.HttpData
import spray.http.FormFile
import spray.http.MediaType

object UploadFileExample extends App {
  implicit val system = ActorSystem("simple-spray-client")
  import system.dispatcher // execution context for futures below

val file = "/home/user/Pictures/timesheet0.jpeg"
val bis = new BufferedInputStream(new FileInputStream(file))
val bArray = Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray

val url = "http://localhost:8080/upload/img"

val httpData = HttpData(bArray)
val httpEntity = HttpEntity(MediaTypes.`application/octet-stream`, httpData).asInstanceOf[HttpEntity.NonEmpty]
val formFile = FormFile("my-image", httpEntity)
val formData = MultipartFormData(Seq(
  BodyPart(formFile, "img"))
)
val req = Post(url, formData)

  
  val pipeline = (addHeader("Content-Type", "application/json")
  ~> sendReceive
)
/*  val payload = MultipartFormData(Seq(BodyPart(new File("/home/user/Pictures/timesheet0.jpeg"), "image", MediaTypes.`application/json`)))
  val request =
    Post("http://localhost:8080/upload/img", payload)
*/
  
  pipeline(req).onComplete { res =>
    println(res)
    system.shutdown()
  }
}