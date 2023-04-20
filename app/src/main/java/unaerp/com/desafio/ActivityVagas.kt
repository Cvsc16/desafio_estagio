package unaerp.com.desafio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityVagas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vagas)
        val vaga1 = Vaga("Desenvolvedor Mobile","PicPay","Ribeirão Preto",
        "Remoto","31/03/2023","1500,00")

        val rvVagas: RecyclerView? = findViewById(R.id.rvVagas)
        rvVagas?.layoutManager = LinearLayoutManager(this)
        rvVagas?.adapter = VagasAdapter(
            listOf(vaga1)
        )
    }
}