import java.nio.file.{Files, Paths}

import com.github.mideo.media.m3u8.AsyncM3U8ParserSuite
import com.github.mideo.media.m3u8.parser._
import com.github.mideo.media.m3u8.persistence.FileSystemDataWriter
import org.scalatest.prop.TableDrivenPropertyChecks._

import scala.io.Source


class StreamPlaylistWriteTest extends AsyncM3U8ParserSuite {
  val dataTable = Table(
    ("playlistFile", "data"),
    ("master.m3u8",  MasterStreamPlaylist(getClass.getClassLoader.getResource("master.m3u8").openStream()).toPlaylistString.getBytes()),
    ("live_asset_1800k.m3u8",  LiveStreamPlaylist(getClass.getClassLoader.getResource("live_asset_1800k.m3u8").openStream()).toPlaylistString.getBytes()),
    ("vod_asset_1800k.m3u8", VodStreamPlaylist(getClass.getClassLoader.getResource("vod_asset_1800k.m3u8").openStream()).toPlaylistString.getBytes())
  )

  forAll(dataTable) {
    (playlistFile, data) =>
      test(s"test saving To File StreamPlaylist type with $playlistFile") {

        //When
        FileSystemDataWriter.write(playlistFile, data) map {

          //Then
          _ =>
            val data2: String = Source.fromFile(playlistFile).getLines() reduce { (a, b) => s"$a\n$b" }
            Files.delete(Paths.get(playlistFile))
            data2 should not be empty
        }
      }
  }
}
