package io.camunda.zeebe.exporters.nats;

import io.camunda.zeebe.exporter.api.context.Controller;
import org.slf4j.Logger;

class NatsExporterContext {
    final Controller controller;
    final NatsConfiguration configuration;
    final Logger log;

    NatsExporterContext(Controller controller, NatsConfiguration configuration, Logger log) {
        this.configuration = configuration;
        this.controller = controller;
        this.log = log;
    }
}