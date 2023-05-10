package unaerp.com.desafio

data class User @JvmOverloads constructor(
    var email: String = "",
    var nome: String = "",
    var tipo: String = ""
)
