package com.cogimag.michalg.einkaufsliste

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView

class LadenBearbeiten : AppCompatActivity() {

    companion object {
        const val KLASSE_LOG_TAG = "LadenBearbeiten"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laden_bearbeiten)


        val ladenZumAndern: Intent = intent

        val tvId:TextView = findViewById(R.id.laden_bearbeiten_txt_laden_id)
        val ladenId:Long = ladenZumAndern.getLongExtra(LadenModell.FELD_LADEN_ID, 0)
        tvId.text = ladenId.toString()
        //get laden name from db
        val tvName:TextView = findViewById(R.id.laden_bearbeiten_txt_laden_alter_name)
        val dbUtility = LokaleDb(context = this)
        val laden:LadenModell = dbUtility.einenLaden(ladenId)
        if (laden.id > 0) {
            tvName.text = laden.name
        }
        else {
            tvName.text = "Error getting laden name"
        }
        Log.i(AppKonstante.APP_LOG_TAG,  KLASSE_LOG_TAG + " got laden name " + laden.name)



    }


    fun laden_bearbeiten_btn_eingeben_Click(v: View) {
        val tvLadenId: TextView = findViewById(R.id.laden_bearbeiten_txt_laden_id)
        val ladenId:Long = tvLadenId.text.toString().toLong()
        val edtxtNeuerName: EditText = findViewById(R.id.laden_bearbeiten_edtxt_laden_neuer_name)
        val neuerName:String = edtxtNeuerName.text.toString()

        val geanderterLaden = LadenModell(ladenId,neuerName)

        val dbUtility = LokaleDb(this)
        val rowsAffected:Int = dbUtility.ladenVerarbeiten(geanderterLaden)
        if (rowsAffected > 0) finish()
        else {
            Log.e(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " fehler laden bearbeiten")
        }
    }

    fun laden_bearbeiten_abbrechen_Click(v: View) = finish()













}
