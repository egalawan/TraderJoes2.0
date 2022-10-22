package com.example.traderjoes20;
import android.content.Context;

import com.example.traderjoes20.Listeners.RandomRecipeResponseListener;
import com.example.traderjoes20.Models.RandomRecipeApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2. Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit. Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    //call from main to get data
    public void getRandomRecipes(RandomRecipeResponseListener listener){
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        //number at end is the total number of random recipes 1-100
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key),"5");
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(),response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    private interface CallRandomRecipes{
        //from spoonacular GET RANDOM RECIPES
        @GET("recipes/random")
        Call<RandomRecipeApiResponse> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number
        );
    }
}
