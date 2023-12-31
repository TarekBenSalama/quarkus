package io.quarkus.it.opentracing;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

@Singleton
@Path("/client")
public class PingPongResource {
    @RegisterRestClient(configKey = "pingpong")
    public interface PingPongRestClient {

        @Path("/client/pong/{message}")
        @GET
        String pingpong(@PathParam("message") String message);

        @GET
        @Path("/client/pong/{message}")
        Uni<String> asyncPingpong(@PathParam("message") String message);
    }

    @Inject
    @RestClient
    PingPongRestClient pingRestClient;

    @GET
    @Path("pong/{message}")
    public String pong(@PathParam("message") String message) {
        return message;
    }

    @GET
    @Blocking
    @Path("ping/{message}")
    public String ping(@PathParam("message") String message) {
        return pingRestClient.pingpong(message);
    }

    @GET
    @Path("async-ping/{message}")
    public Uni<String> asyncPing(@PathParam("message") String message) {
        return pingRestClient.asyncPingpong(message);
    }
}
