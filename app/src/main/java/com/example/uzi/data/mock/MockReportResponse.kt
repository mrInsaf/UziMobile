package com.example.uzi.data.mock

import com.example.uzi.data.models.Formation
import com.example.uzi.data.models.Image
import com.example.uzi.data.models.ReportResponse
import com.example.uzi.data.models.SectorPoint
import com.example.uzi.data.models.Segment
import com.example.uzi.data.models.Tirads
import com.example.uzi.data.models.Uzi

val mockReportResponse = ReportResponse(
    formations = listOf(
        Formation(
            ai = true,
            id = "formation1",
            tirads = Tirads(
                tirads_23 = 15,
                tirads_4 = 15,
                tirads_5 = 80
            )
        ),
        Formation(
            ai = false,
            id = "formation2",
            tirads = Tirads(
                tirads_23 = 60,
                tirads_4 = 30,
                tirads_5 = 10
            )
        )
    ),
    images = listOf(
        Image(
            id = "image1",
            page = 1,
            url = "https://example.com/image1.png"
        ),
        Image(
            id = "image2",
            page = 2,
            url = "https://example.com/image2.png"
        )
    ),
    segments = listOf(
        Segment(
            contor = listOf(
                SectorPoint(x = 0, y = 0),
                SectorPoint(x = 10, y = 10),
                SectorPoint(x = 20, y = 5)
            ),
            formation_id = "formation1",
            id = "segment1",
            image_id = "image1",
            tirads = Tirads(
                tirads_23 = 85,
                tirads_4 = 10,
                tirads_5 = 5
            )
        ),
        Segment(
            contor = listOf(
                SectorPoint(x = 5, y = 5),
                SectorPoint(x = 15, y = 15),
                SectorPoint(x = 25, y = 10)
            ),
            formation_id = "formation2",
            id = "segment2",
            image_id = "image2",
            tirads = Tirads(
                tirads_23 = 70,
                tirads_4 = 20,
                tirads_5 = 10
            )
        )
    ),
    uzi = Uzi(
        device_id = 12345,
        id = "uzi1",
        patient_id = "patient123",
        projection = "Lateral",
        url = "https://example.com/uzi_report.pdf"
    )
)
