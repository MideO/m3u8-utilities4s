package com.github.mideo.media.m3u8.parser

import java.nio.file.{Files, Paths}

trait StreamPlaylist {
  val mediaStreamType: Option[MediaStreamType]
  val mediaStreamTypeInfo: Option[MediaStreamTypeInfo]
  val mediaStreamInfo: Map[String, MediaStreamInfo]
  val mediaStreamFrameInfo: Map[String, MediaStreamFrameInfo]

  def write: String

  def saveToFile(fileName:String): Unit


}

object M3U8MasterStreamPlaylist {
  def apply(data: String): M3U8MasterStreamPlaylist = {
    StreamTransformer.deserialize(data)
  }
}


case class M3U8MasterStreamPlaylist(mediaStreamType: Option[MediaStreamType],
                                    mediaStreamIndependentSegments: Option[MediaStreamIndependentSegments],
                                    mediaStreamTypeInfo: Option[MediaStreamTypeInfo ],
                                    mediaStreamInfo: Map[String, MediaStreamInfo],
                                    mediaStreamFrameInfo: Map[String, MediaStreamFrameInfo]) extends StreamPlaylist {

  override def write: String = StreamTransformer.serialize(this)

  override def saveToFile(fileName:String): Unit = {
    Files.write(Paths.get(fileName), write.getBytes())
  }


  def withMediaStreamType(m: Option[MediaStreamType]): M3U8MasterStreamPlaylist = {
    new M3U8MasterStreamPlaylist(
      m,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfo,
      mediaStreamInfo,
      mediaStreamFrameInfo)
  }

  def withMediaStreamTypeInfo(m: Option[MediaStreamTypeInfo]): M3U8MasterStreamPlaylist = {
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

  def withMediaStreamFrameInfo(m: Map[String, MediaStreamFrameInfo]): M3U8MasterStreamPlaylist = {
    new M3U8MasterStreamPlaylist(
      mediaStreamType,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfo,
      mediaStreamInfo,
      m)
  }
}
