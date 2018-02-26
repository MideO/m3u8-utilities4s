package com.github.mideo.media.m3u8.io

import java.nio.file.{Files, Paths}

import com.github.mideo.media.m3u8.M3U8ParserSuite

import scala.io.Source




class FileSystemTest extends M3U8ParserSuite {

  test("test write") {

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

  test("test read file") {
    //When
    val is = FileSystem.getClass.getClassLoader.getResource("master.m3u8").openStream()


    //Then
    FileSystem.read(is, (a:String, b:String) => s"$a\n$b") map {
      _ should not be empty
    }

  }


  test("test read inputStream") {

    //Given
    val fileName = Paths.get(getClass.getClassLoader.getResource("master.m3u8").toURI).toAbsolutePath.toString

    //Then
    FileSystem.read(fileName, (a: String, b: String) => s"$a\n$b") map {
      _ should not be empty
    }
  }
}
