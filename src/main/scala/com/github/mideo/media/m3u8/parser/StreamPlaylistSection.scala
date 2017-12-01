package com.github.mideo.media.m3u8.parser

trait StreamPlaylistSection

object StreamPlaylistSection {

  sealed trait SectionFields extends Enumeration {
    val XARGS = "XARGS"
  }

  sealed trait MediaStreamPlaylistSection extends SectionFields{
    val identifier: String

    def isSectionType(string: String): Boolean = {
      string.replace("#", "").toLowerCase.startsWith(identifier.toLowerCase)
    }
  }

  object MediaStreamType extends MediaStreamPlaylistSection {
    override def isSectionType(string: String): Boolean = {
      string.toLowerCase.startsWith(identifier.toLowerCase) && !string.contains("-")
    }
    override val identifier: String = "EXT"
  }

  object MediaStreamTypeInfo extends MediaStreamPlaylistSection {
    override val identifier: String = "EXT-X-MEDIA"

    val TYPE = "TYPE"
    val GROUP_ID = "GROUP-ID"
    val LANGUAGE = "LANGUAGE"
    val NAME = "NAME"
    val AUTOSELECT = "AUTOSELECT"
    val DEFAULT = "DEFAULT"
  }

  object MediaStreamInfo extends MediaStreamPlaylistSection {
    override val identifier: String = "EXT-X-STREAM-INF"
    val BANDWIDTH = "BANDWIDTH"
    val CODECS = "CODECS"
    val RESOLUTION = "RESOLUTION"
    val CLOSE_CAPTIONS = "CLOSE-CAPTIONS"
    val AUDIO = "AUDIO"

  }

  object MediaStreamFrameInfo extends MediaStreamPlaylistSection {
    override val identifier: String = "EXT-X-I-FRAME-STREAM-INF"
    val BANDWIDTH ="BANDWIDTH"
    val CODECS = "CODECS"
    val RESOLUTION = "RESOLUTION"
    val URI  = "URI"
  }
}