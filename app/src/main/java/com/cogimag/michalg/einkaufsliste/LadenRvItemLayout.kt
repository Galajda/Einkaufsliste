package com.cogimag.michalg.einkaufsliste

import android.content.Context
import android.util.AttributeSet

class LadenRvItemLayout(context: Context, attributeSet: AttributeSet):BasisRvItemLayout(context,attributeSet) {

    companion object {
        val CLASS_LOG_TAG = "LadenRvItemLayout"
    }
    override fun schichtenBestimmen() {
        hintergrund = findViewById(R.id.laden_rv_layout_hintergrund)
        vordergrund = findViewById(R.id.laden_rv_layout_vordergrund)
    }

    override fun hintergrundBreiteMessen() {
        hintergrund.layoutParams.width = 0
        for (i in 0 until hintergrund.childCount) hintergrund.layoutParams.width += hintergrund.getChildAt(i).minimumWidth
    }




}