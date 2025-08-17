package com.example.vardadienas.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.io.IOException

// Data class for storing web scraped data from PMLP
data class PersonNameData (
    val name: String, // Name of person to be searched
    val amount: Int, // Amount of people with that name
    val nameDay: String, // DD.MM name day date
    val explanation: String // Some names have explanations written for them
)

class PMLPrepository {
    private val client = OkHttpClient()
    private val baseUrl = "https://personvardi.pmlp.gov.lv/"

    /**
     * Fetches person name data by performing a two-step scrape.
     * 1. Searches for the name to get a list and a details link.
     * 2. Fetches the details page to get the final data.
     *
     * @param personName The name to search for (e.g., "Atvars").
     * @return A Result object containing PersonNameData on success or an Exception on failure.
     */
    suspend fun fetchPersonNameData(personName: String): Result<PersonNameData> {
        // Run the entire network and parsing operation on a background thread.
        return withContext(Dispatchers.IO) {
            try {
                // --- STAGE 1: Search for the name and find the details link ---
                val searchUrl = "${baseUrl}index.php?name=$personName"
                val detailsLink = getDetailsLink(searchUrl)
                    ?: return@withContext Result.failure(Exception("No detailed view available for '$personName'. The name might not exist or is not a primary name."))

                val detailsUrl = baseUrl + detailsLink.removePrefix("./")

                // --- STAGE 2: Fetch the details page and parse the final data ---
                val finalData = getPersonDataFromDetailsPage(detailsUrl)

                Result.success(finalData)

            } catch (e: Exception) {
                // Catch any network or parsing errors
                Result.failure(e)
            }
        }
    }

    /**
     * Performs the first request to get the link to the details page.
     */
    private fun getDetailsLink(searchUrl: String): String? {
        val request = Request.Builder().url(searchUrl).build()
        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw IOException("Search request failed: ${response.code}")
        val html = response.body?.string() ?: throw IOException("Empty response body from search.")

        val document = Jsoup.parse(html)

        // Find the first row in the results table
        val firstResultRow = document.selectFirst("div.table-responsive.my-5 table.table tbody tr")
            ?: return null // No results found at all

        // Find the <a> tag within that row to get the link
        return firstResultRow.selectFirst("a")?.attr("href")
    }

    /**
     * Performs the second request and parses the final data from the details table.
     */
    private fun getPersonDataFromDetailsPage(detailsUrl: String): PersonNameData {
        val request = Request.Builder().url(detailsUrl).build()
        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw IOException("Details page request failed: ${response.code}")
        val html = response.body?.string() ?: throw IOException("Empty response body from details page.")

        val document = Jsoup.parse(html)
        val rows = document.select("div.table-responsive.my-5 table.table tbody tr")

        // Use a map to store the found data. It's flexible and robust.
        val dataMap = mutableMapOf<String, String>()
        rows.forEach { row ->
            val key = row.selectFirst("th")?.text()?.trim()
            val value = row.selectFirst("td")?.text()?.trim()
            if (key != null && value != null) {
                dataMap[key] = value
            }
        }

        // Build the final data class, providing defaults for missing values
        return PersonNameData(
            name = dataMap["Vārds"] ?: "N/A",
            amount = dataMap["Sastopams"]?.toIntOrNull() ?: 0,
            nameDay = dataMap["Vārdadiena"] ?: "", // Empty string if not found
            explanation = dataMap["Skaidrojums"] ?: "Nav skaidrojuma." // Default text if not found
        )
    }
}