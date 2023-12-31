package io.quarkus.it.hibernate.validator;

import jakarta.validation.constraints.Digits;

public interface HibernateValidatorTestResourceGenericInterface<T extends Number> {

    T testRestEndpointGenericMethodValidation(@Digits(integer = 5, fraction = 0) T id);

}
