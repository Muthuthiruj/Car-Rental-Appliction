package com.example.carrental.time

import java.util.Calendar

class Clock {
    companion object {
        private var instance: Clock? = null
        private var calendarInstance: Calendar? = null

        fun getInstance(): Clock {
            if (instance == null) {
                instance = Clock()
            }
            return instance!!
        }

        fun getCalendarInstance(): Calendar {
            return getInstance()._calendarInstance()
        }

        fun freeze(freeze: Calendar) {
            calendarInstance = freeze
        }

        fun unfreeze() {
            calendarInstance = null
        }
    }

    private fun _calendarInstance(): Calendar {
        return calendarInstance?.clone() as? Calendar ?: Calendar.getInstance()
    }
}