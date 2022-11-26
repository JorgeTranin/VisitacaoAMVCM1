package com.example.visitacaoamvcm

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.visitacaoamvcm.Business.mCategoriadeVisitantes
import com.example.visitacaoamvcm.databinding.ActivityTesteBinding
import kotlinx.android.synthetic.main.activity_teste.*

class teste : AppCompatActivity() {
    lateinit var binding: ActivityTesteBinding
    var categoria: mCategoriadeVisitantes? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTesteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.title = categoria?.nome
        categoria = intent.getParcelableExtra("categoriaNome")


        Toast.makeText(this, categoria?.nome.toString(), Toast.LENGTH_LONG).show()
        textView2.text = categoria?.nome.toString()
    }
}