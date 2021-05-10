package com.faforever.gw.messaging.client.inbound;

import lombok.Value;

import java.util.UUID;

@Value
public class LeaveAssaultMessage implements InboundClientMessage {
    UUID requestId = UUID.randomUUID();

    UUID battleId;
}
