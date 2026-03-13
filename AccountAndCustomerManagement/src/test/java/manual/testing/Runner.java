package manual.testing;

import controller.AccountController;
import model.account.CheckingAccount;
import model.account.SavingsAccount;
import model.customer.Company;
import model.customer.Person;

public class Runner {

	public static void main() {
		AccountController accountController = new AccountController();
		
		Person jason = (Person) accountController.createCustomer("Jason Todd", "1007 Mountain Drive, Gotham City", "Person");
		CheckingAccount jasonChecking = (CheckingAccount) accountController.createAccount(jason, "Checking");
		SavingsAccount jasonSavings = (SavingsAccount) accountController.createAccount(jason, " savings ");
		
		jasonChecking.deposit(100);
		jasonChecking.getNextCheckNumber();
		jasonSavings.addInterest();
		jason.chargeAllAccounts(999.99);
		jasonChecking.withdraw(1_000_000);
		jasonSavings.withdraw(1_000_000);
		
		accountController.createAccount(jason, "Checking");
		accountController.removeAccount(accountController.getAccounts().get(2));
		
		Company wayneTech = (Company) accountController.createCustomer("Wayne Enterprises", "1939 Kane Street, Gotham City", "company ");
		accountController.createAccount(wayneTech, "checking");
		accountController.createAccount(wayneTech, "savings");
		
		accountController.createCustomer("Miles Morales", "Brooklyn, New York City", "person");
		accountController.createAccount(accountController.getCustomers().get(2), "checking");
		accountController.removeCustomer(accountController.getCustomers().get(2));
	}
}
