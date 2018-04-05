package com.github.mideo.media.m3u8

import java.io.InputStream

import com.github.mideo.media.m3u8.domain.Deserializers._
import com.github.mideo.media.m3u8.domain.Serializers._
import com.github.mideo.media.m3u8.io.FileSystem
import com.github.mideo.media.m3u8.domain.ListOfMediaStreamPlaylistParts._
import com.github.mideo.media.m3u8.domain._

import scala.concurrent.{ExecutionContext, Future}



object VodStreamPlaylist {
  def apply(data: String)(implicit ec:ExecutionContext): Future[VodStreamPlaylist] =  data.toVodStreamPlaylist

  def apply(data: InputStream)(implicit ec:ExecutionContext): Future[VodStreamPlaylist] = FileSystem.read(data, (a:String, b:String) => s"$a\n$b") map {
    _ toVodStreamPlaylist } flatten

  def apply(mappings: List[MediaStreamPlaylistParts])(implicit ec:ExecutionContext): Future[VodStreamPlaylist] = Future {
    VodStreamPlaylist(
      mappings extractMediaStreamType,
      mappings extractMediaStreamTypeInitializationVectorCompatibilityVersion,
      mappings extractMediaStreamTargetDuration,
      mappings extractMediaStreamMediaSequence,
      mappings extractMediaStreamPlaylistType,
      mappings extractMediaStreamProgramDateTime,
      mappings extractMediaStreamPlaylistTransportStreams,
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
                             mediaStreamPlaylistTransportStreams: Option[List[MediaStreamPlaylistTransportStream]],
                             mediaStreamEnd: Option[MediaStreamEnd]) extends StreamPlaylist {

  override def toString: String = this.toVodStreamPlaylistString
}