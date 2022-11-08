package com.example.visitacaoamvcm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class VisitantesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visitantes)

    }
    fun onBtnCadastro (view: View){
        val intent = Intent(this, CadastroUsuario::class.java)
        startActivity(intent)
    }
}