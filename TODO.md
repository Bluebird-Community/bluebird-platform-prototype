# TODOs

With the current prototype the main problem is as follows:

- Alarm/Events from OpenNMS Horizon are problematic:
  - Alarm definitions are created from the OpenNMS Horizon event configuration files. For this, access to the `opennms.home` is necessary/required => okay for prototype, but bad for writing tests and distributed deployments
  - Protobuf Events from OpenNMS Horizon do not contain alarm data and therefore do not contain the reduction or clear key. This should be included, otherwise the reduction/clear keys are re-created from the event configuration files. Which is okay, but makes testing harder. We need to come up with a solution for this problem. 
  - => Solution could be:
    - OpenNMS Horizon sends the required information (either via protobuf or xml or both?)
    - OpenNMS Horizon can also send alarm definitions and/or event-conf definitions actively to symbionte, including also the "id" of the instance, to uniquely identify it. This could allow for individual alarm definitions for multiple OpenNMS Horizon instances