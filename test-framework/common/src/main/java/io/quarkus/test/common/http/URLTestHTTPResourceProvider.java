package io.quarkus.test.common.http;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URLTestHTTPResourceProvider implements TestHTTPResourceProvider<URL> {
    @Override
    public Class<URL> getProvidedType() {
        return URL.class;
    }

    @Override
    public URL provide(String testUri, Field field) {
        try {
            return new URI(testUri).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
