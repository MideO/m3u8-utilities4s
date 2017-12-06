import java.nio.file.{Files, Paths}

import com.github.mideo.media.m3u8.parser._

import scala.io.Source


class StreamPlaylistTest extends M3U8ParserSuite {

  test("test StreamPlaylist") {
    //Given
    val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines()reduce{(a, b) => s"$a\n$b"}

    //When
    val streamPlaylist = MasterStreamPlaylist(data)


    streamPlaylist.mediaStreamType.isInstanceOf[Option[MediaStreamType]] should be(true)
    streamPlaylist.mediaStreamTypeInfo.isInstanceOf[Option[MediaStreamTypeInfo]] should be(true)
    streamPlaylist.mediaStreamInfo.isInstanceOf[Map[String, MediaStreamInfo]] should be(true)
    streamPlaylist.mediaStreamFrameInfo.isInstanceOf[Map[String, MediaStreamFrameInfo]] should be(true)
  }

  test("test write") {
    //Given
    val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines()reduce{(a, b) => s"$a\n$b"}

    //When
    val streamPlaylist = MasterStreamPlaylist(data)

    //Then
    streamPlaylist.write.isInstanceOf[String] should be(true)

  }

  test("test saveToFile") {
    try {
      //Given
      val is = getClass.getClassLoader.getResource("master.m3u8").openStream()

      val data: String = Source.fromInputStream(is).getLines()reduce {_+","+_}

      //When
      val streamPlaylist = MasterStreamPlaylist(data)
      streamPlaylist.saveToFile("master2.m3u8")

      //Then
      val data2: String = Source.fromFile("master2.m3u8").getLines()reduce {_+","+_}

      data2 should not be empty
    } finally {
      Files.delete(Paths.get("master2.m3u8"))
    }
  }

  test("test builders") {
    //Given
    val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines()reduce{(a, b) => s"$a\n$b"}


    //When
    val pl = MasterStreamPlaylist(data)

    val streamPlaylist = pl
      .withMediaStreamType(Option(MediaStreamType("foobar")))
      .withMediaStreamTypeInfo(Option(MediaStreamTypeInfo("AUDIO","aac","fre","French","YES","YES")))
      .withMediaStreamInfo(pl.mediaStreamInfo.values map {
        it => it.bandWith -> MediaStreamInfo(
          it.bandWith,
          it.codecs,
          it.resolution,
          it.closedCaption,
          it.audio, it.asset.replace("asset", "french_asset"))
      } toMap)
      .withMediaStreamFrameInfo(Map("2222" -> MediaStreamFrameInfo("2222", List("sdsd"), "sds", "sdsd")))

    //Then
    streamPlaylist.mediaStreamType.get.name should be("foobar")
    streamPlaylist.mediaStreamTypeInfo.get.name should be("French")
    streamPlaylist.mediaStreamInfo("2100000").asset should be("french_asset_1800k.m3u8")
    streamPlaylist.mediaStreamFrameInfo("2222").bandWith should be("2222")

  }



}
