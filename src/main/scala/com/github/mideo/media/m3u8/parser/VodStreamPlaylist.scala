package com.github.mideo.media.m3u8.parser

object VodStreamPlaylist {
  def apply(data: String): VodStreamPlaylist = {
    StreamTransformer.deserializeVOD(data)
  }
}

case class VodStreamPlaylist(mediaStreamType: Option[MediaStreamType],
                             mediaStreamTypeInitializationVectorCompatibilityVersion: Option[MediaStreamTypeInitializationVectorCompatibilityVersion],
                             mediaStreamTargetDuration: Option[MediaStreamTargetDuration],
                             mediaStreamMediaSequence: Option[MediaStreamMediaSequence],
                             mediaStreamPlaylistType: Option[MediaStreamPlaylistType],
                             mediaStreamProgramDateTime: Option[MediaStreamProgramDateTime],
                             mediaStreamPlaylistItems: Option[List[MediaStreamPlaylistItem]],
                             mediaStreamEnd: Option[MediaStreamEnd]) extends StreamPlaylist {
  override def write: String = StreamTransformer.serializeVOD(this)
}