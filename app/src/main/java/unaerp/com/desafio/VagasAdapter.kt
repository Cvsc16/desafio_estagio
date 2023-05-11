package unaerp.com.desafio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class VagasAdapter(
    val vagaList: MutableList<ClassVaga>,
    val clickListener: OnClickListener,
    val tipoConta: String
) : RecyclerView.Adapter<VagasAdapter.VagaViewHolder>() {
    interface OnClickListener {
        fun onClick(vaga: ClassVaga)
        fun onExcluirClick(vaga: ClassVaga)
    }


    inner class VagaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                clickListener.onClick(vagaList[adapterPosition])
            }

            itemView.findViewById<View>(R.id.ic_excluir).setOnClickListener {
                clickListener.onExcluirClick(vagaList[adapterPosition])
            }
        }


        init {
            itemView.setOnClickListener {
                clickListener.onClick(vagaList[adapterPosition])
            }
        }

        fun setInfoEmpresa(vaga: ClassVaga) {
            val nomeEmpresaTv: TextView = itemView.findViewById(R.id.nome_empresa)
            nomeEmpresaTv.text = vaga.empresa
        }

        fun setInfoCidade(vaga: ClassVaga) {
            val nomeCidadeTv: TextView = itemView.findViewById(R.id.nomeCidade)
            nomeCidadeTv.text = vaga.cidadeEmpresa
        }

        fun setInfoTitulo(vaga: ClassVaga) {
            val nomeTituloTv: TextView = itemView.findViewById(R.id.nomeVaga)
            nomeTituloTv.text = vaga.titulo
        }

        fun setInfoTipoTrabalho(vaga: ClassVaga) {
            val nomeTipoTrabalhoTv: TextView = itemView.findViewById(R.id.tipoVaga)
            nomeTipoTrabalhoTv.text = vaga.tipoTrabalho
        }

        fun setInfoDataInicio(vaga: ClassVaga) {
            val dataInicioTv: TextView = itemView.findViewById(R.id.tempoPostagem)
            dataInicioTv.text = vaga.dataInicio
        }

        fun setInfoSalario(vaga: ClassVaga) {
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

        if (tipoConta == "Anunciante") {
            holder.itemView.findViewById<View>(R.id.ic_excluir).setOnClickListener {
                clickListener.onExcluirClick(vagaList[position])
            }
        } else {
            holder.itemView.findViewById<View>(R.id.ic_excluir).visibility = View.GONE
        }
    }
}