package com.curdfirestore.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.curdfirestore.util.SharedViewModel
import com.curdfirestore.util.UserData
@Composable
fun GetDataScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
) {
    val selectedUserId by rememberUpdatedState(sharedViewModel.selectedUserId)
    val userId = selectedUserId
    // Use `remember` para criar variáveis observáveis
    var inputUserID by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var profession by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var ageInt by remember { mutableStateOf(0) }

    val context = LocalContext.current

    // Use `LaunchedEffect` para buscar os dados do Firestore quando a tela for criada
    LaunchedEffect(userId) {
        sharedViewModel.retrieveData(
            userID = userId ?: "",
            context = context
        ) { data ->
            // Atualize os valores das variáveis com os dados do Firestore
            inputUserID = data.userID
            name = data.name
            profession = data.profession
            age = data.age.toString()
            ageInt = data.age
        }
    }

    // main Layout
    Column(modifier = Modifier.fillMaxSize()) {
        // back button
        Row(
            modifier = Modifier
                .padding(start = 15.dp, top = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back_button")
            }
        }

//        if (selectedUserId != null) {
//            Text(text = "ID é diferente de nulo")
//
//            Spacer(modifier = Modifier.padding(30.dp))
//            Text(text = "valor do selectedUserID: $selectedUserId")
//            Text(text = "valor do UserID: $userId")
//        } else {
//            // Lide com o cenário em que selectedUserId é nulo
//            // Por exemplo, mostre uma mensagem de erro ou navegue de volta
//        }
        // get data Layout
        Column(
            modifier = Modifier
                .padding(start = 60.dp, end = 60.dp, bottom = 50.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // userID
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    value = inputUserID,
                    onValueChange = {
                        inputUserID = it
                    },
                    label = {
                        Text(text = "ID do usuário")
                    }
                )
                // get user data Button
                Button(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .width(100.dp),
                    onClick = {
                        sharedViewModel.retrieveData(
                            userID = inputUserID,
                            context = context
                        ) { data ->
                            // Atualize os valores das variáveis com os dados do Firestore
                            inputUserID = data.userID
                            name = data.name
                            profession = data.profession
                            age = data.age.toString()
                            ageInt = data.age
                        }
                    }
                ) {
                    Text(text = "Buscar dados")
                }
            }
            // Name
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text(text = "Nome")
                }
            )
            // Profession
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = profession,
                onValueChange = {
                    profession = it
                },
                label = {
                    Text(text = "Profissão")
                }
            )
            // Age
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = age,
                onValueChange = {
                    age = it
                    if (age.isNotEmpty()) {
                        ageInt = age.toInt()
                    }
                },
                label = {
                    Text(text = "Idade")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            // save Button
            Button(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(),
                onClick = {
                    val userData = UserData(
                        userID = inputUserID,
                        name = name,
                        profession = profession,
                        age = ageInt
                    )

                    sharedViewModel.saveData(userData = userData, context = context)
                }
            ) {
                Text(text = "Salvar")
            }
            // delete Button
            Button(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                onClick = {
                    sharedViewModel.deleteData(
                        userID = inputUserID,
                        context = context,
                        navController = navController
                    )
                }
            ) {
                Text(text = "Apagar")
            }
        }
    }
}

