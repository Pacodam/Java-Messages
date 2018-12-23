/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.time.LocalDateTime;

/**
 *
 * @author usuario
 */
public class Message {
    
    private LocalDateTime dateTime;
    private String sender;
    private String receiver;
    private String text;
    
    public Message(LocalDateTime dateTime, String sender, String receiver, String text){
        
        this.dateTime = dateTime;
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }


    public String getSender() {
        return sender;
    }


    public String getReceiver() {
        return receiver;
    }


    public String getText() {
        return text;
    }


    public LocalDateTime getTime() {
        return dateTime;
    }

    
    
     @Override
    public String toString() {
        return "Sent datetime: " + getTime() + ". Sender : "+ getSender()+ ". Receiver :" + getReceiver() 
                + ". Text : "+ getText();
              
    }
    
}
