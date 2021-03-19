ZIO
---
>ZIO feature tests, to include the zio ecosystem:
* zio-macros
* zio-config
* zio-streams
* zio-logging
* zio-json
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
1. objektwerks.ConsoleApp
2. objektwerks.ConsoleHorizontalLayerApp
3. objektwerks.ConsoleVerticalLayerApp