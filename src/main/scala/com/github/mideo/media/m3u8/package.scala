package com.github.mideo.media

import com.github.mideo.media.m3u8.domain.MediaStreamType

package object m3u8 {
  private[media] trait StreamPlaylist {
    val mediaStreamType: Option[MediaStreamType]
    override def toString: String
  }
}
