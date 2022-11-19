package com.example.visitacaoamvcm

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.visitacaoamvcm.databinding.ActivityFormCadastroBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class FormCadastro : AppCompatActivity() {
    private lateinit var bilding: ActivityFormCadastroBinding
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        bilding = ActivityFormCadastroBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bilding.root)

        bilding.btnCadastrarUsuario.setOnClickListener { View ->
            val email = bilding.etEmail.text.toString()
            val senha = bilding.etSenha.text.toString()

            // Se o email estiver em branco e a senha tbm, ira aparecer uma snackbar avisando para preencher todos os campos
            if (email.isEmpty() || senha.isEmpty()){
                val snackbar = Snackbar.make(View, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()


                // Caso esteja preenchido ira executar o cadastro no banco de dados
            }else{
                // variavel local auth chamando o metodo de criação do cadastro(passando email e senha)
                // e se der tudo certo entrara como cadastro e aparecera para o usuario
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener { cadastro ->
                    if (cadastro.isSuccessful) {
                        val snackbar = Snackbar.make(
                            View,
                            "Usuario Registrado com Sucesso!",
                            Snackbar.LENGTH_SHORT
                        )
                        snackbar.setBackgroundTint(Color.GREEN)
                        snackbar.show()
                        bilding.etEmail.setText("")
                        bilding.etSenha.setText("")


                        //Função para navegar para tela de login mas esta indo muito rapido, ajustar o tempo de execução
                        //navegarTelaLogin()
                    }

                }
                    // Caso tenha alguma exceção ira ser tratada no bloco de codigo .addOnFailureListener { Exception -> }
                    .addOnFailureListener { Exception ->
                    // validações senha do usuario com menos de 6 caracteres, email invalido, usuario já cadastrado, app sem acesso a internet
                    //Variavel que armazenara a mensagem de erro com um bloco when

                    val mensagemDeErro = when(Exception){

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

    }

    private fun navegarTelaLogin() {
        val intent = Intent(this, FormLogin::class.java)
        startActivity(intent)
        finish()
    }
}