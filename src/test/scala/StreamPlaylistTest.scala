import java.nio.file.{Files, Paths}

import com.github.mideo.media.m3u8.parser._

import scala.io.Source


class StreamPlaylistTest extends M3U8ParserSuite {

  test("test MasterStreamPlaylist from String") {
    //Given
    val is = getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data: String = Source.fromInputStream(is).getLines() reduce { (a, b) => s"$a\n$b" }

    //When
    val streamPlaylist = MasterStreamPlaylist(data)


    streamPlaylist.mediaStreamType.isInstanceOf[Option[MediaStreamType]] should be(true)
    streamPlaylist.mediaStreamTypeInfos.isInstanceOf[Option[List[MediaStreamTypeInfo]]] should be(true)
    streamPlaylist.mediaStreamInfo.isInstanceOf[Map[String, MediaStreamInfo]] should be(true)
    streamPlaylist.mediaStreamFrameInfo.isInstanceOf[Map[String, MediaStreamFrameInfo]] should be(true)
  }

  test("test MasterStreamPlaylist from InputStream") {
    //Given
    val is = getClass.getClassLoader.getResource("master.m3u8").openStream()

    //When
    val streamPlaylist = MasterStreamPlaylist(is)


    streamPlaylist.mediaStreamType.isInstanceOf[Option[MediaStreamType]] should be(true)
    streamPlaylist.mediaStreamTypeInfos.isInstanceOf[Option[List[MediaStreamTypeInfo]]] should be(true)
    streamPlaylist.mediaStreamInfo.isInstanceOf[Map[String, MediaStreamInfo]] should be(true)
    streamPlaylist.mediaStreamFrameInfo.isInstanceOf[Map[String, MediaStreamFrameInfo]] should be(true)
  }

  test("test VodStreamPlaylist from String") {
    //Given
    val is = getClass.getClassLoader.getResource("vod_asset_1800k.m3u8").openStream()

    val data: String = Source.fromInputStream(is).getLines() reduce { (a, b) => s"$a\n$b" }

    //When
    val streamPlaylist: VodStreamPlaylist = VodStreamPlaylist(data)

    streamPlaylist.mediaStreamType.isInstanceOf[Option[MediaStreamType]] should be(true)
    streamPlaylist.mediaStreamTypeInitializationVectorCompatibilityVersion.isInstanceOf[Option[MediaStreamTypeInitializationVectorCompatibilityVersion]] should be(true)
    streamPlaylist.mediaStreamTargetDuration.isInstanceOf[Option[MediaStreamTargetDuration]] should be(true)
    streamPlaylist.mediaStreamMediaSequence.isInstanceOf[Option[MediaStreamMediaSequence]] should be(true)
    streamPlaylist.mediaStreamPlaylistType.isInstanceOf[Option[MediaStreamPlaylistType]] should be(true)
    streamPlaylist.mediaStreamProgramDateTime.isInstanceOf[Option[MediaStreamProgramDateTime]] should be(true)
    streamPlaylist.mediaStreamPlaylistTransportStreams.isInstanceOf[Option[List[MediaStreamPlaylistTransportStream]]] should be(true)
    streamPlaylist.mediaStreamEnd.isInstanceOf[Option[MediaStreamEnd]]
  }

  test("test VodStreamPlaylist from InputStream") {
    //Given
    val is = getClass.getClassLoader.getResource("vod_asset_1800k.m3u8").openStream()

    //When
    val streamPlaylist: VodStreamPlaylist = VodStreamPlaylist(is)

    streamPlaylist.mediaStreamType.isInstanceOf[Option[MediaStreamType]] should be(true)
    streamPlaylist.mediaStreamTypeInitializationVectorCompatibilityVersion.isInstanceOf[Option[MediaStreamTypeInitializationVectorCompatibilityVersion]] should be(true)
    streamPlaylist.mediaStreamTargetDuration.isInstanceOf[Option[MediaStreamTargetDuration]] should be(true)
    streamPlaylist.mediaStreamMediaSequence.isInstanceOf[Option[MediaStreamMediaSequence]] should be(true)
    streamPlaylist.mediaStreamPlaylistType.isInstanceOf[Option[MediaStreamPlaylistType]] should be(true)
    streamPlaylist.mediaStreamProgramDateTime.isInstanceOf[Option[MediaStreamProgramDateTime]] should be(true)
    streamPlaylist.mediaStreamPlaylistTransportStreams.isInstanceOf[Option[List[MediaStreamPlaylistTransportStream]]] should be(true)
    streamPlaylist.mediaStreamEnd.isInstanceOf[Option[MediaStreamEnd]]
  }
  test("test LiveStreamPlaylist from String") {
    //Given
    val is = getClass.getClassLoader.getResource("live_asset_1800k.m3u8").openStream()

    val data: String = Source.fromInputStream(is).getLines() reduce { (a, b) => s"$a\n$b" }

    //When
    val streamPlaylist: LiveStreamPlaylist = LiveStreamPlaylist(data)

    streamPlaylist.mediaStreamType.isInstanceOf[Option[MediaStreamType]] should be(true)
    streamPlaylist.mediaStreamTypeInitializationVectorCompatibilityVersion.isInstanceOf[Option[MediaStreamTypeInitializationVectorCompatibilityVersion]] should be(true)
    streamPlaylist.mediaStreamTargetDuration.isInstanceOf[Option[MediaStreamTargetDuration]] should be(true)
    streamPlaylist.mediaStreamMediaSequence.isInstanceOf[Option[MediaStreamMediaSequence]] should be(true)
    streamPlaylist.mediaStreamProgramDateTime.isInstanceOf[Option[MediaStreamProgramDateTime]] should be(true)
    streamPlaylist.mediaStreamPlaylistTransportStreams.isInstanceOf[Option[List[MediaStreamPlaylistTransportStream]]] should be(true)
  }

