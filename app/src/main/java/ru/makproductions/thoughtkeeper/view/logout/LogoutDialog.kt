package ru.makproductions.thoughtkeeper.view.logout

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

class LogoutDialog : DialogFragment() {

    companion object {
        val TAG = "{${LogoutDialog::class.java.name} TAG}"
        fun getInstance() = LogoutDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = AlertDialog.Builder(context)
        .setTitle("Logout")
        .setMessage("Are you sure you want to leave?")
        .setPositiveButton("Yes") { dialog, which -> (activity as? LogoutListener?).let { it?.onLogout() } }
        .setNegativeButton("No") { dialog, which -> dismiss() }
        .create()

    interface LogoutListener {
        fun onLogout()
    }
}