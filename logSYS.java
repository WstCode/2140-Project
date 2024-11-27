import java.util.*;

// LogSYS class that tracks user creation dates
class LogSYS {
    private List<User> userList;

    // Constructor to initialize the list of users
    public LogSYS() {
        this.userList = new ArrayList<>();
    }

    // Method to add a user to the log
    public void addUser(User user) {
        userList.add(user);
    }

    // Method to retrieve and print all users along with their creation dates
    public void printAllUsers() {
        if (userList.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("List of all users with account creation dates:\n");
            for (User user : userList) {
                System.out.println("User: " + user.getName() + ", Account Created: " + user.getFormattedCreationDate());
            }
        }
    }
}

public class logSYS {

    public static void main(String[] args) {
        // Create the logSYS instance to manage accounts
        LogSYS logLST = new LogSYS();

        // Add users to the log system
        User u = new User(name, email, homeAddress, phoneNumber, password, AccountLevel.ADMIN);
        logLST.addUser(u);
    
        // Print the list of all users along with their creation dates
        logLST.printAllUsers();
    }
}