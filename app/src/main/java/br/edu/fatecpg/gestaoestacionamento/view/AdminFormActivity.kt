package br.edu.fatecpg.gestaoestacionamento.view

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.gestaoestacionamento.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import java.util.*

class AdminFormActivity : AppCompatActivity() {

    private lateinit var edtCidadeVaga: EditText
    private lateinit var edtEstadoVaga: EditText
    private lateinit var edtRuaVaga: EditText
    private lateinit var edtNumeroEnderecoVaga: EditText
    private lateinit var btnCriarVaga: Button
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_admin)

        // Inicializar Firestore
        firestore = FirebaseFirestore.getInstance()

        // Inicializa os componentes da UI
        edtCidadeVaga = findViewById(R.id.edtCidadeVaga)
        edtEstadoVaga = findViewById(R.id.edtEstadoVaga)
        edtRuaVaga = findViewById(R.id.edtRuaVaga)
        edtNumeroEnderecoVaga = findViewById(R.id.edtNumeroEnderecoVaga)
        btnCriarVaga = findViewById(R.id.btnCriarVaga)

        // Clique no botão "Criar Vaga"
        btnCriarVaga.setOnClickListener {
            // Recupera os dados dos campos
            val cidade = edtCidadeVaga.text.toString()
            val estado = edtEstadoVaga.text.toString()
            val rua = edtRuaVaga.text.toString()
            val numeroEndereco = edtNumeroEnderecoVaga.text.toString()

            // Verifica se todos os campos estão preenchidos
            if ( TextUtils.isEmpty(cidade) || TextUtils.isEmpty(estado) ||
                TextUtils.isEmpty(rua) || TextUtils.isEmpty(numeroEndereco)) {
                Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cria o objeto Vaga
            val vaga = hashMapOf(
                "endereco" to mapOf(
                    "cidade" to cidade,
                    "estado" to estado,
                    "rua" to rua,
                    "numero" to numeroEndereco
                ),
                "status" to "Disponível",
                "timestamp" to Timestamp(Date())
            )

            // Salva a vaga no Firestore
            firestore.collection("vagas")
                .add(vaga)
                .addOnSuccessListener {
                    Toast.makeText(this, "Vaga cadastrada com sucesso!", Toast.LENGTH_SHORT).show()

                    // Limpar os campos após o cadastro
                    edtCidadeVaga.text.clear()
                    edtEstadoVaga.text.clear()
                    edtRuaVaga.text.clear()
                    edtNumeroEnderecoVaga.text.clear()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao cadastrar vaga: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
