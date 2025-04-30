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

    /**
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageTextHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesForAccountHandler);

        return app;
    }

    private void exampleHandler(Context ctx) {
        ctx.json("sample text");
    }

    private void postRegisterHandler(Context ctx) {

        // Parse the body
        ObjectMapper mapper = new ObjectMapper();
        Account account;
        try {
            account = mapper.readValue(ctx.body(), Account.class);
        } catch (JsonProcessingException e) {
            ctx.status(400).result("Error parsing JSON body");
            return;
        }

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

        // Create new account
        

    }


}