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
                // Chame o método de filtro com o novo texto digitado
                filterVagas(s?.toString())
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
    }
    private fun filterVagas(text: String?) {
        val filteredList = mutableListOf<ClassVaga>()
        val rvVagas: RecyclerView? = view?.findViewById(R.id.rvVagas)
        rvVagas?.layoutManager = LinearLayoutManager(context)

        text?.let { searchText ->
            val query = searchText.toLowerCase(Locale.getDefault())
            for (vaga in vagasList) {
                if (vaga.titulo.toLowerCase(Locale.getDefault()).contains(query)) {
                    filteredList.add(vaga)
                }
            }
        }

        if (filteredList.isEmpty()) {
            rvVagas?.visibility = View.GONE // Oculta a RecyclerView se não houver vagas correspondentes
        } else {
            rvVagas?.visibility = View.VISIBLE // Exibe a RecyclerView se houver vagas correspondentes
        }

        adapter.filter(filteredList)
        adapter.notifyDataSetChanged() // Notificar o adaptador para atualizar os dados exibidos
    }
}






