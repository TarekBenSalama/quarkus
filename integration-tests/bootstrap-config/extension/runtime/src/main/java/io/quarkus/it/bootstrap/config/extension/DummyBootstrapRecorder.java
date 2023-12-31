package io.quarkus.it.bootstrap.config.extension;

import org.eclipse.microprofile.config.spi.ConfigSourceProvider;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class DummyBootstrapRecorder {
    public RuntimeValue<ConfigSourceProvider> create(DummyConfig dummyConfig) {
        return new RuntimeValue<>(new DummyConfigSourceProvider(dummyConfig));
    }
}
