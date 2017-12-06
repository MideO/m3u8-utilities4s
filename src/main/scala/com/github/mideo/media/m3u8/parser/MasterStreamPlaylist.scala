package com.github.mideo.media.m3u8.parser

import java.nio.file.{Files, Paths}

object MasterStreamPlaylist {
  def apply(data: String): MasterStreamPlaylist = {
    StreamTransformer.deserialize(data)
  }
}


case class MasterStreamPlaylist(mediaStreamType: Option[MediaStreamType],
                                mediaStreamIndependentSegments: Option[MediaStreamIndependentSegments],
                                mediaStreamTypeInfo: Option[MediaStreamTypeInfo ],
                                mediaStreamInfo: Map[String, MediaStreamInfo],
                                mediaStreamFrameInfo: Map[String, MediaStreamFrameInfo]) extends StreamPlaylist {

  override def write: String = StreamTransformer.serialize(this)

  override def saveToFile(fileName:String): Unit = {
    Files.write(Paths.get(fileName), write.getBytes())
  }


  def withMediaStreamType(m: Option[MediaStreamType]): MasterStreamPlaylist = {
    new MasterStreamPlaylist(
      m,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfo,
      mediaStreamInfo,
      mediaStreamFrameInfo)
  }

  def withMediaStreamTypeInfo(m: Option[MediaStreamTypeInfo]): MasterStreamPlaylist = {
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
      mediaStreamTypeInfo,
      m,
      mediaStreamFrameInfo)
  }

  def withMediaStreamFrameInfo(m: Map[String, MediaStreamFrameInfo]): MasterStreamPlaylist = {
    new MasterStreamPlaylist(
      mediaStreamType,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfo,
      mediaStreamInfo,
      m)
  }
}
