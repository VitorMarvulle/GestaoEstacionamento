package br.edu.fatecpg.gestaoestacionamento.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.gestaoestacionamento.R
import br.edu.fatecpg.gestaoestacionamento.model.Vaga

class VagaAdapter(
    private val vagas: List<Vaga>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<VagaAdapter.VagaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vaga, parent, false)
        return VagaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VagaViewHolder, position: Int) {
        val vaga = vagas[position]
        holder.bind(vaga, onClick)
    }

    override fun getItemCount(): Int = vagas.size

    class VagaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtNumeroVaga: TextView = itemView.findViewById(R.id.txtNumeroVaga)
        private val txtLocalizacao: TextView = itemView.findViewById(R.id.txtLocalizacao)
        private val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)

        fun bind(vaga: Vaga, onClick: (String) -> Unit) {
            txtNumeroVaga.text = "Vaga nº: ${vaga.numero}"
            txtLocalizacao.text = "Localização: ${vaga.localizacao}"
            txtStatus.text = "Status: ${vaga.status}"
            itemView.setOnClickListener { onClick(vaga.id) }
        }
    }
}
