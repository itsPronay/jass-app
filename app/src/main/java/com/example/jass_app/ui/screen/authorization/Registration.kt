package com.example.jass_app.ui.screen.authorization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jass_app.R
import com.example.jass_app.data.viewmodel.RegistrationViewModel
import com.example.jass_app.ui.component.CustomButton
import com.example.jass_app.ui.component.CustomOutlinedTextField
import com.example.jass_app.ui.theme.ButtonGradient
import com.example.jass_app.util.PreferencesManager
import org.koin.androidx.compose.getViewModel


@Composable
fun Registration(
    navHostController: NavHostController,
    viewModel: RegistrationViewModel = getViewModel()
) {

    var isCorrectEmail by remember { mutableStateOf(true) }
    var isCorrectPassword by remember { mutableStateOf(true) }
    var isCorrectPasswordRepeat by remember { mutableStateOf(true) }

    val mContext = LocalContext.current
    val preferencesManager = remember { PreferencesManager(mContext) }

    val connectionStatus by viewModel.connectionStatus.observeAsState()
    val accessToke by remember { mutableStateOf(preferencesManager.getData("accessToken", "")) }
    val refreshToke by remember { mutableStateOf(preferencesManager.getData("refreshToken", "")) }

    val buttonModifier = Modifier
        .fillMaxWidth()
    val buttonBorder = BorderStroke(2.dp, ButtonGradient)

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(1.dp))
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.jass_logo_black),
                    contentDescription = "Jass logo"
                )
                Spacer(modifier = Modifier.padding(5.dp))

                RegistrationTextFieldBlock(
                    isCorrectEmail, isCorrectPassword, isCorrectPasswordRepeat, viewModel
                )

                CustomButton(
                    text = { Text("Continue", color = Color.White) },
                    onClick = {
                        if (viewModel.passwordValue.value == viewModel.passwordRepeatValue.value
                            && (viewModel.passwordValue.value != "" || viewModel.emailValue.value != "")
                        ) {
                            viewModel.register()
                            if (viewModel.registrationStatus.value!!) {
                                preferencesManager.saveData(
                                    "accessToken",
                                    viewModel.accessToken.value!!
                                )
                                preferencesManager.saveData(
                                    "refreshToken",
                                    viewModel.refreshToken.value!!
                                )
                            }

                        } else {
                            isCorrectPasswordRepeat = false
                        }
                    }, modifier = buttonModifier, border = buttonBorder
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        CustomButton(
                            text = { Text(stringResource(id = R.string.sign_up), color = Color.White) },
                            onClick = {},
                            modifier = buttonModifier,
                            border = buttonBorder,
                            enable = false
                        )
                    }
                    Column(modifier = Modifier.weight(0.05f)) {}
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        CustomButton(
                            text = { Text(stringResource(id = R.string.sign_in), color = Color.White) },
                            onClick = {
                                navHostController.navigate("Authorization")
                            }, modifier = buttonModifier, border = buttonBorder
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(0.45f)
                    ) {
                        HorizontalDivider()
                    }
                    Column(
                        modifier = Modifier.weight(0.1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(id = R.string.or))
                    }
                    Column(
                        modifier = Modifier.weight(0.45f)
                    ) {
                        HorizontalDivider()
                    }
                }
                CustomButton(
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Sign Up with Google",
                                modifier = Modifier.padding(horizontal = 5.dp), color = Color.White
                            )
                        }
                    },
                    onClick = { /*TODO*/ },
                    icon = painterResource(R.drawable.google_logo),
                    modifier = Modifier.fillMaxWidth(),
                    border = buttonBorder
                )
            }
        }
    }
}

@Composable
fun RegistrationTextFieldBlock(
    isCorrectEmail: Boolean,
    isCorrectPassword: Boolean,
    isCorrectPasswordRepeat: Boolean,
    viewModel: RegistrationViewModel
) {

    val textFieldModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp)

    CustomOutlinedTextField(
        hintText = stringResource(id = R.string.email),
        keyboardType = KeyboardType.Email,
        supportingText = stringResource(R.string.incorrect_email_message),
        isError = !isCorrectEmail,
        modifier = textFieldModifier,
        onChange = { newValue ->
            viewModel.emailValue.value = newValue
        }
    )
    CustomOutlinedTextField(
        hintText = stringResource(id = R.string.password),
        keyboardType = KeyboardType.Password,
        isPasswordToggleEnabled = true,
        supportingText = stringResource(R.string.incorrect_password_message),
        isError = !isCorrectPassword,
        modifier = textFieldModifier,
        onChange = { newValue ->
            viewModel.passwordValue.value = newValue
        }
    )
    CustomOutlinedTextField(
        hintText = stringResource(id = R.string.repeat_password),
        keyboardType = KeyboardType.Password,
        isPasswordToggleEnabled = true,
        supportingText = stringResource(R.string.incorrect_password_repeat_message),
        modifier = textFieldModifier,
        isError = !isCorrectPasswordRepeat,
        onChange = { newValue ->
            viewModel.passwordRepeatValue.value = newValue
        }
    )
}


/*
TODO - Add styles on buttons and field.
TODO - Import correct logos
 */