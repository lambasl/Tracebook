

import com.example.spray.server.da.Postda
import com.tracebook.server.da.CommonDa

object test {
  def main(args: Array[String]): Unit = {
    val da = new Postda
    //var o = da.addLike("565770d1e381412340a583f9","56577079e189ef1052812441" ,"srinivas", "post")
    println(CommonDa.getProfile("56577079e189ef1052812441"))
  }
}