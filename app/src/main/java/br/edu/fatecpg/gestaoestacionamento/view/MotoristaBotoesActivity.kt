package br.edu.fatecpg.gestaoestacionamento.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.gestaoestacionamento.databinding.ActivityBotoesAdministradorBinding
import br.edu.fatecpg.gestaoestacionamento.databinding.ActivityBotoesMotoristaBinding

class MotoristaBotoesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBotoesMotoristaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBotoesMotoristaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o botão de Consultar Reservas
        binding.btnConsultar.setOnClickListener {
            val intent = Intent(this, MotoristaListaActivity::class.java)
            startActivity(intent)
        }

        binding.btnCriar.setOnClickListener {
            val intent = Intent(this, MotoristaFormActivity::class.java)
            startActivity(intent)
        }

    }
}
