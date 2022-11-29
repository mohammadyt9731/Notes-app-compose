package com.example.notes.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.notes.R
import com.example.notes.ui.model.NoteView
import com.example.notes.ui.navigation.Screens
import com.example.notes.ui.viewModel.NotesViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesScreen(navHostController: NavHostController) {
    val notesViewModel = hiltViewModel<NotesViewModel>()

    var searchValueState by remember {
        mutableStateOf("")
    }
    var isTaskSelected by remember {
        mutableStateOf(false)
    }
    val notes by notesViewModel.searchNotes(searchValueState).collectAsState(initial = emptyList())
    val tasks by notesViewModel.searchTasks(searchValueState).collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        topBar = { TopBar(isTaskSelected) { isTaskSelected = it } },
        floatingActionButton = {
            FabButton {
                navHostController.navigate(
                    Screens.Note.withArgs(isTaskSelected.toString(), "-1")
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            SearchField(
                value = searchValueState,
                isTaskSelected = isTaskSelected
            ) {
                searchValueState = it
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isTaskSelected)
                NoteListSection(tasks, navHostController, notesViewModel)
            else
                NoteListSection(notes, navHostController, notesViewModel)
        }
    }
}

@Composable
fun TopBar(isTaskSelected: Boolean, onClick: (Boolean) -> Unit) {

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomText(
                text = stringResource(id = R.string.app_name),
                isTaskSelected = isTaskSelected
            ) {
                onClick(false)
            }

            CustomText(
                text = stringResource(id = R.string.label_task),
                isTaskSelected = !isTaskSelected
            ) {
                onClick(true)
            }
        }

        Icon(
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.CenterEnd),
            painter = painterResource(id = R.drawable.ic_settings),
            contentDescription = stringResource(id = R.string.label_setting_icon),
            tint = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun CustomText(
    text: String,
    isTaskSelected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = if (isTaskSelected) {

                    MaterialTheme.colors.onBackground
                } else {
                    MaterialTheme.colors.primary
                },
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End
            )
        )

        Icon(
            modifier = Modifier
                .padding(top = 12.dp),
            painter = painterResource(id = R.drawable.ic_arrow_down),
            contentDescription = stringResource(id = R.string.label_arrow_down_icon),
            tint = if (isTaskSelected) {
                MaterialTheme.colors.background

            } else {
                MaterialTheme.colors.primary
            }

        )
    }
}

@Composable
fun FabButton(onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .size(56.dp)
            .background(
                color = MaterialTheme.colors.primary,
                shape = CircleShape
            )
            .clickable { onClick() }
            .clip(CircleShape)
            .padding(8.dp),
        imageVector = Icons.Default.Add,
        contentDescription = stringResource(id = R.string.label_add_note_button),
        tint = MaterialTheme.colors.background
    )
}

@Composable
fun SearchField(value: String, isTaskSelected: Boolean, onValueChange: (String) -> Unit) {

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = { onValueChange(it) },
        shape = MaterialTheme.shapes.large,
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onSurface,
            backgroundColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = if (isTaskSelected) {
                    stringResource(id = R.string.label_search_tasks)
                } else {
                    stringResource(id = R.string.label_search_notes)
                }
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.label_search_notes)
            )
        },
        maxLines = 1,
    )
}

@Composable
fun NoteListSection(
    notes: List<NoteView>,
    navHostController: NavHostController,
    notesViewModel: NotesViewModel
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        content = {
            items(notes) { note ->
                NotesItem(
                    noteView = note,
                    onClick = {
                        navHostController.navigate(
                            Screens.Note.withArgs(
                                note.isTask.toString(),
                                note.id.toString()
                            )
                        )
                    },
                    onLongPress = { notesViewModel.deleteNote(it) })
            }
        }
    )
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun NotesItem(noteView: NoteView, onClick: () -> Unit, onLongPress: (NoteView) -> Unit) {
    var isShowingDialog by remember {
        mutableStateOf(false)
    }

    if (isShowingDialog) {
        DeleteDialog(
            onDismiss = { isShowingDialog = false },
            onClick = { onLongPress(noteView) }
        )
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(140.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onClick()
                    },
                    onLongPress = {
                        isShowingDialog = true
                    }
                )
            },
        shape = MaterialTheme.shapes.large,
        backgroundColor = Color.White,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(all = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = noteView.title,
                style = TextStyle(
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                ), maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = noteView.notes,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Light,
                    letterSpacing = TextUnit(2f, TextUnitType.Sp),
                    textAlign = TextAlign.Start
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DeleteDialog(onDismiss: () -> Unit, onClick: () -> Unit) {

    AlertDialog(
        modifier = Modifier.padding(all = 8.dp),
        title = {
            Text(
                text = stringResource(id = R.string.label_delete_item),
                style = TextStyle(color = MaterialTheme.colors.onBackground),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.label_delete_message),
                style = TextStyle(
                    color = MaterialTheme.colors.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                DialogButton(
                    backgroundColor = MaterialTheme.colors.background,
                    text = stringResource(id = R.string.label_cancel)
                ) {
                    onDismiss()
                }

                DialogButton(
                    backgroundColor = MaterialTheme.colors.primary,
                    text = stringResource(id = R.string.label_delete)
                ) {
                    onClick()
                    onDismiss()
                }
            }
        },
        onDismissRequest = { onDismiss() })
}

@Composable
fun DialogButton(backgroundColor: Color, text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(8.dp),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = MaterialTheme.colors.onPrimary
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = MaterialTheme.colors.onPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}
