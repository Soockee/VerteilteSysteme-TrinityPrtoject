package trinitityproject.support.service

import org.apache.commons.lang3.time.DateUtils
import org.springframework.stereotype.Service
import java.util.*


@Service
class TimeService(private val dayInSeconds: Int = 5) {

    /**
     * Compares a given date to the current date by converting each into a virtual date with shorter time
     *
     * @return true if the given date matches the current date
     */
    fun isToday(realTimestamp: Long): Boolean {
        val realDate = this.getVirtualCalendar(realTimestamp).time
        val currentDate = this.getVirtualCalendar(System.currentTimeMillis()).time

        return DateUtils.isSameDay(realDate, currentDate)
    }

    /**
     * Return the hour of day for the given date by converting it into a virtual date with shorter time
     *
     * @return The hour of day for the given date
     */
    fun getVirtualTime(realTimestamp: Long): Int {
        return this.getVirtualCalendar(realTimestamp).get(Calendar.HOUR_OF_DAY)
    }

    /**
     * Creates a calendar object for a given timestamp by converting it into a virtual date with shorter time
     *
     * @return Calendar representing the shorter virtual time
     */
    private fun getVirtualCalendar(realTimestamp: Long): Calendar {
        var hoursInMillis = dayInSeconds.toDouble() / 24 * 1000
        var hoursSinceZero = realTimestamp.toDouble() / hoursInMillis % 1000
        var daysSinceZero = hoursSinceZero / 24
        var yearsSinceZero = daysSinceZero / 365

        var calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, yearsSinceZero.toInt())
        calendar.set(Calendar.DAY_OF_YEAR, daysSinceZero.toInt() - yearsSinceZero.toInt() * 365)
        calendar.set(Calendar.HOUR_OF_DAY, hoursSinceZero.toInt() - daysSinceZero.toInt() * 24)

        return calendar
    }
}