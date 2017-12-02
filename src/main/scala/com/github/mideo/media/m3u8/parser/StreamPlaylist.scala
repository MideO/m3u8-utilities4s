package com.github.mideo.media.m3u8.parser

trait StreamPlaylist {
  val mediaStreamType: MediaStreamType
  val mediaStreamTypeInfo: MediaStreamTypeInfo
  val mediaStreamInfo: Map[String, MediaStreamInfo]
  val mediaStreamFrameInfo: Map[String, MediaStreamFrameInfo]

  def write: String

}

object M3U8MasterStreamPlaylist {
  def apply(data: String): M3U8MasterStreamPlaylist = {
    StreamTransformer.deserialize(data)
  }
}


case class M3U8MasterStreamPlaylist(mediaStreamType: MediaStreamType,
                                    mediaStreamIndependentSegments: MediaStreamIndependentSegments,
                                    mediaStreamTypeInfo: MediaStreamTypeInfo,
                                    mediaStreamInfo: Map[String, MediaStreamInfo],
                                    mediaStreamFrameInfo: Map[String, MediaStreamFrameInfo]) extends StreamPlaylist {

  override def write: String = StreamTransformer.serialize(this)

  def withMediaStreamType(m: MediaStreamType): M3U8MasterStreamPlaylist = {
    new M3U8MasterStreamPlaylist(
      m,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfo,
      mediaStreamInfo,
      mediaStreamFrameInfo)
  }

  def withMediaStreamTypeInfo(m: MediaStreamTypeInfo): M3U8MasterStreamPlaylist = {
    new M3U8MasterStreamPlaylist(
      mediaStreamType,
      mediaStreamIndependentSegments,
      m,
      mediaStreamInfo,
      mediaStreamFrameInfo)
  }

  def withMediaStreamInfo(m: Map[String, MediaStreamInfo]): M3U8MasterStreamPlaylist = {
    new M3U8MasterStreamPlaylist(
      mediaStreamType,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfo,
      m,
      mediaStreamFrameInfo)
  }

  def withMediaStreamFrameInfo(m:Map[String, MediaStreamFrameInfo]): M3U8MasterStreamPlaylist= {
    new M3U8MasterStreamPlaylist(
      mediaStreamType,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfo,
      mediaStreamInfo,
      m)
  }

}
