package com.example.visitacaoamvcm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.visitacaoamvcm.databinding.ActivityVisitantesBinding
import com.google.firebase.firestore.FirebaseFirestore

class VisitantesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVisitantesBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitantesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recuperarNoticias()

    }
    fun onBtnCadastro (view: View){
        val intent = Intent(this, CadastroUsuario::class.java)
        startActivity(intent)
    }
    //Função para recuperar os dados no DB
    private fun recuperarNoticias(){
        // primeiro minha variavel db depois falo qual collecion quero acessar, depois falar qual documento quero acessar(seria qual visitante)

        db.collection("Visitantes").document("antonio").get()
                //O metodo add recupera o meu documento completo, e seus campos
            .addOnCompleteListener {documento ->
                //Se o documento for recuperado, vai executar o codigo
                if (documento.isSuccessful){
                    //Variaveis responsaveis por armazenar cada campo do meu visitante, dentro do get tem que passar o nome da chave do banco de dados.
                    val NomedoVisitante = documento.result.get("NomeVisitante").toString()
                    val Documento = documento.result.get("Documento").toString()
                    val endereco = documento.result.get("Endereco").toString()
                    val Nascimento = documento.result.get("dataDeNascimento").toString()

                    //Passando para meu TextView o valor recebido do servidor
                    binding.tvVisitante1.text = NomedoVisitante

                }
        }
    }
}