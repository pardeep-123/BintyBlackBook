package com.bintyblackbook.util

import com.google.android.material.datepicker.DateValidatorPointBackward.before
import java.util.*


class NSDate : Date {
    constructor() : super() {}

    /**
     * Represents the specified number of seconds from the current date.
     */
    constructor(seconds: Double) : super(
        NSDate().getTime() as Long
                + timeIntervalToMilliseconds(seconds)
    ) {
    }

    /**
     * Represents the specified number of seconds from the specified date.
     */
    constructor(
        seconds: Double,
        sinceDate: Date
    ) : super(sinceDate.time as Long + timeIntervalToMilliseconds(seconds)) {
    }

    /**
     * Returns the interval between this date and 1 January 2001 GMT.
     */
    fun timeIntervalSinceReferenceDate(): Double {
        val referenceDate = GregorianCalendar(
            TimeZone
                .getTimeZone("UTC")
        )

        referenceDate.set(2001, 1, 1, 0, 0, 0)
        return timeIntervalSinceDate(referenceDate.time)
    }

    /**
     * Returns the interval between this date and the specified date in seconds.
     */
    fun timeIntervalSinceDate(aDate: Date): Double {
        return millisecondsToTimeInterval(this.getTime() - aDate.getTime())
    }

    /**
     * Returns the interval between this date and the current date in seconds.
     */
    fun timeIntervalSinceNow(): Double {
        return timeIntervalSinceDate(NSDate())
    }

    /**
     * Compares this date to the specified date and returns the earlier date.
     * Unspecified which is returned if both are equal.
     */
    fun earlierDate(aDate: NSDate?): NSDate {
        if (aDate == null) return this
        return if (after(aDate)) aDate else this
    }

    /**
     * Compares this date to the specified date and returns the later date.
     * Unspecified which is returned if both are equal.
     */
    fun laterDate(aDate: NSDate?): NSDate {
        if (aDate == null) return this
        return if (before(aDate)) aDate else this
    }

    /**
     * Returns a negative value if the specified date is later than this date, a
     * positive value if the specified date is earlier than this date, or zero
     * if the dates are equal. The return values are compatible with type
     * NSComparisonResult.
     */
    fun compare(aDate: Date?): Int {
        if (before(aDate)) return NSOrderedAscending
        return if (after(aDate)) NSOrderedDescending else NSOrderedSame
    }

    /**
     * Returns whether the this date is equal to the specified date, per the
     * result of equals().
     */
    fun isEqualToDate(aDate: Date?): Boolean {
        return equals(aDate)
    }

    /**
     * Returns a date that differs from this date by the specified number of
     * seconds.
     */
    fun dateByAddingTimeInterval(seconds: Double): NSDate {
        return NSDate(seconds, this)
    }

    companion object {
        /**
         *
         */
        private const val serialVersionUID = 6203488883212496944L
        const val NSOrderedAscending = -1
        const val NSOrderedSame = 0
        const val NSOrderedDescending = 1

        /**
         * Returns the number of seconds between now and the reference date.
         */
        fun currentTimeIntervalSinceReferenceDate(): Double {
            return NSDate().timeIntervalSinceReferenceDate()
        }

        /**
         * Converts seconds to milliseconds. Included for compatibility.
         */
        fun timeIntervalToMilliseconds(seconds: Double): Long {
            return seconds.toLong() * 1000
        }

        /**
         * Converts milliseconds to seconds. Included for compatibility.
         */
        fun millisecondsToTimeInterval(millis: Long): Double {
            return millis / 1000.0
        }
    }
}