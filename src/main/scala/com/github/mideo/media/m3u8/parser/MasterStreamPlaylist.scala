package com.github.mideo.media.m3u8.parser


object MasterStreamPlaylist {
  def apply(data: String): MasterStreamPlaylist = {
    StreamTransformer.deserializeMaster(data)
  }
}


case class MasterStreamPlaylist(mediaStreamType: Option[MediaStreamType],
                                mediaStreamIndependentSegments: Option[MediaStreamIndependentSegments],
                                mediaStreamTypeInfos: Option[List[MediaStreamTypeInfo]],
                                mediaStreamInfo: Map[String, MediaStreamInfo],
                                mediaStreamFrameInfo: Map[String, MediaStreamFrameInfo]) extends StreamPlaylist {


  def withMediaStreamType(m: Option[MediaStreamType]): MasterStreamPlaylist = {
    new MasterStreamPlaylist(
      m,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfos,
      mediaStreamInfo,
      mediaStreamFrameInfo)
  }

  def withMediaStreamTypeInfo(m: Option[List[MediaStreamTypeInfo]]): MasterStreamPlaylist = {
    new MasterStreamPlaylist(
      mediaStreamType,
      mediaStreamIndependentSegments,
      m,
      mediaStreamInfo,
      mediaStreamFrameInfo)
  }

  def withMediaStreamInfo(m: Map[String, MediaStreamInfo]): MasterStreamPlaylist = {
    new MasterStreamPlaylist(
      mediaStreamType,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfos,
      m,
      mediaStreamFrameInfo)
  }

  def withMediaStreamFrameInfo(m: Map[String, MediaStreamFrameInfo]): MasterStreamPlaylist = {
    new MasterStreamPlaylist(
      mediaStreamType,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfos,
      mediaStreamInfo,
      m)
  }

  override def write: String = StreamTransformer.serializeMaster(this)
}
