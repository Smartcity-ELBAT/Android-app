package be.henallux.ig3.smartcity.elbatapp.ui.tableBooking

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import be.henallux.ig3.smartcity.elbatapp.data.model.Establishment
import androidx.lifecycle.LiveData
import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError
import be.henallux.ig3.smartcity.elbatapp.repositories.web.ELBATWebService
import be.henallux.ig3.smartcity.elbatapp.service.mappers.EstablishmentMapper
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.EstablishmentDto
import be.henallux.ig3.smartcity.elbatapp.utils.errors.NoConnectivityException
import be.henallux.ig3.smartcity.elbatapp.repositories.web.RetrofitConfigurationService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EstablishmentsMapViewModel(application: Application) : AndroidViewModel(application) {
    private val _establishments = MutableLiveData<List<Establishment>>()
    val establishments: LiveData<List<Establishment>> = _establishments
    private val _error = MutableLiveData<NetworkError?>()
    val error: LiveData<NetworkError?>
        get() = _error
    private val webService: ELBATWebService = RetrofitConfigurationService
            .getInstance(getApplication())
            .elbatWebService
    private val establishmentMapper: EstablishmentMapper = EstablishmentMapper.getInstance()

    fun requestEstablishments() {
        val token = "Bearer " + getApplication<Application>()
                .getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE)
                .getString("JSONWEBTOKEN", "")
        webService.getAllEstablishments(token).enqueue(object : Callback<List<EstablishmentDto?>?> {
            override fun onResponse(call: Call<List<EstablishmentDto?>?>, response: Response<List<EstablishmentDto?>?>) {
                if (response.isSuccessful) {
                    _establishments.value = establishmentMapper.mapToEstablishments(response.body())
                    _error.value = null
                }
            }

            override fun onFailure(call: Call<List<EstablishmentDto?>?>, t: Throwable) {
                _error.value = if (t is NoConnectivityException) NetworkError.NO_CONNECTION else NetworkError.TECHNICAL_ERROR
            }
        })
    }

}