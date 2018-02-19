package com.github.mideo.media.m3u8.persistence
import java.nio.file.{Files, Path, Paths}

import scala.concurrent.{ExecutionContext, Future}



object FileSystemDataWriter {
  def write(fileName: String, data:Array[Byte])(implicit ec:ExecutionContext): Future[Unit] = Future {
    Files.write(Paths.get(fileName), data)
  }

}

