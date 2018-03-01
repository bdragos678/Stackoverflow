package com.example.bdrag.stackoverflow;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SaveReadDataToCacheOrPersistentStorage {

    private static final String CACHE_MESSAGE = "CACHE";
    private static final String PERSISTENT_STORAGE_MESSAGE = "PERSISTENT_STORAGE_MESSAGE";

    public static void writeObject(Context context, Object object, String cacheOrPersistentStorage) {

        ObjectOutput out = null;

        try {
            if(cacheOrPersistentStorage.equals(CACHE_MESSAGE)){
                out = new ObjectOutputStream(new FileOutputStream
                        (new File(context.getCacheDir(), "") + File.separator + "cacheFile.srl"));
            }else if(cacheOrPersistentStorage.equals(PERSISTENT_STORAGE_MESSAGE)){
                out = new ObjectOutputStream(context.openFileOutput("users.data", Context.MODE_PRIVATE));
            }

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String currentDate = dateFormat.format(date);

            out.writeObject(currentDate);
            out.writeObject(object);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Object readObject(Context context, String cacheOrPersistentStorage, int minutes){

        ObjectInputStream in = null;
        Object object = null;

        try {
            if(cacheOrPersistentStorage.equals(CACHE_MESSAGE)){
                in = new ObjectInputStream(new FileInputStream
                        (new File(context.getCacheDir() + File.separator + "cacheFile.srl")));
            }else if(cacheOrPersistentStorage.equals(PERSISTENT_STORAGE_MESSAGE)){
                in = new ObjectInputStream(context.openFileInput("users.data"));
            }

            String dateFromFile = (String)in.readObject();

            if(canIReadFromFile(dateFromFile, minutes)){
                object = in.readObject();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {

            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return object;
    }

    private static boolean canIReadFromFile(String dateFromFile, int minutes){

        String[] parsedDateFromFile = dateFromFile.split(" ");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        String[] parsedCurrentDate = currentDate.split(" ");

        if(parsedDateFromFile[0].equals(parsedCurrentDate[0])){
            Date dateFile = null;

            try {
                dateFile = dateFormat.parse(dateFromFile);

                long timestampDateFile = dateFile.getTime();
                long timestampCurrentDate = date.getTime();

                if(Math.abs(timestampCurrentDate - timestampDateFile) < TimeUnit.MINUTES.toMillis(minutes)){
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
