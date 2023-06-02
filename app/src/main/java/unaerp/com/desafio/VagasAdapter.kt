package unaerp.com.desafio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class VagasAdapter(
    val vagaList: MutableList<ClassVaga>,
    val clickListener: OnClickListener,
    val tipoConta: String
) : RecyclerView.Adapter<VagasAdapter.VagaViewHolder>() {

    private var filteredVagasList: MutableList<ClassVaga> = vagaList.toMutableList() // Inicializa a lista filtrada com todas as vagas

    interface OnClickListener {
        fun onClick(vaga: ClassVaga)
        fun onExcluirClick(vaga: ClassVaga)
    }

    fun filter(filteredList: MutableList<ClassVaga>) {
        filteredVagasList.clear()
        filteredVagasList.addAll(filteredList)
        notifyDataSetChanged()
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
            salarioTv.text = "R$ ${vaga.pagamento}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardvaga, parent, false)
        return VagaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredVagasList.size
    }

    override fun onBindViewHolder(holder: VagaViewHolder, position: Int) {
        if (filteredVagasList.isNotEmpty()) {
            val vaga = filteredVagasList[position] // Obter a vaga da lista filtrada

            holder.setInfoEmpresa(vaga)
            holder.setInfoCidade(vaga)
            holder.setInfoTitulo(vaga)
            holder.setInfoTipoTrabalho(vaga)
            holder.setInfoDataInicio(vaga)
            holder.setInfoSalario(vaga)

            if (tipoConta == "Anunciante") {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val idAnunciante = vaga.idAnunciante

                if (userId == idAnunciante) {
                    holder.itemView.findViewById<View>(R.id.ic_excluir).visibility = View.VISIBLE
                    holder.itemView.findViewById<View>(R.id.ic_excluir).setOnClickListener {
                        clickListener.onExcluirClick(vaga)
                    }
                } else {
                    holder.itemView.findViewById<View>(R.id.ic_excluir).visibility = View.GONE
                }
            } else {
                holder.itemView.findViewById<View>(R.id.ic_excluir).visibility = View.GONE
            }
        } else {
            // Trate o caso em que a lista está vazia, se necessário
        }
    }
}