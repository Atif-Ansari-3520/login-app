package com.example.myloginapplication
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth


import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalConfiguration
import com.google.firebase.auth.userProfileChangeRequest


@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { welcomescreen(navController) }
        composable("signin") { SignInScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("verifyemail") { VerifyEmailScreen(navController) }
        composable("userscreen") { UserScreen(navController) }

    }
}


// Updated welcomescreen with navController parameter (kept as you provided)
@Composable
fun welcomescreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Red, Color.Black)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 66.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dumbbell),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(70.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = "FITNESS CLUB",
                    color = Color.White,
                    fontSize = 28.sp,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(26.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome Back",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(25.dp))

                // SIGN IN button navigates to signin screen
                OutlinedButton(
                    onClick = { navController.navigate("signin") },
                    modifier = Modifier.size(width = 300.dp, height = 50.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Text(
                        text = "SIGN IN",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // SIGN UP button navigates to signup screen
                OutlinedButton(
                    onClick = { navController.navigate("signup") },
                    modifier = Modifier.size(width = 300.dp, height = 50.dp),
                    border = BorderStroke(2.dp, Color.White),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                ) {
                    Text(
                        text = "SIGN UP",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 70.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login with Social Media",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 19.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.facebook),
                        contentDescription = "Facebook Icon",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.instagram),
                        contentDescription = "Instagram Icon",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

// ---------- SignInScreen implementation (fixed) ----------
// Password validation function
fun isValidPassword(password: String): Boolean {
    val regex = Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")
    return regex.matches(password)
}


@Composable
fun SignInScreen(navController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } // 👈 for loading animation

    val auth = FirebaseAuth.getInstance()

    val leftGradientColor = Color(0xFFCC0A1E)
    val rightGradientColor = Color(0xFF24002A)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(listOf(leftGradientColor, rightGradientColor)))
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 100.dp)
                .animateContentSize(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            tonalElevation = 4.dp
        ) {
            Column {
                // Gradient header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(Brush.horizontalGradient(listOf(leftGradientColor, rightGradientColor))),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column(modifier = Modifier.padding(start = 22.dp)) {
                        Text("Hello", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        Text("Sign in!", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                val emailFocusedColor by animateColorAsState(
                    targetValue = if (email.isNotEmpty()) leftGradientColor else Color(0xFFBDBDBD),
                    label = "emailBorder"
                )

                var emailError by rememberSaveable { mutableStateOf(false) }

                Text(
                    "Gmail",
                    color = leftGradientColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 20.dp, top = 8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = !email.contains("@") // 👈 must contain "@"
                        if (emailError) {
                            Toast.makeText(
                                navController.context,
                                "Email must contain '@' symbol.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    isError = emailError,
                    placeholder = { Text("abc123@gmail.com", fontSize = 16.sp) },
                    trailingIcon = { Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .animateContentSize(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = if (emailError) Color.Red else emailFocusedColor,
                        unfocusedIndicatorColor = if (emailError) Color.Red else Color(0xFFBDBDBD),
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )


                Spacer(modifier = Modifier.height(12.dp))

                val passwordFocusedColor by animateColorAsState(
                    targetValue = if (password.isNotEmpty()) leftGradientColor else Color(0xFFBDBDBD),
                    label = "passwordBorder"
                )

                Text(
                    "Password",
                    color = leftGradientColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 20.dp, top = 8.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = !isValidPassword(it)
                        if (passwordError) {
                            Toast.makeText(
                                navController.context,
                                "Password must be 8+ chars, include uppercase, number & special character.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    isError = passwordError,
                    placeholder = { Text("********", fontSize = 14.sp) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            val description = if (passwordVisible) "Hide password" else "Show password"
                            Icon(imageVector = icon, contentDescription = description)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .animateContentSize(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = if (passwordError) Color.Red else passwordFocusedColor,
                        unfocusedIndicatorColor = if (passwordError) Color.Red else Color(0xFFBDBDBD),
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )


                Spacer(modifier = Modifier.height(8.dp))

                // 🔧 Forgot Password with check
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Text(
                        "Forgot password?",
                        color = Color(0xFF3C2E3A),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable {
                                if (email.isNotEmpty()) {
                                    isLoading = true
                                    auth.fetchSignInMethodsForEmail(email)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val signInMethods = task.result?.signInMethods
                                                if (signInMethods.isNullOrEmpty()) {
                                                    Toast.makeText(
                                                        navController.context,
                                                        "Please register your account first.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    auth.sendPasswordResetEmail(email)
                                                        .addOnCompleteListener { resetTask ->
                                                            if (resetTask.isSuccessful) {
                                                                Toast.makeText(
                                                                    navController.context,
                                                                    "Reset email sent!",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            } else {
                                                                Toast.makeText(
                                                                    navController.context,
                                                                    "Failed to send reset email.",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        }
                                                }
                                            } else {
                                                Toast.makeText(
                                                    navController.context,
                                                    "Error checking account: ${task.exception?.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            isLoading = false
                                        }
                                } else {
                                    Toast.makeText(
                                        navController.context,
                                        "Please enter your email first.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // SIGN IN button with Firebase logic
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .height(55.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Brush.horizontalGradient(listOf(leftGradientColor, rightGradientColor)))
                        .clickable {
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val user = auth.currentUser
                                        if (user != null && user.isEmailVerified) {
                                            navController.navigate("userscreen")
                                        } else {
                                            Toast.makeText(
                                                navController.context,
                                                "Please verify your email before signing in.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            navController.context,
                                            "Sign in failed: ${task.exception?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("SIGN IN", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(26.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Don't have account?", color = Color(0xFF9E9E9E))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Sign up",
                        color = Color(0xFF24002A),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { navController.navigate("signup") }
                    )
                }
            }
        }

        // 👇 Animated loading overlay
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = leftGradientColor)
            }
        }
    }
}



@Composable
fun SignUpScreen(navController: NavController) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var emailOrPhone by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf(false) }
    var confirmError by rememberSaveable { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()

    val leftGradientColor = Color(0xFFCC0A1E)
    val rightGradientColor = Color(0xFF24002A)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(listOf(leftGradientColor, rightGradientColor)))
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 20.dp)
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp - 20.dp)
                .animateContentSize(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            tonalElevation = 4.dp
        ) {
            Column {
                // Fixed header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(Brush.horizontalGradient(listOf(leftGradientColor, rightGradientColor))),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Create Your Account",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                // Scrollable form
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    // Full Name
                    Text("Full Name", color = leftGradientColor, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 20.dp, top = 6.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = { Text("M.Atif", fontSize = 16.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .animateContentSize(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = leftGradientColor,
                            unfocusedIndicatorColor = Color(0xFFBDBDBD),
                            cursorColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    var emailError by rememberSaveable { mutableStateOf(false) }

                    Text(
                        "Email",
                        color = leftGradientColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 20.dp, top = 6.dp)
                    )

                    OutlinedTextField(
                        value = emailOrPhone,
                        onValueChange = {
                            emailOrPhone = it
                            emailError = !emailOrPhone.contains("@") // 👈 must contain "@"
                            if (emailError) {
                                Toast.makeText(
                                    navController.context,
                                    "Email must contain '@' symbol.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        isError = emailError,
                        placeholder = { Text("abc123@gmail.com", fontSize = 16.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .animateContentSize(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = if (emailError) Color.Red else leftGradientColor,
                            unfocusedIndicatorColor = if (emailError) Color.Red else Color(0xFFBDBDBD),
                            cursorColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        "Password",
                        color = leftGradientColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 20.dp, top = 6.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = !isValidPassword(it)
                            confirmError = confirmPassword.isNotEmpty() && confirmPassword != it

                            // 👇 Show Toast if password invalid
                            if (passwordError) {
                                Toast.makeText(
                                    navController.context,
                                    "Password must be 8+ chars, include uppercase, number & special character.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        isError = passwordError,
                        placeholder = { Text("********", fontSize = 14.sp) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                Icon(imageVector = icon, contentDescription = null)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .animateContentSize(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = if (passwordError) Color.Red else leftGradientColor,
                            unfocusedIndicatorColor = if (passwordError) Color.Red else Color(0xFFBDBDBD),
                            cursorColor = Color.Black
                        )
                    )


                    Spacer(modifier = Modifier.height(10.dp))

                    // Confirm Password
                    Text("Confirm Password", color = leftGradientColor, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 20.dp, top = 6.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            confirmError = it != password
                        },
                        isError = confirmError,
                        placeholder = { Text("********", fontSize = 14.sp) },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                val icon = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                Icon(imageVector = icon, contentDescription = null)
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .animateContentSize(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = if (confirmError) Color.Red else leftGradientColor,
                            unfocusedIndicatorColor = if (confirmError) Color.Red else Color(0xFFBDBDBD),
                            cursorColor = Color.Black
                        )
                    )

                    if (confirmError) {
                        Text(
                            text = "Passwords do not match.",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 20.dp, top = 4.dp, end = 20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))
//
//                     SIGN UP Button with Firebase logic// SIGN UP Button with Firebase logic
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp)
                            .height(55.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(Brush.horizontalGradient(listOf(leftGradientColor, rightGradientColor)))
                            .clickable {
                                if (fullName.isNotEmpty() && emailOrPhone.isNotEmpty() && password.isNotEmpty() && !passwordError && !confirmError) {
                                    auth.createUserWithEmailAndPassword(emailOrPhone, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val user = auth.currentUser

                                                // 👇 Save the user's name in Firebase profile
                                                val profileUpdates = userProfileChangeRequest {
                                                    displayName = fullName
                                                }
                                                user?.updateProfile(profileUpdates)

                                                // 👇 Send verification email
                                                user?.sendEmailVerification()
                                                    ?.addOnCompleteListener { verifyTask ->
                                                        if (verifyTask.isSuccessful) {
                                                            Toast.makeText(navController.context, "Verification email sent!", Toast.LENGTH_SHORT).show()
                                                            navController.navigate("verifyemail")
                                                        } else {
                                                            Toast.makeText(navController.context, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                            } else {
                                                Toast.makeText(navController.context, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(navController.context, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "Sign Up",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // Bottom Sign In prompt
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Already have account?", color = Color(0xFF9E9E9E))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Sign In",
                            color = Color(0xFF24002A),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { navController.navigate("signin") }
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun VerifyEmailScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    val leftGradientColor = Color(0xFFCC0A1E)
    val rightGradientColor = Color(0xFF24002A)

    var showMessage by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(listOf(leftGradientColor, rightGradientColor))),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color.White.copy(alpha = 0.9f),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(
                    visible = showMessage,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = "A verification link has been sent to your email.",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = leftGradientColor,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }

                Button(
                    onClick = {
                        user?.reload()?.addOnCompleteListener {
                            if (user != null && user.isEmailVerified) {
                                Toast.makeText(navController.context, "Email verified!", Toast.LENGTH_SHORT).show()
                                navController.navigate("userscreen") {
                                    popUpTo("verifyemail") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(navController.context, "Please verify your email first.", Toast.LENGTH_SHORT).show()
                                showMessage = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = leftGradientColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirm", color = Color.White, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = {
                    user?.sendEmailVerification()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(navController.context, "Verification email resent!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Text("Resend Email", color = rightGradientColor, fontSize = 16.sp)
                }
            }
        }
    }
}



@Composable
fun UserScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    // Gradient background same as SignUp/VerifyEmail
    val leftGradientColor = Color(0xFFCC0A1E)
    val rightGradientColor = Color(0xFF24002A)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(listOf(leftGradientColor, rightGradientColor))),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color.White.copy(alpha = 0.9f),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                        text = "Hi welcome ${user?.displayName ?: "User"}",
                            fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = leftGradientColor
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        Toast.makeText(navController.context, "Logging out...", Toast.LENGTH_SHORT).show()
                        auth.signOut()
                        navController.navigate("signin") {
                            popUpTo("userscreen") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = leftGradientColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}
