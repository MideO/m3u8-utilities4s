# m3u8-parser - (wip)
[![Build Status](https://travis-ci.org/MideO/m3u8-parser.svg?branch=master)](https://travis-ci.org/MideO/m3u8-parser)

library to help:
 * understand m3u8 playlist by de-serialising into a scala object
 * differentiate between a master playlist and asset playlist (wip)
 * modify a playlist and serialise from scala objects to playlist file content (wip)
    
    
#### implemented m3u8 parts, so far
```scala
//master m3u8
case class MediaStreamType(name: String) 

case class MediaStreamIndependentSegments() 

case class MediaStreamTypeInfo(mediaType: String,groupId: String,language: String,name: String,autoSelect: String,mediaDefault: String) 

case class MediaStreamInfo(bandWith: String,codecs: List[String],resolution: String,closedCaption: String,audio: String,asset: String) 

case class MediaStreamFrameInfo(bandWith: String,codecs: List[String],resolution: String,uri: String)


//Vod m3u8
case class MediaStreamTypeInitializationVectorCompatibilityVersion(version: String)

case class MediaStreamTargetDuration(duration: String)

case class MediaStreamMediaSequence(numberOfUrls: String)

case class MediaStreamPlaylistType(steamType: String)

case class MediaStreamProgramDateTime(dateTime: String) 

case class MediaStreamPlaylistItemDRMInfo(method: String, uri: String, initializationVector: String)

case class MediaStreamPlaylistItem(duration: String, drmInfo: Option[MediaStreamPlaylistItemDRMInfo], uri: String)

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
import com.github.mideo.media.m3u8.parser._

import scala.io.Source
import com.github.mideo.media.m3u8.parser._


val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

val data:String = Source.fromInputStream(is).getLines()reduce{_+""+_}

//create domain object
val streamPlaylist = MasterStreamPlaylist(data)
streamPlaylist.mediaStreamType.name
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

vodAsset.m3u8
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
val vodDataFile = Source.fromFile("vodAsset.m3u8")
val streamPlaylist = VodStreamPlaylist(vodDataFile)
```