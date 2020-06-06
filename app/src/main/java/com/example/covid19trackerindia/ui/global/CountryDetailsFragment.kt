package com.example.covid19trackerindia.ui.global

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.example.covid19trackerindia.R
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_country_details.*
import kotlinx.android.synthetic.main.fragment_country_details.view.*
import org.json.JSONException

class CountryDetailsFragment : Fragment() {
    lateinit var slug:String
    lateinit var name:String
    lateinit var date:ArrayList<String>
    lateinit var mRequestQueue: RequestQueue
    lateinit var entrylist:ArrayList<Entry>
    lateinit var datelist:ArrayList<DayItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_country_details, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        entrylist= ArrayList<Entry>()
        datelist= ArrayList<DayItem>()
        date= ArrayList()
        name= arguments?.getString("name").toString()
        heading.text=name
        slug= arguments?.getString("slug").toString()
        mRequestQueue=Volley.newRequestQueue(context)
        getDate()
        parseJson()
        refresh.setOnClickListener{
            cChart.fitScreen()
        }
    }
    fun getDate(){
        val url="https://api.covid19api.com/summary"
        val request=
            JsonObjectRequest(
                Request.Method.GET,url,null,
                Response.Listener { response ->
                    try {
                        val jsonArray=response.getJSONArray("Countries")
                        for (i in 0 until jsonArray.length()){
                            val cData=jsonArray.getJSONObject(i)
                            if(cData.getString("Country").equals(name)){
                                cConfirmed.text=cData.getString("TotalConfirmed")
                                cRecovered.text=cData.getString("TotalRecovered")
                                cDeaths.text=cData.getString("TotalDeaths")
                                clastup.text="*Last updated: "+cData.getString(("Date"))
                                cNewConfirmed.text=cData.getString("NewConfirmed")
                                cNewRecovered.text=cData.getString("NewRecovered")
                                cNewDeaths.text=cData.getString("NewDeaths")
                            }
                        }
                    }catch (e:JSONException){
                        e.printStackTrace()
                    }
                },Response.ErrorListener { error -> error.printStackTrace() }
            )
        mRequestQueue.add(request)
    }
    fun parseJson(){
        val url="https://api.covid19api.com/total/dayone/country/$slug"
        val requestZone=
            JsonArrayRequest(
                Request.Method.GET,url,null,
                Response.Listener {response ->
                    try {
                        for(i in 0 until response.length()){
                            val countriesdata= response.getJSONObject(i)
                            date.add(countriesdata.getString("Date").toString().substring(5,10))
                            datelist.add(DayItem(countriesdata.getString("Date"),countriesdata.getString("Confirmed").toInt()))
                            entrylist.add(Entry(i.toFloat(),countriesdata.getString("Confirmed").toFloat()))
                        }
                        if(response.length()!=0) {
                            val dataset: LineDataSet = LineDataSet(entrylist, "")
                            dataset.setColor(Color.BLUE)
                            dataset.setDrawFilled(true)
                            dataset.setDrawValues(false)
                            dataset.setDrawCircles(false)

                            val lineData = LineData(dataset);
                            cChart.setData(lineData);
                            cChart.setTouchEnabled(true)
                            cChart.isKeepPositionOnRotation=true
                            cChart.setDrawMarkers(false)
                            cChart.invalidate()
                            cChart.description.isEnabled=false
                            cChart.axisLeft.isEnabled=false
                            cChart.xAxis.valueFormatter=IndexAxisValueFormatter(date)
                            cChart.xAxis.position=XAxis.XAxisPosition.BOTTOM
                        }
                    }
                    catch (e: JSONException){
                        Log.e("Volley", "exception coloe")
                        e.printStackTrace()
                    }
                },Response.ErrorListener {  error ->
                    error.printStackTrace()
                    Log.e("Volley", "error")
                }
            )
        mRequestQueue.add(requestZone)
    }

}
