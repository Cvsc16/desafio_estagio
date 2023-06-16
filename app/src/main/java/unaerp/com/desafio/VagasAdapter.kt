package unaerp.com.desafio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import unaerp.com.desafio.databinding.CardvagaBinding
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
        fun onEditarClick(vaga: ClassVaga)
    }

    fun filter(filteredList: MutableList<ClassVaga>) {
        filteredVagasList.clear()
        filteredVagasList.addAll(filteredList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagaViewHolder {
        val binding = CardvagaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VagaViewHolder(binding)
    }
    inner class VagaViewHolder(private val binding: CardvagaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vaga: ClassVaga) {
            binding.nomeEmpresa.text = vaga.empresa
            binding.nomeCidade.text = vaga.cidadeEmpresa
            binding.nomeVaga.text = vaga.titulo
            binding.tipoVaga.text = vaga.tipoTrabalho
            binding.tempoPostagem.text = vaga.dataInicio
            binding.pagamento.text = "R$ ${vaga.pagamento}"

            binding.cardVaga.setOnClickListener {
                clickListener.onClick(filteredVagasList[adapterPosition])
            }

            binding.icExcluir.setOnClickListener {
                clickListener.onExcluirClick(vagaList[adapterPosition])
            }

            binding.icEditar.setOnClickListener {
                clickListener.onEditarClick(vagaList[adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredVagasList.size
    }

    override fun onBindViewHolder(holder: VagaViewHolder, position: Int) {
        holder.bind(filteredVagasList[position])
        if (vagaList.isNotEmpty()) {
            val vaga = filteredVagasList[position] // Obter a vaga da lista filtrada

            if (tipoConta == "Anunciante") {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val idAnunciante = vaga.idAnunciante

                if (userId == idAnunciante) {
                    holder.itemView.findViewById<View>(R.id.ic_excluir).visibility = View.VISIBLE
                    holder.itemView.findViewById<View>(R.id.ic_excluir).setOnClickListener {
                        clickListener.onExcluirClick(vaga)
                    }
                    holder.itemView.findViewById<View>(R.id.ic_editar).visibility = View.VISIBLE
                } else {
                    holder.itemView.findViewById<View>(R.id.ic_excluir).visibility = View.GONE
                    holder.itemView.findViewById<View>(R.id.ic_editar).visibility = View.GONE
                }
            } else {
                holder.itemView.findViewById<View>(R.id.ic_excluir).visibility = View.GONE
                holder.itemView.findViewById<View>(R.id.ic_editar).visibility = View.GONE
            }
        } else {
            // Trate o caso em que a lista está vazia, se necessário
        }
    }
}