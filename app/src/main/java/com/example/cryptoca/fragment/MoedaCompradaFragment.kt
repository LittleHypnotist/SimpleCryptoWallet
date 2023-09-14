package com.example.cryptoca.fragment


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.cryptoca.CriptoBD.Data
import com.example.cryptoca.adapter.MoedaCompradaAdapter
import com.example.cryptoca.apis.ApiInterface
import com.example.cryptoca.apis.ApiUtilities
import com.example.cryptoca.databinding.FragmentMoedaCompradaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList



class MoedaCompradaFragment : Fragment() {

    private lateinit var binding: FragmentMoedaCompradaBinding

    private lateinit var list: List<Data>
    private lateinit var adapter: MoedaCompradaAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentMoedaCompradaBinding.inflate(layoutInflater)


        list = listOf()

        adapter = MoedaCompradaAdapter(requireContext(), list, "compras")
        binding.listaCriptoRecyclerView.adapter = adapter

        //Faz pedido a API utilizando o retrofit e a interface APIInterface
        //Se a resposta não for nula esta atualiza uma lista de dados
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getCriptoMercado()

            if (res.body() != null){
                withContext(Dispatchers.Main){
                    list = res.body()!!.data

                    adapter.updateData(list)
                    binding.spinKitViewThreeBounce.visibility = View.GONE
                }
            }
        }

        procurarMoeda()

        return binding.root
    }

    //Quando o texto da EditText muda o método afterTextChanged
    //Nesse método a variavel procurarNome está a ser atualizada para letras minusculas para a comparação ser mais fácil
    //De seguida é atualizada a lista com o metodo updateRecyclerView
    lateinit var procurarNome : String
    private fun procurarMoeda() {
        binding.procurarCriptoText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                procurarNome = s.toString().toLowerCase()

                updateRecyclerView()
            }
        })
    }

    //Filtragem
    private fun updateRecyclerView() {
        val data = ArrayList<Data>()

        for (item in list){
            val nomeMoeda = item.name.lowercase(Locale.getDefault())
            val simboloMoeda = item.symbol.lowercase(Locale.getDefault())

            if (nomeMoeda.contains(procurarNome) || simboloMoeda.contains(procurarNome)){
                data.add(item)
            }
        }

        adapter.updateData(data)
    }



}