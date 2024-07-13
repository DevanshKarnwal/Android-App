package com.example.firebasesignin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.firebasesignin.ui.theme.FireBaseSignInTheme

class MainActivity : ComponentActivity() {
    private val googlrAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FireBaseSignInTheme {
                val navController = rememberNavController()
                NavHost(navController,startDestination = "SignIn"){
                    composable("SignIn"){
                        val viewmodel = viewModel<SignInViewModel>()
                        val state by viewmodel.state.collectAsStateWithLifecycle()

                        val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
                            contract =ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = {
                                result ->
                                if(result.resultCode == RESULT_OK){
                                    lifecycleScope.launch {
                                        val signInResult = googlrAuthUiClient.getSignWithIntent(result.data ?: return@launch)
                                        viewmodel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        )
                        SignInScreen(state = state , onSignInClick = {
                            lifecycleScope.launch {
                                val signInIntentSender = googlrAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?:retrun @launch
                                    ).build()
                                )
                        })
                    }
                }
            }
        }
    }
}
