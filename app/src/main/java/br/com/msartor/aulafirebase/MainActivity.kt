package br.com.msartor.aulafirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.msartor.aulafirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val binding by lazy {  ActivityMainBinding.inflate(layoutInflater)   }
    private val autenticacao by lazy {  FirebaseAuth.getInstance() }
    private val bancoDeDados by lazy {  FirebaseFirestore.getInstance() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnExecutar.setOnClickListener {
            //cadastroUsuario()
            //logarUsuario()
            salvarDados()
        }
    }

    private fun salvarDados() {
        val dados = mapOf(
            "nome" to "Vanessa",
            "idade" to "51"
        )

        bancoDeDados
            .collection("usuarios")
            .document("2")
            .set(dados)
            .addOnSuccessListener {
                exibirMensagem("Usuario Salvo com sucesso!")
            }
            .addOnFailureListener { exception ->
                exibirMensagem("Erro ao salvar dados!")
            }

    }


    override fun onStart() {
        super.onStart()
        //verificarUsuarioLogado()
    }

    private fun verificarUsuarioLogado() {
        val usuario = autenticacao.currentUser
        if(usuario != null){
            exibirMensagem("Usuário logado: ${usuario.uid}-${usuario.email}")
            startActivity(
                Intent(this, PrincipalActivity::class.java)
            )
        }else{
            exibirMensagem("Não tem usuário logado")
        }
    }


    private fun logarUsuario() {
        val email = "marcelo.msartor@gmail.com"
        val senha = "12345ms@"

        autenticacao.signInWithEmailAndPassword(email,senha)
            .addOnSuccessListener { result->
                binding.textResultado.text = "Sucesso ao logar usuario!"
                startActivity(
                    Intent(this, PrincipalActivity::class.java)
                )
            }
            .addOnFailureListener { exception ->
                binding.textResultado.text = "Usuario ou senha Incorretos!"

            }

    }


    private fun cadastroUsuario() {
        val email = "marcelo.msartor@gmail.com"
        val senha = "12345ms@"


        autenticacao.createUserWithEmailAndPassword(
            email,senha
        ).addOnSuccessListener { authResult ->
            val email = authResult.user?.email
            val id = authResult.user?.uid

            exibirMensagem("Sucesso ao Cadastrar $id - $email")
            binding.textResultado.text = "Sucessp:  $id - $email"

        }.addOnFailureListener { exception ->
            val mensagemErro = exception.message
           exibirMensagem("Erro ao Cadastrar: $mensagemErro")
            binding.textResultado.text = "Sucessp:  $mensagemErro"
        }

    }

    private fun exibirMensagem(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }
}