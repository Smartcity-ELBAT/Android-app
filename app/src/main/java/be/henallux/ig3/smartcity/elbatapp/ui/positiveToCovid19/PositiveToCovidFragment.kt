package be.henallux.ig3.smartcity.elbatapp.ui.positiveToCovid19

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import be.henallux.ig3.smartcity.elbatapp.R
import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError

class PositiveToCovidFragment : Fragment() {

    private lateinit var viewModel: PositiveToCovidViewModel
    private lateinit var text : TextView
    private lateinit var button : Button
    private lateinit var error : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.positive_to_covid_fragment, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = resources.getString(R.string.covid_title)

        text = root.findViewById(R.id.text_positif_covid)
        button = root.findViewById(R.id.button_positif_covid)
        error = root.findViewById(R.id.error_covid)
        error.visibility = View.INVISIBLE
        error.text = null

        button.setOnClickListener {
            viewModel.updatePositiveToCovid()
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PositiveToCovidViewModel::class.java)

        viewModel.getError().observe(viewLifecycleOwner, { networkError: NetworkError ->
            if (networkError != null) {
                error.visibility = View.VISIBLE
                error.setText(networkError.errorMessage)
            }
        })

        viewModel.getStatutCode().observe(viewLifecycleOwner, { integer: Int ->
            if (integer != 204)
                error.visibility = View.VISIBLE

            when (integer) {
                400 -> error.setText(R.string.error_400_covid)
                401 -> error.setText(R.string.error_401_unauthorized)
                404 -> error.setText(R.string.error_404_update_covid)
                500 -> error.setText(R.string.error_500)
                204 -> {
                    text.setText(R.string.positif_to_covid_change)
                    button.setText(R.string.go_back_home)
                    button.setOnClickListener {
                        view?.let { it1 ->
                            Navigation.findNavController(it1).navigate(R.id.action_nav_corona_to_nav_booking)
                        }
                    }
                }
            }
        })
    }
}