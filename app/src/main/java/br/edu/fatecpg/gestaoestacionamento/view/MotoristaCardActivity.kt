package br.edu.fatecpg.gestaoestacionamento.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        val txtPlaca: TextView = findViewById(R.id.txtPlaca)
        val txtSaudacao: TextView = findViewById(R.id.txtSaudacao) // Adiciona a TextView da saudação

        val user = auth.currentUser
        user?.let {
            val uid = it.uid

            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val nomeUsuario = document.getString("nome")


                        // Configura a saudação
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
                        val placa = reserva?.get("placa") as String
                        val endereco = reserva["endereco"] as Map<String, String>
                        val cidade = endereco["cidade"]
                        val estado = endereco["estado"]
                        val rua = endereco["rua"]
                        val numero = endereco["numero"]
                        val data = reserva["data"] as String

                        // Atribui os dados nas TextViews correspondentes
                        txtPlaca.text = "Placa: $placa"
                        txtRua.text = "Rua: $rua"
                        txtNumero.text = "Número: $numero"
                        txtCidade.text = "Cidade: $cidade"
                        txtEstado.text = "Estado: $estado"
                        txtData.text = "Data: $data"
                    } else {
                        // Caso não tenha reserva, mostre uma mensagem e redirecione
                        Toast.makeText(this, "Você não tem nenhuma reserva.", Toast.LENGTH_SHORT).show()
                        // Redireciona para a tela de criação de reserva
                        val intent = Intent(this, MotoristaActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao buscar reserva: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
