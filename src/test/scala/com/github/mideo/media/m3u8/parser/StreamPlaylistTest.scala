package com.github.mideo.media.m3u8.parser
import scala.io.Source


class StreamPlaylistTest extends M3U8ParserSuite {

  test("StreamPlaylist") {
    //Given
    val is =   getClass.getClassLoader.getResource("no_drm.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}

    //When
    val streamPlaylist = M3U8MasterStreamPlaylist(data)


    streamPlaylist.mediaStreamType.isInstanceOf[MediaStreamType] should be(true)
    streamPlaylist.mediaStreamTypeInfo.isInstanceOf[MediaStreamTypeInfo] should be(true)
    streamPlaylist.mediaStreamInfo.isInstanceOf[Map[String, MediaStreamInfo]] should be(true)
    streamPlaylist.mediaStreamFrameInfo.isInstanceOf[Map[String, MediaStreamFrameInfo]] should be(true)
  }

  test("write") {
    //Given
    val is =   getClass.getClassLoader.getResource("no_drm.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}

    //When
    val streamPlaylist = M3U8MasterStreamPlaylist(data)

    //Then
    streamPlaylist.write.isInstanceOf[String] should be(true)

  }



}
