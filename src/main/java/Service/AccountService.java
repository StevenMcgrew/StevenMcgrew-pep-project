package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    public AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO AccountDAO) {
        this.accountDAO = AccountDAO;
    }

    public Account createAccount(Account account) {
        return this.accountDAO.insertAccount(account);
    }

    public Account login(String username, String password) {
        Account account = this.accountDAO.getAccountByUsername(username);
        if (account == null) {
            return null;
        }
        if (account.password != password) {
            return null;
        }
        return account;
    }

}
