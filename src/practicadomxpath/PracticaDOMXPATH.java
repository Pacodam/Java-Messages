/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicadomxpath;

import controller.IOMethods;
import java.util.ArrayList;


/**
 *
 * @author usuario
 */
public class PracticaDOMXPATH {

   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int option;
        do{
            showMenu();
            option = inputMethods.askInt("Select an option:");
            
            switch(option){
                case 1:
                    sendMail();
                    break;
                case 2:
                    messagesByReceptor();
                    break;
                case 3:
                    messagesByReceptor2();
                    break;
                case 4:
                    deleteMessage();
                    break;
                case 0:
                    System.out.println("Leaving application");
                    break;
                default:
                    System.out.println("Option not allowed");
                      
            }
        }while(option != 0);
    }
    
    
    
    /**
     * Sends a mail and stores into xml file
     */
     public static void sendMail(){
        System.out.println("*** New mail ***");
        String sender = inputMethods.askString("Enter sender:");
        String receiver = inputMethods.askString("Enter receiver:");
        String text = inputMethods.askString("Enter message:");
        /*now we send data to IOMethods.saveData(), where he will insert new object into arrayList
        and call to output method. The localDateTime will be added by controller in 
        new instance */
        if(IOMethods.saveData(sender, receiver, text)){
            System.out.println("New mail created");
        }
        else{
            System.out.println("Sorry. We had a problem. Try later");
        }
        
    }
     
    /**
     * The method shows all available receptors (unique if case that any of them is the same) in the 
     * xml file and the user can select from which one he wants to read messages. XPath variation
     */
     
      public static void messagesByReceptor() {
          String receiver = inputMethods.askString("Enter receiver name:");
          ArrayList<String> messages = IOMethods.messagesByReceiver(receiver);
          if(messages.isEmpty()){
              System.out.println("There is no messages for "+ receiver);
          }
          else{
              System.out.println("Messages for " + receiver + ":");
              for(int i = 0, j = 1; i < messages.size(); i++, j++){
                  System.out.println( j + ". "+ messages.get(i));
              }
          }
    }
     
     
    /**
     * The method shows all available receptors (unique if case that any of them is the same) in the 
     * xml file and the user can select from which one he wants to read messages. DOM variation
     */
     public static void messagesByReceptor2(){
         String receiver = inputMethods.askString("Enter receiver name:");
         ArrayList<String> messages = IOMethods.messagesByReceiver2(receiver);
         if(messages.isEmpty()){
              System.out.println("There is no messages for "+ receiver);
          }
          else{
              System.out.println("Messages for " + receiver + ":");
              for(int i = 0, j = 1; i < messages.size(); i++, j++){
                  System.out.println( j + ". "+ messages.get(i));
              }
          }
    }
    
    
      
    /**
    * The user selects a message to delete.
    */
    public static void deleteMessage(){
        System.out.println("*** Delete message ***");
        ArrayList<String> messages = IOMethods.getMessages();
        if(messages.isEmpty()){
            System.out.println("There is no messages yet");
        }
        else{
            System.out.println("Messages: ");
            for(int i=0, j=1; i < messages.size(); i++,j++){
                System.out.println("  "+ j + ". "+ messages.get(i));
            }
            int option = -1;
            do{
                option = inputMethods.askInt("Select a message to delete");
                if(option < 1 || option > messages.size()){
                    System.out.println("Option not allowed");
                    option = -1;
                }
                else{
                   if(IOMethods.deleteMessage(option-1)){
                       System.out.println("Message deleted");
                   }
                   else{
                       System.out.println("Sorry, message wasn't deleted, try later");
                   }
                }
            }while(option == -1);
        }
    }
    
    
    public static void showMenu(){
        
        System.out.println("\n*** DOM/Xpath activity ***");
        System.out.println("1. Send a mail");
        System.out.println("2. Search messages by receptor (XPath Version)");
        System.out.println("3. Search messages by receptor (DOM Version)");
        System.out.println("4. Delete specific message");
        System.out.println("0. Exit");
    }
    
    
}
