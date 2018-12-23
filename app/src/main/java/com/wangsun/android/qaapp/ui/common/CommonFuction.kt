package com.wangsun.android.qaapp.ui.common

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.wangsun.android.qaapp.R

class CommonFuction {

    companion object {
        fun showToast(context: Context?,input: String){
            Toast.makeText(context,input,Toast.LENGTH_LONG).show()
        }
    }


    class CustomProgress(context: Context,layoutInflater: LayoutInflater) {
        var alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
                .setView(layoutInflater.inflate(R.layout.progress_bar, null))
                .setCancelable(false)
        var dialog: AlertDialog

        init {
            dialog = alertDialog.create()
        }
    }

    /*
           id_progress.animate()
                                .translationY(id_progress.getHeight().toFloat())
                                .alpha(0.0f)
                                .setDuration(300)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        super.onAnimationEnd(animation)
                                        id_progress.setVisibility(View.GONE)
                                    }
                                })
     */
}