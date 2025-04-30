package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    public AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account createAccount(Account account) {
        boolean isSuccess = this.accountDAO.insertAccount(account);
        if (!isSuccess) {
            return null;
        }
        return this.accountDAO.getAccountByUsername(account.username);
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

    public Account getAccountByUsername(String username) {
        return this.accountDAO.getAccountByUsername(username);
    }

}
