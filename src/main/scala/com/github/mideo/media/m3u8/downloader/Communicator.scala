package com.github.mideo.media.m3u8.downloader

import java.io.InputStream
import java.net.{HttpURLConnection, URL}


import scala.collection.JavaConverters._


private[downloader] case class HttpResponse(Headers: Map[String, String], content: InputStream)

private[downloader] case class HttpRequest(Method: String, Url: String, ConnectTimeout: Int = 5000, ReadTimeout: Int = 5000)

trait Communicator {
  @throws(classOf[java.io.IOException])
  @throws(classOf[java.net.SocketTimeoutException])
  def makeHttpRequest(httpRequest: HttpRequest): HttpResponse = {
    val connection = createConnection(httpRequest)

    def headers = connection
      .getHeaderFields
      .keySet()
      .asScala map {
      it => it -> connection.getHeaderField(it)
    } toMap

    HttpResponse(headers, connection.getInputStream)
  }

  def createConnection(httpRequest: HttpRequest): HttpURLConnection
}

trait HttpConnection {
  def createConnection(httpRequest: HttpRequest): HttpURLConnection = {
    val connection = new URL(httpRequest.Url).openConnection.asInstanceOf[HttpURLConnection]
    connection.setConnectTimeout(httpRequest.ConnectTimeout)
    connection.setReadTimeout(httpRequest.ReadTimeout)
    connection.setRequestMethod(httpRequest.Method)
    connection
  }
}


