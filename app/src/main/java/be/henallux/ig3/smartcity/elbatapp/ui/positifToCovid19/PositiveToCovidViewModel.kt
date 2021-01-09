package be.henallux.ig3.smartcity.elbatapp.ui.positifToCovid19

import android.app.Application
import android.content.Context

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError
import be.henallux.ig3.smartcity.elbatapp.data.model.Reservation
import be.henallux.ig3.smartcity.elbatapp.data.model.User
import be.henallux.ig3.smartcity.elbatapp.repositories.web.RetrofitConfigurationService
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.ReservationDto
import be.henallux.ig3.smartcity.elbatapp.utils.errors.NoConnectivityException
import com.auth0.android.jwt.JWT
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PositiveToCovidViewModel(application: Application) : AndroidViewModel(application) {

    private val webService = RetrofitConfigurationService.getInstance(application).elbatWebService
    val token = application
            .getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE)
            .getString("JSONWEBTOKEN", "")

    val jwt = token?.let { JWT(it) }
    val userData = jwt?.getClaim("userData")
    val userId = userData?.asObject(User::class.java)!!.id

//    fun updatePositiveToCovid(){
//        // en cours dans webstorm
//
//    }


}