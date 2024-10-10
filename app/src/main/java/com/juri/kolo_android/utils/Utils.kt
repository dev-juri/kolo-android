package com.juri.kolo_android.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.text.NumberFormat
import java.util.Locale

fun currencyFormatterDecimal(amount: Double): String {  // Specify the locale for Nigeria
    val nigeriaLocale = Locale("en", "NG")
    val currencyFormat = NumberFormat.getCurrencyInstance(nigeriaLocale)
    return currencyFormat.format(amount)
}

fun formatDateTime(dateTime: String): String {
    val localDateTime = DateTime.parse(dateTime).withZone(DateTimeZone.getDefault())
    return localDateTime.toString("MMM dd, yyyy hh:mm a")
}

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, object : androidx.lifecycle.Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer(value)
        }
    })
}