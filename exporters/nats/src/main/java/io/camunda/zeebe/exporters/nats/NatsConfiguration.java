package io.camunda.zeebe.exporters.nats;

public class NatsConfiguration {
    /**
     * The URL of the Event Store database REST endpoint
     */
    String url = "http://localhost:2113";

    /**
     * The name of the stream in Event Store. This will automatically be created
     * in Event Store when we first post data to it.
     */
    String streamName = "zeebe";

    /**
     * To configure the amount of records, which has to be reached before the
     * records are exported to the database. Only counts the records which are
     * in the end actually exported.
     */
    int batchSize = 100;

    /**
     * To configure the time in milliseconds, when the batch should be executed
     * regardless whether the batch size was reached or not.
     */
    int batchTimeMilli = 300;
}