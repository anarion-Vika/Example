package lineup.example.book

import android.os.Parcelable

@Entity
@Parcelize
data class BookResponse(
    val accountId: String,
    val deletionDate: String?,
    val createdAt: String?,
    val id: Long,
    val countryId: Int,
    val name: String,
    val organisationId: Long,
    val subAccountId: String?,
    val updatedAt: String?,
    val taxNumber: String?,
    val taxDetails: List<TaxDetails>?,
    val taxYear: String?,
    val country: CountryResponse?
) : Parcelable {
    @Parcelize
    data class TaxDetails(
        val name: String,
        val rate: Float
    ) : Parcelable
}