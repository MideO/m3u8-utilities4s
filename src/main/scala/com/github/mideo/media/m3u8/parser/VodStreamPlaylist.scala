package com.github.mideo.media.m3u8.parser

import java.io.InputStream

import scala.io.Source

import Deserializers._
import Serializers._


object VodStreamPlaylist {

  private implicit class PimpedListOfMediaStreamPlaylistParts(s: List[MediaStreamPlaylistParts]) {
    def extractMediaStreamType: Option[MediaStreamType] = s collectFirst { case c if c.isInstanceOf[MediaStreamType] => c.asInstanceOf[MediaStreamType] }

    def extractMediaStreamTypeInitializationVectorCompatibilityVersion: Option[MediaStreamTypeInitializationVectorCompatibilityVersion] = s collectFirst { case c if c.isInstanceOf[MediaStreamTypeInitializationVectorCompatibilityVersion] => c.asInstanceOf[MediaStreamTypeInitializationVectorCompatibilityVersion] }

    def extractMediaStreamTargetDuration: Option[MediaStreamTargetDuration] = s collectFirst { case c if c.isInstanceOf[MediaStreamTargetDuration] => c.asInstanceOf[MediaStreamTargetDuration] }

    def extractMediaStreamMediaSequence: Option[MediaStreamMediaSequence] = s collectFirst { case c if c.isInstanceOf[MediaStreamMediaSequence] => c.asInstanceOf[MediaStreamMediaSequence] }

    def extractMediaStreamPlaylistType: Option[MediaStreamPlaylistType] = s collectFirst { case c if c.isInstanceOf[MediaStreamPlaylistType] => c.asInstanceOf[MediaStreamPlaylistType] }

    def extractMediaStreamProgramDateTime: Option[MediaStreamProgramDateTime] = s collectFirst { case c if c.isInstanceOf[MediaStreamProgramDateTime] => c.asInstanceOf[MediaStreamProgramDateTime] }

    def extractMediaStreamPlaylistItems: Option[List[MediaStreamPlaylistItem]] = Option(s collect { case c if c.isInstanceOf[MediaStreamPlaylistItem] => c.asInstanceOf[MediaStreamPlaylistItem] })

    def extractMediaStreamEnd: Option[MediaStreamEnd] = s collectFirst { case c if c.isInstanceOf[MediaStreamEnd] => c.asInstanceOf[MediaStreamEnd] }

  }

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
      mappings extractMediaStreamPlaylistItems,
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
                             mediaStreamPlaylistItems: Option[List[MediaStreamPlaylistItem]],
                             mediaStreamEnd: Option[MediaStreamEnd]) extends StreamPlaylist {
  override def toPlaylistString: String = this.toVodStreamPlaylistString
}