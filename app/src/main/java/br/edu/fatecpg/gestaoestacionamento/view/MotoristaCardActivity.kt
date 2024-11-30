package br.edu.fatecpg.gestaoestacionamento.view

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.gestaoestacionamento.MainActivity
import br.edu.fatecpg.gestaoestacionamento.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MotoristaCardActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motorista_card)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Inicializa os componentes da UI
        val txtData: TextView = findViewById(R.id.txtData)
        val txtCidade: TextView = findViewById(R.id.txtCidade)
        val txtEstado: TextView = findViewById(R.id.txtEstado)
        val txtNumero: TextView = findViewById(R.id.txtNumero)
        val txtRua: TextView = findViewById(R.id.txtRua)
        val txtNome: TextView = findViewById(R.id.txtNome)
        val txtPlaca: TextView = findViewById(R.id.txtPlaca)
        val txtPreco: TextView = findViewById(R.id.txtPreco)
        val txtTempo: TextView = findViewById(R.id.txtTempo)
        val txtSaudacao: TextView = findViewById(R.id.txtSaudacao)
        val txtTimer: TextView = findViewById(R.id.txtTimer) // Adicionado o txtTimer
        val btnExcluir: Button = findViewById(R.id.btnExcluir) // Botão para excluir a reserva
        val btnSair: Button = findViewById(R.id.btnSair)

        val user = auth.currentUser
        user?.let {
            val uid = it.uid

            // Busca o nome do usuário para exibir na saudação
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val nomeUsuario = document.getString("nome")
                        txtSaudacao.text = "Olá, $nomeUsuario"
                    }
                }

            // Verificar se o motorista tem uma reserva associada ao seu UID
            firestore.collection("reservas")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Exibir os dados da reserva
                        val reserva = document.data
                        val nome = reserva?.get("nome") as? String ?: "Nome não disponível"
                        val placa = reserva?.get("placa") as? String ?: "Placa não disponível"
                        val endereco = reserva?.get("endereco") as? Map<String, String> ?: emptyMap() // Evita NullPointerException
                        val cidade = endereco["cidade"] ?: "Cidade não disponível"
                        val estado = endereco["estado"] ?: "Estado não disponível"
                        val rua = endereco["rua"] ?: "Rua não disponível"
                        val numero = endereco["numero"] ?: "Número não disponível"
                        val data = reserva?.get("data") as? String ?: "Data não disponível"
                        val preco = reserva?.get("preco") as? String ?: "Preço não disponível"
                        val tempo = reserva?.get("tempo") as? String ?: "Tipo de reserva não disponível"
                        val inicioTimestamp = reserva?.get("inicioTimestamp") as? Long ?: 0L
                        val duracaoMinutos = reserva?.get("duracaoMinutos") as? Long ?: 0L

                        // Atribui os dados nas TextViews correspondentes
                        txtNome.text = "Nome: $nome"
                        txtPlaca.text = "Placa: $placa"
                        txtRua.text = "Rua: $rua"
                        txtNumero.text = "Número: $numero"
                        txtCidade.text = "Cidade: $cidade"
                        txtEstado.text = "Estado: $estado"
                        txtData.text = "Data: $data"
                        txtPreco.text = "Preço: $preco"
                        txtTempo.text = "Tipo de Reserva: $tempo"

                        // Configura o timer
                        val tempoRestanteMillis = (inicioTimestamp + (duracaoMinutos * 60 * 1000)) - System.currentTimeMillis()
                        if (tempoRestanteMillis > 0) {
                            iniciarTimer(tempoRestanteMillis, txtTimer)
                        } else {
                            txtTimer.text = "Tempo esgotado!"
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao buscar reserva: ${e.message}", Toast.LENGTH_SHORT).show()
                }

            // Botão para excluir a reserva
            btnExcluir.setOnClickListener {
                firestore.collection("reservas")
                    .document(uid)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Reserva excluída com sucesso!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MotoristaActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao excluir a reserva: ${e.message}", Toast.LENGTH_SHORT).show()
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

    // Método para iniciar o timer
    private fun iniciarTimer(tempoRestanteMillis: Long, txtTimer: TextView) {
        val timer = object : CountDownTimer(tempoRestanteMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val horas = millisUntilFinished / (1000 * 60 * 60) // Calcula as horas
                val minutos = (millisUntilFinished / (1000 * 60)) % 60 // Calcula os minutos restantes
                val segundos = (millisUntilFinished / 1000) % 60 // Calcula os segundos restantes

                // Formata para exibir horas, minutos e segundos
                if (horas > 0) {
                    txtTimer.text = String.format("%02d:%02d:%02d", horas, minutos, segundos)
                } else {
                    txtTimer.text = String.format("%02d:%02d", minutos, segundos)
                }
            }

            override fun onFinish() {
                txtTimer.text = "Tempo esgotado!"
            }
        }
        timer.start()
    }

}
