package com.example.covid19trackerindia.ui.india

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.covid19trackerindia.R
import kotlinx.android.synthetic.main.fragment_district.*
import kotlinx.android.synthetic.main.fragment_global.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.json.JSONException

class DistrictFragment : Fragment() {
    lateinit var mRequestQueue: RequestQueue
    lateinit var name:String
    lateinit var mDistrictAdapter: DistrictAdapter
    lateinit var mDistrictList: ArrayList<DistrictItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_district, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        name= arguments?.getString("name").toString()
        mDistrictList= ArrayList()
        mRequestQueue=Volley.newRequestQueue(context)
        recyclerView_Districts.layoutManager=LinearLayoutManager(context)
        recyclerView_Districts.setHasFixedSize(true)
        CoroutineScope(IO).launch {
            parseJsonState(name)
            parseJson(name)
            setZoneColor()
        }
    }
    fun getdone(){
        dload.visibility=View.GONE
    }
    fun setZoneColor() {
        val url = "https://api.covid19india.org/zones.json"
        val requestZone =
            JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    try {
                        var pos = -1
                        val jsonArray = response.getJSONArray("zones")
                        Log.e("Volley", "started")
                        for (i in mDistrictList.indices) {
                            for (j in 0 until jsonArray.length()) {
                                if (mDistrictList.get(i).dname.equals(
                                        jsonArray.getJSONObject(j).getString("district")
                                    )
                                ) {
                                    pos = j
                                    break
                                }
                            }
                            if (pos != -1) {
                                setcolor(jsonArray.getJSONObject(pos).getString("zone"), i)
                            } else {
                                setcolor("Black", i)
                            }
                            Log.e("Volley", "Done color")
                            mDistrictAdapter = DistrictAdapter( mDistrictList)
                            recyclerView_Districts.setAdapter(mDistrictAdapter)
                        }
                    } catch (e: JSONException) {
                        Log.e("Volley", "exception coloe")
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
                    error.printStackTrace()
                    Log.e("Volley", "error")
                })
        mRequestQueue.add(requestZone)
    }

    fun parseJson(districtname: String?) {
        val url = "https://api.covid19india.org/state_district_wise.json"
        val requestdistrict =
            JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    try {
                        val state = response.getJSONObject(districtname)
                        Log.e("Volley", districtname)
                        val jsonObject = state.getJSONObject("districtData")
                        val jsonKeys =
                            jsonObject.keys()
                        while (jsonKeys.hasNext()) {
                            val namedistrict = jsonKeys.next()
                            val statedata = jsonObject.getJSONObject(namedistrict)
                            val confirmedCases = statedata.getString("confirmed")
                            val activecases = statedata.getString("active")
                            val death = statedata.getString("deceased")
                            //                                    JSONObject deltas=state.getJSONObject(statename).getJSONObject("delta");
                            //                                    String newCases = deltas.getString("confirmed");
                            val mdi =
                                DistrictItem(namedistrict, confirmedCases, activecases,death)
                            mDistrictList.add(mdi)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error -> error.printStackTrace() })
        mRequestQueue.add(requestdistrict)
    }

    fun parseJsonState(state: String?) {
        val url1 = "https://api.covid19india.org/data.json"
        val stateRequest =
            JsonObjectRequest(
                Request.Method.GET, url1, null,
                Response.Listener { response ->
                    try {
                        var i: Int
                        Log.e("Volley", "started")
                        val jsonArray = response.getJSONArray("statewise")
                        i = 1
                        while (i < jsonArray.length()) {
                            if (name == jsonArray.getJSONObject(i).getString("state")) {
                                break
                            }
                            i++
                        }
                        Log.e("Volley", i.toString() + "")
                        val statejson = jsonArray.getJSONObject(i)
                        Log.e("Volley", "mid")
                        districttotalname.text = state
                        districttotalconfirmed.setText(statejson.getString("confirmed"))
                        districttotalactive.setText(statejson.getString("active"))
                        districttotaldeath.setText(statejson.getString("deaths"))
                        districttotalrecovered.setText(statejson.getString("recovered"))
                        dlastup.setText("*Last updated "+statejson.getString("lastupdatedtime"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("Volley", "exception")
                    }
                }, Response.ErrorListener { error ->
                    error.printStackTrace()
                    Log.e("Volley", "error")
                })
        mRequestQueue.add(stateRequest)
    }

    fun setcolor(color: String, pos: Int) {
        when (color) {
            "Green" -> { mDistrictList[pos].zone=Color.GREEN }
            "Red" -> { mDistrictList[pos].zone=Color.RED }
            "Orange" -> { mDistrictList[pos].zone=Color.rgb(255, 165, 0) }
            else -> { mDistrictList[pos].zone=Color.BLACK }
        }
        getdone()
    }
}
