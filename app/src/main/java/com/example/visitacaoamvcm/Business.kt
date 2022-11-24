 package Business

 import android.content.Context
 import android.view.View
 import android.view.ViewGroup
 import android.view.ViewParent
 import androidx.recyclerview.widget.RecyclerView

 //classe responsavel por passar para o Firebase os campos a serem utilizados no get
// sendo obrigatorio o nome das variaveis o mesmo que consta no Bancco de dados
class InfoCadastroDeVisitantes(
    var Documento: String?, var Endereco: String?,
    var NomeVisitante: String?, var dataDeNascimento: String?
) {
    // Declaração de um construtor para caso algum campo seja null
    constructor() : this(null, null, null, null)
}


 //classe de categoria de visitantes
 class mCategoriadeVisitantes(var nome:String?, var id:Int?){


 }
//-----------------------------------------------------------------Adaptador de gerenciamento da minha lista--------------------------------------------------------------------------


 //Adapter ficara responsavel por gerenciar a minha lista, apresentação, auteração e é quem faz a lista funcionar
 class AdapterRecyclerviewCategoria(val context:Context, var categorias: ArrayList<mCategoriadeVisitantes>)
     : RecyclerView.Adapter<AdapterRecyclerviewCategoria.ViewHolder>(){


    //Informa qual layout são os itens, criação do layout e todas as auterações que quero modificar na visualização
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")

     }

     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         TODO("Not yet implemented")
     }

     override fun getItemCount(): Int {
         TODO("Not yet implemented")
     }

     // classe
     class ViewHolder(intemView: View): RecyclerView.ViewHolder(intemView) {

     }

 }