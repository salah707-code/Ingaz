package com.example.ui.utils

import com.example.data.model.TaskEntity
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtils {

    private val hijriMonthsAr = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    private val hijriMonthsEn = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Ula", "Jumada al-Akhirah", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    fun toArabicNumerals(number: Int): String {
        val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        return number.toString().map { char ->
            if (char in '0'..'9') arabicDigits[char - '0'] else char
        }.joinToString("")
    }

    fun toArabicNumerals(text: String): String {
        val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        return text.map { char ->
            if (char in '0'..'9') arabicDigits[char - '0'] else char
        }.joinToString("")
    }

    fun getHijriDate(dateMillis: Long): Triple<Int, Int, Int> {
        return try {
            val localDate = Instant.ofEpochMilli(dateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            val hijrahDate = HijrahDate.from(localDate)
            val year = hijrahDate.get(ChronoField.YEAR)
            val month = hijrahDate.get(ChronoField.MONTH_OF_YEAR)
            val day = hijrahDate.get(ChronoField.DAY_OF_MONTH)
            Triple(day, month, year)
        } catch (e: Exception) {
            // Algorithmic estimate fallback if Hijrah Chronology is unavailable
            val cal = Calendar.getInstance().apply { timeInMillis = dateMillis }
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val approxHijriYear = ((year - 622) * 33 / 32)
            Triple(day.coerceIn(1, 30), month.coerceIn(1, 12), approxHijriYear)
        }
    }

    fun formatHijriDate(dateMillis: Long, isArabic: Boolean = true): String {
        val (day, month, year) = getHijriDate(dateMillis)
        val monthIdx = (month - 1).coerceIn(0, 11)
        return if (isArabic) {
            val dayStr = toArabicNumerals(day)
            val yearStr = toArabicNumerals(year)
            val monthName = hijriMonthsAr[monthIdx]
            "$dayStr $monthName $yearStr هـ"
        } else {
            val monthName = hijriMonthsEn[monthIdx]
            "$day $monthName $year AH"
        }
    }

    fun formatBothDates(dateMillis: Long, isArabic: Boolean = true): String {
        val hijri = formatHijriDate(dateMillis, isArabic)
        val gregorian = formatFullDate(dateMillis, isArabic)
        return if (isArabic) "$hijri • $gregorian" else "$hijri • $gregorian"
    }

    fun getTodayStartMillis(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun getTodayEndMillis(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }

    fun getTomorrowStartMillis(): Long {
        return Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun getThisWeekEndMillis(): Long {
        return Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 7)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }

    fun normalizeToStartOfDay(millis: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = millis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun formatDateDisplay(dateMillis: Long, isArabic: Boolean): String {
        val today = getTodayStartMillis()
        val tomorrow = getTomorrowStartMillis()
        val dayAfterTomorrow = tomorrow + 24 * 60 * 60 * 1000L
        val yesterday = today - 24 * 60 * 60 * 1000L

        val targetStart = normalizeToStartOfDay(dateMillis)

        return when (targetStart) {
            today -> if (isArabic) "اليوم" else "Today"
            tomorrow -> if (isArabic) "غداً" else "Tomorrow"
            yesterday -> if (isArabic) "أمس" else "Yesterday"
            dayAfterTomorrow -> if (isArabic) "بعد غد" else "In 2 days"
            else -> {
                val locale = if (isArabic) Locale("ar") else Locale.ENGLISH
                val formatter = SimpleDateFormat("EEE، d MMM", locale)
                formatter.format(Date(dateMillis))
            }
        }
    }

    fun formatFullDate(dateMillis: Long, isArabic: Boolean): String {
        val locale = if (isArabic) Locale("ar") else Locale.ENGLISH
        val formatter = SimpleDateFormat("EEEE، d MMMM yyyy", locale)
        return formatter.format(Date(dateMillis))
    }

    fun formatTimeDisplay(hour: Int, minute: Int, isArabic: Boolean): String {
        if (hour < 0 || minute < 0) return if (isArabic) "طوال اليوم" else "All day"

        val isPm = hour >= 12
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        val formattedMin = String.format(Locale.ENGLISH, "%02d", minute)
        val amPm = if (isArabic) {
            if (isPm) "م" else "ص"
        } else {
            if (isPm) "PM" else "AM"
        }
        val rawTime = "$displayHour:$formattedMin $amPm"
        return if (isArabic) toArabicNumerals(rawTime) else rawTime
    }

    fun getTimeRemainingString(task: TaskEntity, isArabic: Boolean): String {
        val targetMillis = if (task.timeHour in 0..23 && task.timeMinute in 0..59) {
            Calendar.getInstance().apply {
                timeInMillis = task.date
                set(Calendar.HOUR_OF_DAY, task.timeHour)
                set(Calendar.MINUTE, task.timeMinute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        } else {
            Calendar.getInstance().apply {
                timeInMillis = task.date
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        }

        val diffMillis = targetMillis - System.currentTimeMillis()
        if (diffMillis < 0) {
            return if (isArabic) "متأخرة" else "Overdue"
        }

        val minutes = diffMillis / (1000 * 60)
        val hours = minutes / 60
        val days = hours / 24

        return when {
            minutes < 60 -> if (isArabic) "بعد ${toArabicNumerals(minutes.toInt())} دقيقة" else "In $minutes min"
            hours < 24 -> if (isArabic) "بعد ${toArabicNumerals(hours.toInt())} ساعة" else "In $hours hrs"
            days == 1L -> if (isArabic) "غداً" else "Tomorrow"
            days < 7 -> if (isArabic) "بعد ${toArabicNumerals(days.toInt())} أيام" else "In $days days"
            else -> formatDateDisplay(task.date, isArabic)
        }
    }

    fun isOverdue(task: TaskEntity): Boolean {
        if (task.isCompleted) return false
        val today = getTodayStartMillis()
        val targetDay = normalizeToStartOfDay(task.date)
        if (targetDay < today) return true
        if (targetDay == today && task.timeHour in 0..23) {
            val nowCal = Calendar.getInstance()
            val nowHour = nowCal.get(Calendar.HOUR_OF_DAY)
            val nowMin = nowCal.get(Calendar.MINUTE)
            if (nowHour > task.timeHour || (nowHour == task.timeHour && nowMin > task.timeMinute)) {
                return true
            }
        }
        return false
    }
}
