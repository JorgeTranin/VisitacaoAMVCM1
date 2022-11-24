 package Business

 import android.content.Context
 import android.view.LayoutInflater
 import android.view.View
 import android.view.ViewGroup
 import androidx.recyclerview.widget.RecyclerView
 import com.example.visitacaoamvcm.R
 import kotlinx.android.synthetic.main.item_lista_cadastro_visitantes_recycleview.view.*


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

        //Inflate para apresentar na tela do usuario o layout criado
         val view = LayoutInflater.from(context).inflate(R.layout.item_lista_cadastro_visitantes_recycleview,parent,false)

        val holder = ViewHolder(view)

        return holder
     }

     // Metodo para exibir os elementos da minha lista
     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val categoria: mCategoriadeVisitantes = categorias.get(position)
         holder.nome.text = categoria.nome


     }
     //Metodo que informa qual o tamanho que vai ser a lista de Visitantes
     override fun getItemCount(): Int {
         // Irá depender do tamanho da variavel categorias
         return categorias.size
     }

     // classe que irá pegar os elementos que estão no layout item_Lista_cadastro_visitantes_recycleView
     class ViewHolder(intemView: View): RecyclerView.ViewHolder(intemView) {
         val nome = intemView.textView_ListaItem_Categoria_Item_Nome


     }

 }