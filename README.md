# Symbionte Prototype

**NOTE: THIS IS A PROTOTYPE**

The `Symbiote Prototype` consumes `OpenNMS Horizon` events (implementd), alarms (not implemented) and nodes (not implemented) to allow working on the conceptional debt mentioned [here](https://opennms.discourse.group/t/the-future-of-opennms-horizon/3952).

## What does it do

The `Symbiote` listens to `OpenNMS Horizon` events - sent by the `KafkaEventProducer` - and persists them to a (temporary) postgres database table: `events`

Besides this it implements a very basic event escalation model (similar to the OpenNMS Horizon's one). If an event is escalated to an alarm, it is persisted to a (temporary) postgres database table: `alarms`.

## Alarm Definition Model

**NOTE: This is a prototype, proof-of-concept**

An alarm is defined as follows:

```
UEI: uei.opennms.org/nodes/nodeDown
Label: Alarm from 'uei.opennms.org/nodes/nodeDown' event
Description: TBD
Severity: MAJOR
Level: 1
Raise:
    on: Event: uei == 'uei.opennms.org/nodes/nodeDown'
    key: uei.opennms.org/nodes/nodeDown:%dpname%:%nodeid%
Clear:
    on: Event: uei == 'uei.opennms.org/nodes/nodeUp'
    key: uei.opennms.org/nodes/nodeDown:%dpname%:%nodeid%
```

The alarm definition defines a `raise` and a `clear` section.

The `raise` section defines a `condition` when the alarm should be raised (e.g. based on event data, but can be basically any (programmable) condition).
As known from `OpenNMS Horizon` this is usually a matching based on the Event UEI.
The `key` of the `raise` defines the `consolidation key` on which the created alarm must be cleared afterward.
Meaning, an event with a `clear key` matching the `raise key`, will clear the alarm.

The clear condition is similar to the raise condition, but defines when the alarm is cleared.

In addition to the clear and raise definition a `level` and a `severity` must be defined.

At the moment the alarm definitions are automatically calculated on the basis of the `OpenNMS Horizon event configuration`. See class `OpennmsAlarmDefinitionProvider` for more details.

### Alarm Propagation

**NOTE: This is a prototype, proof-of-concept**

If an alarm is created, an according `alarmPropagation/${originalEvent.uei}` event is sent out, with a consolidation key of `"alarmPropagation/${originalEvent.consolidationKey}/level=${originalEvent.level == null ? 0 : 1}:${originalEvent.level == null ? 1 : originalEvelt.level + 1}"`.
This allows for alarm definitions to "listen" for these events and therefore allow implementing a very basic alarm propagation model.


```
UEI: alarmPropagation/uei.opennms.org/nodes/nodeDown
Label: TBD
Description: TBD
Severity: MAJOR
Level: 2
Raise:
    on: uei == 'alarmPropagation/uei.opennms.org/nodes/nodeDown'
    key: %key%
Clear:
    on: uei == 'alarmPropagation/uei.opennms.org/nodes/nodeUp'
    key: %key%
```

An alarm propagation at the moment can be defined either on the level or the severity. 
Each event creates a new alarm, but it could be extended to allow updating the alarm instead of creating a new one instead.

### Where do I define Alarm Definitions?

Alarm Definitions are provided by `AlarmDefinitionProvider`. 
At the moment there are two implementations available by default:
 - `TestAlarmDefinitionProvider`: It exposes two test alarms, which result in an escalation alarm for each `nodeDown` events (including clearing), but escalates two times, whereas the alarm on level 2 cannot automatically be cleared.
 - `OpennmsAlarmDefinitionProvider`: Converts all opennms internal events to alarm definitions of `Symbiote`.

### Differences to the OpenNMS Horizon Model

1. events are not defined explicitly. An event can be anything, as long as it defines the required parameters/properties
2. Alarm definition is explicit, meaning it is it's own thing and no longer derived from event configuration
3. Alarm and Events have a level attribute, allowing for event propagation (simplified bsm or alarm situation capabilities).
4. Events do not have a severity, but alarms do.
5. Alarms are just state holders
6. No difference between `reductionKey` and `clearKey`. An event defines a `consolidationKey`. If a matching is done on `clear` or `raise` condition defines if it is a clearing or raising event.

## Event Model

**NOTE: This is a prototype, proof-of-concept**

There are two implemented event models: `EventLogEntity` and `EventDTO`.
At the moment only `EventDTO` is used.

The goal here is to find a generic, but yet concrete enough data model to work on.
For example the `properties` and `payload` in there could be used both, or just one, depending on how we want to model things. Whereas the `EventLogEntity` is probably too close to the original `OpenNMS Horizon` event data model.

TL;DR: Model works for this use case but obviously not in a "productive" system.


## How to get it started

### Run `Postgres`

Either use your existing postgres or start the one via `docker-copmose.yml` with `docker compose up -d`.

### Run `Kafka`

Either use your existing cluster or start the cluster from the `docker-compose.yml` via `docker compose up -d`.

### Configure `OpenNMS Horizon`

Install and configure the `Kafka Producer`.

Installation guide: https://docs.opennms.com/horizon/30/operation/kafka-producer/enable-kafka.html

Configuration guide: https://docs.opennms.com/horizon/30/operation/kafka-producer/configure-kafka.html


TL;DR: 

```bash
# ssh into the karaf
ssh -p 8101 admin@localhost

# configure the plugin
# NOTE: default kafka topics are events, alarms and nodes
config:edit org.opennms.features.kafka.producer.client
config:property-set bootstrap.servers localhost:29092 
config:update

# install the feature
feature:install opennms-kafka-producer

```

### `Symbiote`

Compile from source:

`mvn clean install -DskipTests`

Configure the thing:

Look at `application.properties` and provide appropriate settings by either overriding them in the file directly or `-Da.b.c=asdf` or use environment variables.

Run the thing:

`java -jar target/eventbus.jar`

If everything is working, you should see the alarm defintions printed at the very beginning and soon events should be consumed.

You can check the `events` and `alarms` table of the postgres to see if everything is working.

If you don't have configured OpenNMS with requisitions and such, you can simply send events via `send-event.pl` to see it in action:

```
# will create 4 events and 3 alarms
./bin/send-event.pl uei.opennms.org/nodes/nodeDown -x 6 -n 1

# will create 3 events and clears 2 alarms
./bin/send-event.pl uei.opennms.org/nodes/nodeUp -x 6 -n 1
```