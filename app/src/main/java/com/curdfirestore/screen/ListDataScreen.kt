package com.curdfirestore.screen

import android.database.DatabaseUtils
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.curdfirestore.nav.Screens
import com.curdfirestore.util.SharedViewModel
import com.curdfirestore.util.UserData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun ListDataScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {

    val firestore = Firebase.firestore.collection("user")


    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { navController.popBackStack() }
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back_button" )
        }

        Text(text = "Usuários cadastrados no Firebase", fontWeight = FontWeight.Bold)
    }

    Column(
        modifier = Modifier.padding(20.dp)
    ) {

        Spacer(modifier = Modifier.padding(top = 20.dp))

        var documents by remember { mutableStateOf<List<String>>(emptyList()) }



        val firestore = Firebase.firestore

        // Função para listar documentos na coleção "user"
        fun listDocuments() {
            firestore.collection("user")
                .get()
                .addOnSuccessListener { result ->
                    val documentIds = result.documents.map { it.id }
                    documents = documentIds
                }
                .addOnFailureListener { exception ->
                    // Lidar com erros
                }
        }

        listDocuments()

        fun listDocuments(userId: String) {
            val firestore = Firebase.firestore

            // Consulta à coleção "user" no Firestore com base no userId
            val userCollection = firestore.collection("user")
            val userDocument = userCollection.document(userId)

            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // O documento do usuário com o userId existe
                        val userData = documentSnapshot.toObject(UserData::class.java)

                        // Verifique se userData não é nulo e, em seguida, atualize os valores
                        if (userData != null) {
                            userData.name = userData.name
                            userData.profession = userData.profession
                            userData.age = userData.age
                        }
                    } else {
                        // O documento do usuário com o userId não existe
                        // Lide com esse cenário adequadamente (por exemplo, exiba uma mensagem de erro)
                    }
                }
                .addOnFailureListener { exception ->
                    // Lidar com erros ao consultar o Firebase Firestore
                }
        }

      //  listDocuments(userId)



        LazyColumn {
            items(documents) { documentId ->
                val onItemClick: () -> Unit = {
                    sharedViewModel.selectedUserId = documentId // Define o userID no SharedViewModel
                    navController.navigate(Screens.GetDataScreen.route)
                }
                ClickableListItem(text = documentId, onItemClick = onItemClick)
            }
        }

        sharedViewModel.documents = documents
    }


}

@Composable
fun ClickableListItem(
    text: String,
    onItemClick: () -> Unit,
) {
    ClickableText(
        modifier = Modifier.padding(vertical = 12.dp),
        text = AnnotatedString(text),
        onClick = { offset ->
            onItemClick()
        }
    )
}