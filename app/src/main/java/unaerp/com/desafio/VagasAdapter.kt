package unaerp.com.desafio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VagasAdapter(val vagaList: List<Vaga>): RecyclerView.Adapter<VagasAdapter.VagaViewHolder>() {

    inner class VagaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setInfo(vaga: Vaga) {
            val nomeEmpresaTv: TextView = itemView.findViewById(R.id.nome_empresa)
            nomeEmpresaTv.text = vaga.empresa

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardvaga, parent, false)
        return VagaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return vagaList.size
    }

    override fun onBindViewHolder(holder: VagaViewHolder, position: Int) {
        holder.setInfo(vagaList[position])
    }

}