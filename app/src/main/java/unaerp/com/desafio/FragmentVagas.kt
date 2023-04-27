package unaerp.com.desafio

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentVagas : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vagas, container, false)

        val iconeFiltro = view.findViewById<ImageView>(R.id.ic_filtro)

        val vaga1 = Vaga("Desenvolvedor Mobile JR","PicPay","Campinas",
            "Presencial","22/02/2023","R$ 6300,00")

        val vaga2 = Vaga("Desenvolvedor Backend Pleno","Nubank","São Paulo",
            "Remoto","01/03/2023","R$ 8000,00")

        val vaga3 = Vaga("Engenheiro de Dados Sênior","iFood","São Paulo",
            "Presencial","15/03/2023","R$ 12000,00")

        val rvVagas: RecyclerView? = view.findViewById(R.id.rvVagas)
        rvVagas?.layoutManager = LinearLayoutManager(context)

        val listener = object : VagasAdapter.OnClickListener {
            override fun onClick(vaga: Vaga) {
                val intent = Intent(activity, ActivityDetalhesVaga::class.java)
                intent.putExtra("vaga", vaga)
                startActivity(intent)
            }
        }

        val adapter = VagasAdapter(listOf(vaga1, vaga2, vaga3), listener)
        rvVagas?.adapter = adapter

        iconeFiltro.setOnClickListener{
            val intent = Intent(context, ActivityFiltragem::class.java)
            startActivity(intent)
        }

        return view
    }
}






