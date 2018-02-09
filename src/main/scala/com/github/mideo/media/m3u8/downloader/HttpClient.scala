package com.github.mideo.media.m3u8.downloader

import java.net.{HttpURLConnection, URL}

import scala.collection.JavaConverters._
import scala.io.{BufferedSource, Source}


private[downloader] case class HttpResponse(Headers: Map[String, String],
                                            content: BufferedSource)

private[downloader] case class HttpRequest(Method: String, Url: String,
                                           Headers: Map[String, String] = Map.empty,
                                           Content: Option[Array[Byte]] = None,
                                           ConnectTimeout: Int = 5000,
                                           ReadTimeout: Int = 5000)

trait Communicator {
  @throws(classOf[java.io.IOException])
  @throws(classOf[java.net.SocketTimeoutException])
  def makeHttpRequest(httpRequest: HttpRequest): HttpResponse = {
    val connection = createConnection(httpRequest)
    def headers = connection
      .getHeaderFields
      .keySet().asScala map { it => it -> connection.getHeaderField(it)} toMap

    HttpResponse(headers, Source.fromInputStream(connection.getInputStream))
  }

  def createConnection(httpRequest: HttpRequest): HttpURLConnection
}

trait HttpConnection {
  def createConnection(httpRequest: HttpRequest): HttpURLConnection = {
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

object HttpClient extends Communicator with HttpConnection

