package com.github.mideo.media.m3u8.io

import java.net.{HttpURLConnection, URL}

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}
import sun.misc.IOUtils.readFully


object Http {
  case class Response(Headers: Map[String, String], content: Array[Byte])

  case class Request(Method: String, Url: String,
                     Headers: Map[String, String] = Map.empty,
                     Content: Option[Array[Byte]] = None,
                     ConnectTimeout: Int = 5000,
                     ReadTimeout: Int = 5000)

  @throws(classOf[java.io.IOException])
  @throws(classOf[java.net.SocketTimeoutException])
  def write(httpRequest: Request)(implicit ec:ExecutionContext): Future[Response] = createConnection(httpRequest) map {
      connection =>
        def headers = connection
          .getHeaderFields
          .keySet().asScala map { it => it -> connection.getHeaderField(it) } toMap

        Response(headers, readFully(connection.getInputStream, -1, true))
    }


  private def createConnection(httpRequest: Request)(implicit ec:ExecutionContext): Future[HttpURLConnection] = Future {

    val connection = new URL(httpRequest.Url).openConnection.asInstanceOf[HttpURLConnection]
    connection.setConnectTimeout(httpRequest.ConnectTimeout)
    connection.setReadTimeout(httpRequest.ReadTimeout)
    connection.setRequestMethod(httpRequest.Method)

    if (List("post", "put").contains(httpRequest.Method.toLowerCase)) connection.setDoOutput(true)

    for {(key, value) <- httpRequest.Headers} yield connection.setRequestProperty(key, value)
    httpRequest.Content match {
      case Some(content) =>
        val data = connection.getOutputStream
        data.write(content)
        data.close()
      case _ => //Do Nothing
    }
    connection
  }

}


