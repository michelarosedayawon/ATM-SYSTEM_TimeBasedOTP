import java.util.Random;
import java.util.Scanner;

class BankAcc {
    
    private double balance;
    
    public BankAcc(){
        this.balance = 0.0;
    }
    
    // check balance method
    public double CB(){
        return balance;
    }
    
    // withdrawal method
    public String W(double wd_amount){
        if (wd_amount <= 0){
            return "Invalid withdrawal amount. Please enter a positive number.";
        }
        else if (wd_amount > balance){
            return "You don't have enough balance to withdraw this amount.";   
        }
        else{
            balance -= wd_amount;
            return "Your balance is " + balance;
        }
    }
    
    // deposit method
    public String D(double d_amount){
        if (d_amount > 0){
            balance += d_amount;
            return "Your balance is " + balance;
        }
        else{
            return "Invalid deposit amount.";
        }
    }
}

class Authenticator {
    
    // mock user account details
    private final String acc = "1234567890", pin = "3030";
    private String generatedOTP;
    private long timebasedOTP;
    private static final long OTP_VALIDITY_DURATION = 60 * 1000;
    
    // verify account number and pin
    public boolean verifyCredentials(String accNum, String p){
        return accNum.equals(acc) && p.equals(pin);
    }
    
    // 6-digit OTP generator
    public String OTP_generator(){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        generatedOTP = String.valueOf(otp);
        timebasedOTP = System.currentTimeMillis();
        return generatedOTP;
    }
    
    // time-based otp expiration
    public boolean isOTPExpired(){
        return System.currentTimeMillis() - timebasedOTP > OTP_VALIDITY_DURATION;
    }
    
    // verify otp
    public boolean verifyOTP(String inputOTP){
        return inputOTP.equals(generatedOTP);
    }
}

public class ATMSystem_TimeBasedOTP {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Authenticator auth = new Authenticator();
        
        // introduction
        System.out.println("Welcome! This is a Mock ATM System.");
        System.out.println();
        System.out.println("The following are your account details:");
        System.out.println("Account Number: 1234567890");
        System.out.println("PIN: 3030");
        System.out.println("------------------");
        
        // user input (account details)
        System.out.print("Enter your Account Number: ");
        String accNum = input.nextLine();
        System.out.print("Enter your PIN: ");
        String p = input.nextLine();
        
        // validate account and pin
        if (!auth.verifyCredentials(accNum, p)){
            System.out.println("------------------");
            System.out.println("Invalid input. Access denied.");
            return;
        }
        
        int OTPattempts = 0;
        boolean authenticated = false;
        
        while (OTPattempts < 2){
            
            // generate and send OTP
            String otp = auth.OTP_generator();
            System.out.println();
            System.out.println("A 6-digit OTP has been sent to your registered device. (Valid for 1 minute only)");
            System.out.println("(For simulation, your OTP is: " + otp + ")");

            System.out.print("Enter your OTP to continue: ");
            String inputOTP = input.nextLine();
            
            boolean isExpired = auth.isOTPExpired();
            boolean isCorrect = auth.verifyOTP(inputOTP);

            // validate otp
            if (isExpired && !isCorrect){
                System.out.println("------------------");
                System.out.println("OTP has expired. Please try again.");
                OTPattempts++;
            }
            else if (!isExpired && !isCorrect){
                System.out.println("------------------");
                System.out.println("Incorrect OTP. Please try again.");
                OTPattempts++;
            }
            else if (!isExpired && isCorrect){
                authenticated = true;
                break;
            }
            else if (isExpired && isCorrect){
                System.out.println("------------------");
                System.out.println("OTP has expired. Please try again.");
                OTPattempts++;
            }
        }
        if (!authenticated){
            System.out.println("------------------");
            System.out.println("Too many failed attempts. Access denied.");
            return;
        }
        
        BankAcc account = new BankAcc();
        
        //transaction options
        while (true){
            String choice;
            System.out.println("------------------");
            System.out.println("1 - Check Balance");
            System.out.println("2 - Withdrawal");
            System.out.println("3 - Deposit");
            System.out.println("4 - Exit");
            System.out.println("------------------");
            System.out.print("Please enter number of choice: ");
            choice = input.nextLine();
            
            switch (choice){
                case "1":{
                    System.out.println("------------------");
                    System.out.println("Your balance is: " + account.CB());
                    break;
                }
                case "2":{
                    System.out.println("------------------");
                    System.out.print("Enter amount to withdraw: ");
                    if (input.hasNextDouble()){
                        double wd_amount = input.nextDouble();
                        input.nextLine();
                        System.out.println(account.W(wd_amount));
                    }
                    else{
                        System.out.println("Invalid input. Please enter a numeric value.");
                        input.nextLine();
                    }
                    break;
                }
                case "3":{
                    System.out.println("------------------");
                    System.out.print("Enter amount to deposit: ");
                    if (input.hasNextDouble()){
                        double d_amount = input.nextDouble();
                        input.nextLine();
                        System.out.println(account.D(d_amount));
                    }
                    else{
                        System.out.println("Invalid input. Please enter a numeric value.");
                        input.nextLine();
                    }
                    break;
                }
                case "4":{
                    System.out.println("------------------");
                    System.out.println("Thank you for using our service!");
                    return;
                }
                default:{
                    System.out.println("------------------");
                    System.out.println("Invalid choice.");
                }
            }
        }
    }
}