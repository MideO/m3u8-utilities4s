package com.github.mideo.media.m3u8.downloader
import java.util

import scala.collection.JavaConverters._
import com.github.mideo.media.m3u8.M3U8ParserSuite
import org.scalatest.mockito.MockitoSugar
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock._


class HttpClientTest extends M3U8ParserSuite with MockitoSugar {
  val headers: util.Map[String, util.List[String]] = Map("header1" -> util.Arrays.asList("value1")).asJava

  val wireMockServer = new WireMockServer()
  wireMockServer.start()
  Runtime.getRuntime.addShutdownHook(new Thread {override def run(): Unit = wireMockServer.shutdownServer()})

  test("makeHttpRequest Post") {
    //Given
    val request = HttpRequest("POST", wireMockServer.url("/test"), Map("header2" -> "value2"), Some("payload".getBytes()), 3000, 2000)
    wireMockServer.stubFor(post(urlEqualTo("/test"))
      .willReturn(aResponse()
        .withHeader("header1", "value1")
        .withBody("abcs")))

    //When
    val response = HttpClient.makeHttpRequest(request)

    //Then
    wireMockServer.verify(postRequestedFor(urlEqualTo("/test"))
      .withHeader("header2", equalTo("value2"))
      .withRequestBody(equalTo("payload")))

    //And
    response.Headers.keySet should contain("header1")
    response.Headers("header1") should be("value1")
    response.content.mkString should be("abcs")
  }


  test("makeHttpRequest Get") {
    //Given
    wireMockServer.stubFor(get(urlEqualTo("/test"))
      .willReturn(aResponse()
        .withHeader("header1", "value1")
        .withBody("abcs")))

    //When
    val response: HttpResponse = HttpClient.makeHttpRequest(HttpRequest("GET", wireMockServer.url("/test")))

    //Then
    wireMockServer.verify(getRequestedFor(urlEqualTo("/test")))

    //And
    response.Headers.keySet should contain("header1")
    response.Headers("header1") should be("value1")
    response.content.mkString should be("abcs")

  }
}


