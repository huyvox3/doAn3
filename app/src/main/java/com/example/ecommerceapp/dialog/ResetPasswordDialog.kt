package com.example.ecommerceapp.dialog

import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior


import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomSheetDialog(
    onSendClick: (String) -> Unit
){
    val dialog = BottomSheetDialog(requireContext(),R.style.DialogStyle)

    val view = layoutInflater.inflate(R.layout.reset_password_dialog,null)
    dialog.setContentView(view)

    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val emailEt = view.findViewById<EditText>(R.id.resetCodeEt)
    val sendBtn = view.findViewById<AppCompatButton>(R.id.sendBtn)
    val cancelBtn = view.findViewById<AppCompatButton>(R.id.cancelBtn)

    sendBtn.setOnClickListener{
        val email = emailEt.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }


    cancelBtn.setOnClickListener{
        dialog.dismiss()
    }
}