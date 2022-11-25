package com.example.visitacaoamvcm

import Business.AdapterRecyclerviewCategoria
import Business.mCategoriadeVisitantes
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.visitacaoamvcm.databinding.ActivityPesquisaDeVisitantesBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_pesquisa_de_visitantes.*

class PesquisaDeVisitantes : AppCompatActivity(), AdapterRecyclerviewCategoria.ClickCategoria {
    private lateinit var binding: ActivityPesquisaDeVisitantesBinding
    private var bd: FirebaseFirestore? = null

    private var reference: CollectionReference? = null
    var searchView: SearchView? = null


    var AdapterRecyclerviewCategoria: AdapterRecyclerviewCategoria? = null

    //Inicialização da minha lista que irá conter meus cadastros de Visitantes
    var categorias: ArrayList<mCategoriadeVisitantes> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesquisaDeVisitantesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFireStoreListaVisitantesExibirMais.setOnClickListener {
            Toast.makeText(this, "btnFireStoreListaVisitantesExibirMais.", Toast.LENGTH_LONG).show()
        }

        IniciarRecyclerView()


//-----------------------------------------------------------------Metodos Search para Pesquisa--------------------------------------------------------------------------
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }


        })


    }

    // Função para buscar minha lista no banco de dados
    private fun Visitantes() {

        bd!!.collection("Visitantes").get().addOnSuccessListener { visitantes ->
            for (visitante in visitantes) {
                val dados = visitante.data

                val nome = dados?.get("NomeVisitante").toString()
                val id = visitante.id
                //var teste = visitante.toObject(mCategoriadeVisitantes::class.java)

                val item = mCategoriadeVisitantes(nome, 1)
                categorias.add(item)


            }//Fim do for

        }.addOnFailureListener { erros ->

        }
    }

    private fun IniciarRecyclerView() {


        //val item1 = mCategoriadeVisitantes("jj", 1)
        //val item2 = mCategoriadeVisitantes("gg", 2)

        //categorias.add(item1)
        //categorias.add(item2)


        // Passando para o adaptador quais itens ele irá trabalhar, responsavel por fazer o gerenciamento da lista
        AdapterRecyclerviewCategoria = AdapterRecyclerviewCategoria(this, categorias, this)

        //Irá determinar como o item vai ser apresentado, podendo apresentar um do lado do outro e diferentes itens
        RecyclerView_listadeVisitantes.layoutManager = LinearLayoutManager(this)

        RecyclerView_listadeVisitantes.adapter = AdapterRecyclerviewCategoria
    }

    //Click no item da lista
    override fun clickCategoria(categoria: mCategoriadeVisitantes) {
        val intent = Intent(this, CadastroUsuario::class.java)

        //intenção para mandar para tela de cadastro todas as informações do visitante dentro
        // da variavel categoria

        intent.putExtra("categoriaNome", categoria)


        startActivity(intent)
    }


}


