package io.quarkus.resteasy.test;

import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;
import io.restassured.RestAssured;

public class CustomExceptionMapperTestCase {
    @RegisterExtension
    static QuarkusUnitTest runner = new QuarkusUnitTest()
            .withApplicationRoot((jar) -> jar
                    .addClasses(RootResource.class, CustomExceptionMapper.class, MessageService.class));

    @Test
    public void testResourceNotFound() {
        RestAssured.when().get("/not_found")
                .then()
                .statusCode(200)
                .body(Matchers.is("not found but OK"));
    }

    @Provider
    public static class CustomExceptionMapper implements ExceptionMapper<NotFoundException> {

        final MessageService service;

        public CustomExceptionMapper(MessageService service) {
            this.service = service;
        }

        @Override
        public Response toResponse(NotFoundException exception) {
            return Response.status(200).entity("not found but OK").build();
        }
    }

    @Dependent
    public static class MessageService {
        public String msg() {
            return "not found but OK";
        }
    }
}
