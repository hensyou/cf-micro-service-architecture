package com.cloudnativecoffee.order.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OrderChannels {

    @Output
    MessageChannel orderChannel();
    
    @Input
    MessageChannel confirmationChannel();

}



