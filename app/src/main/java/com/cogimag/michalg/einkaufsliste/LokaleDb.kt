package com.cogimag.michalg.einkaufsliste


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class LokaleDb(val context:Context) {
    companion object {
        //die Datenbank
        private const val KLASSE_LOG_TAG = "LokaleDb"
        private const val LOCAL_DB_NAME = "EinkaufenDatenbank.db"
        private const val LOCAL_DB_VERSION = 1

        //change log

        //die Läden
        private const val LADEN_TBL_NAME = "lokalLadenTbl"
        private val PROJECTION_LADEN_ID_FELDER = arrayOf(LadenModell.FELD_LADEN_ID)
        private val PROJECTION_ALLE_LADEN_FELDER = arrayOf(LadenModell.FELD_LADEN_ID, LadenModell.FELD_LADEN_NAME)


        //die Waren
        private const val WAREN_TBL_NAME = "lokalWarenTbl"
        private val PROJECTION_ALLE_WAREN_FELDER =
            arrayOf(WarenModell.FELD_WAREN_ID + " _id", WarenModell.FELD_WAREN_LADEN, WarenModell.FELD_WAREN_NAME)
    }



    //************
    // catch android.database.sqlite.SQLiteException
    //************

    //Laden Änderungen: CRUD
    //create
    fun insertLadenRecord(ladenModell: LadenModell): Long {
        var newRowId: Long = -1
        val dbHelper = SqliteDbHelper(context)
        dbHelper.writableDatabase.use {
            newRowId = it.insert(LADEN_TBL_NAME, null, ladenModell.contentValues())
            it.close()
        }
        dbHelper.close()
        return newRowId
    }
    //read one
    fun einenLaden(id:Long): LadenModell {
        var laden:LadenModell = LadenModell()
        val dbHelper = SqliteDbHelper(context)

        try {
            dbHelper.readableDatabase.use {
                val sqlWhere = LadenModell.FELD_LADEN_ID + "=?"
                val sqlWhereArgs = arrayOf(id.toString())
                val cursor:Cursor = it.query(LADEN_TBL_NAME, PROJECTION_ALLE_LADEN_FELDER,
                    sqlWhere, sqlWhereArgs, null, null, null)
                if (cursor.moveToFirst()) {
                    Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " Laden id " + cursor.getLong(0) + " holen")
                    laden = LadenModell(cursor)
    //                cursor.moveToNext()
                }
                cursor.close()
                it.close()
            }
            dbHelper.close()
        } catch (ex: Exception) {
            Log.e(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + "fehler einen Laden ablesen " + ex.message)
        }
        return laden
    }

    //read all
    fun alleLaden():ArrayList<LadenModell> {
        val ladenModellArrayList:ArrayList<LadenModell> = ArrayList()
        val dbHelper = SqliteDbHelper(context)

        try {
            dbHelper.readableDatabase.use {
                val cursor:Cursor =
                    it.query(LADEN_TBL_NAME, PROJECTION_ALLE_LADEN_FELDER,
                        null, null, null, null, null)
                if (cursor.moveToFirst()) {
                    //SO 37051197
                    //use get column index
                    do {
                        val ladenModell:LadenModell = LadenModell(cursor.getLong(0), cursor.getString(1))
                        ladenModellArrayList.add(ladenModell)
                    }
                    while (cursor.moveToNext())
                }
                cursor.close()
                it.close()
            }
            dbHelper.close()
        } catch (ex: Exception) {
            Log.e(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + "fehler alle Läden ablesen " + ex.message)
        }
//        Log.i(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " alle laden anzahl " + ladenModellArrayList.size)
        return ladenModellArrayList
    }

    //update
    fun ladenVerarbeiten(ladenModell: LadenModell):Int {
        var rowsAffected:Int = -1
        val dbHelper = SqliteDbHelper(context)
        try {
            dbHelper.writableDatabase.use {
                val ladenContentValues:ContentValues = ContentValues()
                ladenContentValues.put(LadenModell.FELD_LADEN_NAME, ladenModell.name)
                val sqlWhere = LadenModell.FELD_LADEN_ID + "=?"
                val sqlWhereArgs = arrayOf(ladenModell.id.toString())
                rowsAffected = it.update(LADEN_TBL_NAME, ladenContentValues,
                    sqlWhere, sqlWhereArgs)
                it.close()
            }
            dbHelper.close()
        }
        catch (ex:java.lang.Exception) {
            Log.e(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + "fehler laden verarbeiten " + ex.message)
        }

        return rowsAffected
    }
    //delete
    fun deleteLadenRecord(ladenId:Long):Int {
        var rowAffected:Int = -1
        val dbHelper = SqliteDbHelper(context)
        try {
            dbHelper.writableDatabase.use {
                val sqlWhere:String = LadenModell.FELD_LADEN_ID + "=?"
                val sqlWhereArgs = Array<String>(1){ladenId.toString()}
                    rowAffected = it.delete(LADEN_TBL_NAME, sqlWhere, sqlWhereArgs)
            }
        }
        catch (ex:Exception) {
            Log.e(AppKonstante.APP_LOG_TAG, KLASSE_LOG_TAG + " Fehler Laden löschen " + ex.message)
        }
        return rowAffected

    }

    //Waren Änderungen
    //create

    //read all where laden = param

    //read one where id = param

    //update

    //delete


    //db connection class
    private inner class SqliteDbHelper(c: Context) : SQLiteOpenHelper(c, LOCAL_DB_NAME, null, LOCAL_DB_VERSION) {

        private val CLASS_LOG_TAG = "SqliteDbHelper"

        //die Läden
        private val SQL_CREATE_LADEN_TBL = "CREATE TABLE $LADEN_TBL_NAME " +
                "(${LadenModell.FELD_LADEN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${LadenModell.FELD_LADEN_NAME} TEXT NOT NULL);"

        private val SQL_DROP_LADEN_TBL = "DROP TABLE IF EXISTS $LADEN_TBL_NAME"
//        private val SQL_CREATE_LADEN_TBL = "CREATE TABLE " + LADEN_TBL_NAME +
//                " (" + LadenModell.FELD_LADEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                LadenModell.FELD_LADEN_NAME + " TEXT NOT NULL);"


        //die Waren
        private val SQL_CREATE_WAREN_TBL = "CREATE TABLE $WAREN_TBL_NAME " +
                "(${WarenModell.FELD_WAREN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${WarenModell.FELD_WAREN_LADEN} INTEGER NOT NULL, " +
                "${WarenModell.FELD_WAREN_NAME} TEXT NOT NULL, " +
                "${WarenModell.FELD_WAREN_MENGE} INTEGER, " +
                "FOREIGN KEY (${WarenModell.FELD_WAREN_LADEN}) " +
                "REFERENCES $LADEN_TBL_NAME (${LadenModell.FELD_LADEN_ID}));"
        private val SQL_DROP_WAREN_TBL = "DROP TABLE IF EXISTS $WAREN_TBL_NAME"

        override fun onCreate(db: SQLiteDatabase) {
            Log.i(AppKonstante.APP_LOG_TAG ,CLASS_LOG_TAG +"create db on path " + db.path)
            db.execSQL(SQL_CREATE_WAREN_TBL)
            db.execSQL(SQL_CREATE_LADEN_TBL)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            Log.i(AppKonstante.APP_LOG_TAG, CLASS_LOG_TAG +" upgrade db old version: $oldVersion new version: $newVersion path: ${db.path}")
            db.execSQL(SQL_DROP_WAREN_TBL)

            db.execSQL(SQL_DROP_LADEN_TBL)

            onCreate(db)
        }
    }



}