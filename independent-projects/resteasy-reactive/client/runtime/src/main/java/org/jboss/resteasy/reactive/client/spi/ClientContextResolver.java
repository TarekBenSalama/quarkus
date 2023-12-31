package org.jboss.resteasy.reactive.client.spi;

import java.util.ServiceLoader;

import org.jboss.resteasy.reactive.client.impl.DefaultClientContext;

public interface ClientContextResolver {

    ClientContext resolve(ClassLoader classLoader);

    static ClientContextResolver getInstance() {
        return getInstance(Thread.currentThread().getContextClassLoader());
    }

    static ClientContextResolver getInstance(ClassLoader classLoader) {
        ServiceLoader<ClientContextResolver> services = ServiceLoader.load(ClientContextResolver.class, classLoader);
        ClientContextResolver selected = null;
        for (ClientContextResolver i : services) {
            if (selected != null) {
                throw new RuntimeException("More than one ClientContextResolver implementation, " + selected + " and " + i);
            }
            selected = i;
        }
        if (selected == null) {
            return DefaultClientContext.RESOLVER;
        }
        return selected;
    }

}
