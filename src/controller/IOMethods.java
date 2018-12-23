/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import models.Message;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

/**
 *
 * @author usuario
 */


public class IOMethods {
    
    
   
   /**
    * Method for option 1, send message. The method receives the data from the new sender,
    * message and receiver, adds the LocalTimeDate formatted to String and adds the new element
    * mensaje into the XML file. Returns boolean true or false deppending of the result of the action.
    * @param sender - String
    * @param receiver - String
    * @param text - String
    * @return boolean
    */
   public static boolean saveData(String sender, String receiver, String text){
        
        try {
            //creation of local date time in String format
            LocalDateTime dateTime = LocalDateTime.now();   
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter); // "1986-04-08 12:30:00"
            
            //initialization of DocumentBuilderFactory, Document and Node root
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File f = new File("messages.xml");
            Document doc = builder.parse(f);
            Node root = doc.getFirstChild();
            
            //Creation of the node "Mensaje" where we add the local date time as attribute
             Node mensaje = doc.createElement("mensaje");
             Element n = (Element)mensaje;
             n.setAttribute("fechahora", formattedDateTime);
             
            // Creation of the  three nodes that are descendants of "mensaje"
            Node emisor = doc.createElement("emisor");
            Node receptor = doc.createElement("receptor");
            Node texto = doc.createElement("texto");
            
            // Creation of text nodes with the respective parameters
            Node textoEmisor = doc.createTextNode(sender);
            Node textoReceptor = doc.createTextNode(receiver);
            Node textoTexto = doc.createTextNode(text);
            
            // Addition of every text to his node
            emisor.appendChild(textoEmisor);
            receptor.appendChild(textoReceptor);
            texto.appendChild(textoTexto);
            
            // Addition to mensaje his childs
            n.appendChild(emisor);
            n.appendChild(receptor);
            n.appendChild(texto);
            
            // Addition of message inside our tree (mensajes)
            root.appendChild(n);
            
            //Save of modified dom into xml file
            OutputFormat format = new OutputFormat(doc);
            format.setIndenting(true);
            
            // Serialization
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(f), format);
            
            // Writing into file
            serializer.serialize(doc);
            
