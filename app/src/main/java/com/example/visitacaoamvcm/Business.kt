package Business

//classe responsavel por passar para o Firebase os campos a serem utilizados no get
// sendo obrigatorio o nome das variaveis o mesmo que consta no Bancco de dados
class InfoCadastroDeVisitantes(
    var Documento: String?, var Endereco: String?,
    var NomeVisitante: String?, var dataDeNascimento: String?
) {
    // Declaração de um construtor para caso algum campo seja null
    constructor() : this(null, null, null, null)
}