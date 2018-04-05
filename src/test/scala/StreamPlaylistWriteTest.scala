import java.nio.file.{Files, Paths}

import com.github.mideo.M3U8ParserSuite
import com.github.mideo.media.m3u8.io.FileSystem
import com.github.mideo.media.m3u8._
import org.scalatest.prop.TableDrivenPropertyChecks._

import scala.io.Source


class StreamPlaylistWriteTest extends M3U8ParserSuite {
  val dataTable = Table(
    ("playlistFile", "data"),
    ("master.m3u8",  MasterStreamPlaylist(getClass.getClassLoader.getResource("master.m3u8").openStream()).toString.getBytes()),
    ("live_asset_1800k.m3u8",  LiveStreamPlaylist(getClass.getClassLoader.getResource("live_asset_1800k.m3u8").openStream()).toString.getBytes()),
    ("vod_asset_1800k.m3u8", VodStreamPlaylist(getClass.getClassLoader.getResource("vod_asset_1800k.m3u8").openStream()).toString.getBytes())
  )

  forAll(dataTable) {
    (playlistFile, data) =>
      test(s"test saving To File StreamPlaylist type with $playlistFile") {

        //When
        FileSystem.write(playlistFile, data) map {

          //Then
          _ =>
            val data2: String = Source.fromFile(playlistFile).getLines() reduce { (a, b) => s"$a\n$b" }
            Files.delete(Paths.get(playlistFile))
            data2 should not be empty
        }
      }
  }
}
