package com.cogimag.michalg.einkaufsliste

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_ware_hinzufuegen.*
import java.lang.Exception

class WareHinzufuegen : AppCompatActivity() {
    private val KLASSE_LOG_TAG = "WareHinzufuegen"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ware_hinzufuegen)

        (intent.getLongExtra(WarenModell.FELD_WAREN_LADEN, -1)).also {ladenId ->
            if (ladenId > 0) {
                val dbUtility:LokaleDb = LokaleDb(this)
                val laden: LadenModell = dbUtility.einenLaden(ladenId)
                ware_hinzufügen_tv_laden_name.text = if (laden.id > 0)  laden.name
                    else "Laden unzulänglich"
            }
        }

        //prefer this construct?
        with(intent.getLongExtra(WarenModell.FELD_WAREN_LADEN, -1)) {
            if (this > 0) {

            }
        }

    }

    fun btnWareEingebenClick(view: View) {
        //get intent, laden id
        //if -1, warn, cancel
        //better: disable + button if no store selected
        val wareName: String = ware_hinzufügen_txt_name.text.toString()
        var wareMenge: Int
        try {
            wareMenge = ware_hinzufügen_spn_menge.text.toString().toInt()


            (intent.getLongExtra(WarenModell.FELD_WAREN_LADEN, -1)).also { ladenId ->
                if (ladenId > 0) {
                    val warenModell: WarenModell = WarenModell(0, ladenId, wareName, wareMenge)
                    LokaleDb(this).apply {
                        val rowId: Long = insertWarenRecord(warenModell)
                        if (rowId > 0) {
                            finish()
                        }
                        else {
                            Log.e(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG neue Reihe Fehler")
                            //snackbar
                        }
                    }
                }
            }



        }
        catch (ex:Exception) {
            wareMenge = -1
            Log.e(AppKonstante.APP_LOG_TAG, "$KLASSE_LOG_TAG Ware Menge unzulänglich")
        }

    }


    fun btnWareHinzufuegenAbbrechenClick(view: View) = finish()
}
