package com.api.instaclone.repository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializerDeserializer {
    
    public static final Object deserializeObject(byte[] data) throws Exception {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return  objectInputStream.readObject();
        }
    }

    public static final byte[] serializeObject(Object object){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            
            objectOutputStream.writeObject(object);
        }catch(Exception e){
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

}
