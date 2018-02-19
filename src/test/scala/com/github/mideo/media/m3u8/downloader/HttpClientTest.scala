package com.github.mideo.media.m3u8.downloader

import java.util

import scala.collection.JavaConverters._
import com.github.mideo.media.m3u8.AsyncM3U8ParserSuite

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock._

import scala.concurrent.Future


class HttpClientTest extends AsyncM3U8ParserSuite {
  val headers: util.Map[String, util.List[String]] = Map("header1" -> util.Arrays.asList("value1")).asJava

  val wireMockServer = new WireMockServer()
  wireMockServer.start()
  Runtime.getRuntime.addShutdownHook(new Thread {
    override def run(): Unit = wireMockServer.shutdownServer()
  })

  test("makeHttpRequest Post") {
    //Given
    val request = HttpRequest("POST", wireMockServer.url("/test"), Map("header2" -> "value2"), Some("payload".getBytes()), 3000, 2000)
    wireMockServer.stubFor(post(urlEqualTo("/test"))
      .willReturn(aResponse()
        .withHeader("header1", "value1")
        .withBody("abcs")))

    //When
    val response: Future[HttpResponse] = HttpClient.makeHttpRequest(request)

    //Then
    response map {
      it =>
        wireMockServer.verify(postRequestedFor(urlEqualTo("/test"))
          .withHeader("header2", equalTo("value2"))
          .withRequestBody(equalTo("payload")))
        it.Headers.keySet should contain("header1")
        it.Headers("header1") should be("value1")
        it.content.mkString should be("abcs")

    }


  }

  test("makeHttpRequest Get") {
    //Given
    wireMockServer.stubFor(get(urlEqualTo("/test"))
      .willReturn(aResponse()
        .withHeader("header1", "value1")
        .withBody("abcs")))

    //When
    val response: Future[HttpResponse] = HttpClient.makeHttpRequest(HttpRequest("GET", wireMockServer.url("/test")))

    //Then
    response map {
      it =>
        wireMockServer.verify(getRequestedFor(urlEqualTo("/test")))
        it.Headers.keySet should contain("header1")
        it.Headers("header1") should be("value1")
        it.content.mkString should be("abcs")
    }




  }
}


