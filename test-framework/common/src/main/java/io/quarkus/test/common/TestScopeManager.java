package io.quarkus.test.common;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import io.quarkus.runtime.test.TestScopeSetup;

public class TestScopeManager {

    private static final List<TestScopeSetup> SCOPE_MANAGERS = new ArrayList<>();

    static {
        for (TestScopeSetup i : ServiceLoader.load(TestScopeSetup.class, Thread.currentThread().getContextClassLoader())) {
            SCOPE_MANAGERS.add(i);
        }
    }

    public static void setup(boolean isIntegrationTest) {
        for (TestScopeSetup i : SCOPE_MANAGERS) {
            i.setup(isIntegrationTest);
        }
    }

    public static void tearDown(boolean isIntegrationTest) {
        for (TestScopeSetup i : SCOPE_MANAGERS) {
            i.tearDown(isIntegrationTest);
        }
    }
}
