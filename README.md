ZIO
---
>ZIO feature tests, to include the zio ecosystem:
* zio-macros
* zio-config
* zio-streams
* zio-logging
* zio-json
* zio-http ( **This library is not ready yet.** )
* zio-test

Macros
------
>The zio-macros library contains the @accessible annotation, allowing for the
>auto-generation of ZIO accessors. See console/ConsoleStore for an example.

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
4. objektwerks.http.NowClient ( does not currently work )
5. objektwerks.http.NowServer ( curl http://localhost:7979/now )