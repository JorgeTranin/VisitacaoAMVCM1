package com.example.visitacaoamvcm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.visitacaoamvcm.Business.mCategoriadeVisitantes
import com.example.visitacaoamvcm.databinding.ActivityCadastroUsuarioBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_cadastro_usuario.*
import java.io.ByteArrayOutputStream

class CadastroUsuario : AppCompatActivity() {
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
            if (result.data?.data != null) {
                val bitmap: Bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        baseContext.contentResolver,
                        result.data?.data
                    )
                } else {
                    val source =
                        ImageDecoder.createSource(this.contentResolver, result.data?.data!!)
                    ImageDecoder.decodeBitmap(source)

                }
                binding.btnImagemVisitante.setImageBitmap(bitmap)
            }

        }
    var storege: FirebaseStorage? = null

    var storageRef = storege?.reference
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityCadastroUsuarioBinding
    var categoria: mCategoriadeVisitantes? = null
    val storageReference = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storege = Firebase.storage
        binding.btnImagemVisitante.setOnClickListener {
            verificaPermissaoDaGaleria()
        }

        // Pegando as informações passadas pela tela de Pesquisa de Visitantes e passando para a variavel categoria
        categoria = intent.getParcelableExtra("categoriaNome")

        // se caso categoria vier com algum nome selecionado para auteração na tela de pesquisa de visitantes,
        // irá preencher nos campos determinados as informações do visitante
        if (categoria != null) {
            val nome = categoria?.nome.toString()
            supportActionBar?.title = categoria?.nome
            edit_Text_NomeCompleto.hint = categoria?.nome.toString()
            edit_Text_Documento.hint = categoria?.id.toString()
            //downloadImagem(nome)
            download_Imagem_1()


            //Toast.makeText(this, categoria?.nome.toString() + categoria?.id, Toast.LENGTH_LONG).show()
        }


        binding.btnSalvarCadastro.setOnClickListener {
            val NomeVisitante = binding.editTextNomeCompleto.text.toString()
            val Documento = binding.editTextDocumento.text.toString()
            val dataDeNascimento = binding.editTextDatadeNascimento.text.toString()
            val Endereco = binding.editTextEndereOAtual.text.toString()


            //Validação para ver se os campos estão vazios

            if (NomeVisitante.isEmpty() || Documento.isEmpty() || dataDeNascimento.isEmpty() || Endereco.isEmpty()) {
                //Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()


            } else {
                //chamada da função do salvamento no DB passando os paramentros do visitante
                upload_image(NomeVisitante)
                salvarVisitante(NomeVisitante)
            }

        }


    }

    //Função responsavel por salvar no DB
    private fun salvarVisitante(
        NomeVisitante: String,

        //dataDeNascimento: String,
        //Endereco: String
    ) {
        val mapVisitantes = hashMapOf(
            "nome" to NomeVisitante,
            "id" to "22",

            )
        db.collection("Categorias").document(NomeVisitante).set(mapVisitantes)
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
    private fun limparCampos() {
        binding.editTextDocumento.setText("")
        binding.editTextEndereOAtual.setText("")
        binding.editTextDatadeNascimento.setText("")
        binding.editTextNomeCompleto.setText("")
    }

    //-----------------------------------------------------------------Metodos para buscar imagem na galeria e exibir--------------------------------------------------------------------------


    //Verifica se o tem permissao para acessar a galeria, da camera da localização entre outras, RETORNA TRUE OU FALSE
    private fun verificaPermissao(permissao: String) =
        //verificando se esta permissao esta aceita ou não
        ContextCompat.checkSelfPermission(this, permissao) == PackageManager.PERMISSION_GRANTED


    //Verifica se a permissao para acessar a galeria foi aceita
    private fun verificaPermissaoDaGaleria() {
        val permissaoGaleriaAceita = verificaPermissao(CadastroUsuario.PERMISSAO_GALERIA)

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
            shouldShowRequestPermissionRationale(CadastroUsuario.PERMISSAO_GALERIA) -> showDialogPermissao()


            else -> requestGaleria.launch(CadastroUsuario.PERMISSAO_GALERIA)


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

    //----------------------------------------------------------Metodo para fazer o Upload da imagem do visitante------------------------------------------------
    fun upload_image(referencia: String) {
        val bitmap = (btn_ImagemVisitante.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()
        val reference = storege!!.reference.child("imagens/${referencia}.jpg")
        storageRef = storege!!.reference.child("imagens/${referencia}.jpg")

        var uploadTask = reference.putBytes(data)
        uploadTask.addOnSuccessListener {
            Toast.makeText(this, "sucesso", Toast.LENGTH_LONG).show()
        }.addOnFailureListener { taskSnapshot ->
            Toast.makeText(this, "erro ${taskSnapshot.message}", Toast.LENGTH_LONG).show()
        }

    }

    fun downloadImagem(referencia: String) {
        var islandRef = storageRef?.child("imagens/${referencia}")

        val ONE_MEGABYTE: Long = 1024 * 1024
        islandRef?.getBytes(ONE_MEGABYTE)?.addOnSuccessListener { sucesso ->
            // Data for "images/island.jpg" is returned, use this as needed
            Glide.with(this /* context */).load(islandRef).into(btn_ImagemVisitante)


        }?.addOnFailureListener { erro ->
            // Handle any errors
            Toast.makeText(this, "erro ${erro.message}", Toast.LENGTH_LONG).show()
        }


    }

    fun download_Imagem_1() {

        val urlimagem = storageRef?.child("imagens/${categoria?.nome}")

        Glide.with(baseContext).asBitmap().load(urlimagem).into(btn_ImagemVisitante)


    }

}