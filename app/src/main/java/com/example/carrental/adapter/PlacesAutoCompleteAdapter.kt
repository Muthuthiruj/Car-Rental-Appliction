package com.example.carrental.view


import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.example.carrental.R
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

class PlacesAutoCompleteAdapter(context: Context, private val placesClient: PlacesClient) :
    ArrayAdapter<AutocompletePrediction>(context, R.layout.item_search_suggestion), Filterable {
    private var resultList: MutableList<AutocompletePrediction> = ArrayList()

    override fun getCount(): Int {
        return resultList.size
    }

    override fun getItem(position: Int): AutocompletePrediction {
        return resultList[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()
                if (constraint != null) {
                    val request =
                        FindAutocompletePredictionsRequest.builder()
                            .setQuery(constraint.toString())
                            .build()

                    placesClient.findAutocompletePredictions(request)
                        .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                            resultList.clear()
                            resultList.addAll(response.autocompletePredictions)
                            notifyDataSetChanged()
                        }

                    results.values = resultList
                    results.count = resultList.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                if (results.values != null) {
                    resultList = results.values as MutableList<AutocompletePrediction>
                    notifyDataSetChanged()
                }
            }
        }
    }
}