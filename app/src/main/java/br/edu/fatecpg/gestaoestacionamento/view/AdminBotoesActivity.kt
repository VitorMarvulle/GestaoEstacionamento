package br.edu.fatecpg.gestaoestacionamento.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.gestaoestacionamento.databinding.ActivityBotoesAdministradorBinding

class AdminBotoesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBotoesAdministradorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBotoesAdministradorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o botão de Consultar Reservas
        binding.btnConsultar.setOnClickListener {
            val intent = Intent(this, AdminListaActivity::class.java)
            startActivity(intent)
        }

        binding.btnCriar.setOnClickListener {
            val intent = Intent(this, AdminFormActivity::class.java)
            startActivity(intent)
        }

    }
}
