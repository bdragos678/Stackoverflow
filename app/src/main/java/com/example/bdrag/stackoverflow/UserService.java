package com.example.bdrag.stackoverflow;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("2.2/users")
    public Call<User> getUsers(@Query("pagesize") int pagesize, @Query("order") String order, @Query("sort") String sort, @Query("site") String site);
}
