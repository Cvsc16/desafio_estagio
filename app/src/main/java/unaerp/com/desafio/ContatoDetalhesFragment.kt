package unaerp.com.desafio

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ContatoFragment : Fragment() {

    private val requestPhoneCall = 1
    private lateinit var textViewtelefone: TextView
    private lateinit var textViewemail: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contato, container, false)

        textViewtelefone = view.findViewById(R.id.numeroTelefone)
        textViewemail= view.findViewById(R.id.emailEmpresa)
        val emailContato: ConstraintLayout = view.findViewById(R.id.fragment_email)
        val telefoneContato: ConstraintLayout = view.findViewById(R.id.fragment_telefone)

        val vaga = arguments?.getSerializable("vaga") as ClassVaga?
        vaga?.let {
            val telefone = vaga.telefone
            textViewtelefone.text = telefone

            val email = vaga.emailEmpresa
            textViewemail.text = email

        }

        telefoneContato.setOnClickListener {
            val phoneNumber = textViewtelefone.text.toString().trim()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")

            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    // Exibe a mensagem de que a permissão é necessária para fazer ligações
                    AlertDialog.Builder(requireContext())
                        .setTitle("Permissão necessária")
                        .setMessage("A permissão para fazer ligações é necessária para usar esta função. Por favor, habilite a permissão!")
                        .setPositiveButton("OK") { _, _ ->
                            // Solicita a permissão
                            ActivityCompat.requestPermissions(
                                requireActivity(),
                                arrayOf(Manifest.permission.CALL_PHONE),
                                requestPhoneCall
                            )
                        }
                        .setNegativeButton("Cancelar", null)
                        .create()
                        .show()
                } else {
                    // Solicita a permissão
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CALL_PHONE),
                        requestPhoneCall
                    )
                }
            } else {
                // Inicia a chamada
                startActivity(intent)
            }
        }

        emailContato.setOnClickListener {
            val recipient =  textViewemail.text.toString().trim()
            val message = "Olá, vi a publicação da sua vaga de estágio na DC Estágios e gostaria de me candidatar"
            val uri = Uri.parse("mailto:$recipient?body=${Uri.encode(message)}")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            startActivity(intent)
        }

        return view
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == requestPhoneCall) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida
                val phoneNumber = textViewtelefone.text.toString().trim()
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$phoneNumber")
                startActivity(intent)
            } else {
                // Permissão negada
                Toast.makeText(requireContext(), "Permissão de chamada telefônica negada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

