package unaerp.com.desafio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import unaerp.com.desafio.databinding.FragmentContatoBinding
import unaerp.com.desafio.databinding.FragmentDescricaoBinding

class DescricaoFragment : Fragment() {

    private lateinit var binding: FragmentDescricaoBinding
    private lateinit var textViewDescricao: TextView
    private lateinit var textViewAreaConhecimento: TextView
    private lateinit var textViewDataFim: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDescricaoBinding.inflate(inflater, container, false)
        val view = binding.root

        textViewDescricao = binding.textareaDescricao
        textViewAreaConhecimento = binding.tvDescAreaConhecimento
        textViewDataFim = binding.tvDescDatafim

        val vaga = arguments?.getSerializable("vaga") as ClassVaga?
        vaga?.let {
            val descricao = vaga.descricao
            textViewDescricao.text = descricao

            val areaConhecimento = vaga.areaConhecimento
            textViewAreaConhecimento.text = areaConhecimento

            val datalimite = vaga.dataFim
            textViewDataFim.text = datalimite
        }

        return view
    }
}