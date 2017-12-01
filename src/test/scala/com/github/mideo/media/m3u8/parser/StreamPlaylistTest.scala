package com.github.mideo.media.m3u8.parser


import scala.io.Source

class StreamPlaylistTest extends M3U8PerserSuite {

  test("StreamPlaylist should deserialize") {
    //Given
    val is =   getClass.getClassLoader.getResource("no_drm.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}

    val streamPlaylist = StreamPlaylist(data)

    streamPlaylist.mediaStreamType.name should be("EXTM3U")
    streamPlaylist.mediaStreamTypeInfo.name should be("English")
    streamPlaylist.mediaStreamInfo.isEmpty should be(false)
    streamPlaylist.mediaStreamFrameInfo.isEmpty should be(false)
  }



}
