package com.maimai.billingcalculationengine.common.annotations;

import com.maimai.billingcalculationengine.common.enums.Layer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackExecution {
    Layer value();
}
