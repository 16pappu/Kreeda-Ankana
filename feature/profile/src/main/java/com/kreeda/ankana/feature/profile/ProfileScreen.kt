package com.kreeda.ankana.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.profile.collectAsState()

    var displayName by remember(profile?.displayName) { mutableStateOf(profile?.displayName ?: "") }
    var teamName by remember(profile?.teamName) { mutableStateOf(profile?.teamName ?: "") }
    var role by remember(profile?.role) { mutableStateOf(profile?.role ?: "PLAYER") }
    var sports by remember(profile?.sports) { mutableStateOf(profile?.sports?.joinToString(", ") ?: "") }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Profile", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = displayName, onValueChange = { displayName = it }, label = { Text("Display Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = teamName, onValueChange = { teamName = it }, label = { Text("Team Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = role, onValueChange = { role = it }, label = { Text("Role (CAPTAIN or PLAYER)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = sports, onValueChange = { sports = it }, label = { Text("Sports (comma separated)") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { viewModel.saveProfile(displayName, teamName, role, sports) }, modifier = Modifier.fillMaxWidth()) {
            Text("Save Profile")
        }
        androidx.compose.material3.TextButton(
            onClick = { viewModel.logout() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout", color = MaterialTheme.colorScheme.error)
        }
    }
}
