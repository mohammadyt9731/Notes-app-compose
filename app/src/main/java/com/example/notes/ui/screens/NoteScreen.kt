package com.example.notes.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.notes.R
import com.example.notes.ui.model.NoteView
import com.example.notes.ui.viewModel.NoteViewModel

@Composable
fun AddNoteScreen(
    navController: NavController,
    isTask: Boolean?,
    id: Int
) {
    val context = LocalContext.current

    var titleValueState by remember {
        mutableStateOf("")
    }
    var notesValueState by remember {
        mutableStateOf("")
    }

    val noteViewModel = hiltViewModel<NoteViewModel>()

    if (id != -1) {
        noteViewModel.getNote(id)
    }

    val noteState by noteViewModel.getNote(id).collectAsState(
        initial = NoteView(
            id = 0,//auto generate
            title = "",
            notes = "",
            isTask = isTask ?: false
        )
    )


    LaunchedEffect(key1 = noteState) {
        titleValueState = noteState?.title ?: ""
        notesValueState = noteState?.notes ?: ""
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ToolBar(isTask = isTask ?: false, id = id) {
            navController.navigateUp()
        }
        Spacer(modifier = Modifier.height(16.dp))
        InputField(
            value = titleValueState,
            label = stringResource(id = R.string.label_title),
            maxLine = 1,
            imeAction = ImeAction.Next,
            onValueChange = { titleValueState = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputField(
            modifier = Modifier.weight(1f),
            value = notesValueState,
            label = if (isTask == true) {
                stringResource(id = R.string.label_task)
            } else {
                stringResource(id = R.string.label_notes)
            },
            maxLine = Int.MAX_VALUE,
            imeAction = ImeAction.Go,
            onValueChange = { notesValueState = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        RoundedButton(title = stringResource(id = R.string.label_save)) {
            if (titleValueState.isBlank() || notesValueState.isBlank()) {
                Toast.makeText(context, "Input title and notes", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (id == -1) {
                    noteViewModel.insertNote(
                        NoteView(
                            id = 0,
                            title = titleValueState,
                            notes = notesValueState,
                            isTask = isTask ?: false
                        )
                    )
                } else {
                    noteViewModel.updateNote(
                        NoteView(
                            id = id,
                            title = titleValueState,
                            notes = notesValueState,
                            isTask = isTask ?: false
                        )
                    )
                }
                navController.navigateUp()
            }
        }
    }
}

@Composable
fun ToolBar(isTask: Boolean, id: Int, onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 4.dp)
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onClick() },
            imageVector = Icons.Default.ArrowBack,
            contentDescription = stringResource(id = R.string.icon_back),
            tint = MaterialTheme.colors.onPrimary
        )
        if (id == -1){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = if (isTask) {
                    stringResource(id = R.string.label_add_task)
                } else {
                    stringResource(id = R.string.label_add_note)
                },
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp
                )
            )
        }
    }
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    maxLine: Int,
    imeAction: ImeAction,
    onValueChange: (String) -> Unit
) {

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        value = value,
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        onValueChange = { onValueChange(it) },
        shape = MaterialTheme.shapes.small,
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onBackground,
            backgroundColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        placeholder = { Text(text = label) },
        maxLines = maxLine,
    )
}

@Composable
fun RoundedButton(title: String, onClick: () -> Unit) {

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = MaterialTheme.colors.onPrimary,
                shape = MaterialTheme.shapes.medium
            ),
        onClick = onClick
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = MaterialTheme.colors.onPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}