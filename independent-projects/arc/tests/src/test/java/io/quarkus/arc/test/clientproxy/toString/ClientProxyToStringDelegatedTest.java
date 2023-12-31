package io.quarkus.arc.test.clientproxy.toString;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.arc.Arc;
import io.quarkus.arc.test.ArcTestContainer;

public class ClientProxyToStringDelegatedTest {

    @RegisterExtension
    public ArcTestContainer container = new ArcTestContainer(Foo.class);

    @Test
    public void testToStringIsDelegated() {
        Foo bean = Arc.container().instance(Foo.class).get();
        Assertions.assertFalse(bean.toString().contains("_ClientProxy"));
    }
}
