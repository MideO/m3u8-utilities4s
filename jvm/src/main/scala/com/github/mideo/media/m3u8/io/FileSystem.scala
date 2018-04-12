package com.github.mideo.media.m3u8.io

import java.io.InputStream
import java.nio.file.{Files, Paths}

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source


object FileSystem {
  def write(fileName: String, data: Array[Byte])(implicit ec: ExecutionContext): Future[Unit] = Future {
    Files.write(Paths.get(fileName), data)
  }

  def read(fileName: String, withReducer: (String, String) => String)(implicit ec: ExecutionContext) = Future {
    Source.fromFile(fileName)
      .getLines() reduce withReducer

  }

  def read(data: InputStream, withReducer: (String, String) => String)(implicit ec: ExecutionContext) = Future {
    Source.fromInputStream(data)
      .getLines() reduce withReducer
  }
}

