
import com.github.mideo.M3U8ParserSuite
import com.github.mideo.media.m3u8._
import com.github.mideo.media.m3u8._
import com.github.mideo.media.m3u8.domain._
import org.scalatest.{Assertion, Failed, Outcome, Succeeded}

import scala.io.Source
import scala.concurrent.Future


class StreamPlaylistTest extends M3U8ParserSuite {

  test("test MasterStreamPlaylist from String") {
    //Given
    val is = getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data: String = Source.fromInputStream(is).getLines() reduce { (a, b) => s"$a\n$b" }

    //When
    MasterStreamPlaylist(data) map {
      streamPlaylist: MasterStreamPlaylist =>
        streamPlaylist.mediaStreamType.isInstanceOf[Option[MediaStreamType]] should be(true)
        streamPlaylist.mediaStreamTypeInfos.isInstanceOf[Option[List[MediaStreamTypeInfo]]] should be(true)
        streamPlaylist.mediaStreamInfo.isInstanceOf[Map[String, MediaStreamInfo]] should be(true)
        streamPlaylist.mediaStreamFrameInfo.isInstanceOf[Map[String, MediaStreamFrameInfo]] should be(true)
    }


  }

  test("test MasterStreamPlaylist from InputStream") {
    //Given
    val is = getClass.getClassLoader.getResource("master.m3u8").openStream()

    //When
    MasterStreamPlaylist(is) map {
      streamPlaylist: MasterStreamPlaylist =>
        streamPlaylist.mediaStreamType.isInstanceOf[Option[MediaStreamType]] should be(true)
        streamPlaylist.mediaStreamTypeInfos.isInstanceOf[Option[List[MediaStreamTypeInfo]]] should be(true)
        streamPlaylist.mediaStreamInfo.isInstanceOf[Map[String, MediaStreamInfo]] should be(true)
        streamPlaylist.mediaStreamFrameInfo.isInstanceOf[Map[String, MediaStreamFrameInfo]] should be(true)
    }
  }

  test("test VodStreamPlaylist from String") {
    //Given
    val is = getClass.getClassLoader.getResource("vod_asset_1800k.m3u8").openStream()

    val data: String = Source.fromInputStream(is).getLines() reduce { (a, b) => s"$a\n$b" }

    //When
    VodStreamPlaylist(data) map {
      streamPlaylist: VodStreamPlaylist =>
        streamPlaylist.mediaStreamType.isInstanceOf[Option[MediaStreamType]] should be(true)
        streamPlaylist.mediaStreamTypeInitializationVectorCompatibilityVersion.isInstanceOf[Option[MediaStreamTypeInitializationVectorCompatibilityVersion]] should be(true)
        streamPlaylist.mediaStreamTargetDuration.isInstanceOf[Option[MediaStreamTargetDuration]] should be(true)
        streamPlaylist.mediaStreamMediaSequence.isInstanceOf[Option[MediaStreamMediaSequence]] should be(true)
        streamPlaylist.mediaStreamPlaylistType.isInstanceOf[Option[MediaStreamPlaylistType]] should be(true)
        streamPlaylist.mediaStreamProgramDateTime.isInstanceOf[Option[MediaStreamProgramDateTime]] should be(true)
        streamPlaylist.mediaStreamPlaylistTransportStreams.isInstanceOf[Option[List[MediaStreamPlaylistTransportStream]]] should be(true)
        streamPlaylist.mediaStreamEnd.isInstanceOf[Option[MediaStreamEnd]] should be(true)
    }
  }

  test("test VodStreamPlaylist from InputStream") {
    //Given
    val is = getClass.getClassLoader.getResource("vod_asset_1800k.m3u8").openStream()

    //When
    VodStreamPlaylist(is) map {
      streamPlaylist: VodStreamPlaylist =>

        streamPlaylist.mediaStreamType.isInstanceOf[Option[MediaStreamType]] should be(true)
        streamPlaylist.mediaStreamTypeInitializationVectorCompatibilityVersion.isInstanceOf[Option[MediaStreamTypeInitializationVectorCompatibilityVersion]] should be(true)
        streamPlaylist.mediaStreamTargetDuration.isInstanceOf[Option[MediaStreamTargetDuration]] should be(true)
        streamPlaylist.mediaStreamMediaSequence.isInstanceOf[Option[MediaStreamMediaSequence]] should be(true)
        streamPlaylist.mediaStreamPlaylistType.isInstanceOf[Option[MediaStreamPlaylistType]] should be(true)
        streamPlaylist.mediaStreamProgramDateTime.isInstanceOf[Option[MediaStreamProgramDateTime]] should be(true)
        streamPlaylist.mediaStreamPlaylistTransportStreams.isInstanceOf[Option[List[MediaStreamPlaylistTransportStream]]] should be(true)
        streamPlaylist.mediaStreamEnd.isInstanceOf[Option[MediaStreamEnd]] should be(true)
    }
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
    streamPlaylist.toString.isInstanceOf[String] should be(true)

  }


  test("test builders") {
    //Given
    val is = getClass.getClassLoader.getResource("master.m3u8").openStream()

    //When
    MasterStreamPlaylist(is) map {
      it: MasterStreamPlaylist =>
        it.withMediaStreamType(Option(MediaStreamType("foobar")))

    } map {
      it: MasterStreamPlaylist =>
        it.withMediaStreamTypeInfo(Option(
          List(
            MediaStreamTypeInfo("AUDIO", "aac", "fre", "French", "YES", "YES"),
            MediaStreamTypeInfo("AUDIO", "aac", "eng", "English", "YES", "YES")
          )
        ))
    } map {
      m: MasterStreamPlaylist =>
        m.withMediaStreamInfo(Map("2100000" -> MediaStreamInfo("2100000", List("avc1.42001e", "mp4a.40.2"), "320", "NONE", "acc", "vod_french_asset_1800k.m3u8")))

    } map {
      m: MasterStreamPlaylist => m.withMediaStreamFrameInfo(Map("2222" -> MediaStreamFrameInfo("2222", List("sdsd"), "sds", "sdsd")))
    } map {
      //Then
      streamPlaylist: MasterStreamPlaylist =>
        streamPlaylist.mediaStreamType.get.name should be("foobar")
        streamPlaylist.mediaStreamTypeInfos.get.head.name should be("French")
        streamPlaylist.mediaStreamTypeInfos.get.last.name should be("English")
        streamPlaylist.mediaStreamInfo("2100000").asset should be("vod_french_asset_1800k.m3u8")
        streamPlaylist.mediaStreamFrameInfo("2222").bandWith should be("2222")
    }

  }


}
