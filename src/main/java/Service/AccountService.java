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

    public Account getAccountByUsername(String username) {
        return this.accountDAO.getAccountByUsername(username);
    }

    public Account getAccountById(int id) {
        return this.accountDAO.getAccountById(id);
    }

}
