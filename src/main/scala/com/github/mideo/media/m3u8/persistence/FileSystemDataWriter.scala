package com.github.mideo.media.m3u8.persistence
import java.nio.file.{Files, Path, Paths}

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source


object FileSystemDataWriter {
  def write[T <: Source](data:T, fileName: String)(implicit ec:ExecutionContext): Future[Unit] = Future {
    Files.write(Paths.get(fileName), data.mkString.getBytes)
  }

}

