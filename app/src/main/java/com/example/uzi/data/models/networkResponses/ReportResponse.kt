package com.example.uzi.data.models.networkResponses

import android.net.Uri

data class ReportResponse(
    val formations: List<Formation>?,
    val images: List<Image>?,
    val segments: List<SegmentFake>?,
    val fakeUzi: FakeUzi?
)

data class Formation(
    val ai: Boolean,
    val id: String,
    val tirads: Tirads
) {
    // Вычисляемое свойство для максимального TIRADS
    val maxTirads: Int
        get() = maxOf(
            tirads.tirads_23,
            tirads.tirads_4,
            tirads.tirads_5
        )

    // Вычисляемое свойство для класса формации
    val formationClass: Int
        get() = when (maxTirads) {
            tirads.tirads_23 -> 2 // TIRADS 2-3
            tirads.tirads_4 -> 4 // TIRADS 4
            tirads.tirads_5 -> 5 // TIRADS 5
            else -> 0 // На случай ошибки
        }
}

data class Tirads(
    val tirads_23: Int, // вероятность класса 23
    val tirads_4: Int,  // вероятность класса 4
    val tirads_5: Int   // вероятность класса 5
)

data class Image(
    val id: String,
    val page: Int,
    val url: Uri
)

data class SegmentFake(
    val contor: List<SectorPoint>,
    val formation_id: String,
    val id: String,
    val image_id: String,
    val tirads: Tirads
)


data class FakeUzi(
    val device_id: Int,
    val id: String,
    val patient_id: String,
    val projection: String,
    val url: String,
    val dateOfAdmission: String,
    val clinicName: String,
)