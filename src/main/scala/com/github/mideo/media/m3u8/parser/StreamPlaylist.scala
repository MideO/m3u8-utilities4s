package com.github.mideo.media.m3u8.parser

import java.nio.file.{Files, Paths}


trait StreamPlaylist {
  val mediaStreamType: Option[MediaStreamType]

  def toPlaylistString: String

  def saveToFile(fileName:String): Unit = {
    Files.write(Paths.get(fileName), toPlaylistString.getBytes())
  }

}