            return true;
       } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println("Error al cargar el DOM: " + ex.getMessage());
            return false;
       }
   } 

   
   /**
     * This method receives a String with the name of the person who received the message (if any),
     * using XPath methods searches all elements Mensaje where receptor equals the receiver passed
     * by parameter and returns an String ArrayList with all the data of these elements (dateTime, sender, receiver,
     * and text. 
     * @param receiver - String
     * @return 
     */
    public static ArrayList<String> messagesByReceiver(String receiver){
        
        //ArrayList and vars to store the info
        ArrayList<String> messages = new ArrayList<>();
        LocalDateTime dateTime = null;
        String sender = null;
        String receiv = null;
        String text = null;
        int i, j = 0;
        try{
            //initial elements
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File f = new File("messages.xml");
            Document doc = builder.parse(f);
            
            //Searching all elements Mensaje which receptor descendant text equals the parameter received
            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = "//mensaje[descendant::receptor[text()=" + "'" + receiver + "'" + "]]";
            NodeList nodeList = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);
                
            //Going over all elements obtained, getting values and creating objects of class Mensaje
            for (i = 0; i < nodeList.getLength(); i++) {
                Element n = (Element)nodeList.item(i);
                //transformation of String into localDateTime:
                String date = n.getAttribute("fechahora");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
                dateTime = LocalDateTime.parse(date, formatter);
                NodeList descendants = nodeList.item(i).getChildNodes();
                
                //Going over all descendants and switch to his tag name to save his value
                for(j = 0; j < descendants.getLength(); j++){
                   switch(descendants.item(j).getNodeName()){
                      case "emisor":
                        sender = descendants.item(j).getTextContent();
                        break;
                      case "receptor":
                        receiv = descendants.item(j).getTextContent();
                        break; 
                      case "texto":
                         text = descendants.item(j).getTextContent();
                   }
                }
                //creation of the object, and calling to toString(), to save into ArrayList to be returned.   
                Message message = new Message(dateTime, sender, receiv, text);
                messages.add(message.toString());
                } 
                return messages;
             
        }catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println("Error al cargar el DOM: " + ex.getMessage());
        } catch (XPathExpressionException ex) {
           Logger.getLogger(IOMethods.class.getName()).log(Level.SEVERE, null, ex);
       }
        
        return messages;
    }
   
    /**
     * This method does the same than previous messagesByReceiver2, but now no XPath methods
     * are used, only DOM.
     * @param receiv String
     * @return ArrayList String
     */
    
    public static ArrayList<String> messagesByReceiver2(String receiv){
        ArrayList<String> data = new ArrayList<>();
        LocalDateTime dateTime = null;
        String sender = null;
        String receiver = null;
        String text = null;
        int i, j = 0;
        try{
            //initial elements
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File f = new File("messages.xml");
            Document doc = builder.parse(f);
            
            //Main node and child Nodelist
            Node root = doc.getFirstChild();
            NodeList child = root.getChildNodes();
            
            //Processing of every child into the NodeList
            for(i = 0; i < child.getLength(); i++) {
                Node actualItem = child.item(i);
                if(actualItem.getNodeType() == Node.ELEMENT_NODE){
                    
                    //transformation of String into localDateTime:
                    Element n = (Element)actualItem;
                    String date = n.getAttribute("fechahora");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
                    dateTime = LocalDateTime.parse(date, formatter);
                    
                    //Processing of every element inside the actualItem. We control its type with a switch and save text node
                    NodeList content = actualItem.getChildNodes();
                    for (j = 0; j < content.getLength(); j++){
                        Node actual = content.item(j);
                         if (actual.getNodeType() == Node.ELEMENT_NODE) {
                            switch (actual.getNodeName()) {
                                case "emisor":
                                    sender = actual.getTextContent();
                                    break;
                                case "receptor":
                                    receiver = actual.getTextContent();
                                    break;
                                case "texto":
                                    text = actual.getTextContent();
                                    break;
                                default:
                                    break;
                            }
                         }  
                    }
                }
                if(sender != null && receiver != null && text != null){
                    if(receiver.equals(receiv)){
                       Message message = new Message(dateTime,sender, receiver, text);
                       data.add(message.toString());
                       dateTime = null;
                       sender = null;
                       receiver = null;
                       text = null;
                    }
                }
                
            }
        }catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println("Error al cargar el DOM: " + ex.getMessage());
        }
        return data;
    }
    
    
    /**
     * Method to return all messages text from the XML file. Every element mensaje is converted to an
     * object of class Message in order to access to his toString() method and save into the 
     * ArrayList<String> the method returns.
     * @return ArrayList String
     */
    
    public static ArrayList<String> getMessages(){
        
        ArrayList<String> messages = new ArrayList<>();
        LocalDateTime dateTime = null;
        String sender = null;
        String receiver = null;
        String text = null;
        int i, j = 0;
        
        try{
            //initial elements
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File f = new File("messages.xml");
            Document doc = builder.parse(f);
             
            //Xpath object creation and query of all elements mensaje
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression m  = xpath.compile("/mensajes/mensaje");
            
            // cast to NodeList
            NodeList nodeList = (NodeList) m.evaluate(doc, XPathConstants.NODESET);
            
            //Going over all elements obtained, getting values and creating objects of class Mensaje
            for (i = 0; i < nodeList.getLength(); i++) {
                Element n = (Element)nodeList.item(i);
                //transformation of String into localDateTime:
                String date = n.getAttribute("fechahora");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
                dateTime = LocalDateTime.parse(date, formatter);
                NodeList descendants = nodeList.item(i).getChildNodes();
                
                //Going over all descendants and switch to his tag name to save his value
                for(j = 0; j < descendants.getLength(); j++){
                   switch(descendants.item(j).getNodeName()){
                      case "emisor":
                        sender = descendants.item(j).getTextContent();
                        break;
                      case "receptor":
                        receiver = descendants.item(j).getTextContent();
                        break; 
                      case "texto":
                         text = descendants.item(j).getTextContent();
                   }
                }
                //creation of the object, and calling to toString(), to save into ArrayList to be returned.   
                Message message = new Message(dateTime, sender, receiver, text);
                messages.add(message.toString());
                } 
                return messages;
        }catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println("Error al cargar el DOM: " + ex.getMessage());
        } catch (XPathExpressionException ex) {
            Logger.getLogger(IOMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        return messages;
    }
    
    /**
     * The method receives an int and the node mensaje on that position is deleted. Return
     * true if the task is accomplished, false elsewhere.
     * @param option - int
     * @return boolean
     */
    public static boolean deleteMessage(int option){
        
        try{
            //initial elements
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File f = new File("messages.xml");
            Document doc = builder.parse(f);
            
            
            // XPath class initialization and XpathExpression of all mensajes elements
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression text  = xpath.compile("/mensajes/mensaje");
            
            // cast to NodeList. We get the Node from the NodeList with the position of the
            //option we received by parameter and finally the node is deleted.
            NodeList textRes = (NodeList) text.evaluate(doc, XPathConstants.NODESET);
            Node delete = textRes.item(option);
            delete.getParentNode().removeChild(delete);
             
            //serialization and tabulation
            OutputFormat format = new OutputFormat(doc);
            format.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(f), format);
            
            // writing of the xml
            serializer.serialize(doc);
            
            return true;
        }catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println("Error al cargar el DOM: " + ex.getMessage());
            return false;
        } catch (XPathExpressionException ex) {
            Logger.getLogger(IOMethods.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    } 
    
   
 }
    
    
/*
https://www.baeldung.com/java-xpath
*/