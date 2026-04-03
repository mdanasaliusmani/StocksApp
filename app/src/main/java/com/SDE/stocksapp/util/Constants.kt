package com.SDE.stocksapp.util

class Constants {
    companion object {
        const val API_KEY = "PFFOYIBX37Z89SZI" // Remove this before pushing on github
        const val BASE_URL = "https://www.alphavantage.co"

        enum class TimePeriod { ONE_DAY, ONE_WEEK, ONE_MONTH, THREE_MONTHS, SIX_MONTHS, ONE_YEAR }

    }
}