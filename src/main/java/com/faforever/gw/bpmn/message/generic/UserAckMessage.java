package com.faforever.gw.bpmn.message.generic;

import com.faforever.gw.bpmn.accessors.UserInteractionProcessAccessor;
import com.faforever.gw.security.GwUserRegistry;
import com.faforever.gw.services.messaging.client.MessagingService;
import com.faforever.gw.services.messaging.client.outgoing.AckMessage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Slf4j
@Component
public class UserAckMessage implements JavaDelegate {
    private final MessagingService messagingService;
    private final GwUserRegistry gwUserRegistry;

    @Inject
    public UserAckMessage(MessagingService messagingService, GwUserRegistry gwUserRegistry) {
        this.messagingService = messagingService;
        this.gwUserRegistry = gwUserRegistry;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        UserInteractionProcessAccessor accessor = UserInteractionProcessAccessor.of(execution);

        val requestId = accessor.getRequestId();
        val fafUserId = accessor.getRequestFafUser();

        gwUserRegistry.getUser(fafUserId)
                .ifPresent(user -> {
                    log.debug("Sending UserAckMessage (requestId: {})", requestId);
                    messagingService.send(new AckMessage(user, requestId));
                });
    }
}
