package com.github.mideo.media.m3u8.parser

trait MediaStreamPlaylistParts

sealed trait StreamInfo extends MediaStreamPlaylistParts {
  val bandWith: String
  val codecs: List[String]
  val resolution: String
}

case class MediaStreamType(name: String) extends MediaStreamPlaylistParts

case class MediaStreamTypeInfo(mediaType: String,
                               groupId: String,
                               language: String,
                               name: String,
                               autoSelect: String,
                               mediaDefault: String) extends MediaStreamPlaylistParts

case class MediaStreamInfo(bandWith: String,
                           codecs: List[String],
                           resolution: String,
                           closedCaption: String,
                           audio: String, asset: String
                          ) extends StreamInfo


case class MediaStreamFrameInfo(bandWith: String,
                                codecs: List[String],
                                resolution: String,
                                uri: String) extends StreamInfo