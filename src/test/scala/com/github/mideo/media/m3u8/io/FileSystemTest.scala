package com.github.mideo.media.m3u8.io

import java.nio.file.{Files, Paths}

import com.github.mideo.media.m3u8.M3U8ParserSuite

import scala.io.Source



class FileSystemTest extends M3U8ParserSuite {

  test("testSaveToFile") {

    //Given
    val content = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit."

    //When
    FileSystem
      .write("content", content.getBytes()) map {
      _ =>
        //Then
        withClue("File does not exist ./content: ") {
          val data2: String = Source.fromFile("./content").getLines() reduce { (a, b) => s"$a\n$b" }
          Files.delete(Paths.get("./content"))
          data2 should not be empty
      }
    }

  }

}
