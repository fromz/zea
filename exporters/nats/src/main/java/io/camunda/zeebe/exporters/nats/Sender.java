package io.camunda.zeebe.exporters.nats;

import io.camunda.zeebe.exporter.api.context.Controller;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.JSONArray;
import org.slf4j.Logger;

import java.io.IOException;

import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.impl.NatsMessage;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

class Sender {
    private final int[] FIBONACCI = new int[]{ 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144};

    private Controller controller;
    private Logger log;
    // private HttpStatelessSender http;
    private int sendTimeMilli;
    private int backOffFactor = 1;
    private String subject = "";
    private Connection natsClient;
    int sendPeriod;

    Sender(NatsExporterContext context) throws IOException, InterruptedException {
        sendTimeMilli = context.configuration.batchTimeMilli;
        this.controller = context.controller;
        this.log = context.log;
        this.subject = context.configuration.subject;
        this.natsClient = Nats.connect(context.configuration.url);
    }

    void sendFrom(Batcher batcher) {
        if (batcher.queue.isEmpty()) {
            return;
        }
        
        final ImmutablePair<Long, JSONArray> batch = batcher.queue.getFirst();
        final JSONArray eventBatch = batch.getValue();
        final Long positionOfLastEventInBatch = batch.getKey();
        try {
            natsClient.publish(NatsMessage.builder()
                .subject(this.subject)
                .data(eventBatch.toString(), StandardCharsets.UTF_8)
                .build()
            );
            natsClient.flush(Duration.ofSeconds(5));

            batcher.queue.pollFirst();
            controller.updateLastExportedRecordPosition(positionOfLastEventInBatch);
            backOffFactor = 1;
            sendPeriod = sendTimeMilli;
        } catch (TimeoutException e) {
            backOff();
            e.printStackTrace();
        } catch (InterruptedException e) {
            backOff();
            e.printStackTrace();
        }
    }

    private void backOff() {
        backOffFactor ++;
        sendPeriod = FIBONACCI[Math.min(backOffFactor, FIBONACCI.length -  1)] * sendTimeMilli;
        log.debug("Post to Event Store failed.");
        log.debug("Retrying in " + sendPeriod + "ms...");
    }
}