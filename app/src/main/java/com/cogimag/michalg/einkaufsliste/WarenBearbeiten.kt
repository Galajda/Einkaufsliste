package com.cogimag.michalg.einkaufsliste

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_waren_bearbeiten.*

class WarenBearbeiten : AppCompatActivity() {
    companion object {
        const val KLASSE_LOG_TAG = "WarenBearbeiten"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waren_bearbeiten)
        val wareId:Long = intent.getLongExtra(WarenModell.FELD_WAREN_ID, 0)
        waren_bearbeiten_txt_waren_id.text = wareId.toString()

        LokaleDb(this).apply {
            val ware:WarenModell = eineWare(wareId)
            waren_bearbeiten_txt_waren_alter_name.text = ware.name
        }


        //menge
        //laden umlagern


    }

    fun waren_bearbeiten_btn_eingeben_Click(v: View) {
//        val tvWareId: TextView = findViewById(R.id.waren_bearbeiten_txt_waren_id)
        val wareId: Long = waren_bearbeiten_txt_waren_id.text.toString().toLong()
//        val edtxtNeuerName: EditText = findViewById(R.id.waren_bearbeiten_edtxt_waren_neuer_name)
        val neuerName: String = waren_bearbeiten_edtxt_waren_neuer_name.text.toString()



        LokaleDb(this).apply {
            val ursprünglicheWare = eineWare(wareId)
            val geanderteWare =
                WarenModell(wareId,ursprünglicheWare.laden, neuerName, ursprünglicheWare.menge)

            val rowsAffected:Int = wareBearbeiten(geanderteWare)

            if (rowsAffected > 0) finish()
            else {
                Log.e(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG Fehler Ware bearbeiten")
            }
        }

    }


    fun waren_bearbeiten_abbrechen_Click(v: View) = finish()
}
