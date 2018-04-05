package com.github.mideo.media.m3u8

import com.github.mideo.M3U8ParserSuite
import com.github.mideo.media.m3u8.domain.Deserializers._

import scala.io.Source


class DeserializersTest extends M3U8ParserSuite {

  test("testDeserialize master playlist") {
    //Given
    val is = getClass.getClassLoader.getResource("master.m3u8").openStream()

    val data: String = Source.fromInputStream(is).getLines() reduce {
      _ + "\n" + _
    }

    //When
    data.toMasterPlaylist map {
      streamPlaylist =>
        //Then
        streamPlaylist.mediaStreamType.get.name should be("EXTM3U")
        streamPlaylist.mediaStreamIndependentSegments.get.toString should be("#EXT-X-INDEPENDENT-SEGMENTS")
        streamPlaylist.mediaStreamTypeInfos.get.head.name should be("English")
        streamPlaylist.mediaStreamInfo.isEmpty should be(false)
        streamPlaylist.mediaStreamFrameInfo.isEmpty should be(false)

    }


  }

  test("testDeserialize vod playlist") {
    //Given
    val is = getClass.getClassLoader.getResource("vod_asset_1800k.m3u8").openStream()

    val data: String = Source.fromInputStream(is).getLines() reduce {
      _ + "\n" + _
    }

    data.toVodStreamPlaylist map {
      streamPlaylist =>
        //Then
        streamPlaylist.mediaStreamType.get.name should be("EXTM3U")
        streamPlaylist.mediaStreamTypeInitializationVectorCompatibilityVersion.get.version should be("4")
        streamPlaylist.mediaStreamTargetDuration.get.duration should be("5")
        streamPlaylist.mediaStreamMediaSequence.get.numberOfUrls should be("1")
        streamPlaylist.mediaStreamPlaylistType.get.steamType should be("VOD")
        streamPlaylist.mediaStreamProgramDateTime.get.dateTime should be("2017-11-16T00:00:00.000Z")
        streamPlaylist.mediaStreamPlaylistTransportStreams.isEmpty should be(false)
        streamPlaylist.mediaStreamEnd.isEmpty should be(false)

    }


  }

  test("testDeserialize with missing parts") {
    //Given
    val is = getClass.getClassLoader.getResource("master_missing_parts.m3u8").openStream()

    val data: String = Source.fromInputStream(is).getLines() reduce {
      _ + "\n" + _
    }

    data.toMasterPlaylist map {
      streamPlaylist =>
        streamPlaylist.mediaStreamType.get.name should be("EXTM3U")
        streamPlaylist.mediaStreamIndependentSegments should be(None)
        streamPlaylist.mediaStreamTypeInfos.get.head.name should be("English")
        streamPlaylist.mediaStreamInfo.isEmpty should be(false)
        streamPlaylist.mediaStreamFrameInfo.isEmpty should be(true)

    }



  }

}
