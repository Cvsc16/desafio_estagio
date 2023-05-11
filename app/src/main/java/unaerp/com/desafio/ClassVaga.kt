package unaerp.com.desafio

import java.io.Serializable

data class ClassVaga(
    val titulo: String,
    val empresa: String,
    val cidadeEmpresa: String,
    val tipoTrabalho: String,
    val dataInicio: String,
    val pagamento: String,
    val areaConhecimento: String,
    val descricao: String,
    val dataFim: String,
    val telefone: String,
    val emailEmpresa: String
) : Serializable

