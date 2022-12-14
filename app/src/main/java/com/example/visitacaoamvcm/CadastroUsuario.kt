package com.example.visitacaoamvcm

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.DatePicker
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
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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
    var cal = Calendar.getInstance()
    var checagemFoto: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storege = Firebase.storage


        var dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayofmonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayofmonth)

                val formatoDeData = "dd/MM/yyyy"
                val formatador = SimpleDateFormat(formatoDeData, Locale.ITALY)
                val TV_data = binding.tvDataDeNascimento
                TV_data.text = formatador.format(cal.time)
            }

        }
        binding.btnSelecionarDataDeNascimento.setOnClickListener {


            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnImagemVisitante.setOnClickListener {
            verificaPermissaoDaGaleria()
            checagemFoto += 1

        }

        // Pegando as informa????es passadas pela tela de Pesquisa de Visitantes e passando para a variavel categoria
        categoria = intent.getParcelableExtra("categoriaNome")

        // se caso categoria vier com algum nome selecionado para autera????o na tela de pesquisa de visitantes,
        // ir?? preencher nos campos determinados as informa????es do visitante
        if (categoria != null) {
            supportActionBar?.title = categoria?.nome
            binding.editTextNomeCompleto.setText(categoria?.nome)
            binding.editTextDocumento.isEnabled = false
            binding.editTextNomeCompleto.isEnabled = false
            edit_Text_Documento.setText(categoria?.id.toString())
            download_Imagem()

            binding.btnEditarCadastroVisitante.setOnClickListener {
                binding.editTextNomeCompleto.isEnabled = true
                binding.editTextDocumento.isEnabled = true

            }
            binding.btnSalvarCadastro.setOnClickListener {
                val NomeVisitante = binding.editTextNomeCompleto.text.toString()
                val Documento = binding.editTextDocumento.text.toString()
                val dataDeNascimento = binding.tvDataDeNascimento.text.toString()
                val Endereco = binding.editTextEndereOAtual.text.toString()


                //Valida????o para ver se os campos est??o vazios

                if (NomeVisitante.isEmpty()) {
                    Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()


                } else {
                    //chamada da fun????o do salvamento no DB passando os paramentros do visitante
                    upload_image(NomeVisitante)
                    atualizarVisitante(Documento.toInt(), NomeVisitante)
                    val intent = Intent(this, PesquisaDeVisitantes::class.java)
                    startActivity(intent)
                    finish()


                }

            }


        } else {
            val buttonEditarCadastroVisitante: Button = binding.btnEditarCadastroVisitante
            buttonEditarCadastroVisitante.visibility = View.INVISIBLE

            val buttonExcluirCadastro: Button = binding.btnExcluirVisitante
            buttonExcluirCadastro.visibility = View.INVISIBLE


            binding.btnSalvarCadastro.setOnClickListener {
                val NomeVisitante = binding.editTextNomeCompleto.text.toString()
                var Documento = binding.editTextDocumento.text.toString()
                val dataDeNascimento = binding.tvDataDeNascimento.text.toString()
                val Endereco = binding.editTextEndereOAtual.text.toString()


                //Valida????o para ver se os campos est??o vazios

                if (NomeVisitante.isEmpty()) {
                    Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()


                } else {
                    //pega o tamanho da cole????o categoria para salvar o proximo visitante no final da fila
                    /* val collection = db.collection("Categorias")
                     val countQuery = collection.count()
                     countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
                         if (task.isSuccessful) {
                             val snapshot = task.result
                             val tamanho = snapshot.count + 1
                             salvarVisitante(tamanho.toInt(), NomeVisitante)

                         } else {//caso tenha falha caira aqui
                             Log.d(TAG, "Count failed: ", task.getException())
                         }
                     }

                     */
                    //chamada da fun????o do salvamento no DB passando os paramentros do visitante
                    upload_image(NomeVisitante)
                    salvarVisitante(Documento.toInt(), NomeVisitante)
                }

            }
        }
        binding.btnExcluirVisitante.setOnClickListener {
            deletarVisitante(categoria?.nome.toString())
            val intent = Intent(this, PesquisaDeVisitantes::class.java)
            startActivity(intent)
            finish()
        }


    }

    //Fun????o responsavel por salvar no DB
    private fun salvarVisitante(
        documento: Int,
        NomeVisitante: String,


        //dataDeNascimento: String,
        //Endereco: String
    ) {
        val mapVisitantes = hashMapOf(
            "id" to documento,
            "nome" to NomeVisitante,


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

    //Fun????o para limpar os campos quando for Salvo no DB
    private fun limparCampos() {
        // testando github

        binding.editTextDocumento.setText("")
        binding.editTextEndereOAtual.setText("")
        binding.tvDataDeNascimento.setText("")
        binding.editTextNomeCompleto.setText("")
    }

    //-----------------------------------------------------------------Metodo para atualizar os dados do visitante--------------------------------------------------------------------------
    private fun atualizarVisitante(
        documento: Int,
        NomeVisitante: String,


        //dataDeNascimento: String,
        //Endereco: String
    ) {
        val mapVisitantes = hashMapOf(
            "id" to documento,
            "nome" to NomeVisitante,


            )
        db.collection("Categorias").document(NomeVisitante)
            .update(mapVisitantes as Map<String, Any>)
            .addOnCompleteListener { tarefa ->
                if (tarefa.isSuccessful) {
                    Toast.makeText(this, "Cadastro de Visitante Salvo", Toast.LENGTH_LONG).show()

                }

            }.addOnFailureListener {
                Toast.makeText(this, "Erro ao Cadastrar", Toast.LENGTH_LONG).show()
            }
    }

    //-----------------------------------------------------------------Metodo para Deletar os dados do visitante--------------------------------------------------------------------------
    private fun deletarVisitante(NomeVisitante: String) {

        db.collection("Categorias").document(NomeVisitante).delete()
            .addOnCompleteListener { tarefa ->
                if (tarefa.isSuccessful) {
                    Toast.makeText(this, "Visitante excluido com sucesso", Toast.LENGTH_LONG).show()

                }

            }.addOnFailureListener {
                Toast.makeText(this, "Erro ao deletar", Toast.LENGTH_LONG).show()
            }
    }


    //-----------------------------------------------------------------Metodos para buscar imagem na galeria e exibir--------------------------------------------------------------------------


    //Verifica se o tem permissao para acessar a galeria, da camera da localiza????o entre outras, RETORNA TRUE OU FALSE
    private fun verificaPermissao(permissao: String) =
        //verificando se esta permissao esta aceita ou n??o
        ContextCompat.checkSelfPermission(this, permissao) == PackageManager.PERMISSION_GRANTED


    //Verifica se a permissao para acessar a galeria foi aceita
    private fun verificaPermissaoDaGaleria() {
        val permissaoGaleriaAceita = verificaPermissao(CadastroUsuario.PERMISSAO_GALERIA)

        when {
            // se esta aceita a permissao ir?? abrir a galeria
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
        val buider = AlertDialog.Builder(this).setTitle("Aten????o")
            .setMessage("1 Precisamos do acesso a galeria do dispositivo, deseja pernitir agora ?")
            .setNegativeButton("N??o") { _, _ ->
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
        if (checagemFoto >= 1) {

            val bitmap = (btn_ImagemVisitante.drawable as BitmapDrawable).bitmap

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
            val data = baos.toByteArray()
            val reference = storege!!.reference.child("imagens/${referencia}.jpg")
            storageRef = storege!!.reference.child("imagens/${referencia}.jpg")

            var uploadTask = reference.putBytes(data)

            uploadTask.addOnSuccessListener {
                //val estadoInicial = binding.btnImagemVisitante.getDrawable(R.drawable.ic_baseline_person_24).
                btn_ImagemVisitante.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, // Context
                        R.drawable.ic_baseline_person_24 // Drawable
                    )
                )
                //Toast.makeText(this, "sucesso", Toast.LENGTH_LONG).show()

            }.addOnFailureListener { taskSnapshot ->
                Toast.makeText(this, "erro ${taskSnapshot.message}", Toast.LENGTH_LONG).show()
            }
        }


    }


    //----------------------------------------------------------Metodo para fazer o Download da imagem do visitante pelo nome dele------------------------------------------------
    fun download_Imagem() {
        val islandRef = storageReference.child("imagens/${categoria?.nome}.jpg")

        val localFile = File.createTempFile("imagens", "jpg")

        islandRef.getFile(localFile).addOnSuccessListener {
            // Local temp file has been created

            Glide.with(baseContext).load(localFile).into(btn_ImagemVisitante)

        }.addOnFailureListener {
            // Handle any errors
        }


    }

}