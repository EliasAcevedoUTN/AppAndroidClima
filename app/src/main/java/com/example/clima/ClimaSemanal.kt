package com.example.clima

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.clima.io.ApiAdapter
import com.example.clima.adaptador.DayAdapter
import com.example.clima.entity.PronosticoDiario
import com.example.clima.api.API_KEY
import com.example.clima.api.LANG_LOCATION
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.clima_semanal.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import kotlin.collections.ArrayList as ArrayList1

class ClimaSemanal : AppCompatActivity() {
    var lista : List<PronosticoDiario>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clima_semanal)
        val key: Int = intent.extras?.getInt("key") ?: 1//obtenemos la key enviada del otro activity
        getCincoDias(key)//llamamos al metodo para obtener los cinco dias pasando como parametro la key de la ubicacion
    }

    /**
     * metodo para obtener el pronostico de 5 dias
     */
    private fun getCincoDias(key : Int){
        ApiAdapter()
            .getApiService().getDay("5day", key, API_KEY, LANG_LOCATION,true).enqueue(object :
                Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    when {
                        response.isSuccessful -> {//cuando la respuesta es exitosa
                            val posts: JsonElement? = response.body()
                            if (posts != null){
                                val gson = Gson()
                                val asJsonArray = posts.asJsonObject.get("DailyForecasts").asJsonArray
                                val type: Type = object : TypeToken<List<PronosticoDiario?>?>() {}.type
                                lista = gson.fromJson(asJsonArray, type)
                                var listas : ArrayList1<PronosticoDiario> = lista as kotlin.collections.ArrayList<PronosticoDiario>
                                val baseAdapter = DayAdapter(this@ClimaSemanal, listas)
                                listSemanal.adapter = baseAdapter
                            }
                        }

                    }
                }
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    t?.printStackTrace()
                }
            })
    }
}