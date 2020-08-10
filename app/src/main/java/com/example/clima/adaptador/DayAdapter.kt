package com.example.clima.adaptador

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ahmadrosid.svgloader.SvgLoader
import com.example.clima.R
import com.example.clima.entity.PronosticoDiario
import com.example.clima.api.URL_IMG
import kotlinx.android.synthetic.main.clima_semanal_item.view.*

class DayAdapter(val context: Context, val dataSource: ArrayList<PronosticoDiario>) :
    BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val view: View
//        println(context)
//        print(dataSource)
        if (convertView == null) {//cargamos los datos de los 5 dias si esta vacio
            view = LayoutInflater.from(context).inflate(R.layout.clima_semanal_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {//sino esta vacio
            viewHolder = convertView.tag as ViewHolder
            view = convertView
        }

        val convertView = dataSource[position]
        viewHolder.apply {
            //llenamos los campos de textos
            fecha.text = "Fecha: " + convertView.date.substring(0, 10)
            tempMax.text = convertView.temperature.maximum.value.toString()
            tempMin.text = convertView.temperature.minimum.value.toString()
            //cargmos los iconos de tiempo
            SvgLoader.pluck()
                .with(context as Activity?)
                .setPlaceHolder(R.drawable.weather_icon, R.drawable.weather_icon)
                .load(URL_IMG + convertView.day.icon + ".svg", imgdia);
            //cargmos los iconos de tiempo
            SvgLoader.pluck()
                .with(context as Activity?)
                .setPlaceHolder(R.drawable.weather_icon, R.drawable.weather_icon)
                .load(URL_IMG + convertView.night.icon + ".svg", imgNoche);


            proDia.text = convertView.day.iconPhrase
            proNoche.text = convertView.night.iconPhrase
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    /*
    Aplicamos el modelo Holder
     */
    private class ViewHolder(view: View) {
        val fecha: TextView = view.fecha
        val tempMin: TextView = view.txtTempMin
        val tempMax: TextView = view.txtTempMax
        val imgdia: ImageView = view.imgdia
        val imgNoche: ImageView = view.imgNoche
        val proDia: TextView = view.prDia
        val proNoche: TextView = view.prNoche

    }
}