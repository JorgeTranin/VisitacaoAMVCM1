package com.example.visitacaoamvcm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
    //Função responsavel por escutar eventos de clicks
    fun onBtnVisitantes(view: View) {
        // setar variavel intent          esta instancia  e pra onde vai
        val intent = Intent(this, PesquisaDeVisitantes::class.java)
        //starta a ação
        startActivity(intent)
    }

    fun onBtnRegistrodevisitacao(view: View) {
        val intent = Intent(this, RegistrosdeVisitantes::class.java)
        startActivity(intent)
    }

    fun onBtnDeslogar(view: View) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, FormLogin::class.java)
        startActivity(intent)
    }


}