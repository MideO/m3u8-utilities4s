package com.github.mideo.media.m3u8.parser

trait StreamPlaylist  {

  val mediaStreamType: Option[MediaStreamType]

  def toPlaylistString: String

}


