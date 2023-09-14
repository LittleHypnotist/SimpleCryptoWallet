package com.example.cryptoca.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.cryptoca.adapter.TopDezMoedasAdapter
import com.example.cryptoca.adapter.TopGanhosPerdasAdapter
import com.example.cryptoca.apis.ApiInterface
import com.example.cryptoca.apis.ApiUtilities
import com.example.cryptoca.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.view.View.GONE
import android.view.View.VISIBLE
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        getPrecoMoedas()

        TopMoedasGanhosPerdas()

        return binding.root

    }

    //Fazer uma chamada API e atualizar o interface com os dados
    private fun getPrecoMoedas() {
        lifecycleScope.launch(Dispatchers.IO) {
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getCriptoMercado()

            withContext(Dispatchers.Main) {
                binding.topMoedasHome.adapter =
                    TopDezMoedasAdapter(requireContext(), res.body()!!.data)
            }

        }

    }

    private fun TopMoedasGanhosPerdas() {

        val adapter = TopGanhosPerdasAdapter(this)
        binding.listaTopDezCripto.adapter = adapter

        binding.listaTopDezCripto.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    binding.topGanhos.visibility = VISIBLE
                    binding.topPerdas.visibility = GONE
                } else {
                    binding.topGanhos.visibility = GONE
                    binding.topPerdas.visibility = VISIBLE
                }
            }

        })


        TabLayoutMediator(binding.tabLayout, binding.listaTopDezCripto) { tab, position ->

            var topGP: String

            if (position == 0) {
                topGP = "TOP 5 GANHOS"
            } else {
                topGP = "TOP 5 PERDAS"
            }
            tab.text = topGP
        }.attach()


    }


}