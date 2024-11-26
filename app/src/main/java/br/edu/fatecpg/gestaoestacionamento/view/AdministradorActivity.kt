package br.edu.fatecpg.gestaoestacionamento.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.gestaoestacionamento.R
import br.edu.fatecpg.gestaoestacionamento.adapter.ReservaAdapter
import br.edu.fatecpg.gestaoestacionamento.model.Reserva

class AdministradorActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var recyclerView: RecyclerView
    private lateinit var reservaAdapter: ReservaAdapter
    private lateinit var reservasList: MutableList<Reserva>

    private val TAG = "AdministradorActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_administrador)

        Log.d(TAG, "onCreate: Iniciando AdministradorActivity")

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recyclerViewReservas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        reservasList = mutableListOf()

        // Passando a função de exclusão para o adaptador
        reservaAdapter = ReservaAdapter(reservasList) { reservaId ->
            excluirReserva(reservaId)  // Chama a função para excluir a reserva
        }
        recyclerView.adapter = reservaAdapter

        loadReservas()
    }

    private fun loadReservas() {
        // Recuperar as reservas da coleção "reservas"
        firestore.collection("reservas")
            .orderBy("timestamp", Query.Direction.DESCENDING) // Ordena por timestamp
            .get()
            .addOnSuccessListener { documents ->
                reservasList.clear()
                for (document in documents) {
                    try {
                        val reserva = document.toObject(Reserva::class.java)
                        reserva.id = document.id // Atribuindo o ID da reserva
                        reservasList.add(reserva)
                    } catch (e: Exception) {
                        Log.e(TAG, "Erro ao converter documento para objeto Reserva", e)
                    }
                }
                reservaAdapter.notifyDataSetChanged() // Atualiza o RecyclerView
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Erro ao carregar as reservas", exception)
                Toast.makeText(this, "Erro ao carregar as reservas: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun excluirReserva(reservaId: String) {
        // Deletar o documento da reserva no Firestore
        firestore.collection("reservas").document(reservaId).delete()
            .addOnSuccessListener {
                // Exibir uma mensagem de sucesso
                Toast.makeText(this, "Reserva excluída com sucesso!", Toast.LENGTH_SHORT).show()
                // Atualizar a lista local e o RecyclerView
                val position = reservasList.indexOfFirst { it.id == reservaId }
                if (position != -1) {
                    reservasList.removeAt(position)
                    reservaAdapter.notifyItemRemoved(position)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao excluir reserva: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
