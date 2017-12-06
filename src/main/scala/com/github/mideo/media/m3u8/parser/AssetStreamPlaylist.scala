package com.github.mideo.media.m3u8.parser

object AssetStreamPlaylist {
  def apply(data: String): StreamPlaylist = {
    StreamTransformer.deserialize(data)
  }
}

abstract case class AssetStreamPlaylist(mediaStreamType: Option[MediaStreamType],
                                        mediaStreamTypeInitializationVectorCompatibilityVersion: Option[MediaStreamTypeInitializationVectorCompatibilityVersion],
                                        mediaStreamTargetDuration: Option[MediaStreamTargetDuration ],
                                        mediaStreamMediaSequence: Option[MediaStreamMediaSequence],
                                        mediaStreamPlaylistType: Option[MediaStreamPlaylistType],
                                        mediaStreamProgramDateTime: Option[MediaStreamProgramDateTime],
                                        mediaStreamPlaylistItems: List[MediaStreamPlaylistItem],
                                        mediaStreamEnd: Option[MediaStreamEnd]) extends StreamPlaylist