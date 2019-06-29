package com.cogimag.michalg.einkaufsliste

import android.content.Context
import android.util.AttributeSet

class WarenRvItemLayout(context: Context, attributeSet: AttributeSet):BasisRvItemLayout(context, attributeSet) {
    override fun schichtenBestimmen() {
        hintergrund = findViewById(R.id.waren_rv_layout_hintergrund)
        vordergrund = findViewById(R.id.waren_rv_layout_vordergrund)
    }

    override fun hintergrundBreiteMessen() {
        hintergrund.layoutParams.width = 0
        for (i in 0 until hintergrund.childCount) hintergrund.layoutParams.width += hintergrund.getChildAt(i).minimumWidth
    }
}