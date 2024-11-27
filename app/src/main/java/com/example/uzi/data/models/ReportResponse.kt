package com.example.uzi.data.models

data class ReportResponse(
    val formations: List<Formation>,
    val images: List<Image>,
    val segments: List<Segment>,
    val uzi: Uzi
)

data class Formation(
    val ai: Boolean,
    val id: String,
    val tirads: Tirads
)

data class Tirads(
    val tirads_23: Int, // вероятность класса 23
    val tirads_4: Int,  // вероятность класса 4
    val tirads_5: Int   // вероятность класса 5
)

data class Image(
    val id: String,
    val page: Int,
    val url: String
)

data class Segment(
    val contor: List<SectorPoint>,
    val formation_id: String,
    val id: String,
    val image_id: String,
    val tirads: Tirads
)


data class Uzi(
    val device_id: Int,
    val id: String,
    val patient_id: String,
    val projection: String,
    val url: String
)