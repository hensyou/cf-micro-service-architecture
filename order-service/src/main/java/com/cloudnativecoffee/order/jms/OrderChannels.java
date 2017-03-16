package com.cloudnativecoffee.order.jms;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OrderChannels {

    @Output
    MessageChannel output();

}



