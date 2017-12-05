package com.github.mideo.media.m3u8.parser

import scala.collection.mutable

private[media] object StreamTransformer {

  import Serializer._
  import Deserializer._

  val deserialize: String => M3U8MasterStreamPlaylist = mapData _ andThen buildMediaStreamPlaylist

  val serialize: M3U8MasterStreamPlaylist => String = stringifyPlaylist _ andThen fold
}


private object Deserializer {
  private def mapFields(s: String): Map[String, String] = {
    val listData = s.split(":")
    if (listData.length > 1) {
      val splat = {
        if (listData.tail.head.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)|\n").length > 1) {
          listData.tail.head.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)|\n")
        }
        else listData.tail.head.split(",")
      }

      return splat
        .map(text => text.split("="))
        .map(list =>
          if (list.length > 1) list.head -> list.tail.head.replace("\"", "").replace("\n", "")
          else "XARGS" -> list.head.replace("\"", "").replace("\n", "")
        ).toMap
    }
    Map("XARGS" -> listData.head.replace("\n", "").replace("\"", ""))
  }

  def buildMediaStreamPlaylist(mappings: Array[MediaStreamPlaylistParts]): M3U8MasterStreamPlaylist = {
    val mediaStreamType = mappings.filter {
      _.isInstanceOf[MediaStreamType]
    }.head.asInstanceOf[MediaStreamType]
    val mediaStreamIndependentSegments = mappings.filter {
      _.isInstanceOf[MediaStreamIndependentSegments]
    }.head.asInstanceOf[MediaStreamIndependentSegments]

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

    M3U8MasterStreamPlaylist(mediaStreamType, mediaStreamIndependentSegments, mediaStreamTypeInfo, mediaStreamInfo, mediaStreamFrameInfo)

  }

  def mapData(data: String): Array[MediaStreamPlaylistParts] = {

    data.split("#") map {
      case line: String if StreamPlaylistSection.MediaStreamType.isSectionType(line) =>
        val data = mapFields(line)
        MediaStreamType(data(StreamPlaylistSection.MediaStreamType.XARGS))
      case line: String if StreamPlaylistSection.MediaStreamIndependentSegments.isSectionType(line) =>
        val data = mapFields(line)
        MediaStreamIndependentSegments()
      case line: String if StreamPlaylistSection.MediaStreamFrameInfo.isSectionType(line) =>
        val data = mapFields(line)
        MediaStreamFrameInfo(data(StreamPlaylistSection.MediaStreamFrameInfo.BANDWIDTH),
          data(StreamPlaylistSection.MediaStreamFrameInfo.CODECS).split(",").toList,
          data(StreamPlaylistSection.MediaStreamFrameInfo.RESOLUTION),
          data(StreamPlaylistSection.MediaStreamFrameInfo.URI)
        )
      case line: String if StreamPlaylistSection.MediaStreamInfo.isSectionType(line) =>
        val data = mapFields(line)
        MediaStreamInfo(
          data(StreamPlaylistSection.MediaStreamInfo.BANDWIDTH),
          data(StreamPlaylistSection.MediaStreamInfo.CODECS).split(",").toList,
          data(StreamPlaylistSection.MediaStreamInfo.RESOLUTION),
          data(StreamPlaylistSection.MediaStreamInfo.CLOSED_CAPTIONS),
          data(StreamPlaylistSection.MediaStreamInfo.AUDIO),
          data(StreamPlaylistSection.MediaStreamInfo.XARGS))
      case line: String if StreamPlaylistSection.MediaStreamTypeInfo.isSectionType(line) =>
        val data = mapFields(line)
        MediaStreamTypeInfo(
          data(StreamPlaylistSection.MediaStreamTypeInfo.TYPE),
          data(StreamPlaylistSection.MediaStreamTypeInfo.GROUP_ID),
          data(StreamPlaylistSection.MediaStreamTypeInfo.LANGUAGE),
          data(StreamPlaylistSection.MediaStreamTypeInfo.NAME),
          data(StreamPlaylistSection.MediaStreamTypeInfo.AUTOSELECT),
          data(StreamPlaylistSection.MediaStreamTypeInfo.DEFAULT))

      case line: String if StreamPlaylistSection.MediaStreamTypeInitializationVectorCompatibilityVersion.isSectionType(line) =>
        val data = mapFields(line)
        MediaStreamTypeInitializationVectorCompatibilityVersion(data(StreamPlaylistSection.MediaStreamType.XARGS))
      case line: String if StreamPlaylistSection.MediaStreamTargetDuration.isSectionType(line) =>
        val data = mapFields(line)
        MediaStreamTargetDuration(data(StreamPlaylistSection.MediaStreamType.XARGS))
      case line: String if StreamPlaylistSection.MediaStreamMediaSequence.isSectionType(line) =>
        val data = mapFields(line)
        MediaStreamMediaSequence(data(StreamPlaylistSection.MediaStreamType.XARGS))
      case line: String if StreamPlaylistSection.MediaStreamPlaylistType.isSectionType(line) =>
        val data = mapFields(line)
        MediaStreamPlaylistType(data(StreamPlaylistSection.MediaStreamType.XARGS))
      case line: String if StreamPlaylistSection.MediaStreamProgramDateTime.isSectionType(line) =>
        val data = mapFields(line)
        MediaStreamProgramDateTime(data(StreamPlaylistSection.MediaStreamType.XARGS))


      case _ => None
    } filter {
      x => x != None
    } map {
      _.asInstanceOf[MediaStreamPlaylistParts]
    }
  }
}

private object Serializer {

  def fold(playListPartsString: List[String]): String = {
    playListPartsString.fold("") { (a, b) => s"$a\n$b" }
  }

  def stringifyPlaylist(m3U8StreamPlaylist: M3U8MasterStreamPlaylist): List[String] = {
    val arr = mutable.ArrayBuffer.empty[String]
    List(m3U8StreamPlaylist.mediaStreamType.toString,
      m3U8StreamPlaylist.mediaStreamIndependentSegments,
      m3U8StreamPlaylist.mediaStreamTypeInfo.toString,
      m3U8StreamPlaylist.mediaStreamInfo,
      m3U8StreamPlaylist.mediaStreamFrameInfo) foreach {
      case value: Map[_, _] => arr ++= value.values.toList map (_.toString)
      case x => arr += x.toString
    }
    arr.toList
  }
}