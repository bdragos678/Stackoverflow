package com.example.bdrag.stackoverflow;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ItemAdapter adapter;
    private List<Item> items;
    private UserService service;

    private static final int PAGE_SIZE = 10;
    private static final String ORDER = "desc";
    private static final String SORT = "reputation";
    private static final String SITE = "stackoverflow";

    private static final String CACHE_MESSAGE = "CACHE";
    private static final String PERSISTENT_STORAGE_MESSAGE = "PERSISTENT_STORAGE_MESSAGE";
    private static final int CACHE_MINUTES = 10;
    private static final int PERSISTENT_STORAGE_MINUTES = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.list_view);
        adapter = new ItemAdapter(MainActivity.this, R.layout.list_item, new ArrayList<Item>());
        listView.setAdapter(adapter);

        items = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        items.clear();

        if(CheckInternetConnection.check(MainActivity.this)){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.stackexchange.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(UserService.class);

            service.getUsers(PAGE_SIZE, ORDER, SORT, SITE).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if(response.isSuccessful()){
                        items = response.body().getItems();

                        adapter.clear();
                        adapter.addAll(items);

                        SaveDataAsyncTask task = new SaveDataAsyncTask();
                        task.execute();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }else{
            Toast.makeText(MainActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    private class SaveDataAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            for(int i=0;i<items.size();i++){

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Bitmap imagine = ImageHelper.getImageBitmap(items.get(i).getProfileImage());
                imagine.compress(Bitmap.CompressFormat.PNG, 100, out);
                String image64 = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);

                items.get(i).setImageBase64(image64);
            }

            SaveReadDataToCacheOrPersistentStorage.writeObject(MainActivity.this, items, CACHE_MESSAGE);
            SaveReadDataToCacheOrPersistentStorage.writeObject(MainActivity.this, items, PERSISTENT_STORAGE_MESSAGE);

            return null;
        }
    }
}
