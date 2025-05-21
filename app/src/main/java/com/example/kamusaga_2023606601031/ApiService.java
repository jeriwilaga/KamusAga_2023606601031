package com.example.kamusaga_2023606601031;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ApiService {

    @POST("create_entry.php")
    Call<ResponseResult> createEntry(@Body KamusEntry entry);

    @GET("read_entries.php")
    Call<List<KamusEntry>> getAllEntries();

    @PUT("update_entry.php/{id}")
    Call<Void> updateEntry(@Path("id") int id, @Body KamusEntry entry);


    @GET("delete_entry.php")
    Call<Void> deleteEntry(@Query("id") int id);
}