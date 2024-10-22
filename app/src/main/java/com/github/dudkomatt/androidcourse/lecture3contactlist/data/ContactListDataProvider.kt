package com.github.dudkomatt.androidcourse.lecture3contactlist.data

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log

private const val NOT_AVAILABLE = "N/A"

fun Context.fetchAllContacts(): List<Contact> {
    Log.d("CUSTOM", "fetchAllContacts()")

    contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )
        .use { cursor ->
            if (cursor == null) {
                return emptyList()
            }

            var idx = 0
            val builder = ArrayList<Contact>()

            while (cursor.moveToNext()) {
                builder.add(
                    Contact(
                        idx++,
                        getContactName(cursor),
                        getContactPhoneNumber(cursor)
                    )
                )
            }

            return builder
        }
}

private fun getContactName(cursor: Cursor): String {
    var displayNameIndex =
        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

    return when (displayNameIndex) {
        -1 -> null
        else -> cursor.getString(displayNameIndex)
    } ?: NOT_AVAILABLE
}

private fun getContactPhoneNumber(cursor: Cursor): String {
    var displayNumberIndex =
        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

    return when (displayNumberIndex) {
        -1 -> null
        else -> cursor.getString(displayNumberIndex)
    } ?: NOT_AVAILABLE
}
