package com.lance.bytecode;

/**
 * Created by lihua on 18/4/2017.
 * email: lihua@qq.com
 */
public class BankTransactions {

  public static void main(String[] args) {
    BankTransactions bank = new BankTransactions();
    for (int i = 0; i < 5; i++) {
      String accountId = "account" + i;
      bank.login("password", accountId, "Ashley");
      bank.withdraw(accountId, Double.valueOf(i));
    }
    System.out.println("Transactions completed");
  }

  @ImportantLog(fields = { "1", "2" })
  public String login(String password, String accountId, String userName) {
    BankAccount account = new BankAccount();
    account.setName(userName);
    System.out.println("Login Success!" + account.getName());
    return account.getName();
  }

  @ImportantLog(fields = { "0", "1" })
  public String withdraw(String accountId, Double moneyToRemove) {
    System.out.println("Withdraw Success!");
    return accountId + moneyToRemove;
  }

}
