package com.cogimag.michalg.einkaufsliste

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText

class LadenHinzufuegen : AppCompatActivity() {

    companion object {

        private const val CLASS_LOG_TAG:String = "LadenHinzufuegen"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laden_hinzufuegen)
    }


    fun btn_laden_eingeben_Click(view: View) {
//        Log.i(AppKonstante.APP_LOG_TAG, CLASS_LOG_TAG + "btn_laden_eingeben_Click")
        //db utility
        val dbUtility:LokaleDb = LokaleDb(this)
        val ladenEingabe: EditText = findViewById(R.id.laden_hinzufügen_txt_name)
        val ladenName:String = ladenEingabe.text.toString()
        val ladenModell:LadenModell = LadenModell(0, ladenName)

        val rowId:Long = dbUtility.insertLadenRecord(ladenModell)
        if (rowId > 0) {
//            Log.i(AppKonstante.APP_LOG_TAG, CLASS_LOG_TAG + " neue Reihe " + rowId)
            finish()
        }
        else {
            Log.e(AppKonstante.APP_LOG_TAG, CLASS_LOG_TAG + " neue Reihe Fehler")
            //snackbar message, stay in activity
        }

    }

    fun btn_laden_hinzufügen_abbrechen_Click(view: View) {
        finish()
    }


}
