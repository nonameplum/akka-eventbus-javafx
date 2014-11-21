akka-eventbus-javafx
====================

EventBus for JavaFX using akka framework

Bild and install to local maven using command:
```
gradle install
```

Then you can include this dependecy from your local maven:
```
repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compile group: 'pl.plum.javafx', name: 'akka-eventbus', version: '1.0-SNAPSHOT'
}
```

This is experimental API showing how akka framework can be used in JavaFX application.
Please check out usage example: https://github.com/nonameplum/akka-eventbus-javafx-example
