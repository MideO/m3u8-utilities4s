package com.github.mideo.media.m3u8.parser

class M3U8StreamPlaylistSectionTest extends M3U8ParserSuite {


  test("MediaStreamType testIsSectionType match") {
    StreamPlaylistSection.MediaStreamType.isSectionType("EXTM3U") should be(true)
  }

  test("MediaStreamType testIsSectionType unmatched") {
    StreamPlaylistSection.MediaStreamType.isSectionType("ABCS") should be(false)
  }


  test("MediaStreamTypeInfo testIsSectionType match") {
    StreamPlaylistSection.MediaStreamTypeInfo.isSectionType("#EXT-X-MEDIA:TYPE=AUDIO,GROUP-ID=\"aac\",LANGUAGE=\"eng\",NAME=\"English\",AUTOSELECT=YES,DEFAULT=YES") should be(true)
  }

  test("MediaStreamTypeInfo testIsSectionType unmatched") {
    StreamPlaylistSection.MediaStreamTypeInfo.isSectionType("ABCS") should be(false)
  }


  test("MediaStreamInfo testIsSectionType match") {
    StreamPlaylistSection.MediaStreamInfo.isSectionType("#EXT-X-STREAM-INF:BANDWIDTH=1400000,CODECS=\"avc1.4d001f,mp4a.40.2\",RESOLUTION=640x360,CLOSED-CAPTIONS=NONE,AUDIO=\"aac\"") should be(true)
  }

  test("MediaStreamInfo testIsSectionType unmatched") {
    StreamPlaylistSection.MediaStreamInfo.isSectionType("ABCS") should be(false)
  }


  test("MediaStreamFrameInfo testIsSectionType match") {
    StreamPlaylistSection.MediaStreamFrameInfo.isSectionType("#EXT-X-I-FRAME-STREAM-INF:BANDWIDTH=32000,CODECS=\"avc1.42001e\",RESOLUTION=384x216,URI=\"asset_450k_I-Frame.m3u8\"") should be(true)
  }

  test("MediaStreamFrameInfo testIsSectionType unmatched") {
    StreamPlaylistSection.MediaStreamFrameInfo.isSectionType("ABCS") should be(false)
  }


}
