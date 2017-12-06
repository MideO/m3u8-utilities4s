package com.github.mideo.media.m3u8.parser

import scala.io.Source

class StreamTransformerTest extends M3U8ParserSuite {

  test("testDeserialize master playlist") {
    //Given
    val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}

    //When
    val streamPlaylist = StreamTransformer.deserialize(data)

    streamPlaylist.mediaStreamType.get.name should be("EXTM3U")
    streamPlaylist.mediaStreamIndependentSegments.get.toString should be("#EXT-X-INDEPENDENT-SEGMENTS")
    streamPlaylist.mediaStreamTypeInfo.get.name should be("English")
    streamPlaylist.mediaStreamInfo.isEmpty should be(false)
    streamPlaylist.mediaStreamFrameInfo.isEmpty should be(false)

  }

//  test("testDeserialize asset playlist") {
//    //Given
//    val is =   getClass.getClassLoader.getResource("asset.m3u8").openStream()
//
//    val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}
//
//    //When
//    val streamPlaylist = StreamTransformer.deserialize(data)
//
//  }

  test("testSerialize") {
    //Given
    val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}

    val streamPlaylist = StreamTransformer.deserialize(data)

    //When
    val result = StreamTransformer.serialize(streamPlaylist)

    //Then
    val res: Map[String, Boolean] = data.split("\n") map {
      x => x -> result.contains(x)
    } toMap

    res.values foreach(
      _ should be(true)
    )

  }

  test("testDeserialize with missing parts") {
    //Given
    val is =   getClass.getClassLoader.getResource("master_missing_parts.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}

    //When
    val streamPlaylist = StreamTransformer.deserialize(data)

    streamPlaylist.mediaStreamType.get.name should be("EXTM3U")
    streamPlaylist.mediaStreamIndependentSegments should be(None)
    streamPlaylist.mediaStreamTypeInfo.get.name should be("English")
    streamPlaylist.mediaStreamInfo.isEmpty should be(false)
    streamPlaylist.mediaStreamFrameInfo.isEmpty should be(true)

  }
  test("testSerialize with missing parts") {
    //Given
    val is =   getClass.getClassLoader.getResource("master_missing_parts.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}

    val streamPlaylist = StreamTransformer.deserialize(data)

    //When
    val result = StreamTransformer.serialize(streamPlaylist)

    //Then
    val res: Map[String, Boolean] = data.split("\n") map {
      x => x -> result.contains(x)
    } toMap

    res.values foreach(
      _ should be(true)
      )

  }

}
