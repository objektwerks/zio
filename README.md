ZIO
---
>ZIO feature tests, to include the zio ecosystem:
* zio-macros
* zio-config
* zio-streams
* zio-logging
* zio-json
* zio-zmx [todo]
* zio-actors [todo]
* zio-test

Macros
------
>The zio-macros library contains the @accessible annotation, allowing for the
>auto-generation of ZIO accessors. See ConsoleStore for an example.

Test
----
1. sbt clean test

Run
---
1. sbt run
>Multiple main classes detected. Select one to run:
1. objektwerks.console.ConsoleApp
2. objektwerks.console.ConsoleHorizontalLayerApp
3. objektwerks.console.ConsoleVerticalLayerApp