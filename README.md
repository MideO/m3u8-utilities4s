# m3u8-utilities4s
[![Build Status](https://travis-ci.org/MideO/m3u8-utilities4s.svg?branch=master)](https://travis-ci.org/MideO/m3u8-utilities4s)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mideo/m3u8-utilities4s_2.12/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.mideo%22%20a%3A%22m3u8-utilities4s_2.12%22)

#### Scala.js support 
Thanks to [@bpholt](https://github.com/bpholt)   
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mideo/m3u8-utilities4s_sjs0.6_2.12/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.mideo%22%20a%3A%22m3u8-utilities4s_sjs0.6_2.12%22) 


See [Apple HLS Guide](https://developer.apple.com/library/content/technotes/tn2288/_index.html)

m3u8 utilities for scala
    
##### Docs?
  See Functional tests: [StreamPlaylistTest.scala](src/test/scala/StreamPlaylistTest.scala)
  
#### implemented m3u8 parts, so far
```scala
//master m3u8
case class MediaStreamType(name: String) 

case class MediaStreamIndependentSegments() 

case class MediaStreamTypeInfo(mediaType: String,groupId: String,language: String,name: String,autoSelect: String,mediaDefault: String) 

case class MediaStreamInfo(bandWith: String,codecs: List[String],resolution: String,closedCaption: String,audio: String,asset: String) 

case class MediaStreamFrameInfo(bandWith: String,codecs: List[String],resolution: String,uri: String)


//asset m3u8
case class MediaStreamTypeInitializationVectorCompatibilityVersion(version: String)

case class MediaStreamTargetDuration(duration: String)

case class MediaStreamMediaSequence(numberOfUrls: String)

case class MediaStreamPlaylistType(steamType: String)

case class MediaStreamProgramDateTime(dateTime: String) 

case class MediaStreamPlaylistTransportStreamDRMInfo(method: String, uri: String, initializationVector: String)

case class MediaStreamPlaylistTransportStream(duration: String, drmInfo: Option[MediaStreamPlaylistTransportStreamDRMInfo], uri: String)

case class MediaStreamEnd() 

```

#### Usage
master.m3u8
```bash
#EXTM3U
#EXT-X-INDEPENDENT-SEGMENTS
#EXT-X-MEDIA:TYPE=AUDIO,GROUP-ID="aac",LANGUAGE="eng",NAME="English",AUTOSELECT=YES,DEFAULT=YES
#EXT-X-STREAM-INF:BANDWIDTH=2100000,CODECS="avc1.4d001f,mp4a.40.2",RESOLUTION=896x504,CLOSED-CAPTIONS=NONE,AUDIO="aac"
asset_1800k.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=210000,CODECS="avc1.42001e,mp4a.40.2",RESOLUTION=320x180,CLOSED-CAPTIONS=NONE,AUDIO="aac"
asset_192k.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=600000,CODECS="avc1.42001e,mp4a.40.2",RESOLUTION=384x216,CLOSED-CAPTIONS=NONE,AUDIO="aac"
asset_450k.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=1000000,CODECS="avc1.42001e,mp4a.40.2",RESOLUTION=512x288,CLOSED-CAPTIONS=NONE,AUDIO="aac"
asset_800k.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=1400000,CODECS="avc1.4d001f,mp4a.40.2",RESOLUTION=640x360,CLOSED-CAPTIONS=NONE,AUDIO="aac"
asset_1200k.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=3000000,CODECS="avc1.4d001f,mp4a.40.2",RESOLUTION=960x540,CLOSED-CAPTIONS=NONE,AUDIO="aac"
asset_2500k.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=4000000,CODECS="avc1.640028,mp4a.40.2",RESOLUTION=1280x720,CLOSED-CAPTIONS=NONE,AUDIO="aac"
asset_3500k.m3u8
#EXT-X-I-FRAME-STREAM-INF:BANDWIDTH=32000,CODECS="avc1.42001e",RESOLUTION=384x216,URI="asset_450k_I-Frame.m3u8"
#EXT-X-I-FRAME-STREAM-INF:BANDWIDTH=190000,CODECS="avc1.4d001f",RESOLUTION=960x540,URI="asset_2500k_I-Frame.m3u8"
```
```scala
import com.github.mideo.media.m3u8._

import scala.io.Source
import com.github.mideo.media.m3u8._


val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

//create domain object
val streamPlaylist = MasterStreamPlaylist(is)
streamPlaylist.mediaStreamTypeInfo.get("210000")

//edit
val updated: MasterStreamPlaylist = streamPlaylist
.withMediaStreamTypeInfo(MediaStreamTypeInfo("AUDIO","aac","fre","French","YES","YES"))  
.withMediaStreamInfo(updated.mediaStreamInfo.values map {
        it => it.bandWith -> MediaStreamInfo(
          it.bandWith,
          it.codecs,
          it.resolution,
          it.closedCaption,
          it.audio, it.asset.replace("asset", "french_asset"))
      } toMap)




//serialise
val newContent = updated.toPlaylistString

//or saveToFile

updated.saveToFile("master2.m3u8")

```
serializing will `master2.m3u8` produce update data below 
```
#EXTM3U
#EXT-X-INDEPENDENT-SEGMENTS
#EXT-X-MEDIA:TYPE=AUDIO,GROUP-ID="aac",LANGUAGE="fre",NAME="French",AUTOSELECT=YES,DEFAULT=YES
#EXT-X-STREAM-INF:BANDWIDTH=2100000,CODECS="avc1.4d001f,mp4a.40.2",RESOLUTION=896x504,CLOSED-CAPTIONS=NONE,AUDIO="aac"
french_asset_1800k.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=210000,CODECS="avc1.42001e,mp4a.40.2",RESOLUTION=320x180,CLOSED-CAPTIONS=NONE,AUDIO="aac"
french_asset_192k.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=600000,CODECS="avc1.42001e,mp4a.40.2",RESOLUTION=384x216,CLOSED-CAPTIONS=NONE,AUDIO="aac"
french_asset_450k.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=1000000,CODECS="avc1.42001e,mp4a.40.2",RESOLUTION=512x288,CLOSED-CAPTIONS=NONE,AUDIO="aac"
french_asset_800k.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=1400000,CODECS="avc1.4d001f,mp4a.40.2",RESOLUTION=640x360,CLOSED-CAPTIONS=NONE,AUDIO="aac"
french_asset_1200k.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=3000000,CODECS="avc1.4d001f,mp4a.40.2",RESOLUTION=960x540,CLOSED-CAPTIONS=NONE,AUDIO="aac"
french_asset_2500k.m3u8
#EXT-X-STREAM-INF:BANDWIDTH=4000000,CODECS="avc1.640028,mp4a.40.2",RESOLUTION=1280x720,CLOSED-CAPTIONS=NONE,AUDIO="aac"
french_asset_3500k.m3u8
#EXT-X-I-FRAME-STREAM-INF:BANDWIDTH=32000,CODECS="avc1.42001e",RESOLUTION=384x216,URI="asset_450k_I-Frame.m3u8"
#EXT-X-I-FRAME-STREAM-INF:BANDWIDTH=190000,CODECS="avc1.4d001f",RESOLUTION=960x540,URI="asset_2500k_I-Frame.m3u8"
```

vod_asset_1800k.m3u8
```bash
#EXTM3U
#EXT-X-VERSION:4
#EXT-X-TARGETDURATION:5
#EXT-X-MEDIA-SEQUENCE:1
#EXT-X-PLAYLIST-TYPE:VOD
#EXT-X-PROGRAM-DATE-TIME:2017-11-16T00:00:00.000Z
#EXTINF:5,
#EXT-X-KEY:METHOD=AES-128,URI="https://qa-drm-api.svcs.eurosportplayer.com/media/5107ad82-b610-4921-8955-765df71a1f42/keys/0994962d-c1be-454d-9be2-8f723f7458a5",IV=0x9204AA77F72EE39DF47996C0175FF59F
asset_1800k/00000/asset_1800k_00001.ts
#EXTINF:5,
asset_1800k/00000/asset_1800k_00002.ts
#EXTINF:5,
asset_1800k/00000/asset_1800k_00003.ts
#EXTINF:5,
asset_1800k/00000/asset_1800k_00004.ts
#EXTINF:5,
asset_1800k/00000/asset_1800k_00005.ts
#EXTINF:5,
asset_1800k/00000/asset_1800k_00006.ts
#EXTINF:5,
asset_1800k/00000/asset_1800k_00007.ts
#EXTINF:5,
asset_1800k/00000/asset_1800k_00008.ts
#EXTINF:5,
asset_1800k/00000/asset_1800k_00009.ts
#EXTINF:5,
asset_1800k/00000/asset_1800k_00010.ts
#EXTINF:5,
asset_1800k/00000/asset_1800k_00011.ts
#EXTINF:5,
asset_1800k/00000/asset_1800k_00012.ts
#EXTINF:5,
#EXT-X-KEY:METHOD=AES-128,URI="https://qa-drm-api.svcs.eurosportplayer.com/media/5107ad82-b610-4921-8955-765df71a1f42/keys/0994962d-c1be-454d-9be2-8f723f7458a5",IV=0x1EDE7B37CAF0D06537F190695D38FA12
asset_1800k/00001/asset_1800k_00001.ts
#EXTINF:5,
asset_1800k/00001/asset_1800k_00002.ts
#EXTINF:5,
asset_1800k/00001/asset_1800k_00003.ts
#EXTINF:3,
asset_1800k/00001/asset_1800k_00004.ts
#EXT-X-ENDLIST
```
```scala
//Vod
val vodDataFile = Source.fromFile("vod_asset_1800k.m3u8")
val streamPlaylist = VodStreamPlaylist(vodDataFile)
```

live_asset_1800k.m3u8
```bash
#EXTM3U
#EXT-X-VERSION:3
#EXT-X-TARGETDURATION:6
#EXT-X-MEDIA-SEQUENCE:1582751
#EXT-X-PROGRAM-DATE-TIME:2018-01-09T10:57:23.661Z
#EXT-X-KEY:METHOD=AES-128,URI="https://drm-api.com/programs/22086/media/3357bd1a-0c6f-4ce5-9769-350d11f0b7bc/keys/294f651c-68b4-465d-9a5e-909cf68a461e",IV=0xE2FB2FE3B78B23798340D5F70B7FA700
#EXTINF:5,
009/10/57/23_661.ts
#EXTINF:5,
009/10/57/28_661.ts
#EXTINF:5,
009/10/57/33_661.ts
#EXTINF:5,
009/10/57/38_661.ts
#EXTINF:5,
009/10/57/43_661.ts
#EXTINF:5,
009/10/57/48_661.ts
#EXTINF:5,
009/10/57/53_661.ts
#EXTINF:5,
009/10/57/58_661.ts
#EXT-X-KEY:METHOD=AES-128,URI="https://drm-api.com/programs/22086/media/3357bd1a-0c6f-4ce5-9769-350d11f0b7bc/keys/294f651c-68b4-465d-9a5e-909cf68a461e",IV=0xB166F7015CD163935A7DF56E21E0697F
#EXTINF:5,
009/10/58/03_661.ts
#EXTINF:5,
009/10/58/08_661.ts
#EXTINF:5,
009/10/58/13_661.ts
#EXTINF:5,
009/10/58/18_661.ts
#EXTINF:5,
009/10/58/23_661.ts
#EXTINF:5,
009/10/58/28_661.ts
#EXTINF:5,
009/10/58/33_661.ts
#EXTINF:5,
009/10/58/38_661.ts
#EXTINF:5,
009/10/58/43_661.ts
#EXTINF:5,
009/10/58/48_661.ts
#EXTINF:5,
009/10/58/53_661.ts
#EXTINF:5,
009/10/58/58_661.ts
#EXT-X-KEY:METHOD=AES-128,URI="https://drm-api.com/programs/22086/media/3357bd1a-0c6f-4ce5-9769-350d11f0b7bc/keys/294f651c-68b4-465d-9a5e-909cf68a461e",IV=0xECFBBC003C5FF18B0A861C56F04F31A6
#EXTINF:5,
009/10/59/03_661.ts
#EXTINF:5,
009/10/59/08_661.ts
#EXTINF:5,
009/10/59/13_661.ts
#EXTINF:5,
009/10/59/18_661.ts
#EXTINF:5,
009/10/59/23_661.ts
#EXTINF:5,
009/10/59/28_661.ts
#EXTINF:5,
009/10/59/33_661.ts
#EXTINF:5,
009/10/59/38_661.ts
#EXTINF:5,
009/10/59/43_661.ts
#EXTINF:5,
009/10/59/48_661.ts
#EXTINF:5,
009/10/59/53_661.ts
#EXTINF:5,
009/10/59/58_661.ts
#EXT-X-KEY:METHOD=AES-128,URI="https://drm-api.com/programs/22086/media/3357bd1a-0c6f-4ce5-9769-350d11f0b7bc/keys/294f651c-68b4-465d-9a5e-909cf68a461e",IV=0x96061BBE30A1F50FCF13178BD5B874AE
#EXTINF:5,
009/11/00/03_661.ts
#EXTINF:5,
009/11/00/08_661.ts
#EXTINF:5,
009/11/00/13_661.ts
#EXTINF:5,
009/11/00/18_661.ts
#EXTINF:5,
009/11/00/23_661.ts
#EXTINF:5,
009/11/00/28_661.ts
#EXTINF:5,
009/11/00/33_661.ts
#EXTINF:5,
009/11/00/38_661.ts
#EXTINF:5,
009/11/00/43_661.ts
#EXTINF:5,
009/11/00/48_661.ts
#EXTINF:5,
009/11/00/53_661.ts
#EXTINF:5,
009/11/00/58_661.ts
#EXT-X-KEY:METHOD=AES-128,URI="https://drm-api.com/programs/22086/media/3357bd1a-0c6f-4ce5-9769-350d11f0b7bc/keys/294f651c-68b4-465d-9a5e-909cf68a461e",IV=0x3E70F4DA425DFAB2F3164349A2569239
#EXTINF:5,
009/11/01/03_661.ts
#EXTINF:5,
009/11/01/08_661.ts
#EXTINF:5,
009/11/01/13_661.ts
#EXTINF:5,
009/11/01/18_661.ts
#EXTINF:5,
009/11/01/23_661.ts
#EXTINF:5,
009/11/01/28_661.ts
#EXTINF:5,
009/11/01/33_661.ts
#EXTINF:5,
009/11/01/38_661.ts
#EXTINF:5,
009/11/01/43_661.ts
#EXTINF:5,
009/11/01/48_661.ts
#EXTINF:5,
009/11/01/53_661.ts
#EXTINF:5,
009/11/01/58_661.ts
#EXT-X-KEY:METHOD=AES-128,URI="https://drm-api.com/programs/22086/media/3357bd1a-0c6f-4ce5-9769-350d11f0b7bc/keys/294f651c-68b4-465d-9a5e-909cf68a461e",IV=0xE00623A51690767F87F4C142EFB4EC20
#EXTINF:5,
009/11/02/03_662.ts
#EXTINF:5,
009/11/02/08_661.ts
#EXTINF:5,
009/11/02/13_662.ts
#EXTINF:5,
009/11/02/18_662.ts
```
```scala
//Live
val liveDataFile = Source.fromFile("live_asset_1800k.m3u8")
val streamPlaylist = LiveStreamPlaylist(liveDataFile)
```