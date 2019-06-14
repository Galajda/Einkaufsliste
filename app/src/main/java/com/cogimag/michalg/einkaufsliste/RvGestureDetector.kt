package com.cogimag.michalg.einkaufsliste

import android.view.GestureDetector
import android.view.MotionEvent

class RvGestureDetector(val itemLayout:BasisRvItemLayout):GestureDetector.SimpleOnGestureListener() {
    companion object {
        private const val CLASS_LOG_TAG:String = "RvGestureDetector"
    }


    override fun onDown(e: MotionEvent?): Boolean {
//        Log.i(AppKonstante.APP_LOG_TAG, CLASS_LOG_TAG + " on down")

        return true //on down must return true in order to allow processing of further gestures
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
//        Log.i(AppKonstante.APP_LOG_TAG, CLASS_LOG_TAG + " onSingleTapUp")
        return true //consume event
    }


    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
//        Log.i(AppKonstante.APP_LOG_TAG, CLASS_LOG_TAG + " on fling")
        //use this to complete a swipe
        itemLayout.fling(e1!!,e2!!)
        return true //consume event
    }



    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        //scroll looks like swipe
        //e1 is touch down point, remains constant throughout swipe
        //e2 is touch up point
//        rvLayoutInstance
        val dist = e2!!.x - e1!!.x
//        Log.i(AppKonstante.APP_LOG_TAG, CLASS_LOG_TAG + " on scroll. e1.x = " + e1.x +
//            " e2.x = " + e2.x + " dist 2-1 = " + dist)
        //range of x ~330 (right end) to 0 or lower (left end)
        itemLayout.swipe(e1, e2)
//        return true //consume event?
        return false
    }


}