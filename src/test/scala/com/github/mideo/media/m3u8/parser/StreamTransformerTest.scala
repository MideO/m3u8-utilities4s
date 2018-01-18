package com.github.mideo.media.m3u8.parser

import scala.io.Source
import Deserializers._
import Serializers._
class StreamTransformerTest extends M3U8ParserSuite {

  test("testSerialize") {
    //Given
    val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines()reduce {_+"\n"+_}

    val streamPlaylist = data.toMasterPlaylist

    //When
    val result = streamPlaylist.toMasterPlaylistString

    //Then
    data.split("\n") map {
      x => withClue(s"$x not in result") { result.contains(x) should be(true) }
    }

  }

  test("testSerializeVod") {
    //Given
    val is =   getClass.getClassLoader.getResource("vod_asset_1800k.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines()reduce {_+"\n"+_}

    //When
    val streamPlaylist = data.toVodStreamPlaylist

    //When
    val result = streamPlaylist.toVodStreamPlaylistString

    //Then
    data.split("\n") map {
      x => withClue(s"$x not in $result") { result.contains(x) should be(true) }
    }

  }

  test("testSerialize with missing parts") {
    //Given
    val is =   getClass.getClassLoader.getResource("master_missing_parts.m3u8").openStream()

    val data:String = Source.fromInputStream(is).getLines()reduce {_+"\n"+_}

    val streamPlaylist = data.toMasterPlaylist

    //When
    val result = streamPlaylist.toMasterPlaylistString

    //Then
    data.split("\n") map {
      x => withClue(s"$x not in result") { result.contains(x) should be(true) }
    }

  }

}
