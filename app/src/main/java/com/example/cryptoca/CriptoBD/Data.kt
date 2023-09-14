package com.example.cryptoca.CriptoBD

import java.io.Serializable

data class Data(
    val circulating_supply: Double,
    val cmc_rank: Double,
    val date_added: String,
    val id: Int,
    val last_updated: String,
    val max_supply: Double,
    val name: String,
    val num_market_pairs: Int,
    val platform: Platform,
    val quote: Quote,
    val self_reported_circulating_supply: Double,
    val self_reported_market_cap: Double,
    val slug: String,
    val symbol: String,
    val tags: List<String>,
    val total_supply: Double,
    val tvl_ratio: Double
) : Serializable{
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}