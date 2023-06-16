package unaerp.com.desafio

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import unaerp.com.desafio.databinding.FragmentNovavagaBinding
import unaerp.com.desafio.databinding.FragmentPerfilBinding


class FragmentPerfil : Fragment() {
    private lateinit var binding: FragmentPerfilBinding
    private var nomeInicial: String = ""
    private var emailInicial: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        val view = binding.root

        val logout = binding.logout
        val img_perfil = binding.imgFotoUsuario
        val back = binding.back
        val editar_senha = binding.imgEditarSenha
        val excluir_conta = binding.imgExcluirConta
        val nome = binding.cadastroNome
        val email = binding.cadastroEmail
        val btn_salvar = binding.btnSalvar


        val tipoUsuario = arguments?.getString("tipoUsuario")

        if (tipoUsuario == "Anunciante") {
            img_perfil.setImageResource(R.drawable.logo_empresa)
            val layoutParams = img_perfil.layoutParams
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.width_logo_empresa)
            layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_logo_empresa)
            img_perfil.layoutParams = layoutParams
        }


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nomeAtual = nome.text.toString().trim()
                val emailAtual = email.text.toString().trim()

                val enableButton = nomeAtual != nomeInicial || emailAtual != emailInicial
                btn_salvar.isEnabled = enableButton

                val colorResId = if (enableButton) R.color.principal else R.color.detalhe
                btn_salvar.setBackgroundColor(ContextCompat.getColor(view.context, colorResId))
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }


        nome.addTextChangedListener(textWatcher)
        email.addTextChangedListener(textWatcher)

        btn_salvar.isEnabled = false
        btn_salvar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.detalhe))

        btn_salvar.setOnClickListener {
            val nomeAtual = nome.text.toString().trim()
            val emailAtual = email.text.toString().trim()

            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid

            val database = FirebaseDatabase.getInstance().reference
            val userRef = database.child("users").child(userId!!)

            val userData = hashMapOf<String, Any>(
                "nome" to nomeAtual,
                "email" to emailAtual
            )

            userRef.updateChildren(userData)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Dados atualizados com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()

                    val user = FirebaseAuth.getInstance().currentUser
                    val newName = nomeAtual

                    val vagaReference = FirebaseDatabase.getInstance().reference.child("vagas")
                    val query = vagaReference.orderByChild("idAnunciante").equalTo(user?.uid)

                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (vagaSnapshot in snapshot.children) {
                                val vaga = vagaSnapshot.getValue(ClassVaga::class.java)

                                val fullName = newName
                                val names = fullName.split(" ")
                                val firstName = names.getOrElse(0) { "" }
                                val lastName = names.getOrElse(1) { "" }
                                val abbreviatedName = "$firstName $lastName"


                                vaga?.empresa = abbreviatedName

                                vagaSnapshot.ref.setValue(vaga)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d(
                                "LOGEMPRESANOME2ERRORR",
                                "Erro na consulta de vagas no Firebase: ${error.message}"
                            )
                        }
                    })
                    updateProfileFields(nomeAtual)
                    btn_salvar.isEnabled = false
                    btn_salvar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.detalhe))
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        requireContext(),
                        "Erro ao atualizar os dados: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        editar_senha.setOnClickListener {
            val intent = Intent(requireContext(), RedefinirSenhaActivity::class.java)
            startActivity(intent)
        }

        excluir_conta.setOnClickListener {
            // Exibe um diálogo de confirmação para o usuário
            AlertDialog.Builder(requireContext())
                .setTitle("Excluir conta")
                .setMessage("Tem certeza que deseja excluir sua conta?")
                .setPositiveButton("Sim") { dialog, _ ->
                    // Solicita a senha para autenticação
                    val inputPassword = EditText(requireContext())
                    inputPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

                    AlertDialog.Builder(requireContext())
                        .setTitle("Digite sua senha")
                        .setView(inputPassword)
                        .setPositiveButton("Confirmar") { dialog, _ ->
                            val password = inputPassword.text.toString()

                            // Autentica a senha no Firebase
                            val user = FirebaseAuth.getInstance().currentUser
                            val credential = EmailAuthProvider.getCredential(user?.email ?: "", password)
                            user?.reauthenticate(credential)
                                ?.addOnSuccessListener {
                                    // Senha correta, remove a conta do Firebase
                                    val database = FirebaseDatabase.getInstance()
                                    val userRef = database.getReference("users").child(user?.uid ?: "")

                                    userRef.removeValue()
                                        .addOnSuccessListener {
                                            // Dados do usuário excluídos com sucesso, agora exclui a conta
                                            user?.delete()
                                                ?.addOnSuccessListener {
                                                    // Conta excluída com sucesso, retorna para a tela de login
                                                    Toast.makeText(requireContext(), "Conta excluída com sucesso", Toast.LENGTH_SHORT).show()
                                                    val intent = Intent(requireContext(), LoginActivity::class.java)
                                                    startActivity(intent)
                                                    requireActivity().finish()
                                                }
                                                ?.addOnFailureListener { exception ->
                                                    // Falha ao excluir a conta, exibe uma mensagem de erro
                                                    Toast.makeText(requireContext(), "Erro ao excluir a conta: ${exception.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                        ?.addOnFailureListener { exception ->
                                            // Falha ao excluir os dados do usuário, exibe uma mensagem de erro
                                            Toast.makeText(requireContext(), "Erro ao excluir os dados do usuário: ${exception.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                ?.addOnFailureListener { exception ->
                                    // Senha incorreta, exibe uma mensagem de erro
                                    Toast.makeText(requireContext(), "Senha incorreta", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .setNegativeButton("Cancelar", null)
                        .show()
                }
                .setNegativeButton("Não", null)
                .show()
        }

        back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        logout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Sair")
            builder.setMessage("Tem certeza que deseja sair?")
            builder.setPositiveButton("Sim") { dialog, _ ->
                // Realiza o logout
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
                Log.d("LOGOUTPERFIL", "REALIZOU O LOGIN")
                requireActivity().finish()
            }
            builder.setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        return view
    }

    private fun updateProfileFields(fullName: String) {
        val nome = binding.cadastroNome
        val nome2 = binding.cadastro
        val names = fullName.split(" ")
        val firstName = names.getOrElse(0) { "" }
        val lastName = names.getOrElse(1) { "" }
        val abbreviatedName = "$firstName $lastName"

        nome2?.text = abbreviatedName
        nome?.setText(fullName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nome = binding.cadastroNome
        val nome2 = binding.cadastro
        val email = binding.cadastroEmail

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        val database = FirebaseDatabase.getInstance().reference.child("users").child(userId!!)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(ClassUser::class.java)
                    userData?.let { user ->
                        val fullName = user.nome
                        val userEmail = user.email
                        val btn_salvar = binding.btnSalvar

                        // Extrai os dois primeiros nomes
                        updateProfileFields(fullName)

                        email.setText(userEmail)
                        // Salve os valores iniciais dos campos de nome e email
                        nomeInicial = user.nome
                        emailInicial = user.email

                        // Habilita ou desabilita o botão com base nos valores iniciais
                        val enableButton = nome.text.toString().trim() != nomeInicial ||
                                email.text.toString().trim() != emailInicial
                        btn_salvar.isEnabled = enableButton

                        // Define a cor de fundo com base no estado do botão
                        val colorResId = if (enableButton) R.color.principal else R.color.detalhe
                        btn_salvar.setBackgroundColor(ContextCompat.getColor(requireContext(), colorResId))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Trate o erro ao buscar os dados do usuário, se necessário
            }
        })



        // Restante do código...
    }

}



