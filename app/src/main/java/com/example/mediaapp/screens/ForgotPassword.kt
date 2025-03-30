package com.example.mediaapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mediaapp.R
import com.example.mediaapp.Screen
import com.example.mediaapp.viewModels.LoginPageViewModel

@Composable
fun ForgotPasswordPageLayout(navController: NavController,
                             viewModel: LoginPageViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.surface
            )
    ) {
        HeaderText(stringResource(R.string.login_top_name))
        MainTitleText(R.string.login_forgot_password)
        SubTitleText(R.string.login_forgot_password_please)
        TextFieldForInput(viewModel, InputType.Email)
        Button(onClick = {
            viewModel.sendPasswordResetEmail(navController)
        },
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 36.dp, end = 29.dp)
                .align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                stringResource(R.string.login_forgot_password_reset),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        BottomSignText(R.string.login_already_account, R.string.login_already_account_sign, navController)
    }
}