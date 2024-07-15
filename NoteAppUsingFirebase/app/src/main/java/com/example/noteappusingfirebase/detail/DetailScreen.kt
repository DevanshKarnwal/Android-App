package com.example.noteappusingfirebase.detail

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animate
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


import com.example.noteappusingfirebase.Utils
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel?,
    noteId:String,
    onNavigate:() -> Unit
){
    val detailUiState = detailViewModel?.detailUiState?:DetailUiState()
    val isFormsNotBlank = detailUiState.title.isNotBlank() && detailUiState.note.isNotBlank()
    val selectedColor by animateColorAsState(targetValue = Utils.colors[detailUiState.colorIndex])
    val isNoteIdNotBlank = noteId.isNotBlank()
    val icon = if(isNoteIdNotBlank) Icons.Default.Refresh
                else  Icons.Default.Check
    LaunchedEffect(key1 = Unit) {
        if(isNoteIdNotBlank){
            detailViewModel?.getNote(noteId)
        }
        else{
            detailViewModel?.resetstate()
        }
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            AnimatedVisibility(visible = isFormsNotBlank) {
                FloatingActionButton(onClick = {
                    if(isNoteIdNotBlank){
                        detailViewModel?.updateNote(noteId)
                    }else{
                        detailViewModel?.addNote()
                    }
                }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        },
    ) {
        padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = selectedColor)
            .padding(padding)
            ) {
            if(detailUiState.noteAddedStatus){
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("Added not successfully")
                    detailViewModel?.resetNoteAddedStatus()
                    onNavigate()
                }
            }
            if(detailUiState.updateNoteStatus){
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("Note updates successfully")
                    detailViewModel?.resetNoteAddedStatus()
                    onNavigate()
                }
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                contentPadding = PaddingValues(vertical = 16.dp , horizontal = 8.dp)
            ) {
                itemsIndexed(Utils.colors){
                    colorIndex,color -> ColorItem(color = color) {
                        detailViewModel?.onColorChange(colorIndex)
                }
                }
            }
            OutlinedTextField(value = detailUiState.title,
                onValueChange = {detailViewModel?.onTitleChange(it)
                },
                label = {Text(text = "Title")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)

            )

            OutlinedTextField(value = detailUiState.note,
                onValueChange = {detailViewModel?.onNoteChange(it)
                },
                label = {Text(text = "Note")},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp) )

        }

    }

}

@Composable
fun ColorItem(
    color : Color,
    onClick:() -> Unit
){
    Surface(color = color, shape = CircleShape,modifier = Modifier
        .padding(8.dp)
        .size(16.dp)
        .clickable {
            onClick()
        }, border = BorderStroke(2.dp, Color.Black) ) {

    }
}