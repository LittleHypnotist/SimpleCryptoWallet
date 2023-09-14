package com.example.cryptoca.fragment

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
import com.example.cryptoca.databinding.FragmentTopMoedasGanhosPerdasBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections

class TopMoedasGanhosPerdasFragment : Fragment() {

    private lateinit var binding: FragmentTopMoedasGanhosPerdasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTopMoedasGanhosPerdasBinding.inflate(layoutInflater)

        getTopMoedasGP()

        return binding.root
    }

    //obtem o valor da posição passada como argumento utilizando requireArguments().getInt("position")
    //no seu interior faz um pedido GET a um API utilizando o Retrofit e a interface ApiInterface.
    private fun getTopMoedasGP() {
        val position = requireArguments().getInt("position")

        lifecycleScope.launch(Dispatchers.IO){

            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getCriptoMercado()

            if (res.body() != null){
                withContext(Dispatchers.Main){
                    val dataItem = res.body()!!.data

                    //atribui a propriedade dos dados do corpo de resposta à variável dataItem, ordena-a por percentagem_change_24h de EUR
                    Collections.sort(dataItem){
                        o1,o2 -> (o2.quote.EUR.percent_change_24h.toInt()).compareTo(o1.quote.EUR.percent_change_24h.toInt())
                    }

                    binding.spinKitView.visibility = GONE
                    val lista = ArrayList<Data>()

                    //Se a posição for 0, limpa a lista ArrayList
                    //adiciona a moeda da lista DataItem no índice actual à lista ArrayList
                    if (position == 0){
                        lista.clear()
                        for (i in 0..4){
                            lista.add(dataItem[i])
                        }

                        binding.topMoedasGanhosPerdasRecyclerView.adapter = MainAdapter(requireContext(), lista, "home")
                    }
                    //Se a posição não for 0, limpa a lista ArrayList
                    //adiciona a moeda da lista dataItem no índice dataItem.size-1-i à lista ArrayList
                    else{
                        lista.clear()
                        for (i in 0..4){
                            lista.add(dataItem[dataItem.size-1-i])
                        }

                        binding.topMoedasGanhosPerdasRecyclerView.adapter = MainAdapter(requireContext(), lista, "home")

                    }

                }
            }

        }
    }


}