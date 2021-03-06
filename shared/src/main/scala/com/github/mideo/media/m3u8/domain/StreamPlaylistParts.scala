package com.github.mideo.media.m3u8.domain

trait MediaStreamPlaylistParts {
  def toString: String
}


sealed trait StreamInfo {
  val bandWith: String
  val codecs: List[String]
  val resolution: String
}

case class MediaStreamType(name: String) extends MediaStreamPlaylistParts {
  override def toString: String = s"#$name"
}

case class MediaStreamIndependentSegments() extends MediaStreamPlaylistParts {
  override def toString: String = s"#${StreamPlaylistSection.MediaStreamIndependentSegments.identifier}"
}

case class MediaStreamTypeInfo(mediaType: String,
                               groupId: String,
                               language: String,
                               name: String,
                               autoSelect: String,
                               mediaDefault: String) extends MediaStreamPlaylistParts {

  override def toString: String = s"#${StreamPlaylistSection.MediaStreamTypeInfo.identifier}" +
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

  override def toString: String = s"#${StreamPlaylistSection.MediaStreamInfo.identifier}" +
    s"${StreamPlaylistSection.MediaStreamInfo.BANDWIDTH}=$bandWith," +
    s"""${StreamPlaylistSection.MediaStreamInfo.CODECS}="${
      codecs.reduce {
        _ + "," + _
      }
    }",""" +
    s"${StreamPlaylistSection.MediaStreamInfo.RESOLUTION}=$resolution," +
    s"${StreamPlaylistSection.MediaStreamInfo.CLOSED_CAPTIONS}=$closedCaption," +
    s"""${StreamPlaylistSection.MediaStreamInfo.AUDIO}="$audio"""" +
    s"\n$asset"

}


case class MediaStreamFrameInfo(bandWith: String,
                                codecs: List[String],
                                resolution: String,
                                uri: String) extends StreamInfo with MediaStreamPlaylistParts {

  override def toString: String = s"#${StreamPlaylistSection.MediaStreamFrameInfo.identifier}" +
    s"${StreamPlaylistSection.MediaStreamFrameInfo.BANDWIDTH}=$bandWith," +
    s"""${StreamPlaylistSection.MediaStreamFrameInfo.CODECS}="${
      codecs.reduce {
        _ + "," + _
      }
    }",""" +
    s"${StreamPlaylistSection.MediaStreamFrameInfo.RESOLUTION}=$resolution," +
    s"""${StreamPlaylistSection.MediaStreamFrameInfo.URI}="$uri""""

}

case class MediaStreamTypeInitializationVectorCompatibilityVersion(version: String) extends MediaStreamPlaylistParts {
  override def toString: String = s"#${StreamPlaylistSection.MediaStreamTypeInitializationVectorCompatibilityVersion.identifier}$version"
}

case class MediaStreamTargetDuration(duration: String) extends MediaStreamPlaylistParts {
  override def toString: String = s"#${StreamPlaylistSection.MediaStreamTargetDuration.identifier}$duration"
}

case class MediaStreamMediaSequence(numberOfUrls: String) extends MediaStreamPlaylistParts {
  override def toString: String = s"#${StreamPlaylistSection.MediaStreamMediaSequence.identifier}$numberOfUrls"
}

case class MediaStreamPlaylistType(steamType: String) extends MediaStreamPlaylistParts {
  override def toString: String = s"#${StreamPlaylistSection.MediaStreamPlaylistType.identifier}$steamType"
}

case class MediaStreamProgramDateTime(dateTime: String) extends MediaStreamPlaylistParts {
  override def toString: String = s"#${StreamPlaylistSection.MediaStreamProgramDateTime.identifier}$dateTime"
}

case class MediaStreamPlaylistTransportStreamDRMInfo(method: String, uri: String, initializationVector: String) extends MediaStreamPlaylistParts {

  override def toString: String = s"#${StreamPlaylistSection.MediaStreamPlaylistTransportStreamDRMInfo.identifier}" +
    s"${StreamPlaylistSection.MediaStreamPlaylistTransportStreamDRMInfo.METHOD}=$method," +
    s"""${StreamPlaylistSection.MediaStreamPlaylistTransportStreamDRMInfo.URI}="$uri",""" +
    s"${StreamPlaylistSection.MediaStreamPlaylistTransportStreamDRMInfo.IV}=$initializationVector"
}

case class MediaStreamPlaylistTransportStream(duration: String, drmInfo: Option[MediaStreamPlaylistTransportStreamDRMInfo], uri: String) extends MediaStreamPlaylistParts {
  override def toString: String = if (drmInfo.isEmpty) {
    s"#${StreamPlaylistSection.MediaStreamPlaylistTransportStream.identifier}$duration,\n$uri"
  } else {
    s"#${StreamPlaylistSection.MediaStreamPlaylistTransportStream.identifier}$duration,\n" +
      s"${drmInfo.get.toString}\n" +
      s"$uri"
  }
}

case class MediaStreamEnd() extends MediaStreamPlaylistParts {
  override def toString: String = s"#${StreamPlaylistSection.MediaStreamEnd.identifier}"
}