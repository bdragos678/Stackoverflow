package com.example.bdrag.stackoverflow;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveReadDataToCacheOrPersistentStorage {

    public static void writeObject(Context context, Object object, String cacheOrPersistentStorage) throws FileNotFoundException {

        ObjectOutput out = null;

        try {
            if(cacheOrPersistentStorage.equals("CACHE")){
                out = new ObjectOutputStream(new FileOutputStream
                        (new File(context.getCacheDir(), "") + File.separator + "cacheFile.srl"));
            }else if(cacheOrPersistentStorage.equals("PERSISTENT_STORAGE")){
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
}