  test("test LiveStreamPlaylist from InputStream") {
    //Given
    val is = getClass.getClassLoader.getResource("live_asset_1800k.m3u8").openStream()

    //When
    val streamPlaylist: LiveStreamPlaylist = LiveStreamPlaylist(is)

    streamPlaylist.mediaStreamType.isInstanceOf[Option[MediaStreamType]] should be(true)
    streamPlaylist.mediaStreamTypeInitializationVectorCompatibilityVersion.isInstanceOf[Option[MediaStreamTypeInitializationVectorCompatibilityVersion]] should be(true)
    streamPlaylist.mediaStreamTargetDuration.isInstanceOf[Option[MediaStreamTargetDuration]] should be(true)
    streamPlaylist.mediaStreamMediaSequence.isInstanceOf[Option[MediaStreamMediaSequence]] should be(true)
    streamPlaylist.mediaStreamProgramDateTime.isInstanceOf[Option[MediaStreamProgramDateTime]] should be(true)
    streamPlaylist.mediaStreamPlaylistTransportStreams.isInstanceOf[Option[List[MediaStreamPlaylistTransportStream]]] should be(true)
  }

  test("test write") {
    //Given
    val is = getClass.getClassLoader.getResource("master.m3u8").openStream()

    //When
    val streamPlaylist = MasterStreamPlaylist(is)

    //Then
    streamPlaylist.toPlaylistString.isInstanceOf[String] should be(true)

  }

  test("test saveToFile") {
    try {
      //Given
      val is = getClass.getClassLoader.getResource("master.m3u8").openStream()

      //When
      val streamPlaylist = MasterStreamPlaylist(is)
      streamPlaylist.saveToFile("master2.m3u8")

      //Then
      val data2: String = Source.fromFile("master2.m3u8").getLines() reduce { (a, b) => s"$a\n$b" }

      data2 should not be empty
    } finally {
      Files.delete(Paths.get("master2.m3u8"))
    }
  }

  test("test builders") {
    //Given
    val is = getClass.getClassLoader.getResource("master.m3u8").openStream()


    //When
    val pl = MasterStreamPlaylist(is)

    val streamPlaylist = pl
      .withMediaStreamType(Option(MediaStreamType("foobar")))
      .withMediaStreamTypeInfo(Option(
        List(
          MediaStreamTypeInfo("AUDIO", "aac", "fre", "French", "YES", "YES"),
          MediaStreamTypeInfo("AUDIO", "aac", "eng", "English", "YES", "YES")
        )
      ))
      .withMediaStreamInfo(pl.mediaStreamInfo.values map {
        it =>
          it.bandWith -> MediaStreamInfo(
            it.bandWith,
            it.codecs,
            it.resolution,
            it.closedCaption,
            it.audio, it.asset.replace("asset", "french_asset"))
      } toMap)
      .withMediaStreamFrameInfo(Map("2222" -> MediaStreamFrameInfo("2222", List("sdsd"), "sds", "sdsd")))

    //Then
    streamPlaylist.mediaStreamType.get.name should be("foobar")
    streamPlaylist.mediaStreamTypeInfos.get.head.name should be("French")
    streamPlaylist.mediaStreamTypeInfos.get.last.name should be("English")
    streamPlaylist.mediaStreamInfo("2100000").asset should be("vod_french_asset_1800k.m3u8")
    streamPlaylist.mediaStreamFrameInfo("2222").bandWith should be("2222")

  }


}
