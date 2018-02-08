package com.github.mideo.media.m3u8.downloader


import java.io.ByteArrayInputStream
import java.net.HttpURLConnection
import java.util

import com.github.mideo.media.m3u8.M3U8ParserSuite
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito.when
import scala.collection.JavaConverters._
import scala.io.Source

class CommunicatorTest extends M3U8ParserSuite with MockitoSugar{


  val mockHttpURLConnection:HttpURLConnection = mock[HttpURLConnection]

  trait MockHttpConnection  {
    def createConnection(httpRequest: HttpRequest): HttpURLConnection = mockHttpURLConnection
  }

  object MockCommunicator extends Communicator with MockHttpConnection


  test("makeHttpRequest") {
    //Given
    def headers:util.Map[String, util.List[String]] = Map("header1" -> util.Arrays.asList("value1")).asJava
    when(mockHttpURLConnection.getHeaderFields).thenReturn(headers)
    when(mockHttpURLConnection.getHeaderField("header1")).thenReturn("value1")
    when(mockHttpURLConnection.getInputStream).thenReturn(new ByteArrayInputStream("abcs".getBytes()))

    //When
    val response: HttpResponse = MockCommunicator.makeHttpRequest(HttpRequest("GET", "http://example.com"))

    //Then
    response.Headers should be(Map("header1" -> "value1"))
    Source.fromInputStream(response.content).mkString should be("abcs")

  }
}


