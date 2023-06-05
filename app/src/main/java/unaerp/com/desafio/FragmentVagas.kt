package unaerp.com.desafio

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class FragmentVagas : Fragment() {

    private var tipoConta: String? = null
    private lateinit var adapter: VagasAdapter
    private var vagasList: MutableList<ClassVaga> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vagas, container, false)

        val iconeFiltro = view.findViewById<ImageView>(R.id.ic_filtro)

        val rvVagas: RecyclerView? = view.findViewById(R.id.rvVagas)
        rvVagas?.layoutManager = LinearLayoutManager(context)

        val listener = object : VagasAdapter.OnClickListener {
            override fun onClick(vaga: ClassVaga) {
                val intent = Intent(activity, ActivityDetalhesVaga::class.java)
                intent.putExtra("vaga", vaga)
                startActivity(intent)
            }

            override fun onExcluirClick(vaga: ClassVaga) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Excluir Vaga")
                builder.setMessage("Tem certeza que deseja excluir esta vaga?")
                builder.setPositiveButton("OK") { dialog, which ->
                    // Remover a vaga do banco de dados
                    val database = FirebaseDatabase.getInstance().reference
                    val vagasRef = database.child("vagas").child(vaga.id)
                    vagasRef.removeValue()

                    // Remover a vaga da RecyclerView
                    val adapter = rvVagas?.adapter as? VagasAdapter
                    adapter?.vagaList?.remove(vaga)
                    adapter?.notifyDataSetChanged()
                }
                builder.setNegativeButton("Cancelar") { dialog, which ->
                    // Não faz nada
                }
                val dialog = builder.create()
                dialog.show()
            }
        }

        adapter = VagasAdapter(vagasList, listener, tipoConta ?: "")
        rvVagas?.adapter = adapter

        val database = FirebaseDatabase.getInstance().reference
        val vagasRef = database.child("vagas")

        vagasRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val updatedVagasList = mutableListOf<ClassVaga>()

                for (vagaSnapshot in dataSnapshot.children) {
                    val vaga = vagaSnapshot.getValue(ClassVaga::class.java)
                    vaga?.let {
                        updatedVagasList.add(it)
                    }
                }

                vagasList = updatedVagasList

                adapter = VagasAdapter(vagasList, listener, tipoConta ?: "")
                rvVagas?.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Trate o erro, se necessário
            }
        })

        val searchView = view.findViewById<TextView>(R.id.searchView)
        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Não é necessário fazer nada antes da mudança de texto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = searchView.text.toString()

                // Chame o método de filtro com o mesmo texto para todos os parâmetros
                filterVagas(query, query, query, query)
            }

            override fun afterTextChanged(s: Editable?) {
                // Não é necessário fazer nada após a mudança de texto
            }
        })



        class BottomSpaceItemDecoration(private val bottomSpaceHeight: Int) : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val position = parent.getChildAdapterPosition(view)
                val itemCount = state.itemCount

                if (position == itemCount - 1) {
                    // Último item
                    outRect.bottom = bottomSpaceHeight
                } else {
                    outRect.bottom = 0
                }
            }
        }

        val bottomSpaceHeight = resources.getDimensionPixelSize(R.dimen.bottom_space_height)

        rvVagas?.addItemDecoration(BottomSpaceItemDecoration(bottomSpaceHeight))

        tipoConta = arguments?.getString("tipo_conta")

        iconeFiltro.setOnClickListener {
            val intent = Intent(context, ActivityFiltragem::class.java)
            startActivity(intent)
        }

        // Verifica se o usuário logado tem um cadastro de anunciante"
        if (tipoConta == "Anunciante") {
            val encontreEstagio = view.findViewById<TextView>(R.id.encontre_estagio)
            encontreEstagio.text = "Anuncie o seu"
        }


        return view
        val filteredList = mutableListOf<ClassVaga>()
    }
    private fun filterVagas(text: String?, cidade: String?, empresa: String?, tipoTrabalho: String?) {
        val filteredList = mutableListOf<ClassVaga>()
        val rvVagas: RecyclerView? = view?.findViewById(R.id.rvVagas)
        val imageNoResults: ImageView? = view?.findViewById(R.id.imageNoResults)
        val texto_semResultados: TextView? = view?.findViewById(R.id.text_semResultados)
        val texto_tenteNovamente: TextView? = view?.findViewById(R.id.text_tenteNovamente)

        text?.let { searchText ->
            val query = searchText.lowercase(Locale.getDefault())
            for (vaga in vagasList) {
                val tituloVaga = vaga.titulo.lowercase(Locale.getDefault()).contains(query)
                val cidadeVaga = cidade?.let { it.isNotBlank() && vaga.cidadeEmpresa.lowercase(Locale.getDefault()).contains(it.lowercase(Locale.getDefault())) } ?: false
                val empresaVaga = empresa?.let { it.isNotBlank() && vaga.empresa.lowercase(Locale.getDefault()).contains(it.lowercase(Locale.getDefault())) } ?: false
                val tipoVaga = tipoTrabalho?.let { it.isNotBlank() && vaga.tipoTrabalho.lowercase(Locale.getDefault()).contains(it.lowercase(Locale.getDefault())) } ?: false

                if (tituloVaga || cidadeVaga || empresaVaga || tipoVaga) {
                    filteredList.add(vaga)
                }
            }
        }

        if (filteredList.isEmpty()) {

            rvVagas?.visibility =
                View.GONE // Oculta a RecyclerView se não houver vagas correspondentes
            imageNoResults?.visibility =
                View.VISIBLE // Exibe o ImageView de "nenhuma vaga encontrada"
            texto_semResultados?.visibility = View.VISIBLE
            texto_tenteNovamente?.visibility = View.VISIBLE
        } else {
            rvVagas?.visibility =
                View.VISIBLE // Exibe a RecyclerView se houver vagas correspondentes
            imageNoResults?.visibility =
                View.GONE // Oculta o ImageView de "nenhuma vaga encontrada"
            texto_semResultados?.visibility = View.GONE
            texto_tenteNovamente?.visibility = View.GONE
        }

        adapter.filter(filteredList) // Filtra os dados do adaptador com a lista filtrada
        adapter.notifyDataSetChanged() // Notifica o adaptador para atualizar os dados exibidos na RecyclerView
    }

    fun atualizarFiltro(
        areaConhecimento: String,
        localidade: String,
        anunciante: String,
        tipoVaga: String,
        remuneracao: String
    ) {
        val filteredList = mutableListOf<ClassVaga>()

        for (vaga in vagasList) {
            val areaVaga = areaConhecimento?.let { it.isNotBlank() && vaga.areaConhecimento.lowercase(Locale.getDefault()).contains(it.lowercase(Locale.getDefault())) } ?: false
            val cidadeVaga = localidade?.let { it.isNotBlank() && vaga.cidadeEmpresa.lowercase(Locale.getDefault()).contains(it.lowercase(Locale.getDefault())) } ?: false
            val empresaVaga = anunciante?.let { it.isNotBlank() && vaga.empresa.lowercase(Locale.getDefault()).contains(it.lowercase(Locale.getDefault())) } ?: false
            val tipoVaga = tipoVaga?.let { it.isNotBlank() && vaga.tipoTrabalho.lowercase(Locale.getDefault()).contains(it.lowercase(Locale.getDefault())) } ?: false
            val pagamentoVaga = remuneracao?.let { it.isNotBlank() && vaga.pagamento.lowercase(Locale.getDefault()).contains(it.lowercase(Locale.getDefault())) } ?: false

            if ( areaVaga || cidadeVaga || empresaVaga || tipoVaga || pagamentoVaga) {
                filteredList.add(vaga)
            }
        }

        adapter.filter(filteredList) // Filtra os dados do adaptador com a lista filtrada
        adapter.notifyDataSetChanged() // Notifica o adaptador para atualizar os dados exibidos na RecyclerView
    }
}






