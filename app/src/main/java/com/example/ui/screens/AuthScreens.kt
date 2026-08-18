package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.data.utils.LocalAppLanguage
import com.example.data.utils.Strings
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
        )
        alpha.animateTo(1.0f, animationSpec = tween(500))
        delay(1000)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F2B6B),
                        Color(0xFF1D4ED8),
                        Color(0xFF0B193D)
                    )
                )
            )
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scale.value)
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color.White)
                    .padding(18.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.HomeRepairService,
                    contentDescription = "LocalPro Logo",
                    tint = PrimarySapphire,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "LocalPro",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Trusted Local Services at Your Doorstep",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            CircularProgressIndicator(
                color = AccentAmber,
                strokeWidth = 3.dp,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

enum class AuthStep {
    PHONE_INPUT,
    OTP_VERIFY,
    ROLE_SELECT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onLoginSuccess: (UserEntity) -> Unit,
    onNavigateToWorkerOnboarding: () -> Unit
) {
    val language = LocalAppLanguage.current
    var currentStep by remember { mutableStateOf(AuthStep.PHONE_INPUT) }
    var phoneNumber by remember { mutableStateOf("9876543210") }
    var otpCode by remember { mutableStateOf("") }
    var resendTimer by remember { mutableIntStateOf(45) }
    var isTimerRunning by remember { mutableStateOf(true) }
    var simulatedOtpToast by remember { mutableStateOf<String?>(null) }
    var isSendingOtp by remember { mutableStateOf(false) }

    var fullName by remember { mutableStateOf("Ravi Kumar") }
    var email by remember { mutableStateOf("ravi.kumar@example.com") }
    var selectedRole by remember { mutableStateOf(UserRole.CUSTOMER) }

    val focusManager = LocalFocusManager.current

    // Resend countdown timer
    LaunchedEffect(currentStep, isTimerRunning) {
        if (currentStep == AuthStep.OTP_VERIFY && isTimerRunning) {
            while (resendTimer > 0) {
                delay(1000)
                resendTimer--
            }
            isTimerRunning = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HomeRepairService,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LocalPro",
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp
                        )
                    }
                },
                navigationIcon = {
                    if (currentStep != AuthStep.PHONE_INPUT) {
                        IconButton(onClick = {
                            currentStep = when (currentStep) {
                                AuthStep.ROLE_SELECT -> AuthStep.OTP_VERIFY
                                AuthStep.OTP_VERIFY -> AuthStep.PHONE_INPUT
                                else -> AuthStep.PHONE_INPUT
                            }
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(200))
                },
                label = "auth_steps"
            ) { step ->
                when (step) {
                    AuthStep.PHONE_INPUT -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Hero Brand Banner Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = PrimaryContainerBlue)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(CircleShape)
                                            .background(Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PhoneAndroid,
                                            contentDescription = "Phone Login",
                                            tint = PrimarySapphire,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "Welcome to LocalPro",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = OnPrimaryContainerBlue,
                                        textAlign = TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Book 38+ verified home services or register as an expert service provider",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = OnPrimaryContainerBlue.copy(alpha = 0.8f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            Text(
                                text = "Enter Mobile Number",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "We'll send you a 6-digit one-time verification code via SMS",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { input ->
                                    if (input.length <= 10) {
                                        phoneNumber = input.filter { c -> c.isDigit() }
                                    }
                                },
                                label = { Text("Mobile Number") },
                                placeholder = { Text("9876543210") },
                                leadingIcon = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(start = 12.dp, end = 6.dp)
                                    ) {
                                        Text("🇮🇳 +91", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .height(20.dp)
                                                .width(1.dp)
                                                .background(MaterialTheme.colorScheme.outline)
                                        )
                                    }
                                },
                                trailingIcon = {
                                    if (phoneNumber.isNotEmpty()) {
                                        IconButton(onClick = { phoneNumber = "" }) {
                                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                                        }
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("phone_input_field")
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    if (phoneNumber.length >= 10) {
                                        isSendingOtp = true
                                        // Simulate OTP generation
                                        val randomOtp = (100000..999999).random().toString()
                                        simulatedOtpToast = randomOtp
                                        otpCode = ""
                                        resendTimer = 45
                                        isTimerRunning = true
                                        isSendingOtp = false
                                        currentStep = AuthStep.OTP_VERIFY
                                    }
                                },
                                enabled = phoneNumber.length >= 10 && !isSendingOtp,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("send_otp_button")
                            ) {
                                if (isSendingOtp) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Text(
                                        text = "Get OTP Verification Code",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            // Demo Test Accounts Selection
                            Divider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "⚡ Quick One-Tap Demo Profiles",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Demo Account 1: Customer (Ravi Kumar)
                            OutlinedCard(
                                onClick = {
                                    onLoginSuccess(
                                        UserEntity(
                                            id = "cust_001",
                                            phoneNumber = "+91 98765 43210",
                                            name = "Ravi Kumar",
                                            email = "ravi.kumar@example.com",
                                            role = UserRole.CUSTOMER,
                                            latitude = 17.4239,
                                            longitude = 78.4738,
                                            address = "Banjara Hills, Hyderabad",
                                            favoriteWorkerIds = listOf("w_001", "w_002")
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryContainerBlue),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = PrimarySapphire)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Ravi Kumar (Customer)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("+91 98765 43210 • Banjara Hills", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Demo Account 2: Service Provider (Suresh Reddy)
                            OutlinedCard(
                                onClick = {
                                    onLoginSuccess(
                                        UserEntity(
                                            id = "usr_w_001",
                                            phoneNumber = "+91 98480 12345",
                                            name = "Suresh Reddy",
                                            email = "suresh.electrician@example.com",
                                            role = UserRole.WORKER,
                                            latitude = 17.4250,
                                            longitude = 78.4710,
                                            address = "Banjara Hills, Hyderabad"
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(AmberContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = AccentAmberDark)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Suresh Reddy (Electrician Partner)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("+91 98480 12345 • Verified Pro", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }

                    AuthStep.OTP_VERIFY -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryContainerBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LockClock,
                                    contentDescription = "OTP",
                                    tint = PrimarySapphire,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "Verify Mobile Number",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Enter the 6-digit code sent to +91 $phoneNumber",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            TextButton(onClick = { currentStep = AuthStep.PHONE_INPUT }) {
                                Text("✏️ Edit Mobile Number", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Simulated SMS OTP Notification Bubble
                            if (simulatedOtpToast != null) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            otpCode = simulatedOtpToast ?: "582941"
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = AmberContainer)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Sms,
                                            contentDescription = null,
                                            tint = AccentAmberDark,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "SMS: Your LocalPro OTP is ${simulatedOtpToast}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color(0xFF78350F)
                                            )
                                            Text(
                                                text = "Tap here to auto-fill OTP",
                                                fontSize = 11.sp,
                                                color = Color(0xFF92400E)
                                            )
                                        }
                                        Icon(
                                            Icons.Default.TouchApp,
                                            contentDescription = null,
                                            tint = AccentAmberDark
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // OTP Input Field
                            OutlinedTextField(
                                value = otpCode,
                                onValueChange = { input ->
                                    if (input.length <= 6) {
                                        otpCode = input.filter { c -> c.isDigit() }
                                    }
                                },
                                label = { Text("6-Digit OTP Code") },
                                placeholder = { Text("e.g. 582941") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.NumberPassword,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("otp_input_field")
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // OTP Digit Display Boxes
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                for (i in 0 until 6) {
                                    val digit = if (i < otpCode.length) otpCode[i].toString() else ""
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (digit.isNotEmpty()) PrimaryContainerBlue else MaterialTheme.colorScheme.surfaceVariant)
                                            .border(
                                                width = if (digit.isNotEmpty()) 2.dp else 1.dp,
                                                color = if (digit.isNotEmpty()) PrimarySapphire else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                                shape = RoundedCornerShape(10.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = digit,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (digit.isNotEmpty()) PrimarySapphire else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isTimerRunning) "Resend OTP in ${resendTimer}s" else "Didn't get code?",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                TextButton(
                                    onClick = {
                                        resendTimer = 45
                                        isTimerRunning = true
                                        val newOtp = (100000..999999).random().toString()
                                        simulatedOtpToast = newOtp
                                    },
                                    enabled = !isTimerRunning
                                ) {
                                    Text("Resend SMS", fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (phoneNumber == "9848012345" || phoneNumber == "98480 12345") {
                                        // Log in as Worker
                                        onLoginSuccess(
                                            UserEntity(
                                                id = "usr_w_001",
                                                phoneNumber = "+91 $phoneNumber",
                                                name = "Suresh Reddy",
                                                role = UserRole.WORKER
                                            )
                                        )
                                    } else {
                                        // Proceed to Profile Setup or Instant Customer Login
                                        currentStep = AuthStep.ROLE_SELECT
                                    }
                                },
                                enabled = otpCode.length >= 4 || otpCode.isEmpty(),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("verify_otp_button")
                            ) {
                                Text(
                                    text = "Verify & Proceed",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            TextButton(onClick = {
                                // Quick skip
                                onLoginSuccess(
                                    UserEntity(
                                        id = "cust_001",
                                        phoneNumber = "+91 $phoneNumber",
                                        name = fullName.ifBlank { "Ravi Kumar" },
                                        role = UserRole.CUSTOMER
                                    )
                                )
                            }) {
                                Text("⚡ Skip directly to Home")
                            }
                        }
                    }

                    AuthStep.ROLE_SELECT -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Setup Your Profile",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Choose how you'd like to use LocalPro",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                label = { Text("Your Full Name") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email Address") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(22.dp))

                            Text(
                                text = "Select Account Type",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Customer Option
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedRole = UserRole.CUSTOMER }
                                        .border(
                                            width = if (selectedRole == UserRole.CUSTOMER) 2.dp else 1.dp,
                                            color = if (selectedRole == UserRole.CUSTOMER) PrimarySapphire else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                            shape = RoundedCornerShape(14.dp)
                                        ),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selectedRole == UserRole.CUSTOMER) PrimaryContainerBlue else MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = if (selectedRole == UserRole.CUSTOMER) PrimarySapphire else MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Customer",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "Book Services",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                // Service Partner Option
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedRole = UserRole.WORKER }
                                        .border(
                                            width = if (selectedRole == UserRole.WORKER) 2.dp else 1.dp,
                                            color = if (selectedRole == UserRole.WORKER) AccentAmberDark else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                            shape = RoundedCornerShape(14.dp)
                                        ),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selectedRole == UserRole.WORKER) AmberContainer else MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Handyman,
                                            contentDescription = null,
                                            tint = if (selectedRole == UserRole.WORKER) AccentAmberDark else MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Partner / Pro",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "Offer Services",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            Button(
                                onClick = {
                                    val newUser = UserEntity(
                                        id = "usr_${UUID.randomUUID().toString().take(8)}",
                                        phoneNumber = "+91 $phoneNumber",
                                        name = fullName.ifBlank { "User" },
                                        email = email.ifBlank { "user@example.com" },
                                        role = selectedRole,
                                        latitude = 17.4239,
                                        longitude = 78.4738,
                                        address = "Hyderabad"
                                    )
                                    if (selectedRole == UserRole.WORKER) {
                                        onLoginSuccess(newUser)
                                        onNavigateToWorkerOnboarding()
                                    } else {
                                        onLoginSuccess(newUser)
                                    }
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                            ) {
                                Text(
                                    text = "Start Using LocalPro",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
