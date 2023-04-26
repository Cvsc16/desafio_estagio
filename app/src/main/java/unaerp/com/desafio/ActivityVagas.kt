package unaerp.com.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActivityVagas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vagas)

        val iconeFiltro = findViewById<ImageView>(R.id.ic_filtro)

        val vaga1 = Vaga("Desenvolvedor Mobile","PicPay","Ribeirão Preto",
        "Remoto","31/03/2023","1500,00")

        val rvVagas: RecyclerView? = findViewById(R.id.rvVagas)
        rvVagas?.layoutManager = LinearLayoutManager(this)
        rvVagas?.adapter = VagasAdapter(
            listOf(vaga1)
        )

        iconeFiltro.setOnClickListener{
            val intent = Intent(this, ActivityFiltragem::class.java)
            startActivity(intent)
        }
    }
}