package com.example.visitacaoamvcm

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.visitacaoamvcm.databinding.ActivityFormLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

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

                    }.addOnFailureListener { excecoes ->
                        val mensagemDeErro = when (excecoes) {
                            // Metodo para verificar se a senha esta invalida
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha com no minimo 6 caracteres"

                            //metodo para ver se o email esta invalido
                            is FirebaseAuthInvalidCredentialsException -> "Digite um email valido"

                            //Usuario já cadastrado

                            is FirebaseAuthUserCollisionException -> "Usuario já cadastrado"

                            //App sem acesso a internet
                            is FirebaseNetworkException -> "Sem conexão a internet"

                            else -> "Erro ao Cadastrar usuario"
                        }
                        val snackbar = Snackbar.make(View, mensagemDeErro, Snackbar.LENGTH_SHORT)
                        snackbar.setBackgroundTint(Color.RED)
                        snackbar.show()

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
    //Verificação para caso o usuário já esteja logado
    // onde currentUser mostra qual usuario esta logado no momento


    override fun onStart() {
        super.onStart()
        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        // ira ver se o usuario não for null então irá navegar para tela principal
        if (usuarioAtual != null) {
            navegarTelaPrincipal()
        }
    }
}