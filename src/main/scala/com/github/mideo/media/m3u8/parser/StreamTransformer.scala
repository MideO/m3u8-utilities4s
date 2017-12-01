package com.github.mideo.media.m3u8.parser

private[media] object StreamTransformer {
  private def _splatMap(s: String): Map[String, String] = {
    val listData = s.split(":")
    if (listData.length > 1) {
      return listData.tail.head.split(",")
        .map(text => text.split("="))
        .map(list =>
          if (list.length > 1) list.head -> list.tail.head.replace("\"", "").replace("\n", "")
          else "XARGS" -> list.head.replace("\"", "").replace("\n", "")
        ).toMap
    }
    Map("XARGS" -> listData.head.replace("\n", "").replace("\"", ""))
  }

  private def buildMediaStreamPlaylist(mappings: Array[MediaStreamPlaylistParts]): StreamPlaylist = {
    val mediaStreamType = mappings.filter {
      _.isInstanceOf[MediaStreamType]
    }.head.asInstanceOf[MediaStreamType]
    val mediaStreamTypeInfo = mappings.filter {
      _.isInstanceOf[MediaStreamTypeInfo]
    }.head.asInstanceOf[MediaStreamTypeInfo]

    val mediaStreamInfo = mappings.filter {
      _.isInstanceOf[MediaStreamInfo]
    } map { it => {
      val l = it.asInstanceOf[MediaStreamInfo]
      l.bandWith -> l
    }
    } toMap

    val mediaStreamFrameInfo = mappings.filter {
      _.isInstanceOf[MediaStreamFrameInfo]
    } map {
      it => {
        val l = it.asInstanceOf[MediaStreamFrameInfo]
        l.bandWith -> l
      }
    } toMap

    StreamPlaylist(mediaStreamType, mediaStreamTypeInfo, mediaStreamInfo, mediaStreamFrameInfo)

  }

  private def mapData(data: String): Array[MediaStreamPlaylistParts] = {
    data.split("#") map {
      case line: String if StreamPlaylistSection.MediaStreamType.isSectionType(line) =>
        val data = _splatMap(line)
        MediaStreamType(data(StreamPlaylistSection.MediaStreamType.XARGS))
      case line: String if StreamPlaylistSection.MediaStreamFrameInfo.isSectionType(line) =>
        val data = _splatMap(line)
        MediaStreamFrameInfo(data(StreamPlaylistSection.MediaStreamFrameInfo.BANDWIDTH),
          data(StreamPlaylistSection.MediaStreamFrameInfo.CODECS).split(",").toList,
          data(StreamPlaylistSection.MediaStreamFrameInfo.RESOLUTION),
          data(StreamPlaylistSection.MediaStreamFrameInfo.URI)
        )
      case line: String if StreamPlaylistSection.MediaStreamInfo.isSectionType(line) =>
        val data = _splatMap(line)
        MediaStreamInfo(
          data(StreamPlaylistSection.MediaStreamInfo.BANDWIDTH),
          data(StreamPlaylistSection.MediaStreamInfo.CODECS).split(",").toList,
          data(StreamPlaylistSection.MediaStreamInfo.RESOLUTION),
          data(StreamPlaylistSection.MediaStreamInfo.CLOSED_CAPTIONS),
          data(StreamPlaylistSection.MediaStreamInfo.AUDIO),
          data(StreamPlaylistSection.MediaStreamInfo.XARGS))
      case line: String if StreamPlaylistSection.MediaStreamTypeInfo.isSectionType(line) =>
        val data = _splatMap(line)
        MediaStreamTypeInfo(
          data(StreamPlaylistSection.MediaStreamTypeInfo.TYPE),
          data(StreamPlaylistSection.MediaStreamTypeInfo.GROUP_ID),
          data(StreamPlaylistSection.MediaStreamTypeInfo.LANGUAGE),
          data(StreamPlaylistSection.MediaStreamTypeInfo.NAME),
          data(StreamPlaylistSection.MediaStreamTypeInfo.AUTOSELECT),
          data(StreamPlaylistSection.MediaStreamTypeInfo.DEFAULT))
      case _ => None
    } filter { x => x != None } map {
      _.asInstanceOf[MediaStreamPlaylistParts]
    }
  }

  val deserialize: String => StreamPlaylist = mapData _ andThen buildMediaStreamPlaylist
}
