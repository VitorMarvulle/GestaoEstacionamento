package br.edu.fatecpg.gestaoestacionamento.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.gestaoestacionamento.R
import br.edu.fatecpg.gestaoestacionamento.model.Reserva

class ReservaAdapter(
    private val reservas: MutableList<Reserva>,
    private val onDeleteClickListener: (String) -> Unit // Função para o clique de exclusão
) : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {

    // ViewHolder para a consulta
    class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNome: TextView = itemView.findViewById(R.id.txtNome)
        val txtData: TextView = itemView.findViewById(R.id.txtData)
        val txtCidade: TextView = itemView.findViewById(R.id.txtCidade)
        val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)
        val txtNumero: TextView = itemView.findViewById(R.id.txtNumero)
        val txtRua: TextView = itemView.findViewById(R.id.txtRua)
        val txtPlaca: TextView = itemView.findViewById(R.id.txtPlaca)
        val btnExcluir: Button = itemView.findViewById(R.id.btnExcluir) // Botão de excluir
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_consulta, parent, false)
        return ReservaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]
        holder.txtNome.text = reserva.nome
        holder.txtData.text = reserva.data
        holder.txtCidade.text = reserva.cidade
        holder.txtEstado.text = reserva.estado
        holder.txtNumero.text = reserva.numero
        holder.txtRua.text = reserva.rua
        holder.txtPlaca.text = reserva.placa

        // Configura o clique do botão de excluir
        holder.btnExcluir.setOnClickListener {
            // Invoca a função de exclusão passando o ID da reserva
            onDeleteClickListener(reserva.id)
        }
    }

    override fun getItemCount(): Int {
        return reservas.size
    }

    // Método para remover a reserva da lista local e atualizar o RecyclerView
    fun removeItem(position: Int) {
        reservas.removeAt(position)
        notifyItemRemoved(position)
    }
}
