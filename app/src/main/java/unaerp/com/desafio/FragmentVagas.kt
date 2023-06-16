package unaerp.com.desafio

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import unaerp.com.desafio.databinding.FragmentPerfilBinding
import unaerp.com.desafio.databinding.FragmentVagasBinding
import java.text.Normalizer
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class FragmentVagas : Fragment() {

    private lateinit var binding: FragmentVagasBinding
    private var areaConhecimentoSelecionada: String? = null
    private var cidadeSelecionada: String? = null
    private var empresaSelecionada: String? = null
    private var remuneracaoSelecionada: String? = null
    private var tipoTrabalhoSelecionado: String? = null
    private var escolhaUsuarioSelecionada: String? = null
    val REQUEST_FILTRAGEM = 1
    private var tipoConta: String? = null
    private lateinit var adapter: VagasAdapter
    private var vagasList: MutableList<ClassVaga> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVagasBinding.inflate(inflater, container, false)
        val view = binding.root

        val iconeFiltro = binding.icFiltro

        val rvVagas: RecyclerView? = binding.rvVagas
        rvVagas?.layoutManager = LinearLayoutManager(context)

        val handler = Handler(Looper.getMainLooper())
        val intervaloVerificacao = 24 * 60 * 60 * 1000L // 24 horas (em milissegundos)

        val verificarVagasExpiradasRunnable = object : Runnable {
            override fun run() {
                verificarVagasExpiradas()

                // Agende a próxima execução após o intervalo de tempo
                handler.postDelayed(this, intervaloVerificacao)

            }
        }

        handler.postDelayed(verificarVagasExpiradasRunnable, intervaloVerificacao)


        val listener = object : VagasAdapter.OnClickListener {
            override fun onClick(vaga: ClassVaga) {
                val intent = Intent(activity, DetalhesVagaActivity::class.java)
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
                }
                val dialog = builder.create()
                dialog.show()
            }

            override fun onEditarClick(vaga: ClassVaga) {
                val intent = Intent(requireContext(), ActivityEdicaoVagas::class.java)
                intent.putExtra("vaga", vaga)
                startActivity(intent)
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

                updatedVagasList.reverse()

                vagasList = updatedVagasList

                adapter = VagasAdapter(vagasList, listener, tipoConta ?: "")
                rvVagas?.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        val searchView = binding.searchView
        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = searchView.text.toString()

                // Chame o método de filtro com query para exibir todas as vagas
                filterVagas(query,query, query, query, query)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })



        class BottomSpaceItemDecoration(private val bottomSpaceHeight: Int) :
            RecyclerView.ItemDecoration() {

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                val itemCount = state.itemCount

                if (position == itemCount - 1) {
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
            val intent = Intent(requireContext(), FiltragemActivity::class.java)
            intent.putExtra("areaConhecimentoSelecionada", areaConhecimentoSelecionada)
            intent.putExtra("cidadeSelecionada", cidadeSelecionada)
            intent.putExtra("empresaSelecionada", empresaSelecionada)
            intent.putExtra("tipoTrabalhoSelecionado", tipoTrabalhoSelecionado)
            intent.putExtra("remuneracaoSelecionada", remuneracaoSelecionada)
            intent.putExtra("tipoConta", tipoConta)
            Log.d("LOGESCOLHAUSUARIO", "Auser11111:$escolhaUsuarioSelecionada")
            intent.putExtra("escolhaUsuario", escolhaUsuarioSelecionada)

            startActivityForResult(intent, REQUEST_FILTRAGEM)
        }


        // Verifica se o usuário logado tem um cadastro de anunciante"
        if (tipoConta == "Anunciante") {
            val encontreEstagio = binding.encontreEstagio
            encontreEstagio.text = "Anuncie o seu"
        }


        return view
        val filteredList = mutableListOf<ClassVaga>()
    }

    private fun verificarVagasExpiradas() {
        val database = FirebaseDatabase.getInstance().reference
        val vagasRef = database.child("vagas")

        val dataAtual = Date()

        vagasRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (vagaSnapshot in dataSnapshot.children) {
                    val vaga = vagaSnapshot.getValue(ClassVaga::class.java)
                    if (vaga != null) {
                        val dataFimVagaStr = vaga.dataFim
                        val dataFimVaga = parseData(dataFimVagaStr)

                        // Verifica se a vaga já expirou (dataFim antes da data atual)
                        if (dataFimVaga != null && dataFimVaga.before(dataAtual)) {
                            // A vaga expirou, remova-a do banco de dados
                            vagaSnapshot.ref.removeValue()
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun parseData(dataStr: String): Date? {
        val format = SimpleDateFormat("dd/MM/yyyy")
        try {
            return format.parse(dataStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    private fun filterVagas(
        text: String?,
        areaConhecimento: String?,
        cidade: String?,
        empresa: String?,
        tipoTrabalho: String?
    ) {
        val filteredList = mutableListOf<ClassVaga>()
        val rvVagas: RecyclerView? = binding.rvVagas
        val imageNoResults: ImageView? = binding.imageNoResults
        val texto_semResultados: TextView? = binding.textSemResultados
        val texto_tenteNovamente: TextView? = binding.textTenteNovamente

        for (vaga in vagasList) {
            val tituloVaga = text.isNullOrBlank() || vaga.titulo.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))
            val areaConhecimentoVaga = areaConhecimento.isNullOrBlank() || vaga.areaConhecimento.lowercase(Locale.getDefault()).contains(areaConhecimento.lowercase(Locale.getDefault()))
            val cidadeVaga = cidade.isNullOrBlank() || vaga.cidadeEmpresa.lowercase(Locale.getDefault()).contains(cidade.lowercase(Locale.getDefault()))
            val empresaVaga = empresa.isNullOrBlank() || vaga.empresa.lowercase(Locale.getDefault()).contains(empresa.lowercase(Locale.getDefault()))
            val tipoVaga = tipoTrabalho.isNullOrBlank() || vaga.tipoTrabalho.lowercase(Locale.getDefault()).contains(tipoTrabalho.lowercase(Locale.getDefault()))

            if (tituloVaga || areaConhecimentoVaga || cidadeVaga || empresaVaga || tipoVaga) {
                filteredList.add(vaga)
            }
        }

        if (filteredList.isEmpty()) {
            rvVagas?.visibility = View.GONE
            imageNoResults?.visibility = View.VISIBLE
            texto_semResultados?.visibility = View.VISIBLE
            texto_tenteNovamente?.visibility = View.VISIBLE
        } else {
            rvVagas?.visibility = View.VISIBLE
            imageNoResults?.visibility = View.GONE
            texto_semResultados?.visibility = View.GONE
            texto_tenteNovamente?.visibility = View.GONE
        }

        adapter.filter(filteredList)
        adapter.notifyDataSetChanged()
    }

    private fun filterVagasFiltragem(
        areaConhecimento: String?,
        cidade: String?,
        empresa: String?,
        tipoTrabalho: String?,
        remuneracao: String?,
        escolhaUsuario: String?
    ) {
        val filteredList = mutableListOf<ClassVaga>()
        val rvVagas: RecyclerView? = binding.rvVagas
        val imageNoResults: ImageView? = binding.imageNoResults
        val texto_semResultados: TextView? = binding.textSemResultados
        val texto_tenteNovamente: TextView? = binding.textTenteNovamente

        for (vaga in vagasList) {
            val areaConhecimentoVaga =
                areaConhecimento.isNullOrBlank() || areaConhecimento == "Todos" || vaga.areaConhecimento.lowercase(
                    Locale.getDefault()
                ) == areaConhecimento.lowercase(Locale.getDefault())
            val cidadeVaga =
                cidade.isNullOrBlank() || cidade == "Todos" || vaga.cidadeEmpresa.lowercase(Locale.getDefault()) == cidade.lowercase(
                    Locale.getDefault()
                )
            val empresaVaga =
                empresa.isNullOrBlank() || empresa == "Todos" || vaga.empresa.lowercase(Locale.getDefault()) == empresa.lowercase(
                    Locale.getDefault()
                )
            val tipoVaga =
                tipoTrabalho.isNullOrBlank() || tipoTrabalho == "Todos" || vaga.tipoTrabalho.lowercase(
                    Locale.getDefault()
                ) == tipoTrabalho.lowercase(Locale.getDefault())
            val remuneracaoVaga = when (remuneracao) {
                "Até R$1000" -> {
                    val valor = vaga.pagamento?.let { extractRemuneracaoValor(it) }
                    valor != null && valor <= 1000
                }

                "R$1001 a R$2000" -> {
                    val valor = vaga.pagamento?.let { extractRemuneracaoValor(it) }
                    valor != null && valor in 1001..2000
                }

                "Mais de R$2000" -> {
                    val valor = vaga.pagamento?.let { extractRemuneracaoValor(it) }
                    valor != null && valor > 2000
                }

                else -> true
            }

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            Log.d("LOGUSUARIO", "USUARIO ATUAL:$userId")
            Log.d("LOGUSUARIO", "USUARIO VAGA:${vaga.idAnunciante}")

            // Verifica a escolha do usuário entre "Vagas Gerais" e "Minhas Vagas"
            val vagaElegivel = when (escolhaUsuario) {
                "Vagas Gerais" -> true
                "Minhas Vagas" -> !userId.isNullOrBlank() && vaga.idAnunciante == userId
                else -> true
            }
            Log.d("LOGUSUARIO", "vaga elegivel:$vagaElegivel")

            if (areaConhecimentoVaga && cidadeVaga && empresaVaga && tipoVaga && remuneracaoVaga && vagaElegivel) {
                filteredList.add(vaga)
            }
        }


        if (filteredList.isEmpty()) {
            rvVagas?.visibility = View.GONE
            imageNoResults?.visibility = View.VISIBLE
            texto_semResultados?.visibility = View.VISIBLE
            texto_tenteNovamente?.visibility = View.VISIBLE
        } else {
            rvVagas?.visibility = View.VISIBLE
            imageNoResults?.visibility = View.GONE
            texto_semResultados?.visibility = View.GONE
            texto_tenteNovamente?.visibility = View.GONE
        }

        adapter.filter(filteredList)
        adapter.notifyDataSetChanged()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_FILTRAGEM && resultCode == Activity.RESULT_OK) {
            // Obtém os dados do Intent de retorno
            areaConhecimentoSelecionada = data?.getStringExtra("areaConhecimentoSelecionada")
            cidadeSelecionada = data?.getStringExtra("cidadeSelecionada")
            empresaSelecionada = data?.getStringExtra("empresaSelecionada")
            tipoTrabalhoSelecionado = data?.getStringExtra("tipoTrabalhoSelecionado")
            remuneracaoSelecionada = data?.getStringExtra("remuneracaoSelecionada")
            escolhaUsuarioSelecionada = data?.getStringExtra("escolhaUsuario")
            Log.d("LOGESCOLHAUSUARIO", "AREA PASSADA:$escolhaUsuarioSelecionada")

            // Verifica se os valores dos filtros são diferentes de "todos" e "todas"
            if (cidadeSelecionada != "Todos" || areaConhecimentoSelecionada != "Todos" || empresaSelecionada != "Todos" || tipoTrabalhoSelecionado != "Todos" || remuneracaoSelecionada != "Todos" || escolhaUsuarioSelecionada != "Vagas Gerais") {
                // Chama o método de filtro de vagas com os novos dados
                filterVagasFiltragem(
                    areaConhecimentoSelecionada,
                    cidadeSelecionada,
                    empresaSelecionada,
                    tipoTrabalhoSelecionado,
                    remuneracaoSelecionada,
                    escolhaUsuarioSelecionada
                )
            } else {
                // Mostra todas as vagas cadastradas
                filterVagasFiltragem(null, null, null, null, null,"Vagas Gerais")
            }
        }
    }

    private fun extractRemuneracaoValor(remuneracao: String): Int? {
        val valor = remuneracao.replace("R$", "").replace(".", "").trim().toIntOrNull()
        return valor
    }
    fun String.normalizeAndRemoveAccents(): String {
        val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
        val pattern = "\\p{InCombiningDiacriticalMarks}+".toRegex()
        return pattern.replace(normalized, "").lowercase(Locale.getDefault())
    }
}








