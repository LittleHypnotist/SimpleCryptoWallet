package com.example.cryptoca.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cryptoca.CriptoBD.Data
import com.example.cryptoca.R
import com.example.cryptoca.databinding.FragmentDetalhesBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DetalhesFragment : Fragment() {

    lateinit var binding: FragmentDetalhesBinding

    private val item: DetalhesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetalhesBinding.inflate(layoutInflater)

        val data: Data = item.data

        ValoresDetalhes(data)

        InformacoesAdicionais(data)

        Grafico(data)

        CliqueBotao(data)

        AdicionarMoedaFav(data)

        return binding.root
    }

    var favorito : ArrayList<String>? = null
    var MoedaEmFavorio = false

    //Quando é clicado o botão adiciona ou remove a moeda da lista de favoritos e armazena a lista actualizada nas shared preferences
    private fun AdicionarMoedaFav(data: Data) {
        LerInfo()

        MoedaEmFavorio = if (favorito!!.contains(data.symbol)){
            binding.favMoeda.setImageResource(R.drawable.icon_favoritos_com_preenchimento)
            true
        }
        else{
            binding.favMoeda.setImageResource(R.drawable.icon_favoritos_sem_preenchimento)
            false
        }

        binding.favMoeda.setOnClickListener{
            MoedaEmFavorio = if (!MoedaEmFavorio){
                if (!favorito!!.contains(data.symbol)){
                    favorito!!.add(data.symbol)
                }
                storeData()
                binding.favMoeda.setImageResource(R.drawable.icon_favoritos_com_preenchimento)
                true
            }
            else{
                binding.favMoeda.setImageResource(R.drawable.icon_favoritos_sem_preenchimento)
                favorito!!.remove(data.symbol)
                storeData()
                false
            }
        }
    }

    //Função para guardar as criptos adicionadas a lista dos favoritos
    private fun storeData(){
        val partilhaInfo = requireContext().getSharedPreferences("favoritos", Context.MODE_PRIVATE)
        val editor = partilhaInfo.edit()
        val gson = Gson()
        val json = gson.toJson(favorito)
        editor.putString("favoritos", json)
        editor.apply()
    }

    //Ler quais cripto se encontra na lista
    private fun LerInfo() {
        val partilhaInfo = requireContext().getSharedPreferences("favoritos", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = partilhaInfo.getString("favoritos", ArrayList<String>().toString())
        //Esta linha cria um objecto da classe TypeToken que é utilizado para definir o tipo do objecto que está a ser desserializado.
        val type = object : TypeToken<ArrayList<String>>(){}.type
        //Esta linha deserializa a string json numa ArrayList of Strings.
        favorito = gson.fromJson(json, type)

    }

    //Clicar no botão para trocar o espaço temporal
    private fun CliqueBotao(item: Data) {

        val UmMes = binding.botao1minuto
        val UmaSemana = binding.botao1semana
        val UmDia = binding.botao1dia
        val QuatroHoras = binding.botao4horas
        val UmaHora = binding.botao1hora
        val QuinzeMin = binding.botao15minutos

        val CliqueBotao = View.OnClickListener {
            when(it.id){
                UmMes.id -> CarregarInfoGraf(it, "M", item, UmDia, QuinzeMin, UmaSemana, QuatroHoras, UmaHora)
                UmaSemana.id -> CarregarInfoGraf(it, "W", item, UmDia, UmMes, QuinzeMin, QuatroHoras, UmaHora)
                UmDia.id -> CarregarInfoGraf(it, "D", item, QuinzeMin, UmMes, UmaSemana, QuatroHoras, UmaHora)
                QuatroHoras.id -> CarregarInfoGraf(it, "4H", item, UmDia, UmMes, UmaSemana, QuinzeMin, UmaHora)
                UmaHora.id -> CarregarInfoGraf(it, "1H", item, UmDia, UmMes, UmaSemana, QuatroHoras, QuinzeMin)
                QuinzeMin.id -> CarregarInfoGraf(it, "15", item, UmDia, UmMes, UmaSemana, QuatroHoras, UmaHora)
            }
        }

        UmMes.setOnClickListener(CliqueBotao)
        UmaSemana.setOnClickListener(CliqueBotao)
        UmDia.setOnClickListener(CliqueBotao)
        QuatroHoras.setOnClickListener(CliqueBotao)
        UmaHora.setOnClickListener(CliqueBotao)
        QuinzeMin.setOnClickListener(CliqueBotao)

    }

    //Carregar informação especifica da criptomoeda
    //Gráfico carregado é de tradingview.com
    //Chama também a função BotaoDesativado para desactivar a cor de fundo de cinco botões (umDia, umMes, umaSemana, quatroHoras, e umaHora)
    private fun CarregarInfoGraf(
        it: View?,
        s: String,
        item: Data,
        umDia: AppCompatButton,
        umMes: AppCompatButton,
        umaSemana: AppCompatButton,
        quatroHoras: AppCompatButton,
        umaHora: AppCompatButton
    ) {

        BotaoDesativado(umDia,umMes,umaSemana,quatroHoras,umaHora)
        it!!.setBackgroundColor(R.drawable.botao_detalhes)
        //Para o gráfico poder interagir com o utilizador
        binding.graficoWebView.settings.javaScriptEnabled = true
        //Utilizado para melhorar o desempenho em determinadas situações
        binding.graficoWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        binding.graficoWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + item.symbol
                .toString() + "EUR&interval=" + s + "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=" +
                    "F1F3F6&studies=[]&hideideas=1&theme=White&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=" +
                    "[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )

    }

    //Função para manter os botões "desativos"
    private fun BotaoDesativado(umDia: AppCompatButton, umMes: AppCompatButton, umaSemana: AppCompatButton, quatroHoras: AppCompatButton, umaHora: AppCompatButton) {
        umMes.background = null
        umaSemana.background = null
        umDia.background = null
        quatroHoras.background = null
        umaHora.background = null

    }

    //Carregar o gráfico
    private fun Grafico(item: Data) {

        binding.graficoWebView.settings.javaScriptEnabled = true
        binding.graficoWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        binding.graficoWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + item.symbol
                .toString() + "EUR&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=" +
                    "F1F3F6&studies=[]&hideideas=1&theme=White&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=" +
                    "[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )

    }

    //Info sobre moeda
    private fun ValoresDetalhes(data: Data) {
        binding.abreviacaoCripto.text = data.symbol


        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + data.id + ".png"
        ).thumbnail(Glide.with(requireContext()).load(R.drawable.carregar))

            .into(binding.criptoImageView)

        binding.precoTextView.text = "€${String.format("%.02f", data.quote.EUR.price)}"

        if (data.quote.EUR.percent_change_24h > 0) {

            binding.percentagemTextView.text =
                "+${String.format("%.02f", data.quote.EUR.percent_change_24h)}%"
            binding.detailChangeImageView.setImageResource(R.drawable.icon_seta_ganho)

        } else {
            binding.percentagemTextView.text =
                "${String.format("%.02f", data.quote.EUR.percent_change_24h)}%"
            binding.detailChangeImageView.setImageResource(R.drawable.icon_seta_perda)
        }

    }

    //Info adicional
    private fun InformacoesAdicionais(data: Data) {

        binding.MoedasCirculacao.text = "${String.format("%.0f", data.circulating_supply)}"


        if (data.max_supply > 0){
            binding.MaxMoedasCirculacao.text = "${String.format("%.0f", data.max_supply)}"
        }
        else{
            binding.MaxMoedasCirculacao.text = "∞"
        }

        binding.rank.text = "${String.format("%.0f", data.cmc_rank)}"

        binding.MarketCap.text = "${String.format("%.0f", data.quote.EUR.market_cap)}"



    }

}