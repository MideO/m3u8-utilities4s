package com.github.mideo.media.m3u8.persistence

import java.nio.file.{Files, Paths}

import com.github.mideo.media.m3u8.AsyncM3U8ParserSuite

import scala.io.Source



class FileSystemDataWriterTest extends AsyncM3U8ParserSuite {

  test("testSaveToFile") {

    //Given
    val content = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit."
    val s: Source = Source.fromString(content)

    //When
    FileSystemDataWriter
      .write(s, "content") map {
      _ =>

        //Then
        withClue("File does not exist ./content: ") {
        Files.exists(Paths.get("./content")) should be(true)
      }
    }

  }

}
