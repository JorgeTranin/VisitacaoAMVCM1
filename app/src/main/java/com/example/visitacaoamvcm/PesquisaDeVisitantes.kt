package com.example.visitacaoamvcm

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.visitacaoamvcm.databinding.ActivityPesquisaDeVisitantesBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class PesquisaDeVisitantes : AppCompatActivity() {
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

}