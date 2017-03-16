package com.cloudnativecoffee.product.jms;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ProductChannels {

    @Input
    SubscribableChannel input();

}



