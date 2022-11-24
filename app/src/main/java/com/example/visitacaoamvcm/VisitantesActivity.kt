package com.example.visitacaoamvcm

import Business.InfoCadastroDeVisitantes
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.visitacaoamvcm.databinding.ActivityVisitantesBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class VisitantesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVisitantesBinding
    private var db: FirebaseFirestore? = null

    private var reference: CollectionReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitantesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Instanciamento da minha variavel db com o firebase
        db = FirebaseFirestore.getInstance()

        //Variavel que fica com a instancia de DB
        reference = db!!.collection("Visitantes")
        BuscarListaVisitantes()
    }

    // Função responsavel por navegar para a tela CadastroUsuario
    fun onBtnCadastro(view: View) {
        val intent = Intent(this, CadastroUsuario::class.java)
        startActivity(intent)
    }


    fun BuscarListaVisitantes() {


        // Variavel que irá armazenar a lista de Visitantes
        val listaDeVisitantes: MutableList<InfoCadastroDeVisitantes> =
            ArrayList<InfoCadastroDeVisitantes>();

        reference!!.addSnapshotListener { visitantes, error ->
            if (error != null) {
                //caso aja algum erro de comunicação com o DB
                Toast.makeText(
                    this,
                    "Erro com a comunicação com servidor {${error.message.toString()}} ",
                    Toast.LENGTH_LONG
                ).show()

            }
            //Responsavel por armazenar os documentos

            else if (visitantes != null)

            // o for vai percorrer todos os documentos dentro da variavel Visitantes
                for (documento in visitantes.documentChanges) {

                    // o when irá armazerar o tipo de auteração que aconteceu na minha coleção com o .documentChanges(que retorna a auteração feita nos documentos)
                    when (documento.type) {

                        // se o documento for adicionado ele entra aqui
                        DocumentChange.Type.ADDED -> {

                            val key = documento.document.id

                            val visitante =
                                documento.document.toObject(InfoCadastroDeVisitantes::class.java)

                            listaDeVisitantes.add(visitante)

                        }//Se o documento for auterado ele cai aqui
                        DocumentChange.Type.MODIFIED -> {

                            val key = documento.document.id

                            val visitante =
                                documento.document.toObject(InfoCadastroDeVisitantes::class.java)
                            listaDeVisitantes.add(visitante)
                        }//se o documento for removido ele entra aqui
                        DocumentChange.Type.REMOVED -> {


                        }

                    }//Fim do when

                }//Fim do for


            else {
                //Caso a pasta esteja vazia e nada for encontrado
                Toast.makeText(this, "Esta pasta esta vazia ou não existe ", Toast.LENGTH_LONG)
                    .show()
            }
            binding.tvVisitante1.setText(listaDeVisitantes.get(0).NomeVisitante)
            binding.tvVisitante2.setText(listaDeVisitantes.get(1).NomeVisitante)
            binding.tvVisitante3.setText(listaDeVisitantes.get(2).NomeVisitante)
            binding.tvVisitante4.setText(listaDeVisitantes.get(3).NomeVisitante)
            binding.tvVisitante5.setText(listaDeVisitantes.get(4).NomeVisitante)


        }


    }
}