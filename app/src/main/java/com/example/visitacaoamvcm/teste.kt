package com.example.visitacaoamvcm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.visitacaoamvcm.Business.mCategoriadeVisitantes
import com.example.visitacaoamvcm.databinding.ActivityTesteBinding

class teste : AppCompatActivity() {
    lateinit var dialog: AlertDialog

    companion object {
        private val PERMISSAO_GALERIA = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private val requestGaleria =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissao ->
            if (permissao) {
                resultGaleria.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            } else {
                showDialogPermissao()
            }
        }
    private val resultGaleria =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val bitmap: Bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(baseContext.contentResolver, result.data?.data)
            } else {
                val source = ImageDecoder.createSource(this.contentResolver, result.data?.data!!)
                ImageDecoder.decodeBitmap(source)

            }
            binding.imageButtonSelecionar.setImageBitmap(bitmap)

        }
    lateinit var binding: ActivityTesteBinding
    var categoria: mCategoriadeVisitantes? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTesteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.imageButtonSelecionar.setOnClickListener {

        }

        binding.btnSalvarImagem.setOnClickListener {
            verificaPermissaoDaGaleria()
        }


        supportActionBar?.title = categoria?.nome
        categoria = intent.getParcelableExtra("categoriaNome")


        //Toast.makeText(this, categoria?.nome.toString(), Toast.LENGTH_LONG).show()
        //textView2.text = categoria?.nome.toString()
    }


    //Verifica se o tem permissao para acessar a galeria, da camera da localização entre outras, RETORNA TRUE OU FALSE
    private fun verificaPermissao(permissao: String) =
        //verificando se esta permissao esta aceita ou não
        ContextCompat.checkSelfPermission(this, permissao) == PackageManager.PERMISSION_GRANTED


    //Verifica se a permissao para acessar a galeria foi aceita
    private fun verificaPermissaoDaGaleria() {
        val permissaoGaleriaAceita = verificaPermissao(PERMISSAO_GALERIA)

        when {
            // se esta aceita a permissao irá abrir a galeria
            permissaoGaleriaAceita -> {
                resultGaleria.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            }
            shouldShowRequestPermissionRationale(PERMISSAO_GALERIA) -> showDialogPermissao()


            else -> requestGaleria.launch(PERMISSAO_GALERIA)


        }

    }

    private fun showDialogPermissao() {
        val buider = AlertDialog.Builder(this).setTitle("Atenção")
            .setMessage("1 Precisamos do acesso a galeria do dispositivo, deseja pernitir agora ?")
            .setNegativeButton("Não") { _, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Sim") { _, _ ->
                val intent = Intent(
                    android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            }

        dialog = buider.create()
        dialog.show()

    }

}