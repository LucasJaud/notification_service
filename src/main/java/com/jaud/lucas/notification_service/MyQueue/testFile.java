package com.jaud.lucas.notification_service.MyQueue;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.jaud.lucas.notification_service.message.Message;

public class testFile {
public static void main(String[] args) {

    File file = new File("test.json");

    List<Message> queue = new LinkedList<>();
            queue.add(new Message(UUID.randomUUID(),"teste123")); 
            queue.add(new Message(UUID.randomUUID(),"teste123")); 
            queue.add(new Message(UUID.randomUUID(),"teste123")); 

    List<Message> queue2 = new LinkedList<>();
    
    try (FileOutputStream fileStream = new FileOutputStream(file)){

            ObjectOutputStream out = new ObjectOutputStream(fileStream);
            
            for(Message m : queue){
                out.writeObject(m);
            }

            out.close();
            fileStream.close();
    } catch(Exception ex ){
        System.out.println("error: " + ex);
    }

    try (ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file))) {
        for (int i = 0; i < queue.size();i++){
            queue2.add((Message) oin.readObject());
        }
        
        queue2.forEach(msg -> System.out.println(msg));

    } catch (EOFException e) {
        System.out.println("EOFException");
    } catch (IOException e){
        System.out.println("ioException");
    } catch (ClassNotFoundException e){
        System.out.println("ClassNotFoundException");
    }

}


}
