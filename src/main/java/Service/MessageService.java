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
        Integer recordId = this.messageDAO.insertMessage(message);
        if (recordId == null) {
            return null;
        }
        return this.messageDAO.getMessageById(recordId);
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
        Integer recordId = this.messageDAO.deleteMessageById(id);
        if (recordId == null) {
            return null;
        }
        return msg;
    }

    public Message updateMessageText(int id, String text) {
        Integer recordId = this.messageDAO.updateMessageText(id, text);
        if (recordId == null) {
            return null;
        }
        return this.messageDAO.getMessageById(recordId);
    }

    public List<Message> getAllMessagesForAccount(int account_id) {
        return this.messageDAO.getAllMessagesForAccount(account_id);
    }
}
