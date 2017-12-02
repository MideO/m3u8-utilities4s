# m3u8-parser
m3u8 parser [![Build Status](https://travis-ci.org/MideO/m3u8-parser.svg?branch=master)](https://travis-ci.org/MideO/m3u8-parser)
library to help with the following
 * understand m3u8 playlist by de-serialising into a scala object
 * differentiate betwen a master playlist and asset playlist (wip)
 * modify a playlist and serialise from scala objects to playlist file content (wip)
    
    
#### Usage
```scala
import com.github.mideo.media.m3u8.parser._

import scala.io.Source

val is =   getClass.getClassLoader.getResource("master.m3u8").openStream()

val data:String = Source.fromInputStream(is).getLines().fold(""){(a, b) => s"$a\n$b"}
//create domain object
val streamPlaylist = StreamTransformer.deserialize(data)
streamPlaylist.mediaStreamType.name
streamPlaylist.mediaStreamTypeInfo.get("210000")


//serialize 
streamPlaylist.serialize
```