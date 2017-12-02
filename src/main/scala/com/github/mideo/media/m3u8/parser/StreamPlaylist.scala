package com.github.mideo.media.m3u8.parser

trait StreamPlaylist {
  val mediaStreamType: MediaStreamType
  val mediaStreamTypeInfo: MediaStreamTypeInfo
  val mediaStreamInfo: Map[String, MediaStreamInfo]
  val mediaStreamFrameInfo: Map[String, MediaStreamFrameInfo]

  def write:String

}

object M3U8MasterStreamPlaylist {
  def apply(data: String): M3U8MasterStreamPlaylist = {
    StreamTransformer.deserialize(data)
  }
}


case class M3U8MasterStreamPlaylist(mediaStreamType: MediaStreamType,
                                    mediaStreamIndependentSegments:MediaStreamIndependentSegments,
                                    mediaStreamTypeInfo: MediaStreamTypeInfo,
                                    mediaStreamInfo: Map[String, MediaStreamInfo],
                                    mediaStreamFrameInfo: Map[String, MediaStreamFrameInfo]) extends StreamPlaylist{

  override def write: String = StreamTransformer.serialize(this)


}
