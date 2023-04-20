package unaerp.com.desafio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActivityVagas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vagas)
        val vaga1 = Vaga("Desenvolvedor Mobile","Unaerp","Ribeirão Preto",
        "Remoto","31/03/2023","1500,00")


    }
}