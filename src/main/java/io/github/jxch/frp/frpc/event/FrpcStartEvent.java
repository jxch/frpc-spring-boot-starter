package io.github.jxch.frp.frpc.event;

import org.springframework.context.ApplicationEvent;

public class FrpcStartEvent extends ApplicationEvent {

    public FrpcStartEvent(Object source) {
        super(source);
    }

}
