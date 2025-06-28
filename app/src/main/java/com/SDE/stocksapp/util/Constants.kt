package com.example.newsapp.util

class Constants {
    companion object {
        const val API_KEY = "your_api_key_here" // Remove this before pushing on github
        const val BASE_URL = "https://www.alphavantage.co"

        enum class TimePeriod { ONE_DAY, ONE_WEEK, ONE_MONTH, THREE_MONTHS, SIX_MONTHS, ONE_YEAR }

    }
}