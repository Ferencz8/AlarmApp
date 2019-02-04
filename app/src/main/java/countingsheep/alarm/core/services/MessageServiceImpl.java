package countingsheep.alarm.core.services;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.MessageRepository;
import countingsheep.alarm.core.services.interfaces.MessageService;
import countingsheep.alarm.dataaccess.entities.Message;

@Singleton
public class MessageServiceImpl  implements MessageService {


    private MessageRepository messageRepository;
    private TimeService timeService;

    @Inject
    public MessageServiceImpl(MessageRepository messageRepository, TimeService timeService) {
        this.messageRepository = messageRepository;
        this.timeService = timeService;
    }

    @Override
    public void markSeen(int messageId) {
        Message message = this.messageRepository.get(messageId);
        message.setSeen(true);
        message.setSeenAt(this.timeService.getUTCDateNow());
        this.messageRepository.update(message);
    }

    @Override
    public Message getUnseenMessage() {
        return this.messageRepository.getNotSeen();
    }

    @Override
    public void markMessageLiked(int messageId) {
        Message message = this.messageRepository.get(messageId);
        message.setLiked(true);
        this.messageRepository.update(message);
    }

    @Override
    public void markMessageShared(int messageId) {
        Message message = this.messageRepository.get(messageId);
        message.setShared(true);
        this.messageRepository.update(message);
    }

    @Override
    public List<Message> getAllUnsynced() {
        //TODO:: implement everything using try catch + logging
        return this.messageRepository.getAllUnsynced(true);
    }

    @Override
    public boolean markSyncedRange(List<Message> unsyncedMessages) {
        try {

            List<Integer> messageIds = new ArrayList<>();
            for (Message message: unsyncedMessages) {
                messageIds.add(message.getId());
            }

            this.messageRepository.markMessagesSynced(messageIds);

            return true;
        } catch (Exception exception) {
            //TODO:: add logging
            return false;
        }
    }
}
