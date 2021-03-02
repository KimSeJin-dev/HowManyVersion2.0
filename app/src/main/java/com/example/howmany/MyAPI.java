package com.example.howmany;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface MyAPI{

//    @POST("/surveys/")
//    Call<PostItem> post_posts(@Body PostItem post);
//
//    @PATCH("/surveys/{pk}/")
//    Call<PostItem> patch_posts(@Path("pk") int pk, @Body PostItem post);

    @DELETE("/liveData/{pk}/")
    Call<PostItem> delete_posts(@Path("pk") String pk);

    @PATCH("/liveData/{pk}/")
    Call<Post> putData(@Path("pk") String pk , @Body Post post);

    @GET("/liveData/")
    Call<List<PostItem>> get_posts();

    @GET("/covidRecord/")
    Call<List<PostItem>> get_graph_posts();

//    @GET("/surveys/{pk}/")
//    Call<PostItem> get_post_pk(@Path("pk") int pk);

    @GET("/memberData/{pk}")
    Call<ExcelDB> get_excelDb(@Path("pk") String pk);
}