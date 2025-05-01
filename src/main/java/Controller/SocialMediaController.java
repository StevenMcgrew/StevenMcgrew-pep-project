package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
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
            ctx.status(400).result("Username cannot be empty");
            return;
        }
        if (account.password.length() < 4) {
            ctx.status(400).result("Password must have at least 4 characters");
            return;
        }

        // Check if username is already in use
        Account existingAcct = this.accountService.getAccountByUsername(account.username);
        if (existingAcct != null) {
            ctx.status(400).result("An account with that username already exists");
            return;
        }

        // Hash password
        // TODO: implement password hashing

        // Save account to db
        Account savedAccount = this.accountService.createAccount(account);
        if (savedAccount == null) {
            ctx.status(400).result("Error saving to database");
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
            ctx.status(401).result("Account not found. Check username and/or password.");
            return;
        }

        // Respond
        ctx.json(mapper.writeValueAsString(loggedInAccount));
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ctx.result("Not yet implemented");
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ctx.result("Not yet implemented");
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ctx.result("Not yet implemented");
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ctx.result("Not yet implemented");
    }

    private void patchMessageTextHandler(Context ctx) throws JsonProcessingException {
        ctx.result("Not yet implemented");
    }

    private void getMessagesForAccountHandler(Context ctx) throws JsonProcessingException {
        ctx.result("Not yet implemented");
    }

    private void JsonExceptionHandler(Exception e, Context ctx) {
        ctx.status(400).result("Error processing JSON: " + e.getMessage());
    }

}