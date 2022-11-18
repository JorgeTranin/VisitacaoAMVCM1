package com.example.visitacaoamvcm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.visitacaoamvcm.databinding.ActivityCadastroUsuarioBinding
import com.google.firebase.firestore.FirebaseFirestore

class CadastroUsuario : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityCadastroUsuarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSalvarCadastro.setOnClickListener {
            val NomeVisitante = binding.editTextNomeCompleto.text.toString()
            val Documento = binding.editTextDocumento.text.toString()
            val dataDeNascimento = binding.editTextDatadeNascimento.text.toString()
            val Endereco = binding.editTextEndereOAtual.text.toString()


            salvarNoticia(NomeVisitante, Documento, dataDeNascimento, Endereco)
        }



    }

    private fun salvarNoticia(NomeVisitante:String,Documento:String, dataDeNascimento:String, Endereco:String) {
        val mapVisitantes = hashMapOf(
            "NomeVisitante" to NomeVisitante,
            "Documento" to Documento,
            "dataDeNascimento" to dataDeNascimento,
            "Endereco" to Endereco
        )
        db.collection("Visitantes").document("Visitante").set()
    }
}