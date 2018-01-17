package com.github.mideo.media.m3u8.parser

import java.io.InputStream

import Deserializers._
import Serializers._

import scala.io.Source

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
}

case class VodStreamPlaylist(mediaStreamType: Option[MediaStreamType],
                             mediaStreamTypeInitializationVectorCompatibilityVersion: Option[MediaStreamTypeInitializationVectorCompatibilityVersion],
                             mediaStreamTargetDuration: Option[MediaStreamTargetDuration],
                             mediaStreamMediaSequence: Option[MediaStreamMediaSequence],
                             mediaStreamPlaylistType: Option[MediaStreamPlaylistType],
                             mediaStreamProgramDateTime: Option[MediaStreamProgramDateTime],
                             mediaStreamPlaylistItems: Option[List[MediaStreamPlaylistItem]],
                             mediaStreamEnd: Option[MediaStreamEnd]) extends StreamPlaylist {
  override def toPlaylistString: String = this.toVodStreamPlaylistString
}