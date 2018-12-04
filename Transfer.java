/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public class Transfer extends Transaction {
    private double amount; // amount to withdraw
    private Keypad keypad; // reference to keypad
    private BankDatabase bankDatabase;
    private final static int    CANCELED = 0;
    
    
    public Transfer(int userAccountNumber, Screen atmScreen, 
            BankDatabase atmBankDatabase, Keypad atmKeypad) {
        super(userAccountNumber, atmScreen, atmBankDatabase);
    
        keypad = new Keypad();
        bankDatabase = atmBankDatabase;    
    }
    
    @Override
    public void execute(){
        Screen screen = getScreen();
        screen.displayMessage("Enter the recipient's account number : ");
        int accountNumber = keypad.getInput();
        BankDatabase recipient = getBankDatabase();
        if(recipient.findNumberAccount(accountNumber)){
            amount = promptForTransferAmount();
            BankDatabase atmBankDatabase = super.getBankDatabase();
            if(amount <= atmBankDatabase.getTotalBalance(super.getAccountNumber())){
                if(amount!=0){
                    recipient.debit(accountNumber, amount);
                    super.getBankDatabase().credit(super.getAccountNumber(), amount);
                    screen.displayMessage("Insert a deposit envelope containing ");
                    screen.displayDollarAmount(amount);
                    screen.displayMessageLine("\nTransfer Success");
                }else{
                    screen.displayMessageLine("\nCanceling transaction...");
                }
            }else{
                screen.displayMessageLine("\nNot enough Balance to transfer.\nCanceling transaction...");
            }
        }
        else{
            screen.displayMessageLine("Account doesn't exist");
        }
    }
    
    // prompt user to enter a deposit amount in cents 
    private double promptForTransferAmount(){
        Screen screen = getScreen();                                            // get reference to screen
        // display the prompt
        screen.displayMessage("Enter transfer amount in "
            + "CENTS (or 0 to cancel): ");
        int input = keypad.getInput();

        // check whether the user canceled or entered a valid amount
        if (input == CANCELED) {
            return CANCELED;
        }else {
            return (double) input / 100;                                        // return dollar amount
        }
    }
    
//    private void renasfer(){
//        
//    }
}
