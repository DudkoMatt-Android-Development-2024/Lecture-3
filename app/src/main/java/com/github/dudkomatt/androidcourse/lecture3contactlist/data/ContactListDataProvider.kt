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

object ContactListMockDataProvider {
    val contactList: List<Contact> = listOf(
        Contact(0, "Name Surname", "79991234567"),
        Contact(1, "NonStandard PhoneTemplate", "12334567890974435345"),
        Contact(2, "123", "123"),
        Contact(3, "4", "4444"),
        Contact(4, "5", "1-231-231-23"),
        Contact(5, "6", "6666"),
        Contact(6, "7", "7"),
        Contact(7, "8", "8"),
        Contact(8, "9", "9"),
        Contact(9, "10", "10"),
        Contact(10, "11", "111"),
        Contact(11, "12", "12"),
        Contact(12, "13", "13"),
        Contact(13, "14", "14"),
        Contact(14, "15", "15"),
        Contact(15, "16", "16"),
        Contact(
            16,
            "AVeryLongFirstNameThatWillEndWithElipsis",
            "123456789023456789012345678901234567890"
        )
    )
}
