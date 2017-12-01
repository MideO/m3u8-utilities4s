package com.github.mideo.media.m3u8.parser

case class StreamPlaylist(mediaStreamType: MediaStreamType,
                          mediaStreamTypeInfo: MediaStreamTypeInfo,
                          mediaStreamInfo: Map[String, MediaStreamInfo],
                          mediaStreamFrameInfo: Map[String, MediaStreamFrameInfo]) {

}


object StreamPlaylist {
  def apply(data: String): StreamPlaylist = {
    StreamTransformer.deserialize(data)
  }
}