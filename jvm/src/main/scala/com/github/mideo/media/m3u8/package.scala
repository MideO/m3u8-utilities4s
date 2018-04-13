package com.github.mideo.media

import java.io.InputStream

import com.github.mideo.media.m3u8.io._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

package object m3u8 {

  import com.github.mideo.media.m3u8.domain.Deserializers._

  implicit class MasterStreamPlaylistExt(obj: MasterStreamPlaylist.type) {
    def apply(data: InputStream)(implicit ec: ExecutionContext): Future[MasterStreamPlaylist] = FileSystem.read(data, (a: String, b: String) => a + "\n" + b) map {
      _ toMasterPlaylist
    } flatMap identity

  }

  implicit class VodStreamPlaylistExt(obj: VodStreamPlaylist.type) {
    def apply(data: InputStream)(implicit ec: ExecutionContext): Future[VodStreamPlaylist] = FileSystem.read(data, (a: String, b: String) => s"$a\n$b") map {
      _ toVodStreamPlaylist
    } flatMap identity
  }

  implicit class LiveStreamPlaylistExt(obj: LiveStreamPlaylist.type) {
    def apply(data: InputStream): LiveStreamPlaylist = {
      Source.fromInputStream(data)
        .getLines() reduce {
        (a, b) => s"$a\n$b"
      } toLiveStreamPlaylist
    }
  }

}
