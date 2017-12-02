import com.github.mideo.media.m3u8.parser._

import scala.io.Source


class StreamPlaylistTest extends M3U8ParserSuite {

  test("test StreamPlaylist") {
    //Given
    val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}

    //When
    val streamPlaylist = M3U8MasterStreamPlaylist(data)


    streamPlaylist.mediaStreamType.isInstanceOf[MediaStreamType] should be(true)
    streamPlaylist.mediaStreamTypeInfo.isInstanceOf[MediaStreamTypeInfo] should be(true)
    streamPlaylist.mediaStreamInfo.isInstanceOf[Map[String, MediaStreamInfo]] should be(true)
    streamPlaylist.mediaStreamFrameInfo.isInstanceOf[Map[String, MediaStreamFrameInfo]] should be(true)
  }

  test("test write") {
    //Given
    val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}

    //When
    val streamPlaylist = M3U8MasterStreamPlaylist(data)

    //Then
    streamPlaylist.write.isInstanceOf[String] should be(true)

  }

  test("test builders") {
    //Given
    val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}


    //When
    val streamPlaylist = M3U8MasterStreamPlaylist(data)
      .withMediaStreamType(MediaStreamType("foobar"))
      .withMediaStreamTypeInfo(MediaStreamTypeInfo("acb","sss","fff","fff","ssds","sdfffggg"))
      .withMediaStreamInfo(Map("1111" -> MediaStreamInfo("1111", List("sdsd"), "sds", "sdsd", "gggg", "mdkdfk")))
      .withMediaStreamFrameInfo(Map("2222" -> MediaStreamFrameInfo("2222", List("sdsd"), "sds", "sdsd")))

    //Then
    streamPlaylist.mediaStreamType.name should be("foobar")
    streamPlaylist.mediaStreamTypeInfo.mediaType should be("acb")
    streamPlaylist.mediaStreamInfo("1111").bandWith should be("1111")
    streamPlaylist.mediaStreamFrameInfo("2222").bandWith should be("2222")

  }



}
