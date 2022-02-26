package com.example.sunriseapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getSunrise(view:View){
        var city = etCityName.text.toString()
        val url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"+ city + "?key=KT29U5FVU4TTREQ6MA2DFCBWT"
        MyAsyncTask().execute(url)
    }

    inner class MyAsyncTask: AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String {
            try {
                val url = URL(params[0])
                val urlConnect=url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout=10000
                var inString=ConvertStreamToString(urlConnect.inputStream)
                publishProgress(inString)
            }catch (ex:Exception){}

            return ""
        }
        override fun onProgressUpdate(vararg values: String?){
            try {
                var json=JSONObject(values[0])
                val days = json.getJSONArray("days")
                val today=days.getJSONObject(0)
                val sunrise=today.getString("sunrise")
                tvSunsetTime.setText("Sunrise time is " + sunrise)

            }catch (ex:Exception){}
        }

    }

    fun ConvertStreamToString(inputStream:InputStream):String{
        val bufferReader=BufferedReader(InputStreamReader(inputStream))
        var line:String
        var allString:String=""
        try {
            do{
                line=bufferReader.readLine()
                if(line != null){
                    allString+=line
                }
            } while (line != null)
            inputStream.close()
        }catch (ex:Exception){}

        return allString
    }
}