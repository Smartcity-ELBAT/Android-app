package be.henallux.ig3.smartcity.elbatapp.ui.positiveToCovid19

import android.app.Application
import android.content.Context

import androidx.lifecycle.AndroidViewModel
import be.henallux.ig3.smartcity.elbatapp.data.model.User
import be.henallux.ig3.smartcity.elbatapp.repositories.web.RetrofitConfigurationService
import com.auth0.android.jwt.JWT

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