package be.henallux.ig3.smartcity.elbatapp.repositories.web;

import java.util.List;

import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.EstablishmentDto;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.LoginCredentialsDto;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.ReservationDto;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.UserDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ELBATWebService {
    @POST("/user/login")
    Call<String> login(@Body LoginCredentialsDto loginCredentials);

    @GET("/establishment/")
    Call<List<EstablishmentDto>> getAllEstablishments(@Header(value = "Authorization") String token);

    @POST("/person/")
    Call<Void> addUser(@Body UserDto user);

    @POST("/reservation/")
    Call<Void> makeReservation(@Body ReservationDto reservationDto);
}
