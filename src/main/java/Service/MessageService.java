package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {

    public MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message) {
        return this.messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return this.messageDAO.getAllMessages();
    }

    public Message getMessageById(int id) {
        return this.messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id) {
        Message msg = this.messageDAO.getMessageById(id);
        if (msg == null) {
            return null;
        }
        boolean isDeleted = this.messageDAO.deleteMessageById(id);
        if (!isDeleted) {
            return null;
        }
        return msg;
    }

    public Message updateMessageText(int id, String text) {
        Message msg = this.messageDAO.getMessageById(id);
        if (msg == null) {
            return null;
        }
        boolean isUpdated = this.messageDAO.updateMessageText(id, text);
        if (!isUpdated) {
            return null;
        }
        msg.message_text = text;
        return msg;
    }

    public List<Message> getAllMessagesForAccount(int account_id) {
        return this.messageDAO.getAllMessagesForAccount(account_id);
    }
}
