# Tap [![Build Status](https://travis-ci.org/nemosrc/tap.svg?branch=master)](https://travis-ci.org/nemosrc/tap) [![Maintainability](https://api.codeclimate.com/v1/badges/5bfde0d9a7072c48b5b8/maintainability)](https://codeclimate.com/github/nemosrc/tap/maintainability) ![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/nemosrc/tap) ![GitHub](https://img.shields.io/github/license/nemosrc/tap) [![](https://jitpack.io/v/nemosrc/mox-collection.svg)](https://jitpack.io/#nemosrc/mox-collection) ![JitPack - Downloads](https://img.shields.io/jitpack/dm/github/nemosrc/tap)
Sugar codes for spigot

<br>

> **Feature**
>> * Java agent support (asm)
>> * Packets
>> * NMS Blocks
>> * NMS Entities
>> * NMS NBT
>> * NMS Inventory
>> * NMS Items
>> * NMS World
>> * Fast math
>> * Mojang user profile
>> * Fake scoreboard
>> * Entity specificEntity events listener
>> * Sub commands Module

<br>

> **Gradle**
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

...
dependencies {
    implementation 'com.github.nemosrc:tap:1.3.2'
}
```

<br>

**If you want to use net.minecraft.server implementation, Follow the instructions below**
* First, Follow the tutorial -> https://www.spigotmc.org/wiki/spigot-gradle/
* Clone this git repository
* Use the following Gradle command -> `gradlew publishTapPublicationToMavenLocal -PwithNMS`
* Add the following code to build.gradle
```groovy
allprojects {
    repositories {
        ...
        mavenLocal()
    }
}

...
dependencies {
    implementation 'com.nemosw.spigot:tap-v1_12_R1:1.3.3'
}
```

<br>

> **Code sample**
```java
//Sound Packet
Packet.EFFECT.namedSound(Sounds.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, SoundCategory.MASTER, 
    loc.getX(), loc.getY(), loc.getZ(), 0.75F, 2.0F).sendTo(Bukkit.getOnlinePlayers());
```
