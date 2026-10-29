import dao.AccountDAO;
import model.Account;

public class TestCustomer {
    public static void main(String[] args) {
        AccountDAO accountDAO = new AccountDAO();

        // 1. نجيب الحساب الأول
        Account account = accountDAO.getAccountByNumber(1001);
        if (account == null) {
            System.out.println("❌ مفيش حساب بالرقم ده");
            return;
        }

        System.out.println("الرصيد قبل التحديث: " + account.getBalance());

        // 2. نحدث الرصيد
        double newBalance = account.getBalance() + 1000.0; // نضيف 1000
        accountDAO.updateBalance(1001, newBalance);

        // 3. نتأكد من التحديث
        Account updatedAccount = accountDAO.getAccountByNumber(1001);
        System.out.println("الرصيد بعد التحديث: " + updatedAccount.getBalance());
    }
}