package com.github.dudkomatt.androidcourse.lecture3contactlist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.dudkomatt.androidcourse.lecture3contactlist.data.Contact
import com.github.dudkomatt.androidcourse.lecture3contactlist.data.ContactListMockDataProvider
import com.github.dudkomatt.androidcourse.lecture3contactlist.data.fetchAllContacts
import com.github.dudkomatt.androidcourse.lecture3contactlist.ui.theme.Lecture3ContactListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lecture3ContactListTheme {
                MainScreenOrRequestPermission()
            }
        }
    }
}

@Composable
fun MainScreenOrRequestPermission() {
    var currentContext = LocalContext.current

    var isContactsPermissionGranted by remember { mutableStateOf(false) }
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                isContactsPermissionGranted = true
            } else {
                Toast.makeText(currentContext, "Permissions are not granted", Toast.LENGTH_LONG)
                    .show()
            }
        }

    if (!isContactsPermissionGranted) {
        SideEffect {
            permissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
        }
        Text("\"READ_CONTACTS\" permission is not granted")
    } else {
        val allContacts: List<Contact> = remember { currentContext.fetchAllContacts() }
        ContactListApp(allContacts)
    }
}

@Composable
fun ContactListApp(contacts: List<Contact>) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        topBar = {
            TopBar()
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
        ) {
            items(items = contacts, key = { it.idx }) { contact ->
                ContactRow(contact)
            }
        }
    }
}

@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .height((1.5 * MaterialTheme.typography.titleLarge.lineHeight.value).dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary),
    ) {
    }
}

@Composable
fun ContactRow(contact: Contact) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = contact.name,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Number ${contact.phoneNumber}",
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        ContactIcon(
            modifier = Modifier
                .padding(4.dp, 0.dp)
        )
    }
}

@Composable
fun ContactIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(64.dp)
            .requiredHeight(64.dp)
            .clip(RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_launcher_background),
            contentDescription = "Contact background",
            tint = Color.Unspecified
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
            contentDescription = "Contact foreground",
            tint = Color.Unspecified
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactListAppPreview() {
    ContactListApp(ContactListMockDataProvider.contactList)
}

@Preview(showBackground = true)
@Composable
fun ContactRowPreview() {
    ContactRow(Contact(0, "Name", "+7 999 123 45 01"))
}

@Preview(showBackground = true)
@Composable
fun ContactLongRowPreview() {
    ContactRow(
        Contact(
            0,
            "Super Long Display Name In header",
            "+7 999 123 45 01 0000000000000000000000000000000000000000000000"
        )
    )
}
