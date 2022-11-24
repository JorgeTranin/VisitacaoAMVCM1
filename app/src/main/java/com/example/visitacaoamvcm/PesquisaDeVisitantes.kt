package com.example.visitacaoamvcm

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.visitacaoamvcm.databinding.ActivityPesquisaDeVisitantesBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class PesquisaDeVisitantes : AppCompatActivity(), SearchView.OnQueryTextListener,
    SearchView.OnCloseListener {
    private lateinit var binding: ActivityPesquisaDeVisitantesBinding
    private var db: FirebaseFirestore? = null

    private var reference: CollectionReference? = null

    var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesquisaDeVisitantesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFireStoreListaVisitantesExibirMais.setOnClickListener {
            Toast.makeText(this, "btnFireStoreListaVisitantesExibirMais.", Toast.LENGTH_LONG).show()
        }
        val user = arrayOf(
            "Abhay",
            "Joseph",
            "Maria",
            "Avni",
            "Apoorva",
            "Chris",
            "David",
            "Kaira",
            "Dwayne",
            "Christopher",
            "Jim",
            "Russel",
            "Donald",
            "Brack",
            "Vladimir"
        )

        var userAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1,
            user
        )



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if (user.contains(query)) {

                    userAdapter.filter.filter(query)

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userAdapter.filter.filter(newText)
                return false
            }


        })

    }
}

//-----------------------------------------------------------------Menu opções com Pesquisa-------------------------------------------------------------------------
//Set do Menu Search dentro da activity atual


//-----------------------------------------------------------------Metodos Search para Pesquisa--------------------------------------------------------------------------


}