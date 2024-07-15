package com.example.noteappusingfirebase.home

import android.icu.text.SimpleDateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.noteappusingfirebase.Utils
import com.example.noteappusingfirebase.models.Notes
import com.example.noteappusingfirebase.repository.Resource
import com.google.firebase.Timestamp
import java.util.Locale

@Composable
fun Home(
    homeViewModel: HomeViewModel?,
    onNoteClicked : (id : String) -> Unit,
    navToDetailPage : () -> Unit,
    navToLoginPage : () -> Unit,
){
    var homeUiState = homeViewModel?.homeUiState?:HomeUiState()
    
    var openDialog by remember { mutableStateOf(false) }
    
    var selectedNote :  Notes? by remember {
        mutableStateOf(null)
    }
    
    val scope = rememberCoroutineScope()
    var scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = Unit) {
        homeViewModel?.loadNotes()
    }
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = { navToDetailPage() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        topBar = {
            TopAppBar(
                navigationIcon = {},
                actions = {
                    IconButton(onClick = { homeViewModel?.signOut()
                                            navToLoginPage()
                    }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
                    }
                },
                title = { Text(text = "Home")},
            )
        }
        ) {
        padding ->
        Column(
            modifier = Modifier.padding(padding)
        ){
            when(homeUiState.notesList){
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center))
                }
                is Resource.Success ->{
                        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(16.dp)) {
                            items(homeUiState.notesList.data ?: emptyList()){
                                note ->
                                    NotesItem(notes = note , onLongClick = { 
                                        openDialog = true
                                        selectedNote = note
                                    }) {
                                        onNoteClicked(note.documentId)
                                    }
                            }
                        }
                    AnimatedVisibility(visible = openDialog) {
                        AlertDialog(onDismissRequest = { openDialog = false }, title = { Text(text = "Delete Note?")},
                            confirmButton = {
                                Button(onClick = { selectedNote?.documentId?.let {homeViewModel?.deleteNote(it)
                                    openDialog = false}
                                },
                                    ) {
                                    Text(text = "Delete")
                                }
                            }, dismissButton = {
                                Button(onClick = { openDialog = false }) {
                                    Text(text = "Cancel")
                                }
                            }
                            )
                    }
                }
                else -> {
                    Text(text = homeUiState.notesList.throwable?.localizedMessage?:"Unknown Error" , color = Color.Red)
                }
            }
        }
    }
    
    
    LaunchedEffect(key1 = homeViewModel?.hasUser) {
        if(homeViewModel?.hasUser == false){
            navToLoginPage()
        }
    }
    
    
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesItem(notes: Notes , onLongClick: () -> Unit, onClick : () -> Unit){
    Card(modifier = Modifier
        .combinedClickable(
            onLongClick = { onLongClick() }, onClick = onClick
        )
        .padding(8.dp)
        .fillMaxSize()
    , backgroundColor = Utils.colors[notes.colorIndex]
    ) {
        Column {
            Text(text = notes.title, style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold , maxLines = 1,overflow = TextOverflow.Clip , modifier = Modifier.padding(4.dp))
        }
        Spacer(modifier = Modifier.size(4.dp))
        CompositionLocalProvider(LocalContentAlpha provides  ContentAlpha.disabled) {
                Text(text = notes.description , style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp),
                    maxLines = 4
                    )
        }
        Spacer(modifier = Modifier.size(4.dp))
        CompositionLocalProvider(LocalContentAlpha provides  ContentAlpha.disabled) {
            Text(text = formatDate(notes.timestamp) , style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp),
                maxLines = 4
            )
        }
    }
    
    
}

private fun formatDate(timeStamp : Timestamp) : String{
    val sdf = SimpleDateFormat("MM-dd-yy hh:mm:ss", Locale.getDefault())
    return sdf.format(timeStamp.toDate())
}
