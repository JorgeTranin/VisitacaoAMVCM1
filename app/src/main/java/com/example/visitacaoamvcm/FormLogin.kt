package com.example.visitacaoamvcm

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.visitacaoamvcm.databinding.ActivityFormLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class FormLogin : AppCompatActivity() {
    private lateinit var bilding: ActivityFormLoginBinding
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        bilding = ActivityFormLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bilding.root)

        bilding.btnEntrarUsuario.setOnClickListener { View ->
            //Captura dos campos do layout XML
            val email = bilding.etEmail.text.toString()
            val senha = bilding.etSenha.text.toString()

            // Verificação para ver se está em branco os campos
            if (email.isEmpty() || senha.isEmpty()) {
                val snackbar = Snackbar.make(
                    View,
                    "Preencha os campos de Email e senha corretamente",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()

            } else {
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { autenticacao ->
                        if (autenticacao.isSuccessful) {
                            navegarTelaPrincipal()
                        }

                    }.addOnFailureListener {

                }
            }
        }


        // criar uma intenção no meu TextView de cadastro para direcionar para tela de cadastro

        bilding.txtCadastro.setOnClickListener {
            val intent = Intent(this, FormCadastro::class.java)
            startActivity(intent)
        }


    }

    private fun navegarTelaPrincipal() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}