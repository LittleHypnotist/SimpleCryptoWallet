package com.example.cryptoca.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.cryptoca.CriptoBD.Data
import com.example.cryptoca.adapter.MainAdapter
import com.example.cryptoca.apis.ApiInterface
import com.example.cryptoca.apis.ApiUtilities
import com.example.cryptoca.databinding.FragmentMoedasFavoritasBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoedasFavoritasFragment : Fragment() {

    private lateinit var binding : FragmentMoedasFavoritasBinding
    private lateinit var favorito : ArrayList<String>
    private lateinit var favListItem : ArrayList<Data>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoedasFavoritasBinding.inflate(layoutInflater)

        LerInfo()

        //Está a fazer um pedido API utilizando o retrofit e a interface ApiInterface
        //Se o corpo de resposta não for nulo, dentro do bloco withContext é criada uma nova ArrayList (favListItem)
        //Faz uma verificação para ver se o symbol corresponde, caso sim adiciona ao ArrayList
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getCriptoMercado()

            if (res.body() != null){

                withContext(Dispatchers.Main){
                    favListItem = ArrayList()
                    favListItem.clear()

                    for (verificarInfo in favorito){
                        for (item in res.body()!!.data){
                            if (verificarInfo == item.symbol){
                                favListItem.add(item)
                            }
                        }
                    }
                    binding.spinKitView.visibility = GONE
                    binding.favoritosRecyclerView.adapter = MainAdapter(requireContext(), favListItem, "favoritos")

                }
            }
        }

        return binding.root
    }

    //Lida info de SharedPreferences
    //Obtem a instancia de SharedPreferences com a chave "favoritos" chamando requireContext().getSharedPreferences("favoritos", Context.MODE_PRIVATE)
    //Obtém o valor da chave "favoritos" de SharedPreferences, com um valor por defeito de uma ArrayList de Strings vazia usando .getString("favoritos", ArrayList<String>().toString()))
    //Esta função é utilizada para recuperar a lista de moedas favoritas a partir de SharedPreferences.
    private fun LerInfo() {
        val partilhaInfo = requireContext().getSharedPreferences("favoritos", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = partilhaInfo.getString("favoritos", ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        favorito = gson.fromJson(json, type)

    }


}