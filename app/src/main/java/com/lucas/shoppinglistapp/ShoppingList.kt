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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = {showDialog = true},
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ){
            Text(text = "Add Item", color = MaterialTheme.colorScheme.surface)
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)){
            items(sItem){
                ShoppingListItem(it, {} , {})
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
fun ShoppingListItem(item: ShoppingItem, onEditClick:() -> Unit, onDeleteClick:() -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .border(
            border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(20)
        )
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

        ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = "Name: ${item.name}", modifier = Modifier.padding(8.dp))
            Text(text = "Quantity: ${item.quantity}", modifier = Modifier.padding(8.dp))
        }

        Row(modifier = Modifier.padding(8.dp)){
            IconButton(onClick = {onEditClick}) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Icon")
            }
            IconButton(onClick = {onDeleteClick}) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Icon")
            }
        }

    }
}

@Preview
@Composable
fun ShoppingListAppPreview(){
    val item = ShoppingItem(id = 1, name = "Sample Item", quantity = 2)
    ShoppingListItem(item, {}, {})
}