package com.github.mideo.media.m3u8.domain


private[m3u8] object ListOfMediaStreamPlaylistParts {


  implicit class PimpedListOfMediaStreamPlaylistParts(s: List[MediaStreamPlaylistParts]) {
    def extractMediaStreamType: Option[MediaStreamType] = s collectFirst { case c if c.isInstanceOf[MediaStreamType] => c.asInstanceOf[MediaStreamType] }

    def extractMediaStreamTypeInitializationVectorCompatibilityVersion: Option[MediaStreamTypeInitializationVectorCompatibilityVersion] = s collectFirst { case c if c.isInstanceOf[MediaStreamTypeInitializationVectorCompatibilityVersion] => c.asInstanceOf[MediaStreamTypeInitializationVectorCompatibilityVersion] }

    def extractMediaStreamTargetDuration: Option[MediaStreamTargetDuration] = s collectFirst { case c if c.isInstanceOf[MediaStreamTargetDuration] => c.asInstanceOf[MediaStreamTargetDuration] }

    def extractMediaStreamMediaSequence: Option[MediaStreamMediaSequence] = s collectFirst { case c if c.isInstanceOf[MediaStreamMediaSequence] => c.asInstanceOf[MediaStreamMediaSequence] }

    def extractMediaStreamPlaylistType: Option[MediaStreamPlaylistType] = s collectFirst { case c if c.isInstanceOf[MediaStreamPlaylistType] => c.asInstanceOf[MediaStreamPlaylistType] }

    def extractMediaStreamProgramDateTime: Option[MediaStreamProgramDateTime] = s collectFirst { case c if c.isInstanceOf[MediaStreamProgramDateTime] => c.asInstanceOf[MediaStreamProgramDateTime] }

    def extractMediaStreamPlaylistTransportStreams: Option[List[MediaStreamPlaylistTransportStream]] = Option(s collect { case c if c.isInstanceOf[MediaStreamPlaylistTransportStream] => c.asInstanceOf[MediaStreamPlaylistTransportStream] })

    def extractMediaStreamEnd: Option[MediaStreamEnd] = s collectFirst { case c if c.isInstanceOf[MediaStreamEnd] => c.asInstanceOf[MediaStreamEnd] }

  }

}
