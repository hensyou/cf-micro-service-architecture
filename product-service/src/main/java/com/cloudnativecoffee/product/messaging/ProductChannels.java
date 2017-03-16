package com.cloudnativecoffee.product.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ProductChannels {

    @Input
    SubscribableChannel orderChannel();
    
    @Output
    MessageChannel confirmationChannel();

}



