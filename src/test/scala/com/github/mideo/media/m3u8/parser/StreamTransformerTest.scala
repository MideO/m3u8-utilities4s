package com.github.mideo.media.m3u8.parser

import scala.io.Source

class StreamTransformerTest extends M3U8ParserSuite {

  test("testDeserialize") {
    //Given
    val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}

    //When
    val streamPlaylist = StreamTransformer.deserialize(data)

    streamPlaylist.mediaStreamType.name should be("EXTM3U")
    streamPlaylist.mediaStreamIndependentSegments.toString should be("#EXT-X-INDEPENDENT-SEGMENTS")
    streamPlaylist.mediaStreamTypeInfo.name should be("English")
    streamPlaylist.mediaStreamInfo.isEmpty should be(false)
    streamPlaylist.mediaStreamFrameInfo.isEmpty should be(false)

  }

  test("testSerialize") {
    //Given
    val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}

    val streamPlaylist = StreamTransformer.deserialize(data)

    //When
    val result = StreamTransformer.serialize(streamPlaylist)

    //Then
    val res: Map[String, Boolean] = result.split("\n") map {
      x => x -> data.contains(x)
    } toMap

    res.values foreach(
      _ should be(true)
    )

  }

}
