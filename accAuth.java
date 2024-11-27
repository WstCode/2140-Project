import java.util.Scanner;
import java.util.*;
import java.text.*;

// Enum to define the different account levels
enum AccountLevel {
    ADMIN, EMPLOYEE, CUSTOMER;
}

// User class with personal information and account level
class User {
    private String name;
    private String email;
    private String homeAddress;
    private String phoneNumber;
    private String password;
    private AccountLevel accountLevel;
    private Date accountCreationDate;    

    // Constructor to initialize user details
    public User(String name, String email, String homeAddress, String phoneNumber, String password, AccountLevel accountLevel) {
        this.name = name;
        this.email = email;
        this.homeAddress = homeAddress;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.accountLevel = accountLevel;
        this.accountCreationDate = new Date();  // Set current date and time as the account creation date    
    }

    // Getters for user details
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public AccountLevel getAccountLevel() {
        return accountLevel;
    }

    public Date getAccountCreationDate() {
        return accountCreationDate;
    }

    // Method to change account level with admin approval
    public boolean changeAccountLevel(AccountLevel newLevel, Admin admin) {
        if (admin == null) {
            System.out.println("Admin approval required to change account level.");
            return false;
        }
        System.out.println("Request to change account level to: " + newLevel);
        return admin.approveAccountLevelChange(this, newLevel);
    }

    public String getFormattedCreationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(accountCreationDate);
    }

    // Method to print user details
    public void printUserDetails() {
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Address: " + homeAddress);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Account Level: " + accountLevel);
        System.out.println("Account Creation Date: " + getFormattedCreationDate());
    }
}

// Admin class that can approve account level changes
class Admin extends User {

    public Admin(String name, String email, String homeAddress, String phoneNumber, String password) {
        super(name, email, homeAddress, phoneNumber, password, AccountLevel.ADMIN);
    }

    // Method to approve or deny account level change
    public boolean approveAccountLevelChange(User user, AccountLevel newLevel) {
        // Admin can approve changes
        System.out.println("Admin approves the change of account level.");
        // Update user's account level (change to newLevel)
        return true; // Admin approves, the account level can be updated
    }

    // Admin has access to all features
    public void accessAdminFeatures() {
        System.out.println("Admin has full access to all features.");
    }
}

// Employee class, has some access but less than admin
class Employee extends User {

    public Employee(String name, String email, String homeAddress, String phoneNumber, String password) {
        super(name, email, homeAddress, phoneNumber, password, AccountLevel.EMPLOYEE);
    }

    // Employee has access to some features
    public void accessEmployeeFeatures() {
        System.out.println("Employee has access to certain internal features.");
    }

    // Employee tries to access admin features (not allowed)
    public void tryAccessAdminFeatures() {
        System.out.println("Employee does not have access to Admin features.");
    }
}

// Customer class, has the most limited access
class Customer extends User {

    public Customer(String name, String email, String homeAddress, String phoneNumber, String password) {
        super(name, email, homeAddress, phoneNumber, password, AccountLevel.CUSTOMER);
    }

    // Customer has access to basic features
    public void accessCustomerFeatures() {
        System.out.println("Customer can access basic features only.");
    }

    // Customer tries to access employee/admin features (not allowed)
    public void tryAccessEmployeeFeatures() {
        System.out.println("Customer does not have access to Employee features.");
    }

    public void tryAccessAdminFeatures() {
        System.out.println("Customer does not have access to Admin features.");
    }
}

public class accAuth {

    // Method to execute program calls based on account level
    public static void executeProgramCalls(User user) {
        switch (user.getAccountLevel()) {
            case ADMIN:
                Admin admin = (Admin) user;
                admin.accessAdminFeatures();
                break;
            case EMPLOYEE:
                Employee employee = (Employee) user;
                employee.accessEmployeeFeatures();
                employee.tryAccessAdminFeatures();  // Employee can't access admin features
                break;
            case CUSTOMER:
                Customer customer = (Customer) user;
                customer.accessCustomerFeatures();
                customer.tryAccessEmployeeFeatures();  // Customer can't access employee features
                customer.tryAccessAdminFeatures();    // Customer can't access admin features
                break;
            default:
                System.out.println("Invalid account level.");
                break;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create Admin
        Admin admin = new Admin("Admin User", "admin@company.com", "123 Admin St.", "123-456-7890", "adminpass");

        // Create Employee and Customer users
        Employee employee = new Employee("Employee User", "employee@company.com", "456 Employee Ave.", "987-654-3210", "employeepass");
        Customer customer = new Customer("Customer User", "customer@company.com", "789 Customer Rd.", "555-555-5555", "customerpass");

        // Print user details and try accessing features based on account level
        System.out.println("Admin Access:");
        executeProgramCalls(admin);

        System.out.println("\nEmployee Access:");
        executeProgramCalls(employee);

        System.out.println("\nCustomer Access:");
        executeProgramCalls(customer);

        // Now, let's try to change the account type for the customer (Requires admin approval)
        System.out.println("\nRequest to change Customer account to Employee account...");
        boolean changeSuccess = customer.changeAccountLevel(AccountLevel.EMPLOYEE, admin);
        if (changeSuccess) {
            System.out.println("Account level changed successfully.");
        } else {
            System.out.println("Account level change failed.");
        }

        // Try again to access admin features after changing account type
        System.out.println("\nAccess Features After Account Change Request:");
        executeProgramCalls(customer);

        scanner.close();
    }
}