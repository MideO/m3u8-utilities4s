package com.github.mideo.media.m3u8

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncFunSuite, FunSuite, Matchers}

trait M3U8ParserSuite
  extends FunSuite with Matchers

trait AsyncM3U8ParserSuite
  extends AsyncFunSuite
    with Matchers
    with MockitoSugar