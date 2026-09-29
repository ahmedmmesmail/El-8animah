package com.codenytra.amme.el_8animah.features

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.codenytra.amme.el_8animah.BaseActivity
import com.codenytra.amme.el_8animah.BuildConfig
import com.codenytra.amme.el_8animah.R
import com.codenytra.amme.el_8animah.ui.theme.El8animahTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import com.codenytra.amme.el_8animah.ui.theme.Tangerine
import com.codenytra.amme.el_8animah.ui.theme.LightGold
import com.codenytra.amme.el_8animah.ui.theme.Peach



class AboutActivity : BaseActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            El8animahTheme {
                AboutScreen(onBack = { finish() })
            }
        }
    }
}




private data class ContactInfo(
    val labelRes: Int,
    val value: String,
    val icon: ImageVector,
    val uri: String,
)

private val contacts = listOf(
    ContactInfo(
        R.string.about_contact_email,
        "ahmedmme.26@gmail.com",
        Icons.Rounded.Email,
        "mailto:ahmedmme.26@gmail.com"
    ),
    ContactInfo(R.string.about_contact_portfolio, "ahmedmmesmail.me", Icons.Rounded.Language, "https://ahmedmmesmail.me"),
    ContactInfo(
        R.string.about_contact_github,
        "github.com/ahmedmmesmail",
        Icons.Rounded.Code,
        "https://github.com/ahmedmmesmail"
    ),
    ContactInfo(
        R.string.about_contact_linkedin,
        "linkedin.com/in/ahmedmmesmail",
        Icons.Rounded.Person,
        "https://linkedin.com/in/ahmedmmesmail"
    ),
)

/* ----------------------------------------------------------------------
 * Main Screen
 * -------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.about_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedAppIcon()
            Spacer(Modifier.height(20.dp))

            FadeSlideIn(delayMs = 300) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        stringResource(R.string.app_name),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    // Reads version from build.gradle.kts (requires 'buildFeatures { buildConfig = true }')
                    Text(
                        stringResource(R.string.settings_version) + " : " + BuildConfig.VERSION_NAME,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        stringResource(R.string.app_desc),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.height(28.dp))
            FadeSlideIn(delayMs = 500) { DeveloperCard() }

            Spacer(Modifier.height(20.dp))
            FadeSlideIn(delayMs = 700) { ContactSection() }

            Spacer(Modifier.height(24.dp))
            FadeSlideIn(delayMs = 900) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(R.string.about_made_with),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                    Icon(
                        Icons.Rounded.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        stringResource(R.string.about_in_egypt),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

/* ----------------------------------------------------------------------
 * أيقونة التطبيق المتحركة
 * - ظهور بنطّة (spring) أول ما الصفحة تفتح
 * - طفو لفوق وتحت + نبض في الحجم
 * - حلقة متدرجة بتلف حواليها + توهّج
 * - لما تضغط عليها بتلف لفة كاملة
 * -------------------------------------------------------------------- */

@Composable
fun AnimatedAppIcon(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val infinite = rememberInfiniteTransition(label = "icon")

    val floatY by infinite.animateFloat(
        initialValue = 0f,
        targetValue = -14f,
        animationSpec = infiniteRepeatable(
            tween(1800, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "float",
    )
    val pulse by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 1.07f,
        animationSpec = infiniteRepeatable(
            tween(1200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "pulse",
    )
    val ringRotation by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing)),
        label = "ring",
    )
    val glow by infinite.animateFloat(
        initialValue = 0.20f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "glow",
    )

    val entrance = remember { Animatable(0f) }
    val tapRotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        entrance.animateTo(
            1f,
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        )
    }

    val primary = LightGold
    val tertiary = Peach
    val secondary = Tangerine

    Box(
        modifier = modifier
            .size(170.dp)
            .graphicsLayer {
                translationY = floatY.dp.toPx()
                scaleX = entrance.value
                scaleY = entrance.value
                alpha = entrance.value.coerceIn(0f, 1f)
            }
            .drawBehind {
                // توهّج ناعم ورا الأيقونة
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(primary.copy(alpha = glow), Color.Transparent),
                        center = Offset(size.width / 2, size.height / 2),
                        radius = size.minDimension / 2,
                    ),
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        // الحلقة الدوّارة
        Box(
            modifier = Modifier
                .size(150.dp)
                .rotate(ringRotation)
                .drawBehind {
                    drawCircle(
                        brush = Brush.sweepGradient(listOf(primary, tertiary, secondary, primary)),
                        style = Stroke(width = 5.dp.toPx()),
                    )
                },
        )

        // الأيقونة نفسها
        Box(
            modifier = Modifier
                .size(108.dp)
                .graphicsLayer {
                    scaleX = pulse
                    scaleY = pulse
                    rotationZ = tapRotation.value
                }
                .clip(RoundedCornerShape(28.dp))
                .background(Brush.linearGradient(listOf(primary, secondary)))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    scope.launch {
                        tapRotation.animateTo(
                            tapRotation.value + 360f,
                            tween(700, easing = FastOutSlowInEasing)
                        )
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            // بدّل الـ Icon ده بلوجو تطبيقك:
            // Image(painterResource(R.drawable.app_logo), contentDescription = null, modifier = Modifier.size(72.dp))
            Icon(
                painter = painterResource(R.mipmap.ic_launcher_foreground),
                contentDescription = stringResource(R.string.app_icon_desc),
                tint = Color(0xFF4F378B),
                modifier = Modifier.size(100.dp),
            )
        }
    }
}

/* ----------------------------------------------------------------------
 * Developer Card
 * -------------------------------------------------------------------- */

@Composable
private fun DeveloperCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Rounded.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(40.dp),
                )
            }
            Spacer(Modifier.size(16.dp))
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.about_dev_header), fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                Text(stringResource(R.string.about_dev_name), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(
                    stringResource(R.string.about_dev_title),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    stringResource(R.string.about_dev_bio),
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/* ----------------------------------------------------------------------
 * Contact Section
 * -------------------------------------------------------------------- */

@Composable
private fun ContactSection() {
    val context = LocalContext.current
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(stringResource(R.string.about_contact_me), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        contacts.forEachIndexed { index, item ->
            FadeSlideIn(delayMs = 800 + index * 120) {
                ContactRow(item) { context.openUri(item.uri) }
            }
        }
    }
}

@Composable
private fun ContactRow(item: ContactInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    item.icon,
                    contentDescription = stringResource(item.labelRes),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Spacer(Modifier.size(14.dp))
            Column {
                Text(stringResource(item.labelRes), fontWeight = FontWeight.Medium)
                Text(
                    item.value,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Staggered entrance animation: delays appearance and slides content in vertically
@Composable
private fun FadeSlideIn(delayMs: Int, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(delayMs.toLong().milliseconds)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 4 },
    ) {
        content()
    }
}

// Safely launches an external browser/email app for a given URI with a fallback error toast
private fun Context.openUri(uri: String) {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                uri.toUri()
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    } catch (_: Exception) {
        Toast.makeText(this, getString(R.string.no_app_to_open_link), Toast.LENGTH_SHORT).show()
    }
}