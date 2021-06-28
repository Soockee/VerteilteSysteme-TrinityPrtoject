package trinitityproject.factory.service

import org.apache.commons.lang3.time.DateUtils
import org.springframework.stereotype.Service
import java.util.*


@Service
class TimeService(private val dayInMillis: Long = 10000, private val timeZone: String = "CST") {

    /**
     * Compares a given date to the current date by converting each into a virtual date with shorter time
     *
     * @return true if the given date matches the current date
     */
    fun isSameVirtualLocalDate(realTimestamp: Long): Boolean {
        val realDate = this.getVirtualCalendar(realTimestamp).time
        val currentDate = this.getVirtualCalendar(System.currentTimeMillis()).time

        return DateUtils.isSameDay(realDate, currentDate)
    }

    /**
     * Return the hour of day for the given date by converting it into a virtual date with shorter time
     *
     * @return The hour of day for the given date
     */
    fun getVirtualCurrentLocalTime(realTimestamp: Long): Date {
        return this.getVirtualCalendar(realTimestamp).time
    }

    /**
     * Creates a calendar object for a given timestamp by converting it into a virtual date with shorter time
     *
     * @return Calendar representing the shorter virtual time
     */
    fun getVirtualCalendar(realTimestamp: Long): Calendar {
        var hoursInMillis = dayInMillis.toDouble() / 24
        var hoursSinceZero = realTimestamp.toDouble() / hoursInMillis % 1000
        var daysSinceZero = hoursSinceZero / 24
        var yearsSinceZero = daysSinceZero / 365

        var calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, yearsSinceZero.toInt())
        calendar.set(Calendar.DAY_OF_YEAR, daysSinceZero.toInt() - yearsSinceZero.toInt() * 365)
        calendar.set(Calendar.HOUR_OF_DAY, hoursSinceZero.toInt() - daysSinceZero.toInt() * 24)
        calendar.timeZone = TimeZone.getTimeZone(this.timeZone)

        return calendar
    }

    fun getDelay(): Long {
        return dayInMillis / 2;
    }
}