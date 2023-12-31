package io.quarkus.smallrye.openapi.runtime;

import java.util.Optional;
import java.util.Set;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "smallrye-openapi", phase = ConfigPhase.RUN_TIME)
public class OpenApiRuntimeConfig {

    /**
     * Enable the openapi endpoint. By default it's enabled.
     */
    @ConfigItem(defaultValue = "true")
    public boolean enable;

    /**
     * Specify the list of global servers that provide connectivity information
     */
    @ConfigItem
    public Optional<Set<String>> servers;
}
