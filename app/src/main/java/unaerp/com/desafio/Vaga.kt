package unaerp.com.desafio

import java.io.Serializable

data class Vaga(
    val titulo:String,
    val empresa:String,
    val cidadeEmpresa:String,
    val tipoTrabalho:String,
    val dataInicio:String,
    val pagamento:String
): Serializable
