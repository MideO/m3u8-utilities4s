package com.github.mideo.media.m3u8.parser

import java.io.InputStream

import scala.io.Source

import Deserializers._
import Serializers._
import ListOfMediaStreamPlaylistParts._


object VodStreamPlaylist {
  def apply(data: String): VodStreamPlaylist = {
    data.toVodStreamPlaylist
  }

  def apply(data: InputStream): VodStreamPlaylist = {
    Source.fromInputStream(data)
      .getLines() reduce {
      (a, b) => s"$a\n$b"
    } toVodStreamPlaylist
  }

  def apply(mappings: List[MediaStreamPlaylistParts]): VodStreamPlaylist = {
    VodStreamPlaylist(
      mappings extractMediaStreamType,
      mappings extractMediaStreamTypeInitializationVectorCompatibilityVersion,
      mappings extractMediaStreamTargetDuration,
      mappings extractMediaStreamMediaSequence,
      mappings extractMediaStreamPlaylistType,
      mappings extractMediaStreamProgramDateTime,
      mappings extractMediaStreamPlaylistTransportStreams,
      mappings extractMediaStreamEnd
    )
  }
}


case class VodStreamPlaylist(mediaStreamType: Option[MediaStreamType],
                             mediaStreamTypeInitializationVectorCompatibilityVersion: Option[MediaStreamTypeInitializationVectorCompatibilityVersion],
                             mediaStreamTargetDuration: Option[MediaStreamTargetDuration],
                             mediaStreamMediaSequence: Option[MediaStreamMediaSequence],
                             mediaStreamPlaylistType: Option[MediaStreamPlaylistType],
                             mediaStreamProgramDateTime: Option[MediaStreamProgramDateTime],
                             mediaStreamPlaylistTransportStreams: Option[List[MediaStreamPlaylistTransportStream]],
                             mediaStreamEnd: Option[MediaStreamEnd]) extends StreamPlaylist {

  override def toString: String = this.toVodStreamPlaylistString
}