package com.github.mideo.media.m3u8.parser

import scala.io.Source
import Deserializers._
import Serializers._
import com.github.mideo.media.m3u8.M3U8ParserSuite


class StreamTransformerTest extends M3U8ParserSuite {

  test("testSerialize") {
    //Given
    val is = getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data: String = Source.fromInputStream(is).getLines() reduce {
      _ + "\n" + _
    }

    val streamPlaylist = data.toMasterPlaylist

    //When
    val result = streamPlaylist map {
      result => result.toMasterPlaylistString
    }

    //Then
    result map {
      it =>
        data.split("\n") map {
          x =>
            withClue(s"$x not in result") {
             it.contains(x)
            }
        } reduce { (a:Boolean, b:Boolean ) => a && b } should equal(true)
    }


  }

  test("testSerializeVod") {
    //Given
    val is = getClass.getClassLoader.getResource("vod_asset_1800k.m3u8").openStream()

    val data: String = Source.fromInputStream(is).getLines() reduce {
      _ + "\n" + _
    }

    //When
    val streamPlaylist = data.toVodStreamPlaylist

    //When
    val result = streamPlaylist map {
      result => result.toVodStreamPlaylistString
    }


    //Then//Then
    result map {
      it =>
        data.split("\n") map {
          x =>
            withClue(s"$x not in $result") {
              it.contains(x)
            }
        } reduce { (a:Boolean, b:Boolean ) => a && b } should equal(true)
    }

  }

  test("testSerialize with missing parts") {
    //Given
    val is = getClass.getClassLoader.getResource("master_missing_parts.m3u8").openStream()

    val data: String = Source.fromInputStream(is).getLines() reduce {
      _ + "\n" + _
    }

    val streamPlaylist = data.toMasterPlaylist

    //When
    val result = streamPlaylist map {
      result => result.toMasterPlaylistString
    }

    //Then
    result map {
      it =>
        data.split("\n") map {
          x =>
            withClue(s"$x not in result") {
              it.contains(x)
            }
        } reduce { (a:Boolean, b:Boolean ) => a && b } should equal(true)
    }

  }

}
