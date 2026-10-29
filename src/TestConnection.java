import dao.AccountDAO;
import dao.CustomerDAO;
import model.Account;
import model.Customer;
import model.SavingAccount;

public class TestConnection {
    public static void main(String[] args) {
        // 1. نجيب عميل من الداتابيز (عشان نربط الحساب بيه)
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getCustomerById(3); // جرب ID موجود عندك

        if (customer == null) {
            System.out.println("❌ مفيش عميل بالـ ID ده! ضيف عميل الأول (منيو 10).");
            return;
        }

        System.out.println("✅ لقينا العميل: " + customer.getFullName());

        // 2. نعمل حساب جديد للعميل ده
        Account account = new SavingAccount(
                1001,           // رقم الحساب
                customer,       // العميل
                5000.0          // الرصيد
        );

        // 3. نضيفه للداتابيز
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.addAccount(account);

        // 4. نتأكد إنه اتضاف
        System.out.println("الحساب اتحفظ في الداتابيز!");
    }
}