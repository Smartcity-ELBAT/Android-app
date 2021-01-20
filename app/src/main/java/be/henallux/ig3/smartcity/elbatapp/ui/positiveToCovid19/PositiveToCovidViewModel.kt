package be.henallux.ig3.smartcity.elbatapp.ui.positiveToCovid19

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError
import be.henallux.ig3.smartcity.elbatapp.data.model.User
import be.henallux.ig3.smartcity.elbatapp.repositories.web.RetrofitConfigurationService
import be.henallux.ig3.smartcity.elbatapp.utils.errors.NoConnectivityException
import com.auth0.android.jwt.JWT
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PositiveToCovidViewModel(application: Application) : AndroidViewModel(application) {

    private val _error = MutableLiveData<NetworkError>()
    private val error: LiveData<NetworkError> = _error


    private val _statutCode = MutableLiveData<Int>()
    private val statutCode: LiveData<Int> = _statutCode

    private val webService = RetrofitConfigurationService.getInstance(application).elbatWebService
    private val token = application
            .getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE)
            .getString("JSONWEBTOKEN", "")

    private val jwt = token?.let { JWT(it) }
    private val userData = jwt?.getClaim("userData")
    private val userId: Int = userData?.asObject(User::class.java)!!.id

    fun getError(): LiveData<NetworkError> {
        return error
    }

    fun getStatutCode(): LiveData<Int> {
        return statutCode
    }

    fun updatePositiveToCovid() {
        webService.updateCovid("Bearer $token", userId).enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                _statutCode.value = response.code()
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                _error.value = if (t is NoConnectivityException) NetworkError.NO_CONNECTION else NetworkError.TECHNICAL_ERROR
            }
        })
    }
}