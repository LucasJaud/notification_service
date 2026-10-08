package com.jaud.lucas.notification_service.MyQueue;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jaud.lucas.notification_service.message.Message;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class PersistentQueue {
    
    private Logger log = LoggerFactory.getLogger(PersistentQueue.class);
    private File file;
    private File dlqFile;
    private Queue<Message> queue;
    private Queue<Message> dlq;

    public PersistentQueue(String queuePath) throws IOException{
        this.file = new File("queuePath");
        this.queue = new LinkedList<>();
        loadQueue();
    }

    public PersistentQueue() throws IOException{
        this.file = new File("queue.csv");
        this.queue = new LinkedList<>();
        loadQueue();
    }

    public PersistentQueue(String queuePath, boolean dqlOn) throws IOException{
        this.file = new File("queuePath");
        this.queue = new LinkedList<>();
        loadQueue();
        if(dqlOn){
            this.dlq = new LinkedList<>();
            this.dlqFile = new File("dql.csv");
        }
    }

    public PersistentQueue(String queuePath, boolean dqlOn, String dlqPath) throws IOException{
        this.file = new File("queuePath");
        this.queue = new LinkedList<>();
        loadQueue();
        if(dqlOn){
            this.dlq = new LinkedList<>();
            this.dlqFile = new File(dlqPath);
        }
    }

    public PersistentQueue(boolean dqlOn) throws IOException{
        this.file = new File("queue.csv");
        this.queue = new LinkedList<>();
        loadQueue();
        if(dqlOn){
            this.dlq = new LinkedList<>();

        }
    }


    private synchronized void loadQueue() throws IOException{
        if(file.exists()){

            try (CSVReader csvReader = new CSVReader(new FileReader(file));){

                String[] nextLine;

                while((nextLine = csvReader.readNext()) != null){
                    Message msg = new Message();
                    msg.setId(UUID.nameUUIDFromBytes(nextLine[0].getBytes()));
                    msg.setPayload(nextLine[1]);
                    queue.add(msg);
                }
            }
        }
    }

    public void saveQueue() throws FileNotFoundException, IOException{
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(file))){

            csvWriter.flush();
            for(Message msg : queue){
                String[] cell = {msg.getId().toString(),msg.getPayload()};
                csvWriter.writeNext(cell);
            }
        } 
    }

    public Queue<Message> getQueue(){
        return this.queue;
    }

    public void setQueue(Queue<Message> queue){
        this.queue = queue;
    }

}
