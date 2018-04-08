package com.github.mideo.media.m3u8

import com.github.mideo.media.m3u8.domain.Deserializers._
import com.github.mideo.media.m3u8.domain.ListOfMediaStreamPlaylistParts._
import com.github.mideo.media.m3u8.domain.Serializers._
import com.github.mideo.media.m3u8.domain._

import scala.language.postfixOps


object LiveStreamPlaylist {
  def apply(data: String): LiveStreamPlaylist = {
    data.toLiveStreamPlaylist
  }

  def apply(mappings: List[MediaStreamPlaylistParts]): LiveStreamPlaylist = {
    LiveStreamPlaylist(
      mappings extractMediaStreamType,
      mappings extractMediaStreamTypeInitializationVectorCompatibilityVersion,
      mappings extractMediaStreamTargetDuration,
      mappings extractMediaStreamMediaSequence,
      mappings extractMediaStreamProgramDateTime,
      mappings extractMediaStreamPlaylistTransportStreams
    )
  }
}


case class LiveStreamPlaylist(mediaStreamType: Option[MediaStreamType],
                              mediaStreamTypeInitializationVectorCompatibilityVersion: Option[MediaStreamTypeInitializationVectorCompatibilityVersion],
                              mediaStreamTargetDuration: Option[MediaStreamTargetDuration],
                              mediaStreamMediaSequence: Option[MediaStreamMediaSequence],
                              mediaStreamProgramDateTime: Option[MediaStreamProgramDateTime],
                              mediaStreamPlaylistTransportStreams: Option[List[MediaStreamPlaylistTransportStream]]) extends StreamPlaylist {

  override def toString: String = this.toLiveStreamPlaylistString
}
