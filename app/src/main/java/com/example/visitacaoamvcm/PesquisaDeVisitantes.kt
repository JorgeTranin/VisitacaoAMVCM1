package com.example.visitacaoamvcm

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.visitacaoamvcm.Business.AdapterRecyclerviewCategoria
import com.example.visitacaoamvcm.Business.mCategoriadeVisitantes
import com.example.visitacaoamvcm.databinding.ActivityPesquisaDeVisitantesBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_pesquisa_de_visitantes.*

class PesquisaDeVisitantes : AppCompatActivity(), AdapterRecyclerviewCategoria.ClickCategoria {
    private lateinit var binding: ActivityPesquisaDeVisitantesBinding
    private var db: FirebaseFirestore? = null

    private var reference: CollectionReference? = null
    var searchView: SearchView? = null

    var AdapterRecyclerviewCategoria: AdapterRecyclerviewCategoria? = null

    //Inicialização da minha lista que irá conter meus cadastros de Visitantes
    var categorias: ArrayList<mCategoriadeVisitantes> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesquisaDeVisitantesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCadastroNovoVisitante.setOnClickListener {
            val intent = Intent(this, CadastroUsuario::class.java)
            startActivity(intent)
        }

        ///Inicialização variaveis do Banco de dados
        db = FirebaseFirestore.getInstance()
        reference = db?.collection("Categorias")

        ExibirPrimeirosItensDB()
        IniciarRecyclerView()


//-----------------------------------------------------------------Metodos Search para Pesquisa--------------------------------------------------------------------------
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var query = db!!.collection("Categorias").orderBy("nome").startAt(newText)
                    .endAt(newText + "\uf8ff").limit(5)

                query.get().addOnSuccessListener { documentos ->
                    categorias.clear()
                    for (documento in documentos) {

                        val categoria = documento.toObject(mCategoriadeVisitantes::class.java)

                        categorias.add(categoria)
                    }
                    //Comunicar o adaptador dizendo que a variavel categoria foi auterada
                    AdapterRecyclerviewCategoria?.notifyDataSetChanged()
                }.addOnFailureListener {
                    //Toast.makeText(this, "erro", Toast.LENGTH_LONG).show()
                }
                return false
            }


        })


    }


    //----------------------------------------------------------Fim dos Metodos Search para Pesquisa------------------------------------------------------------


    private fun IniciarRecyclerView() {


        //val asasds = hashMapOf("nome" to "Paulo", "id" to 6)
        //reference!!.document("6").set(asasds)
        //val ds = hashMapOf("nome" to "garcia", "id" to 7)
        //reference!!.document("7").set(ds)
        //val ff = hashMapOf("nome" to "tranin", "id" to 8)
        //reference!!.document("8").set(ff)


        // Passando para o adaptador quais itens ele irá trabalhar, responsavel por fazer o gerenciamento da lista
        AdapterRecyclerviewCategoria = AdapterRecyclerviewCategoria(this, categorias, this)

        //Irá determinar como o item vai ser apresentado, podendo apresentar um do lado do outro e diferentes itens
        RecyclerView_listadeVisitantes.layoutManager = LinearLayoutManager(this)

        RecyclerView_listadeVisitantes.adapter = AdapterRecyclerviewCategoria
    }
    //----------------------------------------------------------Função para Click no item da lista------------------------------------------------------------

    override fun clickCategoria(categoria: mCategoriadeVisitantes) {


        val intent = Intent(this, CadastroUsuario::class.java)

        //intenção para mandar para tela de cadastro todas as informações do visitante dentro
        // da variavel categoria
        intent.putExtra("categoriaNome", categoria)
        //Toast.makeText(this, categoria?.nome.toString() + "--" + categoria?.id, Toast.LENGTH_LONG).show()


        startActivity(intent)
    }


    //----------------------------------------------------------Função para ler dados FireBase------------------------------------------------------------
    fun ExibirPrimeirosItensDB() {


        var query = db!!.collection("Categorias").orderBy("nome")

        query.get().addOnSuccessListener { documentos ->

            for (documento in documentos) {

                val categoria = documento.toObject(mCategoriadeVisitantes::class.java)

                categorias.add(categoria)
            }
            AdapterRecyclerviewCategoria?.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(this, "erro", Toast.LENGTH_LONG).show()
        }

    }
}


