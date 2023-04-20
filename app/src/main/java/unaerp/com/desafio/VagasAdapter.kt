package unaerp.com.desafio

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VagasAdapter(list: List<Vaga>): RecyclerView.Adapter<VagasAdapter.VagaViewHolder>() {

    inner class VagaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setInfo(vaga: Vaga) {
            val nomeEmpresaTv: TextView = TODO()
            nomeEmpresaTv.text = vaga.empresa

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagaViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: VagaViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}