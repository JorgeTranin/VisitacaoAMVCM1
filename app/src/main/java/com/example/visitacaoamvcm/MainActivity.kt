package com.example.visitacaoamvcm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
    fun onBtnVisitantes (view: View){
        val intent = Intent(this, VisitantesActivity::class.java)
        startActivity(intent)
    }
    fun onBtnRegistrodevisitacao (view: View){
        val intent = Intent(this, RegistrosdeVisitantes::class.java)
        startActivity(intent)
    }


}