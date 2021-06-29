package trinitityproject.factory.service

import org.apache.commons.lang3.time.DateUtils
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*


@Service
class TimeService(private val dayInMillis: Long = 180000, private val timeZone: String = "GMT+8India: IST") {

    /**
     * Compares a given date to the current date by converting each into a virtual date with shorter time
     *
     * @return true if the given date matches the current date
     */
    fun isSameVirtualLocalDate(timestamp: Long): Boolean {
        val realDate = this.getVirtualCalendar(timestamp).time
        val currentDate = this.getVirtualCalendar(System.currentTimeMillis()).time

        return DateUtils.isSameDay(realDate, currentDate)
    }

    /**
     * Return a timestamp representing a projection from the given real time to the virtual, faster time
     *
     * @return Instant Object representing the virtual time
     */
    fun getVirtualLocalTime(timestamp: Long): Instant {
        return this.getVirtualCalendar(timestamp).toInstant()
    }

    /**
     * Return the given milliseconds in realtime projected to milliseconds in virtual time
     *
     * @return The given milliseconds in virtual time
     */
    fun realtimeToVirtualTimeMillis(realTimeMillis: Long): Long {
        return (realTimeMillis * (dayInMillis.toDouble() / 86400000)).toLong()
    }

    /**
     * Creates a calendar object for a given timestamp by converting it into a virtual date with shorter time
     *
     * @return Calendar representing the shorter virtual time
     */
    fun getVirtualCalendar(realTimestamp: Long): Calendar {
        val hoursInMillis = dayInMillis.toDouble() / 24
        val hoursSinceZero = realTimestamp.toDouble() / hoursInMillis % 1000
        val daysSinceZero = hoursSinceZero / 24
        val yearsSinceZero = daysSinceZero / 365

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, yearsSinceZero.toInt())
        calendar.set(Calendar.DAY_OF_YEAR, daysSinceZero.toInt() - yearsSinceZero.toInt() * 365)
        calendar.set(Calendar.HOUR_OF_DAY, hoursSinceZero.toInt() - daysSinceZero.toInt() * 24)
        calendar.timeZone = TimeZone.getTimeZone(this.timeZone)

        return calendar
    }

    /**
     * Return the delay in milliseconds between the reports
     *
     * @return Delay in milliseconds
     */
    fun getReportDelay(): Long {
        return dayInMillis / 12
    }
}