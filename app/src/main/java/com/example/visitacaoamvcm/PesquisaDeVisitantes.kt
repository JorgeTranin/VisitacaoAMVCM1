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
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_pesquisa_de_visitantes.*

class PesquisaDeVisitantes : AppCompatActivity(), AdapterRecyclerviewCategoria.ClickCategoria,
    AdapterRecyclerviewCategoria.UltimoItemExibindoRecyclerView {
    private lateinit var binding: ActivityPesquisaDeVisitantesBinding
    private var db: FirebaseFirestore? = null

    private var reference: CollectionReference? = null
    var searchView: SearchView? = null

    var AdapterRecyclerviewCategoria: AdapterRecyclerviewCategoria? = null

    //Inicialização da minha lista que irá conter meus cadastros de Visitantes
    var categorias: ArrayList<mCategoriadeVisitantes> = ArrayList()

    var proximoQuery: Query? = null

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

/*
        val teteu = hashMapOf("nome" to "teteu", "id" to 10)
        reference!!.document("10").set(teteu)
        val joao = hashMapOf("nome" to "garcia", "id" to 11)
        reference!!.document("11").set(joao)
        val pedrp = hashMapOf("nome" to "tranin", "id" to 12)
        reference!!.document("12").set(pedrp)
        val kaio = hashMapOf("nome" to "kaio", "id" to 13)
        reference!!.document("13").set(kaio)
        val pedrin = hashMapOf("nome" to "pedrin", "id" to 14)
        reference!!.document("14").set(pedrin)
        val angelo = hashMapOf("nome" to "angelo", "id" to 15)
        reference!!.document("15").set(angelo)
*/

        // Passando para o adaptador quais itens ele irá trabalhar, responsavel por fazer o gerenciamento da lista
        AdapterRecyclerviewCategoria = AdapterRecyclerviewCategoria(this, categorias, this, this)

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

    // metodo que determina ultimo item exibido
    override fun ultimoItemExibindoRecyclerView(isExibindo: Boolean) {
        ExibirMaisItensDB()
        //Toast.makeText(this,"Ultimo", Toast.LENGTH_LONG).show()
    }


    //----------------------------------------------------------Função para ler dados FireBase------------------------------------------------------------
    fun ExibirPrimeirosItensDB() {


        val query = db!!.collection("Categorias").orderBy("nome").limit(5)

        query.get().addOnSuccessListener { documentos ->

            //variavel para guardar o ultimo documento, para poder passar na função ExibirMaisItensDB()
            val ultimoNomeApresentado = documentos.documents[documentos.size() - 1]
            proximoQuery =
                db!!.collection("Categorias").orderBy("nome").startAfter(ultimoNomeApresentado)
                    .limit(3)

            for (documento in documentos) {

                val categoria = documento.toObject(mCategoriadeVisitantes::class.java)

                categorias.add(categoria)
            }
            AdapterRecyclerviewCategoria?.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(this, "erro", Toast.LENGTH_LONG).show()
        }

    }

    //----------------------------------------------------------Função para Exibir mais itens do DB------------------------------------------------------------
    private fun ExibirMaisItensDB() {

        // Usando o startAfter para começar a exibir depois dos itens apresentados na função ExibirPrimeirosItensDB,
        // para não repetir os mesmos itens que vieram na primeira consulta


        proximoQuery!!.get().addOnSuccessListener { documentos ->

            if (documentos.size() > 0) {

                //variavel para guardar o ultimo documento, para poder passar na função ExibirMaisItensDB()
                val ultimoNomeApresentado = documentos.documents[documentos.size() - 1]
                proximoQuery =
                    db!!.collection("Categorias").orderBy("nome").startAfter(ultimoNomeApresentado)
                        .limit(3)

                for (documento in documentos) {

                    val categoria = documento.toObject(mCategoriadeVisitantes::class.java)

                    categorias.add(categoria)
                }
                AdapterRecyclerviewCategoria?.notifyDataSetChanged()
            } else {
                //Toast.makeText(this, "Não existe Mais Cadastros", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "erro", Toast.LENGTH_LONG).show()
        }

    }


}


