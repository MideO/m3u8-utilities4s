package com.github.mideo.media.m3u8.parser

private[parser] object Deserializers {
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

  private def stripSpaces(s: String, subString: String = ""): String = {
    s.replace(subString, "").replace("\n", "").replace("\"", "")
  }

  private def buildVodMediaStreamPlaylist(mappings: Array[MediaStreamPlaylistParts]): VodStreamPlaylist = {
    val mediaStreamType: Option[MediaStreamType] = mappings.filter(
      _.isInstanceOf[MediaStreamType]
    ) match {
      case a: Array[MediaStreamPlaylistParts] if a.isEmpty => None
      case a: Array[MediaStreamPlaylistParts] if !a.isEmpty => Some(a.head.asInstanceOf[MediaStreamType])
    }

    val mediaStreamTypeInitializationVectorCompatibilityVersion: Option[MediaStreamTypeInitializationVectorCompatibilityVersion] = mappings.filter(
      _.isInstanceOf[MediaStreamTypeInitializationVectorCompatibilityVersion]
    ) match {
      case a: Array[MediaStreamPlaylistParts] if a.isEmpty => None
      case a: Array[MediaStreamPlaylistParts] if !a.isEmpty => Some(a.head.asInstanceOf[MediaStreamTypeInitializationVectorCompatibilityVersion])
    }

    val mediaStreamTargetDuration: Option[MediaStreamTargetDuration] = mappings.filter(
      _.isInstanceOf[MediaStreamTargetDuration]
    ) match {
      case a: Array[MediaStreamPlaylistParts] if a.isEmpty => None
      case a: Array[MediaStreamPlaylistParts] if !a.isEmpty => Some(a.head.asInstanceOf[MediaStreamTargetDuration])
    }

    val mediaStreamMediaSequence: Option[MediaStreamMediaSequence] = mappings.filter(
      _.isInstanceOf[MediaStreamMediaSequence]
    ) match {
      case a: Array[MediaStreamPlaylistParts] if a.isEmpty => None
      case a: Array[MediaStreamPlaylistParts] if !a.isEmpty => Some(a.head.asInstanceOf[MediaStreamMediaSequence])
    }

    val mediaStreamPlaylistType: Option[MediaStreamPlaylistType] = mappings.filter(
      _.isInstanceOf[MediaStreamPlaylistType]
    ) match {
      case a: Array[MediaStreamPlaylistParts] if a.isEmpty => None
      case a: Array[MediaStreamPlaylistParts] if !a.isEmpty => Some(a.head.asInstanceOf[MediaStreamPlaylistType])
    }

    val mediaStreamProgramDateTime: Option[MediaStreamProgramDateTime] = mappings.filter(
      _.isInstanceOf[MediaStreamProgramDateTime]
    ) match {
      case a: Array[MediaStreamPlaylistParts] if a.isEmpty => None
      case a: Array[MediaStreamPlaylistParts] if !a.isEmpty => Some(a.head.asInstanceOf[MediaStreamProgramDateTime])
    }

    val mediaStreamPlaylistItems = mappings.filter {
      _.isInstanceOf[MediaStreamPlaylistItem]
    } match {
      case a: Array[MediaStreamPlaylistParts] if a.isEmpty => None
      case a: Array[MediaStreamPlaylistParts] if !a.isEmpty => Some(a.toList.asInstanceOf[List[MediaStreamPlaylistItem]])
    }

    val mediaStreamEnd = mappings.filter {
      _.isInstanceOf[MediaStreamEnd]
    } match {
      case a: Array[MediaStreamPlaylistParts] if a.isEmpty => None
      case a: Array[MediaStreamPlaylistParts] if !a.isEmpty => Some(a.head.asInstanceOf[MediaStreamEnd])
    }

    VodStreamPlaylist(
      mediaStreamType,
      mediaStreamTypeInitializationVectorCompatibilityVersion,
      mediaStreamTargetDuration,
      mediaStreamMediaSequence,
      mediaStreamPlaylistType,
      mediaStreamProgramDateTime,
      mediaStreamPlaylistItems,
      mediaStreamEnd
    )
  }

  private def buildMasterMediaStreamPlaylist(mappings: Array[MediaStreamPlaylistParts]): MasterStreamPlaylist = {

    val mediaStreamType: Option[MediaStreamType] = mappings.filter(
      _.isInstanceOf[MediaStreamType]
    ) match {
      case a: Array[MediaStreamPlaylistParts] if a.isEmpty => None
      case a: Array[MediaStreamPlaylistParts] if !a.isEmpty => Some(a.head.asInstanceOf[MediaStreamType])
    }

    val mediaStreamIndependentSegments = mappings.filter {
      _.isInstanceOf[MediaStreamIndependentSegments]
    } match {
      case a: Array[MediaStreamPlaylistParts] if a.isEmpty => None
      case a: Array[MediaStreamPlaylistParts] if !a.isEmpty => Some(a.head.asInstanceOf[MediaStreamIndependentSegments])
    }

    val mediaStreamTypeInfos = mappings.filter {
      _.isInstanceOf[MediaStreamTypeInfo]
    } match {
      case a: Array[MediaStreamPlaylistParts] if a.isEmpty => None
      case a: Array[MediaStreamPlaylistParts] if !a.isEmpty => Some(a.toList.asInstanceOf[List[MediaStreamTypeInfo]])
    }


    val mediaStreamInfo = mappings.filter {
      _.isInstanceOf[MediaStreamInfo]
    } match {
      case a: Array[MediaStreamPlaylistParts] if a.isEmpty => Map.empty[String, MediaStreamInfo]
      case a: Array[MediaStreamPlaylistParts] if !a.isEmpty => a map {
        it => {
          val l = it.asInstanceOf[MediaStreamInfo]
          l.bandWith -> l
        }
      } toMap
    }

    val mediaStreamFrameInfo = mappings.filter {
      _.isInstanceOf[MediaStreamFrameInfo]
    } match {
      case a: Array[MediaStreamPlaylistParts] if a.isEmpty => Map.empty[String, MediaStreamFrameInfo]
      case a: Array[MediaStreamPlaylistParts] if !a.isEmpty => a map {
        it => {
          val l = it.asInstanceOf[MediaStreamFrameInfo]
          l.bandWith -> l
        }
      } toMap
    }

    MasterStreamPlaylist(
      mediaStreamType,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfos,
      mediaStreamInfo,
      mediaStreamFrameInfo
    )

  }

  private def mapData(data: String): Array[MediaStreamPlaylistParts] = {
    data.split("#(?=(?!EXT-X-KEY))") map {
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

      case line: String if StreamPlaylistSection.MediaStreamPlaylistItem.isSectionType(line) =>
        val data = line.split("\n")
        val durationData = mapFields(data.head)
        if (data.length > 2) {
          val drmInfo = mapFields(data(1))
          val asset = mapFields(data(data.length - 1))
          MediaStreamPlaylistItem(durationData(StreamPlaylistSection.MediaStreamPlaylistItem.XARGS),
            Option(MediaStreamPlaylistItemDRMInfo(
              drmInfo(StreamPlaylistSection.MediaStreamPlaylistItemDRMInfo.METHOD),
              drmInfo(StreamPlaylistSection.MediaStreamPlaylistItemDRMInfo.URI),
              drmInfo(StreamPlaylistSection.MediaStreamPlaylistItemDRMInfo.IV))
            ),
            asset(StreamPlaylistSection.MediaStreamPlaylistItem.XARGS))
        } else {
          val asset = mapFields(data(data.length - 1))
          MediaStreamPlaylistItem(durationData(StreamPlaylistSection.MediaStreamPlaylistItem.XARGS),
            None,
            asset(StreamPlaylistSection.MediaStreamPlaylistItem.XARGS))
        }
      case line: String if StreamPlaylistSection.MediaStreamEnd.isSectionType(line) =>
        MediaStreamEnd()

      case _ => None

    } filter {
      x => x != None
    } map {
      _.asInstanceOf[MediaStreamPlaylistParts]
    }
  }


  implicit class PimpedMediaPlaylistString(val s: String) {
    def toVodStreamPlaylist: VodStreamPlaylist =  (mapData _ andThen buildVodMediaStreamPlaylist).apply(s)
    def toMasterPlaylist: MasterStreamPlaylist = (mapData _ andThen buildMasterMediaStreamPlaylist).apply(s)
  }

}
