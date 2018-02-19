package com.github.mideo.media.m3u8.persistence

import java.nio.file.{Files, Paths}

import com.github.mideo.media.m3u8.AsyncM3U8ParserSuite

import scala.io.Source



class FileSystemDataWriterTest extends AsyncM3U8ParserSuite {

  test("testSaveToFile") {

    //Given
    val content = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit."

    //When
    FileSystemDataWriter
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
