package com.cogimag.michalg.einkaufsliste

import android.content.Context

import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.*

abstract class BasisRvItemLayout(context: Context, attributeSet: AttributeSet):FrameLayout(context,attributeSet)  {
    companion object {
        val CLASS_LOG_TAG = "BasisRvItemLayout"
    }


    val gestureDetector:GestureDetector

    lateinit var hintergrund:RelativeLayout //view group?
    lateinit var vordergrund:RelativeLayout //view group?

    init {
//        schichtenBestimmen()
//        hintergrundBreiteMessen()
        gestureDetector = GestureDetector(context, RvGestureDetector(this))

    }

    /**
     * Hier knüpft man die Veränderlichen <code>hintergrund</code> und <code>vordergrund</code>
     * an die jeweiligen Views an. Die vorgeschlagene Methode lautet
     * hintergrund = findViewById(R.id.my_rv_layout_hintergrund)
     */
    abstract fun schichtenBestimmen()

    /**
     * Die genaue Breite des Hintergrunds muss bekannt werden, um Touch Events
     * richtig zu behandeln. Die vorgeschlagene Methode zählt die Breiten der
     * einzelnen Tasten (Löschen, Verarbeiten, usw.) zusammen.
     */
    abstract fun hintergrundBreiteMessen()

    //override only this fcn in child class
//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        hintergrund = findViewById(R.id.base_rv_layout_hintergrund)
//        vordergrund = findViewById(R.id.base_rv_layout_vordergrund)
//        //so 12343376 set width through layout params
//        hintergrund.layoutParams.width = 0
//        for (i in 0 until hintergrund.childCount) {
//            hintergrund.layoutParams.width += hintergrund.getChildAt(i).minimumWidth
////            Log.i(AppConstants.APP_LOG_TAG, classLogTag +
////                    " Kind " + i + " content description " +  hintergrund.getChildAt(i).contentDescription +
////                    " min width " + hintergrund.getChildAt(i).minimumWidth)
//            //width and meas width are 0
//        }

//    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        schichtenBestimmen()
        hintergrundBreiteMessen()
    }
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var preventClickOnHintergrund:Boolean = true
        if (ev != null) preventClickOnHintergrund =  !( hintergrundSichtbar() and tapOnHintergrund(ev) )

//        Log.i(AppConstants.APP_LOG_TAG, classLogTag + " onInterceptTouchEvent prevent = " + preventClickOnHintergrund)
        val preventClickOnVordergrund:Boolean = hintergrundSichtbar() // or (ev!!.action == MotionEvent.ACTION_MOVE)
//        Log.i(AppKonstante.APP_LOG_TAG, "$CLASS_LOG_TAG intercept touch event of type " + MotionEvent.actionToString(ev.action))
//        Log.i(AppKonstante.APP_LOG_TAG, "$CLASS_LOG_TAG intercept touch event returns " + (preventClickOnHintergrund ))
                //and !preventClickOnVordergrund and preventClickOnVordergrund

        return preventClickOnHintergrund //and preventClickOnVordergrund
        //false allows event to pass to child's touch handler
        //true sends event to onTouchEvent below
//        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        Log.i(AppKonstante.APP_LOG_TAG, CLASS_LOG_TAG + " onTouchEvent type " + MotionEvent.actionToString(event!!.action) )
        return gestureDetector.onTouchEvent(event)
    }

    fun singleTap(event: MotionEvent) {
//        Log.i(AppKonstante.APP_LOG_TAG, "$CLASS_LOG_TAG single tap ")
        if (!hintergrundSichtbar()) vordergrund.performClick()
    }

    fun swipe(touchDown:MotionEvent, touchUp:MotionEvent) {
//        val buttonContainer = findViewById<ImageView>(R.id.perez_rv_icon_supprimer)
        val vordergrundVerschiebung:Int =
            Math.max(touchUp.x.toInt(),(vordergrund.measuredWidth - hintergrund.measuredWidth))
//        Log.i(AppConstants.APP_LOG_TAG, classLogTag + "fun swipe " + "hintergrund.measuredWidth " + hintergrund.measuredWidth)
//        Log.i(AppConstants.APP_LOG_TAG, classLogTag + "fun swipe " + "hintergrund.width " + hintergrund.width)
        //Math.max(touchUp.x.toInt(),(vordergrund.measuredWidth - btnDelete.width - btnEdit.width))
        //vordergrund width changes as layout is moved. measured width is the constant, original width
        vordergrund.right = vordergrundVerschiebung

        //add tolerance. when to use dx instead of x? when touch to left of hintergrund, open is abrupt
        if (touchUp.x < touchDown.x) {
            //swipe left. impose limit
//            vordergrund.right = vordergrund.measuredWidth - hintergrundBreiteMessen

        }
        else {
            //swipe right
            //if x <
        }
//        if ((touchUp.x -touchDown.x) >= (btnEdit.width + btnDelete.width)) {
        if ((touchUp.x -touchDown.x) >= hintergrund.measuredWidth) {
            //swipe right.
            //use dx
            vordergrund.right = vordergrund.measuredWidth
        }


//        Log.i(AppConstants.APP_LOG_TAG, classLogTag + " swipe vordergrund. x Wert " + vordergrundVerschiebung)
    }

    fun fling(touchDown:MotionEvent, touchUp:MotionEvent) {
//        val vordergrund = findViewById<RelativeLayout>(R.id.perez_rv_vordergrund)

        if (touchDown.x > touchUp.x) {
            //fling to left
//            Log.i(AppConstants.APP_LOG_TAG, classLogTag + " fling vordergrund links")
//            val minWidth = vordergrund.findViewById<RelativeLayout>(R.id.perez_rv_vordergrund).width
//            vordergrund.right = vordergrund.measuredWidth - btnDelete.width - btnEdit.width
            vordergrund.right = vordergrund.measuredWidth - hintergrund.measuredWidth
        }
        else {
            //fling to right (close)
//            Log.i(AppConstants.APP_LOG_TAG, classLogTag + " fling vordergrund rechts")
            vordergrund.right = vordergrund.measuredWidth
        }



    }


    fun hintergrundSichtbar():Boolean {
//        return vordergrund.right < vordergrund.measuredWidth - btnDelete.width - btnEdit.width + 20
//        Log.i(AppKonstante.APP_LOG_TAG, "$CLASS_LOG_TAG hintergrund sichtbar = " +
//                (vordergrund.right < vordergrund.measuredWidth - hintergrund.measuredWidth + 20))
        return vordergrund.right < vordergrund.measuredWidth - hintergrund.measuredWidth + 20
    }
    fun tapOnHintergrund(event:MotionEvent):Boolean {
//        return (event.x <= btnDelete.right) and (event.x >= btnEdit.left)
        return (event.x <= hintergrund.right) and (event.x >= hintergrund.left)
//        and (event.action == MotionEvent.ACTION_UP)
    }






}