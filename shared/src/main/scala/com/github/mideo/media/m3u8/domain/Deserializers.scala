package com.github.mideo.media.m3u8.domain

import com.github.mideo.media.m3u8._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

private[m3u8] object Deserializers {
  private def mapFields(s: String): Map[String, String] = {
    val listData = s.split(":(?=(?!//))")
    if (listData.length > 1) {
      val splat = {
        if (listData.last.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)|\n").length > 1) {
          listData.last.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)|\n")
        }
        else listData.last.split(",")
      }

      return splat
        .map(text => text.split("="))
        .map(list =>
          if (list.length > 1) list.head -> list.last.replace("\"", "").replace("\n", "")
          else "XARGS" -> list.head.replace("\"", "").replace("\n", "")
        ).toMap
    }
    Map("XARGS" -> stripSpaces(listData.head))
  }

  private def stripSpaces(s: String, subString: String = ""): String = s.replace(subString, "").replace("\n", "").replace("\"", "")

  private def mapData(data: String): List[MediaStreamPlaylistParts] = {
    data.split("#(?=(?!EXT-X-KEY))").toList map {
      case line: String if StreamPlaylistSection.MediaStreamType.isSectionType(line) =>
        val data = mapFields(line)
        MediaStreamType(data(StreamPlaylistSection.MediaStreamType.XARGS))

      case line: String if StreamPlaylistSection.MediaStreamIndependentSegments.isSectionType(line) =>
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
        MediaStreamTypeInitializationVectorCompatibilityVersion(
          stripSpaces(line, StreamPlaylistSection.MediaStreamTypeInitializationVectorCompatibilityVersion.identifier))

      case line: String if StreamPlaylistSection.MediaStreamTargetDuration.isSectionType(line) =>
        MediaStreamTargetDuration(stripSpaces(line, StreamPlaylistSection.MediaStreamTargetDuration.identifier))

      case line: String if StreamPlaylistSection.MediaStreamMediaSequence.isSectionType(line) =>
        MediaStreamMediaSequence(stripSpaces(line, StreamPlaylistSection.MediaStreamMediaSequence.identifier))

      case line: String if StreamPlaylistSection.MediaStreamPlaylistType.isSectionType(line) =>
        MediaStreamPlaylistType(stripSpaces(line, StreamPlaylistSection.MediaStreamPlaylistType.identifier))

      case line: String if StreamPlaylistSection.MediaStreamProgramDateTime.isSectionType(line) =>
        MediaStreamProgramDateTime(stripSpaces(line, StreamPlaylistSection.MediaStreamProgramDateTime.identifier))

      case line: String if StreamPlaylistSection.MediaStreamPlaylistTransportStream.isSectionType(line) =>
        val data = line.split("\n")
        val durationData = mapFields(data.head)
        if (data.length > 2) {
          val drmInfo = mapFields(data.find(StreamPlaylistSection.MediaStreamPlaylistTransportStreamDRMInfo.isSectionType).get)
          val asset = mapFields(data.find(_.endsWith(".ts")).get)
          MediaStreamPlaylistTransportStream(durationData(StreamPlaylistSection.MediaStreamPlaylistTransportStream.XARGS),
            Option(MediaStreamPlaylistTransportStreamDRMInfo(
              drmInfo(StreamPlaylistSection.MediaStreamPlaylistTransportStreamDRMInfo.METHOD),
              drmInfo(StreamPlaylistSection.MediaStreamPlaylistTransportStreamDRMInfo.URI),
              drmInfo(StreamPlaylistSection.MediaStreamPlaylistTransportStreamDRMInfo.IV))
            ),
            asset(StreamPlaylistSection.MediaStreamPlaylistTransportStream.XARGS))
        } else {
          val asset = mapFields(data(data.length - 1))
          MediaStreamPlaylistTransportStream(durationData(StreamPlaylistSection.MediaStreamPlaylistTransportStream.XARGS),
            None,
            asset(StreamPlaylistSection.MediaStreamPlaylistTransportStream.XARGS))
        }
      case line: String if StreamPlaylistSection.MediaStreamEnd.isSectionType(line) =>
        MediaStreamEnd()

      case _ => None

    } collect {
      case x if x != None => x.asInstanceOf[MediaStreamPlaylistParts]
    }
  }


  implicit class PimpedMediaPlaylistString(val s: String) {
    def toVodStreamPlaylist: Future[VodStreamPlaylist] = (mapData _ andThen VodStreamPlaylist.apply) (s)

    def toLiveStreamPlaylist: LiveStreamPlaylist = (mapData _ andThen LiveStreamPlaylist.apply) (s)

    def toMasterPlaylist: Future[MasterStreamPlaylist] = (mapData _ andThen MasterStreamPlaylist.apply) (s)
  }

}
