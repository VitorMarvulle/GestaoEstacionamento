package br.edu.fatecpg.gestaoestacionamento.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.gestaoestacionamento.MainActivity
import br.edu.fatecpg.gestaoestacionamento.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class MotoristaActivity : AppCompatActivity() {

    private lateinit var edtPlaca: EditText
    private lateinit var edtCidade: EditText
    private lateinit var edtEstado: EditText
    private lateinit var edtNumero: EditText
    private lateinit var edtRua: EditText
    private lateinit var btnCriarAgendamento: Button
    private lateinit var btnSair: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_motorista)

        // Inicializar Firestore e FirebaseAuth
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Inicializa os componentes da UI
        edtPlaca = findViewById(R.id.edtPlacaVeiculo)
        edtCidade = findViewById(R.id.edtCidade)
        edtEstado = findViewById(R.id.edtEstado)
        edtNumero = findViewById(R.id.edtNumero)
        edtRua = findViewById(R.id.edtRua)
        btnCriarAgendamento = findViewById(R.id.btnCadastro)
        btnSair = findViewById(R.id.btnSair)

        // Clique no botão "Criar Agendamento"
        btnCriarAgendamento.setOnClickListener {
            // Recupera os dados dos campos
            val placaVeiculo = edtPlaca.text.toString()
            val cidade = edtCidade.text.toString()
            val estado = edtEstado.text.toString()
            val numero = edtNumero.text.toString()
            val rua = edtRua.text.toString()

            // Verifica se todos os campos estão preenchidos
            if (
                TextUtils.isEmpty(placaVeiculo) ||
                TextUtils.isEmpty(cidade) || TextUtils.isEmpty(estado) ||
                TextUtils.isEmpty(numero) || TextUtils.isEmpty(rua)) {
                Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Recupera o uid do usuário autenticado
            val user = auth.currentUser
            user?.let {
                val uid = it.uid

                // Recupera o nome do usuário do Firestore
                firestore.collection("users").document(uid).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val nomeUsuario = document.getString("nome")
                            Log.d("MotoristaActivity", "Nome do usuário do Firestore: $nomeUsuario")

                            // Verifica se o nome do usuário é vazio ou null
                            val nomeFinal = nomeUsuario?.takeIf { it.isNotBlank() } ?: "Usuário Anônimo"
                            Log.d("MotoristaActivity", "Nome final que será salvo: $nomeFinal") // Log do nome final

                            val timestamp = Timestamp(Date()) // Marca a data e hora da reserva

                            // Formatar a data para o formato dd/MM/yy
                            val simpleDateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                            val formattedDate = simpleDateFormat.format(timestamp.toDate()) // Converte para Date e formata

                            Log.d("MotoristaActivity", "Data formatada: $formattedDate") // Verifica a data formatada

                            // Cria o map de dados para salvar no Firestore
                            val reserva = hashMapOf(
                                "nome" to nomeFinal,  // Usa o nome do usuário, ou "Usuário Anônimo" caso seja nulo ou vazio
                                "placa" to placaVeiculo,
                                "data" to formattedDate,  // Adiciona a data formatada
                                "endereco" to mapOf(
                                    "cidade" to cidade,
                                    "estado" to estado,
                                    "numero" to numero,
                                    "rua" to rua
                                ),
                                "timestamp" to timestamp // Adiciona o timestamp do agendamento
                            )

                            // Salva a reserva associada ao UID do usuário como o ID do documento
                            firestore.collection("reservas")
                                .document(uid)  // Usando o UID como o ID do documento
                                .set(reserva)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Reserva criada com sucesso!", Toast.LENGTH_SHORT).show()
                                    // Limpar os campos após o agendamento
                                    edtPlaca.text.clear()
                                    edtCidade.text.clear()
                                    edtEstado.text.clear()
                                    edtNumero.text.clear()
                                    edtRua.text.clear()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Erro ao reservar a vaga: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Log.d("MotoristaActivity", "Usuário não encontrado no Firestore")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("MotoristaActivity", "Erro ao recuperar dados do Firestore: ${e.message}")
                    }
            }
        }

        // Lógica do botão "Sair"
        btnSair.setOnClickListener {
            // Realiza o logout do Firebase
            auth.signOut()

            // Redireciona para a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
