package be.henallux.ig3.smartcity.elbatapp.repositories.web;

import java.util.List;

import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.CancelDto;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.EstablishmentDto;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.LoginCredentialsDto;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.PasswordDto;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.ReservationDto;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.TableDto;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.UserDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ELBATWebService {
    @POST("/user/login")
    Call<String> login(@Body LoginCredentialsDto loginCredentials);

    @GET("/establishment/")
    Call<List<EstablishmentDto>> getAllEstablishments(@Header(value = "Authorization") String token);

    @POST("/person/")
    Call<Void> addUser(@Body UserDto user);

    @POST("/reservation/")
    Call<Void> makeReservation(@Header(value = "Authorization") String token, @Body ReservationDto reservation);

    @GET("/table/{establishmentId}/{chosenDate}")
    Call<List<TableDto>> getAvailableTablesForDate(@Header(value = "Authorization") String token, @Path("establishmentId") Integer establishmentId, @Path("chosenDate") String wantedDate);
  
    @GET("/person/customer/{id}")
    Call<UserDto> getUserById(@Header(value = "Authorization") String token, @Path("id") Integer id);

    @PATCH("/person/updatePassword/")
    Call<Void> updatePassword(@Header(value = "Authorization") String token, @Body PasswordDto password);

    @PATCH("/person/")
    Call<Void> updateUser(@Header(value = "Authorization") String token, @Body UserDto userDto);

    @GET("/reservation/client/{id}")
    Call<List<ReservationDto>> getReservations(@Header(value = "Authorization") String token, @Path("id") Integer id);

    @PATCH("reservation/cancel/")
    Call<Void> cancelReservations(@Header(value = "Authorization") String token, @Body CancelDto cancelDto);
}
