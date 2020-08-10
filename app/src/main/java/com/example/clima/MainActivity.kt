package com.example.clima

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.clima.io.ApiAdapter
import com.ahmadrosid.svgloader.SvgLoader
import com.example.clima.entity.PronosticoDiario
import com.example.clima.entity.UnaHora
import com.example.clima.api.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    var locationManager : LocationManager? = null
    var locationListener : LocationListener? = null
    var keyLocationApi : Int = 0//vamos a almacenar una clave que se usara para recuperar los datos del clima
    var userLocation : LatLng = LatLng(-32.8968706,-68.8528911)



//    companion object{
//        val CLIMA_SEMANAL = "CLIMA_SEMANAL"
//    }


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(3000)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //pongo el icono en el action bar
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager//cargamos el locationManager
        locationListener = object : LocationListener{
            override fun onLocationChanged(location: Location?) {
                if(location != null)//si trae una ubicacion GPS
                    userLocation = LatLng(location.latitude, location.longitude)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
                TODO("Not yet implemented")
            }

            override fun onProviderDisabled(provider: String?) {
                TODO("Not yet implemented")
            }

        }
        //pedimos los permisos al usuario
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){//comprobamos si el permiso fue concedido
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }else{//en caso de que el permiso fue otorgado
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,2f,locationListener)
            val lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            this.userLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
        }
        val latitud = userLocation.latitude
        val longitud = userLocation.longitude
        getLocationApi(latitud, longitud)

        //Agregamos el metodo para obtener los 5 dias al boton
        btnDays.setOnClickListener {
            getPronosticoSemanal()
        }


    }


    /**
     * Obtenemos la localizacion
     */
    fun getLocationApi(latitud: Double, longitud: Double) {
        ApiAdapter()
                .getApiService().getLocation(API_KEY, "$latitud,$longitud", LANG_LOCATION).enqueue(object : Callback<JsonElement> {
                    override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                        when {
                            response.isSuccessful -> {
                                Log.d("Post:", "Getting data success")
                                var posts: JsonElement? = response.body()

                                if (posts != null){
                                    Log.d("DATOS", posts.toString())
                                    keyLocationApi = posts.asJsonObject.get("Key").asInt//obtenemos la key de la ubicacion donde nos encontramos
                                    getClima()
                                }
                            }

                        }
                    }
                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        t?.printStackTrace()
                    }
                })
    }

    /**
     * una vez que obtengo la key de la ubicacion, puedo obtener el clima actual en este momento (esta api no trae todos los datos en solo endpoint)
     */
    fun getClima(){
        ApiAdapter()
                .getApiService().getHour( keyLocationApi, API_KEY, LANG_LOCATION,true).enqueue(object : Callback<JsonElement> {
                    override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                        when {
                            response.isSuccessful -> {
                                Log.d("Post:", "Getting data success")
                                val posts: JsonElement? = response.body()
                                if (posts != null) {
                                    val gson = Gson()
                                    val asJsonArray = posts.asJsonArray
                                    val type: Type = object : TypeToken<List<UnaHora?>?>() {}.type
                                    var lista : List<UnaHora>? =  gson.fromJson(asJsonArray, type)
                                    SvgLoader.pluck().with(this@MainActivity)
                                            .setPlaceHolder(R.drawable.weather_icon, R.drawable.weather_icon)
                                            .load(URL_IMG + lista?.get(0)?.weatherIcon.toString() + ".svg", imageView)//cargamos la imagen
                                    temp.text = lista?.get(0)?.temperature?.value.toString()//llenamos los datos con la temp
                                    idInfoDia.text = lista?.get(0)?.iconPhrase
                                    getPrimerDia()//obtenemos el clima del primer dia
                                }
                            }
                        }
                    }
                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        t?.printStackTrace()
                    }
                })
    }

    /**
     * obtengo los demas datos, imagenes de la noche y dia como asi su temperatura
     */
    fun getPrimerDia(){
        ApiAdapter()
                .getApiService().getDay("1day", keyLocationApi, API_KEY, LANG_LOCATION,true).enqueue(object :
                        Callback<JsonElement> {
                    override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                        when {
                            response.isSuccessful -> {//cuando la respuesta es exitosa
                                Log.d("Post:", "Getting data success")
                                val posts: JsonElement? = response.body()
                                if (posts != null){//revisamos si la respuesta esta vacia
                                    val gson = Gson()//creamos el objetno Gson para obtener los datos de la api
                                    val asJsonArray = posts.asJsonObject.get("DailyForecasts").asJsonArray//convertimos los datos json a un arreglo
                                    val type: Type = object : TypeToken<List<PronosticoDiario?>?>() {}.type
                                    var lista : List<PronosticoDiario>?  = gson.fromJson(asJsonArray, type)
                                    //completamos los datos que vamos a mostrar por pantalla
                                    tempMax.text = (lista?.get(0)?.temperature?.maximum?.value.toString()) + "°"
                                    tempMin.text = (lista?.get(0)?.temperature?.minimum?.value.toString()) + "°"
                                    pronostDia.text = lista?.get(0)?.day?.iconPhrase
                                    pronostNoche.text = lista?.get(0)?.night?.iconPhrase

                                    SvgLoader.pluck()
                                            .with(this@MainActivity)
                                            .setPlaceHolder(R.drawable.weather_icon, R.drawable.weather_icon)
                                            .load(URL_IMG + lista?.get(0)?.day?.icon.toString() + ".svg", imgpronostDia)

                                    SvgLoader.pluck()
                                            .with(this@MainActivity)
                                            .setPlaceHolder(R.drawable.weather_icon, R.drawable.weather_icon)
                                            .load(URL_IMG + lista?.get(0)?.night?.icon.toString() + ".svg", imgpronostNoche)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        t?.printStackTrace()
                    }
                })
    }
    /*
    Metodo que nos lleva al otro activity con el pronostico extendido
     */
    fun getPronosticoSemanal(){
        val intent = Intent(this, ClimaSemanal::class.java)
        intent.putExtra("key", keyLocationApi)//enviamos la key obtenida
        startActivity(intent)
    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == 1) {
//            if (grantResults.size > 0) {
//                if (ContextCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    ) == PackageManager.PERMISSION_GRANTED
//                ) {
//                    locationManager?.requestLocationUpdates(
//                        LocationManager.GPS_PROVIDER,
//                        2,
//                        2f,
//                        locationListener
//                    )
//                    val lastLocation =
//                        locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                    userLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
//                }
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }


}

