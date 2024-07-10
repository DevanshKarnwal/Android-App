package com.example.firebaseproject1.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.transition.Visibility
import com.example.firebaseproject1.R
import org.intellij.lang.annotations.JdkConstants.BoxLayoutAxis


@Composable
fun UnderlineTextComponent(
    value : String,
    modifier: Modifier = Modifier) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = colorResource(R.color.colorGray),
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )
}


@Composable
fun NormalTextComponent(
    value : String,
    modifier: Modifier = Modifier) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = colorResource(R.color.colorText),
        textAlign = TextAlign.Center
    )
}
@Composable
fun HeadingTextComponent(
    value : String,
    modifier: Modifier = Modifier) {
    Text(
        text = value,
        modifier = Modifier.fillMaxWidth(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        color = colorResource(R.color.colorText),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    label: String, imageVector: ImageVector,onTextSelected : (String) -> Unit,
    errorStatus : Boolean = false,
                ) {
    val textValue = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        label = { Text(text = label) } ,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
              onTextSelected(it)
                        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = null)
        },
        isError = !errorStatus
        )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(label: String, imageVector: ImageVector,onTextSelected : (String) -> Unit,
                      errorStatus : Boolean = false,
                      ) {
    val password = remember {
        mutableStateOf("")
    }
    val passwordVisible = remember {
        mutableStateOf(false)
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        label = { Text(text = label) } ,
        value = password.value,
        onValueChange = {
            password.value = it
            onTextSelected(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = null)
        },
        trailingIcon = {
            val iconImage = if(passwordVisible.value){
                            Icons.Filled.Visibility
            }else{
                Icons.Filled.VisibilityOff
            }
            var description = if(passwordVisible.value) {
                "Hide Password"
            }else {
                "Show Password"
            }
            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = iconImage, contentDescription ="" )
            }
        },
        visualTransformation = if(passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        isError = !errorStatus

    )
}

@Composable
fun CheckBoxComponent(value : String, onTextSelected : (String) -> Unit ,onCheckedChange :(Boolean) ->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val checkedState = remember {
            mutableStateOf(false)
        }
        Checkbox(checked = checkedState.value, onCheckedChange ={
            checkedState.value = !checkedState.value
            onCheckedChange.invoke(it)
        }
        )
        ClickableTextComponent(value = value, onTextSelected )
    }
}

@Composable
fun ClickableTextComponent(value: String , onTextSelected : (String) -> Unit) {
    val initialText = "By continuing you accept our "
    val privacyPolicyText = "Privacy Policy "
    val andText = "and "
    val termsAndConditionsText = "Terms of Use"
    val annonatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Color.Blue)){
            pushStringAnnotation(tag = privacyPolicyText, annotation = privacyPolicyText)
            append(privacyPolicyText)
        }
        append(andText)
        withStyle(style = SpanStyle(color = Color.Blue)){
            pushStringAnnotation(tag = termsAndConditionsText, annotation = termsAndConditionsText)
            append(termsAndConditionsText)
        }
    }
    ClickableText(text = annonatedString,onClick = {
        offset -> annonatedString.getStringAnnotations(offset,offset).firstOrNull()?.also{
            span->
            Log.d("ClickableTextComponent", "ClickableTextComponent:")
            if(span.item == termsAndConditionsText){
                onTextSelected(span.item)
            }
    }
    })
}

@Composable
fun ButtonComponent(value: String,onButtonClicked: () -> Unit,isEnabled : Boolean = false) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        onClick = { onButtonClicked.invoke() },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        enabled = isEnabled
        ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp)
                    .background(
                        brush = Brush.horizontalGradient(listOf(Color.Blue, Color.Red)),
                        shape = RoundedCornerShape(50.dp)
                    ), contentAlignment = Alignment.Center){
                    Text(value, fontWeight = FontWeight.Bold)
                }
    }

}

@Composable
fun DividerTextComponent() {
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        HorizontalDivider(modifier = Modifier
            .fillMaxWidth()
            .weight(1f) , color = Color.Gray, thickness = 1.dp)
        Text(text = "or" , fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp),)
        HorizontalDivider(modifier = Modifier
            .fillMaxWidth()
            .weight(1f) , color = Color.Gray, thickness = 1.dp)
    }
}

@Composable
fun ClickableLoginTextComponent(tryingToLogin:Boolean = true, onTextSelected : (String) -> Unit) {
    val initialText =  if(tryingToLogin) {
        "Already have an account "
    }else{
        "Dont't have an account yet? Register"
    }
    val loginText = if(tryingToLogin) {
        "Login "
    }else{
        "Register"
    }

    val annonatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Color.Blue)){
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }

    }
    ClickableText(text = annonatedString,onClick = {
            offset -> annonatedString.getStringAnnotations(offset,offset).firstOrNull()?.also{
            span->
        Log.d("ClickableTextComponent", "ClickableTextComponent:")
        if(span.item == loginText){
            onTextSelected(span.item)
        }
    }
    })
}
