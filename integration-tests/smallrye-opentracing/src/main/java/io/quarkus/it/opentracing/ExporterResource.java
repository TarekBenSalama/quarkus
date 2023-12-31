package io.quarkus.it.opentracing;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;

@Path("/export")
public class ExporterResource {
    @Inject
    MockTracer mockTracer;

    @GET
    @Path("/clear")
    public void clearExporter() {
        mockTracer.reset();
    }

    @GET
    public List<MockSpan> retrieve() {
        return mockTracer.finishedSpans().stream()
                .filter(span -> !span.operationName().equals("GET:io.quarkus.it.opentracing.ExporterResource.clearExporter") &&
                        !span.operationName().equals("GET:io.quarkus.it.opentracing.ExporterResource.retrieve"))
                .collect(Collectors.toList());
    }
}
