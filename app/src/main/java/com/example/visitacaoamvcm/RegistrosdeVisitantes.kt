package com.example.visitacaoamvcm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class RegistrosdeVisitantes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrosde_visitacao)
    }
    fun onBtnRegistrodevisitacao (view: View){
        val intent = Intent(this, Cadastrodevisitacao::class.java)
        startActivity(intent)
    }

}