package br.edu.fatecpg.gestaoestacionamento.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.gestaoestacionamento.MainActivity
import br.edu.fatecpg.gestaoestacionamento.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Timestamp
import java.util.*

class MotoristaActivity : AppCompatActivity() {

    private lateinit var edtNome: EditText
    private lateinit var edtTel: EditText
    private lateinit var edtData: EditText
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
        edtNome = findViewById(R.id.edtNome)
        edtTel = findViewById(R.id.edtTel)
        edtData = findViewById(R.id.edtData)
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
            val nomePaciente = edtNome.text.toString()
            val telefonePaciente = edtTel.text.toString()
            val dataAgendamento = edtData.text.toString()
            val placaVeiculo = edtPlaca.text.toString()
            val cidade = edtCidade.text.toString()
            val estado = edtEstado.text.toString()
            val numero = edtNumero.text.toString()
            val rua = edtRua.text.toString()

            // Verifica se todos os campos estão preenchidos
            if (TextUtils.isEmpty(nomePaciente) || TextUtils.isEmpty(telefonePaciente) ||
                TextUtils.isEmpty(dataAgendamento) || TextUtils.isEmpty(placaVeiculo) ||
                TextUtils.isEmpty(cidade) || TextUtils.isEmpty(estado) ||
                TextUtils.isEmpty(numero) || TextUtils.isEmpty(rua)) {
                Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cria o map de dados para salvar no Firestore
            val reserva = hashMapOf(
                "nome" to nomePaciente,
                "telefone" to telefonePaciente,
                "data" to dataAgendamento,
                "placa" to placaVeiculo,
                "endereco" to mapOf(
                    "cidade" to cidade,
                    "estado" to estado,
                    "numero" to numero,
                    "rua" to rua
                ),
                "timestamp" to Timestamp(Date()) // Adiciona o timestamp do agendamento
            )

            // Salva os dados na coleção "reservas" no Firestore
            firestore.collection("reservas")
                .add(reserva)
                .addOnSuccessListener {
                    Toast.makeText(this, "Reserva criada com sucesso!", Toast.LENGTH_SHORT).show()
                    // Limpar os campos após o agendamento
                    edtNome.text.clear()
                    edtTel.text.clear()
                    edtData.text.clear()
                    edtPlaca.text.clear()
                    edtCidade.text.clear()
                    edtEstado.text.clear()
                    edtNumero.text.clear()
                    edtRua.text.clear()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao reservar a vaga: ${e.message}", Toast.LENGTH_SHORT).show()
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
