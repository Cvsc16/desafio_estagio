package unaerp.com.desafio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VagasAdapter(val vagaList: List<Vaga>, val clickListener: OnClickListener) : RecyclerView.Adapter<VagasAdapter.VagaViewHolder>() {
    interface OnClickListener {
        fun onClick(vaga: Vaga)
    }
    inner class VagaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                clickListener.onClick(vagaList[adapterPosition])
            }
        }

        fun setInfoEmpresa(vaga: Vaga) {
            val nomeEmpresaTv: TextView = itemView.findViewById(R.id.nome_empresa)
            nomeEmpresaTv.text = vaga.empresa
        }

        fun setInfoCidade(vaga: Vaga) {
            val nomeCidadeTv: TextView = itemView.findViewById(R.id.nomeCidade)
            nomeCidadeTv.text = vaga.cidadeEmpresa
        }

        fun setInfoTitulo(vaga: Vaga) {
            val nomeTituloTv: TextView = itemView.findViewById(R.id.nomeVaga)
            nomeTituloTv.text = vaga.titulo
        }

        fun setInfoTipoTrabalho(vaga: Vaga) {
            val nomeTipoTrabalhoTv: TextView = itemView.findViewById(R.id.tipoVaga)
            nomeTipoTrabalhoTv.text = vaga.tipoTrabalho
        }

        fun setInfoDataInicio(vaga: Vaga) {
            val dataInicioTv: TextView = itemView.findViewById(R.id.tempoPostagem)
            dataInicioTv.text = vaga.dataInicio
        }

        fun setInfoSalario(vaga: Vaga) {
            val salarioTv: TextView = itemView.findViewById(R.id.pagamento)
            salarioTv.text = vaga.pagamento
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
        holder.setInfoEmpresa(vagaList[position])
        holder.setInfoCidade(vagaList[position])
        holder.setInfoTitulo(vagaList[position])
        holder.setInfoTipoTrabalho(vagaList[position])
        holder.setInfoDataInicio(vagaList[position])
        holder.setInfoSalario(vagaList[position])
    }

}