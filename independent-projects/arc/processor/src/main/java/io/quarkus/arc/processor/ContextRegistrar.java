package io.quarkus.arc.processor;

import java.lang.annotation.Annotation;

import io.quarkus.arc.InjectableContext;

/**
 * Use this extension point to register a custom {@link InjectableContext} implementation.
 *
 * @author Martin Kouba
 */
public interface ContextRegistrar extends BuildExtension {

    /**
     *
     * @param registrationContext
     */
    void register(RegistrationContext registrationContext);

    interface RegistrationContext extends BuildContext {

        /**
         *
         * @param scopeAnnotation
         * @return a new custom context configurator
         */
        ContextConfigurator configure(Class<? extends Annotation> scopeAnnotation);

    }

}
