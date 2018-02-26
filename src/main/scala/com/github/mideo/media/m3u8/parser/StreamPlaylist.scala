package com.github.mideo.media.m3u8.parser

private[parser] trait StreamPlaylist {
  val mediaStreamType: Option[MediaStreamType]

  override def toString: String
}


