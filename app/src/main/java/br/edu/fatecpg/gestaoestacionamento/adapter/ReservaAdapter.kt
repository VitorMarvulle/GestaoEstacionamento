package br.edu.fatecpg.gestaoestacionamento.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.gestaoestacionamento.R
import br.edu.fatecpg.gestaoestacionamento.model.Reserva

class ReservaAdapter(private val reservas: List<Reserva>) :
    RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {

    // ViewHolder para a consulta
    class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNome: TextView = itemView.findViewById(R.id.txtNome)
        val txtTelefone: TextView = itemView.findViewById(R.id.txtTelefone)
        val txtData: TextView = itemView.findViewById(R.id.txtData)
        val txtCidade: TextView = itemView.findViewById(R.id.txtCidade)
        val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)
        val txtNumero: TextView = itemView.findViewById(R.id.txtNumero)
        val txtRua: TextView = itemView.findViewById(R.id.txtRua)
        val txtPlaca: TextView = itemView.findViewById(R.id.txtPlaca)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_consulta, parent, false)
        return ReservaViewHolder(itemView)
    }

        override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
            val reserva = reservas[position]
            holder.txtNome.text = reserva.nome
            holder.txtTelefone.text = reserva.telefone
            holder.txtData.text = reserva.data
            holder.txtCidade.text = reserva.cidade
            holder.txtEstado.text = reserva.estado
            holder.txtNumero.text = reserva.numero
            holder.txtRua.text = reserva.rua
            holder.txtPlaca.text = reserva.placa

        }

    override fun getItemCount(): Int {
        return reservas.size
    }


}