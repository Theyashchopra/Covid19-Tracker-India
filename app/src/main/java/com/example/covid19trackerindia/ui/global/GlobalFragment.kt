package com.example.covid19trackerindia.ui.global

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
import kotlinx.android.synthetic.main.fragment_global.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException

class GlobalFragment : Fragment() {
    lateinit var mGlobalAdapter: GlobalAdapter;
    lateinit var mGlobalList: ArrayList<GlobalItem>
    lateinit var mRequestQueue: RequestQueue
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_global, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mRequestQueue= Volley.newRequestQueue(context)
        mGlobalList= ArrayList()
        recyclerView_Global.layoutManager=LinearLayoutManager(context)
        recyclerView_Global.setHasFixedSize(true)
        CoroutineScope(IO).launch{
            parsejson()

        }
    }
    fun updatelast(up:String){
        glastup.text="*Last updated"+up
    }
    fun setData(globalItem: GlobalItem){
        gConfirmed.text=globalItem.cConfirm
        gRecovered.text=globalItem.cRecovered
        gDeaths.text=globalItem.cDeaths
        gActive.text=""+(globalItem.cConfirm.toInt()-(globalItem.cRecovered.toInt()+globalItem.cDeaths.toInt()))

    }
    fun getdone(){
            gload.visibility=View.GONE
    }
    suspend fun parsejson(){
        var temp:Boolean=false
        val url="https://api.covid19api.com/summary"
        val request=
            JsonObjectRequest(
            Request.Method.GET,url,null,
            Response.Listener { response ->
                try {
                    val jsonObject=response.getJSONObject("Global")
                    setData(
                        GlobalItem(
                        "Global",
                        jsonObject.getString("TotalConfirmed"),
                        jsonObject.getString("TotalRecovered"),
                        jsonObject.getString("TotalDeaths")
                        )
                    )
                    mGlobalAdapter= GlobalAdapter(mGlobalList)
                    recyclerView_Global.adapter=mGlobalAdapter
                    val jsonArray=response.getJSONArray("Countries")
                    for(i in 1 until jsonArray.length()){
                        val globaldata=jsonArray.getJSONObject(i)
                        if (!temp){
                            updatelast(globaldata.getString("Date"))
                        }
                        mGlobalList.add(
                            GlobalItem(
                                globaldata.getString("Country"),
                                globaldata.getString("TotalConfirmed"),
                                globaldata.getString("TotalRecovered"),
                                globaldata.getString("TotalDeaths"),
                                globaldata.getString("Slug")
                            )
                        )
                        mGlobalAdapter= GlobalAdapter(mGlobalList)
                        recyclerView_Global.adapter=mGlobalAdapter
                        getdone()
                    }
                }catch (e:JSONException){
                    e.printStackTrace()
                }
            },Response.ErrorListener { error -> error.printStackTrace() }
        )
        mRequestQueue.add(request)
    }
}
