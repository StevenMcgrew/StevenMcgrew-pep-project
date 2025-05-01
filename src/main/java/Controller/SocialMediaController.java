package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Account
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);

        // Messages
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageTextHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesForAccountHandler);

        // Exception handling
        app.exception(JsonProcessingException.class, this::JsonExceptionHandler);

        return app;
    }

    private void postRegisterHandler(Context ctx) throws JsonProcessingException {
        // Parse the body
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        // Validate inputs
        if (account.username.isEmpty()) {
            ctx.status(400);
            return;
        }
        if (account.password.length() < 4) {
            ctx.status(400);
            return;
        }

        // Check if username is already in use
        Account existingAcct = this.accountService.getAccountByUsername(account.username);
        if (existingAcct != null) {
            ctx.status(400);
            return;
        }

        // Hash password
        // TODO: implement password hashing

        // Save account to db
        Account savedAccount = this.accountService.createAccount(account);
        if (savedAccount == null) {
            ctx.status(400);
            return;
        }

        // Respond
        ctx.json(mapper.writeValueAsString(savedAccount));
    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        // Parse the body
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        // Login
        Account loggedInAccount = this.accountService.login(account.username, account.password);
        if (loggedInAccount == null) {
            ctx.status(401);
            return;
        }

        // Respond
        ctx.json(mapper.writeValueAsString(loggedInAccount));
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        // Parse the body
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        // Validate inputs
        if (message.message_text.isEmpty()) {
            ctx.status(400);
            return;
        }
        if (message.message_text.length() > 255) {
            ctx.status(400);
            return;
        }

        // Check if 'posted_by' account exists
        Account existingAcct = this.accountService.getAccountById(message.posted_by);
        if (existingAcct == null) {
            ctx.status(400);
            return;
        }

        // Save message to db
        Message savedMessage = this.messageService.createMessage(message);
        if (savedMessage == null) {
            ctx.status(400);
            return;
        }

        // Respond
        ctx.json(mapper.writeValueAsString(savedMessage));
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        // Get all messages from db
        List<Message> allMessages = this.messageService.getAllMessages();

        // Respond
        ObjectMapper mapper = new ObjectMapper();
        ctx.json(mapper.writeValueAsString(allMessages));
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        // Get id from path parameter
        int id = Integer.parseInt(ctx.pathParam("message_id"));

        // Get message from db
        Message msg = this.messageService.getMessageById(id);

        // Respond
        if (msg == null) {
            ctx.status(200);
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        ctx.json(mapper.writeValueAsString(msg));
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        // Get id from path parameter
        int id = Integer.parseInt(ctx.pathParam("message_id"));

        // Delete message from db
        Message msg = this.messageService.deleteMessageById(id);
        System.out.println("Message: " + msg);

        // Respond
        if (msg == null) {
            ctx.status(200);
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        ctx.json(mapper.writeValueAsString(msg));
    }

    private void patchMessageTextHandler(Context ctx) throws JsonProcessingException {
        // Get id from path parameter
        int id = Integer.parseInt(ctx.pathParam("message_id"));

        // Parse the body
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        // Validate inputs
        if (message.message_text.isEmpty()) {
            ctx.status(400);
            return;
        }
        if (message.message_text.length() > 255) {
            ctx.status(400);
            return;
        }

        // Check if message exists
        Message existingMsg = this.messageService.getMessageById(id);
        if (existingMsg == null) {
            ctx.status(400);
            return;
        }

        // Update message text in db
        Message updatedMsg = this.messageService.updateMessageText(id, message.message_text);
        if (updatedMsg == null) {
            ctx.status(400);
            return;
        }

        // Respond
        ctx.json(mapper.writeValueAsString(updatedMsg));
    }

    private void getMessagesForAccountHandler(Context ctx) throws JsonProcessingException {
        // Get id from path parameter
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));

        // Get messages from db
        List<Message> accountMessages = this.messageService.getAllMessagesForAccount(account_id);

        // Respond
        ObjectMapper mapper = new ObjectMapper();
        ctx.json(mapper.writeValueAsString(accountMessages));
    }

    private void JsonExceptionHandler(Exception e, Context ctx) {
        ctx.status(400);
    }

}