package be.henallux.ig3.smartcity.elbatapp.ui.positiveToCovid19

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import be.henallux.ig3.smartcity.elbatapp.R

class PositiveToCovidFragment : Fragment() {

    companion object {
        fun newInstance() = PositiveToCovidFragment()
    }

    private lateinit var viewModel: PositiveToCovidViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.positive_to_covid_fragment, container, false)

        val text = root.findViewById<TextView>(R.id.text_positif_covid)
        val button = root.findViewById<Button>(R.id.button_positif_covid)

        button.setOnClickListener { v ->

        }



        return root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PositiveToCovidViewModel::class.java)
        // TODO: Use the ViewModel




    }

}