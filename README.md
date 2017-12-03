# m3u8-parser
m3u8 parser [![Build Status](https://travis-ci.org/MideO/m3u8-parser.svg?branch=master)](https://travis-ci.org/MideO/m3u8-parser)

library to help with the following
 * understand m3u8 playlist by de-serialising into a scala object
 * differentiate betwen a master playlist and asset playlist (wip)
 * modify a playlist and serialise from scala objects to playlist file content (wip)
    
    
#### implemented m3u8 parts, so far
```scala

case class MediaStreamType(name: String) 

case class MediaStreamIndependentSegments() 

case class MediaStreamTypeInfo(mediaType: String,groupId: String,language: String,name: String,autoSelect: String,mediaDefault: String) 

case class MediaStreamInfo(bandWith: String,codecs: List[String],resolution: String,closedCaption: String,audio: String,asset: String) 

case class MediaStreamFrameInfo(bandWith: String,codecs: List[String],resolution: String,uri: String)

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
import com.github.mideo.media.m3u8.parser._

import scala.io.Source


val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}
//create domain object
val streamPlaylist = StreamTransformer.deserialize(data)
streamPlaylist.mediaStreamType.name
streamPlaylist.mediaStreamTypeInfo.get("210000")


//edit
val updated: M3U8MasterStreamPlaylist = streamPlaylist
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
val newContent = updated.serialize


```
serializing will produce update data below 
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