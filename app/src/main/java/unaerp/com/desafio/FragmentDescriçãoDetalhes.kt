package unaerp.com.desafio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class DescricaoFragment : Fragment() {

    private lateinit var textViewDescricao: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_descricao, container, false)

        textViewDescricao = view.findViewById(R.id.textarea_descricao)

        val vaga = arguments?.getSerializable("vaga") as ClassVaga?
        vaga?.let {
            val descricao = vaga.descricao
            textViewDescricao.text = descricao
        }

        return view
    }
}