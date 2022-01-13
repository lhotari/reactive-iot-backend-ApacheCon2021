# reactive-iot-backend

The is the source code of the live coding demo for ["Building resilient and scalable API backends with Apache Pulsar and Spring Reactive"
talk held at ApacheCon@Home 2021 by Lari Hotari](https://www.apachecon.com/acah2021/tracks/apimicro.html).

* [Slides for the presentation](<Building resilient and scalable API backends with Apache Pulsar and Spring Reactive.pdf>)
* [Recording for the presentation](https://youtu.be/-Vp2Rxs2l9Y?t=28)


## Running

Run each of the commands in a separate terminal window/tab.

### Start Pulsar in docker

Make sure that ports 8080 and 6650 are available.
```bash
docker run -it -p 8080:8080 -p 6650:6650 apachepulsar/pulsar:2.8.2 /pulsar/bin/pulsar standalone
```

### Start the application

```bash
./gradlew bootRun
```
The application starts on port 8081.

### Generate 1 million telemetry events with a shell script and curl

```bash
{ for i in {1..1000000}; do echo '{"n": "device'$i'/sensor1", "v": '$i'.123}'; done; } \
    | curl -X POST -T - -H "Content-Type: application/x-ndjson" localhost:8081/telemetry
```

### Stream events to console with curl

```bash
curl -N localhost:8081/firehose
```

## License

This is Open Source Software released under the [Apache Software License 2.0](www.apache.org/licenses/LICENSE-2.0).

## References

* Apache Pulsar: https://pulsar.apache.org/
* Spring Reactive: https://spring.io/reactive
* Reactive Pulsar adapter: https://github.com/lhotari/reactive-pulsar 
* Reactive Pulsar showcase application: https://github.com/lhotari/reactive-pulsar-showcase

## Questions

* [apache-pulsar](https://stackoverflow.com/tags/apache-pulsar) and [reactive-pulsar](https://stackoverflow.com/tags/reactive-pulsar) tags on Stackoverflow
* Join [Pulsar Slack for live discussions](https://pulsar.apache.org/en/contact/), there is #reactive-pulsar channel on Pulsar Slack.

