package com.github.mideo.media.m3u8

import com.github.mideo.media.m3u8.domain.Deserializers._
import com.github.mideo.media.m3u8.domain.Serializers._
import com.github.mideo.media.m3u8.domain._

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

object MasterStreamPlaylist {

  protected implicit class PimpedListOfMediaStreamPlaylistParts(s: List[MediaStreamPlaylistParts]) {
    def extractMediaStreamType: Option[MediaStreamType] = s collectFirst { case c if c.isInstanceOf[MediaStreamType] => c.asInstanceOf[MediaStreamType] }

    def extractMediaStreamIndependentSegments: Option[MediaStreamIndependentSegments] = s collectFirst { case c if c.isInstanceOf[MediaStreamIndependentSegments] => c.asInstanceOf[MediaStreamIndependentSegments] }

    def extractMediaStreamTypeInfo: Option[List[MediaStreamTypeInfo]] = Option(s collect { case c if c.isInstanceOf[MediaStreamTypeInfo] => c.asInstanceOf[MediaStreamTypeInfo] })

    def extractMediaStreamInfoToMap: Map[String, MediaStreamInfo] = s collect {
      case c if c.isInstanceOf[MediaStreamInfo] =>
        val l = c.asInstanceOf[MediaStreamInfo]
        l.bandWith -> l
    } toMap

    def extractMediaStreamFrameInfoToMap: Map[String, MediaStreamFrameInfo] = s collect {
      case c if c.isInstanceOf[MediaStreamFrameInfo] =>
        val l = c.asInstanceOf[MediaStreamFrameInfo]
        l.bandWith -> l
    } toMap
  }

  def apply(data: String = "")(implicit ec:ExecutionContext): Future[MasterStreamPlaylist] = data.toMasterPlaylist

  def apply(mappings: List[MediaStreamPlaylistParts])(implicit ec:ExecutionContext): Future[MasterStreamPlaylist] = Future {
    MasterStreamPlaylist(
      mappings extractMediaStreamType,
      mappings extractMediaStreamIndependentSegments,
      mappings extractMediaStreamTypeInfo,
      mappings extractMediaStreamInfoToMap,
      mappings extractMediaStreamFrameInfoToMap
    )
  }

}


case class MasterStreamPlaylist(mediaStreamType: Option[MediaStreamType],
                                mediaStreamIndependentSegments: Option[MediaStreamIndependentSegments],
                                mediaStreamTypeInfos: Option[List[MediaStreamTypeInfo]],
                                mediaStreamInfo: Map[String, MediaStreamInfo],
                                mediaStreamFrameInfo: Map[String, MediaStreamFrameInfo]) extends StreamPlaylist {


  def withMediaStreamType(m: Option[MediaStreamType]): MasterStreamPlaylist = {
    MasterStreamPlaylist(
      m,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfos,
      mediaStreamInfo,
      mediaStreamFrameInfo)
  }

  def withMediaStreamTypeInfo(m: Option[List[MediaStreamTypeInfo]]): MasterStreamPlaylist = {
    MasterStreamPlaylist(
      mediaStreamType,
      mediaStreamIndependentSegments,
      m,
      mediaStreamInfo,
      mediaStreamFrameInfo)
  }

  def withMediaStreamInfo(m: Map[String, MediaStreamInfo]): MasterStreamPlaylist = {
    MasterStreamPlaylist(
      mediaStreamType,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfos,
      m,
      mediaStreamFrameInfo)
  }

  def withMediaStreamFrameInfo(m: Map[String, MediaStreamFrameInfo]): MasterStreamPlaylist = {
    MasterStreamPlaylist(
      mediaStreamType,
      mediaStreamIndependentSegments,
      mediaStreamTypeInfos,
      mediaStreamInfo,
      m)
  }

  override def toString: String = this.toMasterPlaylistString
}
