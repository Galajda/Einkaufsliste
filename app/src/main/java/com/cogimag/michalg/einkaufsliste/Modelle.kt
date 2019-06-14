package com.cogimag.michalg.einkaufsliste


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.ContentValues
import android.database.Cursor
import android.util.Log


data class BasisRVModell(val id:Int = -1, var feld1:String = "feld eins") {
    companion object {
        const val FELD_BASIS_ID:String = "base_id"
        const val FELD_BASIS_FELD1:String = "base_feld1"
    }
}

//want id to be immutable. how to do this with primary and secondary constructors?

data class LadenModell(var id:Long = -1, var name:String = "Ihr Laden"){
    //@JvmField annotation also can indicate static field
    companion object {
        const val FELD_LADEN_ID:String = "laden_id"
        const val FELD_LADEN_NAME:String = "laden_name"
        private const val CLASS_LOG_TAG:String = "LadenModell data class"
    }

    constructor(cursor: Cursor):this() {
        Log.i(AppKonstante.APP_LOG_TAG, CLASS_LOG_TAG + " Laden aus cursor erstellen")
        try {
            this.id = cursor.getLong(0)
            this.name = cursor.getString(1)
        } catch (ex: Exception) {
            this.id = -1
            this.name = "fehler"
//            LadenModell(-1,"fehler")
            //redundant, given default values
        }

    }
    fun contentValues(): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(FELD_LADEN_NAME, name)
        return contentValues
    }

}

data class WarenModell(val id:Long = -1, var laden:String = "Ihr Laden", var name:String = "Ihre Ware", var menge:Int = 0) {
    companion object {
        const val FELD_WAREN_ID:String = "waren_id"
        const val FELD_WAREN_LADEN:String = "waren_laden"
        const val FELD_WAREN_NAME:String = "waren_name"
        const val FELD_WAREN_MENGE:String = "waren_menge"
        private const val CLASS_LOG_TAG:String = "WarenModell data class"
    }

}

class SharedViewModel: ViewModel() {
    val ladenId = MutableLiveData<Long>()

}