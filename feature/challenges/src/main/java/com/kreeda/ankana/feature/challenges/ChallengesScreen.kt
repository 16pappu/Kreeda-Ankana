package com.kreeda.ankana.feature.challenges

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChallengesScreen(
    viewModel: ChallengesViewModel,
    modifier: Modifier = Modifier
) {
    val challenges by viewModel.challenges.collectAsState()
    var showPostDialog by remember { mutableStateOf(false) }
    var commentChallengeId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { showPostDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Post Challenge")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Challenge Board", style = MaterialTheme.typography.titleLarge)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(challenges) { challenge ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(challenge.message, style = MaterialTheme.typography.titleMedium)
                            Text("${challenge.sportType} • ${challenge.scheduledDate}")
                            if (challenge.umpireRequested) {
                                if (challenge.umpireUid != null) {
                                    androidx.compose.material3.Badge(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ) {
                                        Text("Umpire Assigned", modifier = Modifier.padding(4.dp))
                                    }
                                } else {
                                    androidx.compose.material3.Badge(
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                                    ) {
                                        Text("Umpire Requested", modifier = Modifier.padding(4.dp))
                                    }
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { commentChallengeId = challenge.challengeId }) {
                                    Text("Comment")
                                }
                                Button(
                                    onClick = { viewModel.acceptChallenge(challenge.challengeId) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                ) {
                                    Text("Accept")
                                }
                                if (challenge.umpireRequested && challenge.umpireUid == null) {
                                    Button(
                                        onClick = { viewModel.claimUmpire(challenge.challengeId) },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                                    ) {
                                        Text("Claim Umpire")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showPostDialog) {
        var sportType by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }
        var requestUmpire by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showPostDialog = false },
            title = { Text("Post Challenge") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = sportType,
                        onValueChange = { sportType = it },
                        label = { Text("Sport Type") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Message") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Scheduled Date") },
                        singleLine = true
                    )
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        androidx.compose.material3.Checkbox(
                            checked = requestUmpire,
                            onCheckedChange = { requestUmpire = it }
                        )
                        Text("Request Neutral Umpire")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.createChallenge(sportType, message, date, requestUmpire)
                        showPostDialog = false
                    }
                ) {
                    Text("Post")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPostDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (commentChallengeId != null) {
        var commentMessage by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { commentChallengeId = null },
            title = { Text("Add Comment") },
            text = {
                OutlinedTextField(
                    value = commentMessage,
                    onValueChange = { commentMessage = it },
                    label = { Text("Your comment") },
                    singleLine = false,
                    maxLines = 3
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.reply(commentChallengeId!!, commentMessage)
                        commentChallengeId = null
                    }
                ) {
                    Text("Send")
                }
            },
            dismissButton = {
                TextButton(onClick = { commentChallengeId = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
