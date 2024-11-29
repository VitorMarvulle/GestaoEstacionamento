package br.edu.fatecpg.gestaoestacionamento.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.gestaoestacionamento.R
import br.edu.fatecpg.gestaoestacionamento.adapter.ReservaAdapter
import br.edu.fatecpg.gestaoestacionamento.model.Reserva
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AdministradorActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var recyclerView: RecyclerView
    private lateinit var reservaAdapter: ReservaAdapter
    private lateinit var reservasList: MutableList<Reserva>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_administrador)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recyclerViewReservas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        reservasList = mutableListOf()

        reservaAdapter = ReservaAdapter(reservasList) { reservaId ->
            excluirReserva(reservaId) // Chama a função para excluir a reserva
        }
        recyclerView.adapter = reservaAdapter

        loadReservas()
    }

    private fun loadReservas() {
        firestore.collection("reservas")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                reservasList.clear()
                for (document in documents) {
                    try {
                        val reserva = document.toObject(Reserva::class.java)
                        reserva.id = document.id
                        reserva.inicioTimestamp = document.getLong("inicioTimestamp") ?: 0L
                        reserva.duracaoMinutos = (document.getLong("duracaoMinutos") ?: 0L).toInt()
                        reservasList.add(reserva)
                    } catch (e: Exception) {
                        Log.e("AdministradorActivity", "Erro ao converter documento", e)
                    }
                }
                reservaAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao carregar reservas: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun excluirReserva(reservaId: String) {
        firestore.collection("reservas").document(reservaId).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Reserva excluída!", Toast.LENGTH_SHORT).show()
                val position = reservasList.indexOfFirst { it.id == reservaId }
                if (position != -1) {
                    reservasList.removeAt(position)
                    reservaAdapter.notifyItemRemoved(position)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao excluir: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
