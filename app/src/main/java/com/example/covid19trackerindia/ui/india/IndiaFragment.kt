package com.example.covid19trackerindia.ui.india

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_india.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException

class IndiaFragment : Fragment() {
    lateinit var mStateAdapter: StateAdapter
    lateinit var mStateList: ArrayList<StateItem>
    lateinit var mRequestQueue: RequestQueue
    lateinit var confirm: String
    lateinit var active:String
    lateinit var recoverd:String
    lateinit var deaths:String
    lateinit var lastup:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_india, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mStateList= ArrayList()
        mRequestQueue=Volley.newRequestQueue(context)
        recyclerView_States.layoutManager=LinearLayoutManager(context)
        recyclerView_States.setHasFixedSize(true)
        confirm=""
        active=""
        recoverd=""
        deaths=""
        lastup=""
        CoroutineScope(IO).launch {
            parseJson()
            setTextOnMainThread()
        }
    }
    private fun setNewText(confirm: String,active:String,recoverd:String,deaths:String,lastup:String){
        ilastup.text="*Last updated "+lastup
        iConfirmed.text=confirm
        iDeaths.text=deaths
        iActive.text=active
        iRecovered.text=recoverd
    }
    private suspend fun setTextOnMainThread() {
        withContext (Main) {
            setNewText(confirm,active,recoverd,deaths,lastup)
        }
    }
    fun getdone(){
        iload.visibility=View.GONE
    }
    suspend fun parseJson() {
        val url = "https://api.covid19india.org/data.json"
        val request =
            JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    try {
                        val jsonArray = response.getJSONArray("statewise")
                        val totaldata = jsonArray.getJSONObject(0)
      /*                  mStateList.add(StateItem(
                            "India",
                            totaldata.getString("confirmed"),
                            totaldata.getString("active"),
                            totaldata.getString("deaths")
                        ))*/
                        confirm=totaldata.getString("confirmed")
                        active=totaldata.getString("active")
                        recoverd=totaldata.getString("recovered")
                        deaths=totaldata.getString("deaths")
                        lastup=totaldata.getString("lastupdatedtime")
                        mStateAdapter = StateAdapter( mStateList)
                        recyclerView_States.adapter = mStateAdapter
                        for (i in 1 until jsonArray.length()) {
                            val statedata = jsonArray.getJSONObject(i)
                            mStateList.add(
                                StateItem(
                                    statedata.getString("state"),
                                    statedata.getString("confirmed"),
                                    statedata.getString("active"),
                                    statedata.getString("deaths")
                                )
                            )
                            mStateAdapter = StateAdapter( mStateList)
                            recyclerView_States.adapter = mStateAdapter
                            getdone()
                            setNewText(confirm,active,recoverd,deaths,lastup)

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error -> error.printStackTrace() })
        mRequestQueue.add(request)
    }
}
