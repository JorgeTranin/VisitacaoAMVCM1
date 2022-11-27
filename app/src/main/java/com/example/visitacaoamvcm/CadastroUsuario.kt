package com.example.visitacaoamvcm

//import Business.mCategoriadeVisitantes
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.visitacaoamvcm.Business.mCategoriadeVisitantes
import com.example.visitacaoamvcm.databinding.ActivityCadastroUsuarioBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cadastro_usuario.*

class CadastroUsuario : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityCadastroUsuarioBinding
    var categoria: mCategoriadeVisitantes? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Pegando as informações passadas pela tela de Pesquisa de Visitantes e passando para a variavel categoria
        categoria = intent.getParcelableExtra("categoriaNome")

        // se caso categoria vier com algum nome selecionado para auteração na tela de pesquisa de visitantes,
        // irá preencher nos campos determinados as informações do visitante
        if (categoria != null) {
            supportActionBar?.title = categoria?.nome
            edit_Text_NomeCompleto.hint = categoria?.nome.toString()
            edit_Text_Documento.hint = categoria?.id.toString()
            //Toast.makeText(this, categoria?.nome.toString() + categoria?.id, Toast.LENGTH_LONG).show()
        }


        binding.btnSalvarCadastro.setOnClickListener {
            val NomeVisitante = binding.editTextNomeCompleto.text.toString()
            val Documento = binding.editTextDocumento.text.toString()
            val dataDeNascimento = binding.editTextDatadeNascimento.text.toString()
            val Endereco = binding.editTextEndereOAtual.text.toString()


            //Validação para ver se os campos estão vazios

            if (NomeVisitante.isEmpty() || Documento.isEmpty() || dataDeNascimento.isEmpty() || Endereco.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                //chamada da função do salvamento no DB passando os paramentros do visitante
                salvarVisitante(NomeVisitante, Documento, dataDeNascimento, Endereco)
            }

        }


    }

    //Função responsavel por salvar no DB
    private fun salvarVisitante(
        NomeVisitante: String,
        Documento: String,
        dataDeNascimento: String,
        Endereco: String
    ) {
        val mapVisitantes = hashMapOf(
            "NomeVisitante" to NomeVisitante,
            "Documento" to Documento,
            "dataDeNascimento" to dataDeNascimento,
            "Endereco" to Endereco
        )
        db.collection("Visitantes").document(NomeVisitante).set(mapVisitantes)
            .addOnCompleteListener { tarefa ->
                if (tarefa.isSuccessful) {
                    Toast.makeText(this, "Cadastro de Visitante Salvo", Toast.LENGTH_LONG).show()
                    limparCampos()
                }

            }.addOnFailureListener {
            Toast.makeText(this, "Erro ao Cadastrar", Toast.LENGTH_LONG).show()
        }
    }
    //Função para limpar os campos quando for Salvo no DB
    private fun limparCampos(){
        binding.editTextDocumento.setText("")
        binding.editTextEndereOAtual.setText("")
        binding.editTextDatadeNascimento.setText("")
        binding.editTextNomeCompleto.setText("")
    }
}