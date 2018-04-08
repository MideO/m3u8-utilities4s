package com.github.mideo.media.m3u8

import com.github.mideo.media.m3u8.domain.MediaStreamType

private[media] trait StreamPlaylist {
  val mediaStreamType: Option[MediaStreamType]
  override def toString: String
}
