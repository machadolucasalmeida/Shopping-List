package com.lucas.shoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ShoppingItem(val id : Int, var name : String, var quantity : Int, var isEditing : Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp(modifier : Modifier = Modifier) {
    var sItem by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false)}
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize().padding(top = 22.dp),
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = {showDialog = true},
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),

        ){
            Text(text = "Add Item", color = Color.White)
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)){
            items(sItem){
                item ->
                if(item.isEditing){
                    ShoppingItemEditor(
                        item = item,
                        onEditComplete = {
                            editedName, editedQuantity -> sItem = sItem.map {it.copy(isEditing = false)}
                            val editedItem = sItem.find {it.id == item.id}
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editedQuantity
                            }
                        })
                }else{
                    ShoppingListItem(
                        item = item,
                        onEditClick = {
                        sItem = sItem.map { it.copy(isEditing = it.id == item.id)}},
                        onDeleteClick = {sItem = sItem - item})
                }
            }
        }
    }

    if(showDialog){
        AlertDialog(onDismissRequest = {}, modifier = Modifier.clip(RoundedCornerShape(20.dp))) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = 20.dp, end = 20.dp, top = 30.dp, bottom = 30.dp),

                verticalArrangement = Arrangement.Center,

            ){
                Text(text = "Add Shopping Item", fontSize = 22.sp)
                Spacer(modifier = Modifier.padding(20.dp))
                OutlinedTextField(
                    value = itemName,
                    onValueChange = {itemName = it},
                    label = { Text(text = "Item")},
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.padding(12.dp))
                OutlinedTextField(
                    value =itemQuantity,
                    onValueChange = {itemQuantity = it},
                    label = { Text(text = "Quantity")},
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.padding(25.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Button(
                        onClick = {
                            showDialog = false
                            if(itemName.isNotBlank()){
                                val newItem : ShoppingItem = ShoppingItem(name = itemName, quantity = itemQuantity.toInt(), id =sItem.size+1)
                                sItem = sItem + newItem
                                itemName = ""
                                itemQuantity = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(text = "Add", color = MaterialTheme.colorScheme.surface)
                    }
                    Button(onClick = {showDialog = false},  colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)) {
                        Text(text = "Cancel", color = MaterialTheme.colorScheme.surface)
                    }
                }
            }
        }
    }
}

@Composable
fun ShoppingListItem(item: ShoppingItem,
                     onEditClick: () -> Unit,
                     onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, color = (MaterialTheme.colorScheme.primary)),
                shape = RoundedCornerShape(20)
            ),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Name: ${item.name}", modifier = Modifier.padding(8.dp))
        Text(text = "Quantity: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(  modifier = Modifier
            .padding(8.dp)) {
            IconButton(
                onClick = onEditClick,
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(
                onClick = onDeleteClick,
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingItem,onEditComplete: (String, Int) -> Unit) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .border(border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary), shape = RoundedCornerShape(20)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column (modifier = Modifier.padding(start = 22.dp, top = 16.dp, bottom = 16.dp)){
            BasicTextField(
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.surface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.surface),

            )

            BasicTextField(
                value = editedQuantity,
                onValueChange = { editedQuantity = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.surface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.surface)
            )
        }
        Button(
            onClick = {
                isEditing = false
                onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
            },
            modifier = Modifier.padding(end = 22.dp)
        ) {
            Text("Save", color = Color.White)
        }
    }
}
@Preview
@Composable
fun ShoppingListAppPreview(){
    val item = ShoppingItem(id = 1, name = "Sample Item", quantity = 2)
    ShoppingListItem(item, {}, {})
}