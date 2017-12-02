package com.github.mideo.media.m3u8.parser
trait MediaStreamPlaylistParts {
  def toString: String
}

sealed trait StreamInfo  {
  val bandWith: String
  val codecs: List[String]
  val resolution: String
}

case class MediaStreamType(name: String) extends MediaStreamPlaylistParts{
  override def toString: String = s"#$name"
}

case class MediaStreamIndependentSegments() extends MediaStreamPlaylistParts{
  override def toString: String = s"#${StreamPlaylistSection.MediaStreamIndependentSegments.identifier}"
}

case class MediaStreamTypeInfo(mediaType: String,
                               groupId: String,
                               language: String,
                               name: String,
                               autoSelect: String,
                               mediaDefault: String) extends MediaStreamPlaylistParts {

  override def toString: String = s"#${StreamPlaylistSection.MediaStreamTypeInfo.identifier}:" +
    s"${StreamPlaylistSection.MediaStreamTypeInfo.TYPE}=$mediaType," +
    s"""${StreamPlaylistSection.MediaStreamTypeInfo.GROUP_ID}="$groupId",""" +
    s"""${StreamPlaylistSection.MediaStreamTypeInfo.LANGUAGE}="$language",""" +
    s"""${StreamPlaylistSection.MediaStreamTypeInfo.NAME}="$name",""" +
    s"${StreamPlaylistSection.MediaStreamTypeInfo.AUTOSELECT}=$autoSelect," +
    s"${StreamPlaylistSection.MediaStreamTypeInfo.DEFAULT}=$mediaDefault"
}

case class MediaStreamInfo(bandWith: String,
                           codecs: List[String],
                           resolution: String,
                           closedCaption: String,
                           audio: String,
                           asset: String
                          ) extends StreamInfo with MediaStreamPlaylistParts {

  override def toString: String = s"#${StreamPlaylistSection.MediaStreamInfo.identifier}:" +
    s"${StreamPlaylistSection.MediaStreamInfo.BANDWIDTH}=$bandWith," +
    s"""${StreamPlaylistSection.MediaStreamInfo.CODECS}="${codecs.fold("") {(a,b) => s"$a,$b"}.substring(1)}",""" +
    s"${StreamPlaylistSection.MediaStreamInfo.RESOLUTION}=$resolution," +
    s"${StreamPlaylistSection.MediaStreamInfo.CLOSED_CAPTIONS}=$closedCaption," +
    s"""${StreamPlaylistSection.MediaStreamInfo.AUDIO}="$audio""""+
    s"\n$asset"

}


case class MediaStreamFrameInfo(bandWith: String,
                                codecs: List[String],
                                resolution: String,
                                uri: String) extends StreamInfo with MediaStreamPlaylistParts{

  override def toString: String = s"#${StreamPlaylistSection.MediaStreamFrameInfo.identifier}:" +
    s"${StreamPlaylistSection.MediaStreamFrameInfo.BANDWIDTH}=$bandWith," +
    s"""${StreamPlaylistSection.MediaStreamFrameInfo.CODECS}="${codecs.fold("") {(a,b) => s"$a,$b"}.substring(1)}",""" +
    s"${StreamPlaylistSection.MediaStreamFrameInfo.RESOLUTION}=$resolution,"+
    s"""${StreamPlaylistSection.MediaStreamFrameInfo.URI}="$uri""""

}

