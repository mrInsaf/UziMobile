package com.mrinsaf.core.presentation.ui.ui_state

sealed class DiagnosticProgressStateDetail(val message: String) {
    object UploadingUzi: DiagnosticProgressStateDetail("Загрузка изображения на диагностику...")

    sealed class DiagnosticWaiting(val message: String) {
        object New: DiagnosticProgressStateDetail("В очереди на диагностику...")
        object Pending: DiagnosticProgressStateDetail("Ожидание результатов диагностики...")
    }

    class DownloadingImages(val downloadingProgress: Float): DiagnosticProgressStateDetail(
        "Диагностика завершена. Загрузка результатов"
    )
}