package be.henallux.ig3.smartcity.elbatapp.repositories.web;

import java.util.List;

import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.EstablishmentDto;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.LoginCredentialsDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ELBATWebService {
    @POST("/user/login")
    Call<String> login(@Body LoginCredentialsDto loginCredentials);

    @GET("/establishment/")
    Call<List<EstablishmentDto>> getAllEstablishments();
}
