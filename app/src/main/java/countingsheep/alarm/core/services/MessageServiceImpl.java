package countingsheep.alarm.core.services;

import java.sql.Time;

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
}
