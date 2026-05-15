package com.nammapride.channapatna

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

import coil.compose.AsyncImage
import com.nammapride.channapatna.data.NammaPrideDatabase
import com.nammapride.channapatna.data.ToyEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

private val LocalKannadaEnabled = staticCompositionLocalOf { false }

@Composable
private fun uiText(english: String, kannada: String): String {
    return if (LocalKannadaEnabled.current) kannada else english
}

@Composable
private fun categoryText(category: String): String {
    return when (category) {
        "All" -> uiText("All", "ಎಲ್ಲಾ")
        "Animals" -> uiText("Animals", "ಪ್ರಾಣಿಗಳು")
        "Baby Toys" -> uiText("Baby Toys", "ಮಕ್ಕಳ ಆಟಿಕೆಗಳು")
        "Dolls" -> uiText("Dolls", "ಗೊಂಬೆಗಳು")
        "Musical Toys" -> uiText("Musical Toys", "ಸಂಗೀತ ಆಟಿಕೆಗಳು")
        "Puzzles" -> uiText("Puzzles", "ಪಜಲ್‌ಗಳು")
        "Rocking Horse" -> uiText("Rocking Horse", "ರಾಕಿಂಗ್ ಕುದುರೆ")
        "Spinning Tops" -> uiText("Spinning Tops", "ಬುಗುರಿಗಳು")
        "Vehicles" -> uiText("Vehicles", "ವಾಹನಗಳು")
        else -> category
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NammaPrideApp() }
    }
}

private enum class Screen {
    Splash, Role, Bazaar, VerifyToy, HowMade, Product, MakerMap, Saved, Account, ArtisanProfile, ArtisanDashboard, AddToy
}

private data class Toy(
    val name: String,
    val category: String,
    val price: String,
    val rating: String,
    val artisan: String,
    val image: String
)

private data class ToyVerification(
    val toyId: String,
    val toyName: String,
    val artisanName: String,
    val workshop: String,
    val artisanImageUrl: String
)

private val toyVerifications = listOf(
    ToyVerification("284901", "Classic Rocking Horse", "Master Raghu V.", "Raghu V. Workshop", "artisan_raghu"),
    ToyVerification("184502", "Nesting Heritage Dolls", "Meera Bai", "Meera Bai Lacquer Studio", "artisan_meera"),
    ToyVerification("736214", "Majestic Spinning Tops", "Kumar S.", "Kumar Heritage Turning Unit", "artisan_kumar"),
    ToyVerification("519873", "Geometry Puzzle Set", "Lakshmi K.", "Lakshmi Learning Toys", "artisan_lakshmi"),
    ToyVerification("660125", "Wooden Elephant", "Your Workshop", "Student Artisan Studio", "artisan_raghu")
)

private fun commonsImage(fileName: String): String {
    return "https://commons.wikimedia.org/wiki/Special:Redirect/file/${fileName.replace(" ", "%20")}?width=900"
}

private val seedToys = listOf(
    Toy(
        "Classic Rocking Horse",
        "Rocking Horse",
        "Rs. 1,249",
        "4.8",
        "Master Raghu V.",
        "rocking_horse"
    ),
    Toy(
        "Nesting Heritage Dolls",
        "Dolls",
        "Rs. 899",
        "4.5",
        "Meera Bai",
        "nesting_dolls"
    ),
    Toy(
        "Majestic Spinning Tops",
        "Spinning Tops",
        "Rs. 350",
        "4.9",
        "Kumar S.",
        "spinning_tops"
    ),
    Toy(
        "Geometry Puzzle Set",
        "Puzzles",
        "Rs. 1,599",
        "4.7",
        "Lakshmi K.",
        "puzzle_set"
    ),
    Toy(
        "Wooden Elephant",
        "Animals",
        "Rs. 750",
        "4.6",
        "Your Workshop",
        "wooden_elephant"
    ),
    Toy(
        "Rainbow Rattle Set",
        "Baby Toys",
        "Rs. 420",
        "4.6",
        "Namma Gombe Collective",
        "rainbow_rattel_set"
    ),
    Toy(
        "Lacquer Pull Cart",
        "Vehicles",
        "Rs. 680",
        "4.5",
        "Raghu V. Workshop",
        "lacquer_pull_cart"
    ),
    Toy(
        "Festival Doll Pair",
        "Dolls",
        "Rs. 1,150",
        "4.7",
        "Meera Bai",
        "festival_doll_pair"
    ),
    Toy(
        "Learning Ring Tower",
        "Baby Toys",
        "Rs. 540",
        "4.4",
        "Lakshmi Learning Toys",
        "learning_ring_tower"
    ),
    Toy(
        "Mini Rocking Horse",
        "Rocking Horse",
        "Rs. 699",
        "4.6",
        "Master Raghu V.",
        "mini_rocking_horse"
    ),
    Toy(
        "Spinning Top Combo",
        "Spinning Tops",
        "Rs. 499",
        "4.8",
        "Kumar S.",
        "spinning_top_combo"
    ),
    Toy(
        "Shape Sorting Blocks",
        "Puzzles",
        "Rs. 1,299",
        "4.6",
        "Lakshmi K.",
        "shape_sorting_blocks"
    ),
    Toy(
        "Toy Flute",
        "Musical Toys",
        "Rs. 390",
        "4.3",
        "Namma Gombe Collective",
        "toy_flute"
    )
)

private fun ToyEntity.toToy(): Toy {
    return Toy(
        name = name,
        category = category,
        price = price,
        rating = rating,
        artisan = artisan,
        image = image
    )
}

private fun Toy.toEntity(): ToyEntity {
    return ToyEntity(
        name = name,
        category = category,
        price = price,
        rating = rating,
        artisan = artisan,
        image = image
    )
}

private object PrideColors {
    val Background = Color(0xFFFFF8F7)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceLow = Color(0xFFFFF0EE)
    val SurfaceHigh = Color(0xFFFFE2DE)
    val Primary = Color(0xFFB7131A)
    val PrimaryContainer = Color(0xFFDB322F)
    val Secondary = Color(0xFF835400)
    val Yellow = Color(0xFFFCAB28)
    val Text = Color(0xFF271716)
    val Muted = Color(0xFF5B403D)
    val Green = Color(0xFF388E3C)
    val Teal = Color(0xFF006578)
}

@Composable
private fun NammaPrideApp() {
    val context = LocalContext.current
    val database = remember { NammaPrideDatabase.get(context) }
    val toyEntities by database.toyDao().observeAll().collectAsState(initial = emptyList())
    val toys = toyEntities.map { it.toToy() }.ifEmpty { seedToys }
    val scope = rememberCoroutineScope()
    var screen by remember { mutableStateOf(Screen.Splash) }
    var selectedToy by remember { mutableStateOf(seedToys.first()) }
    var savedToyNames by remember { mutableStateOf(seedToys.map { it.name }.toSet()) }
    var kannadaEnabled by remember { mutableStateOf(false) }
    val savedToys = toys.filter { it.name in savedToyNames }


    LaunchedEffect(Unit) {
        val existingNames = database.toyDao().names().toSet()
        seedToys.forEach { toy ->
            if (toy.name in existingNames) {
                database.toyDao().updateByName(
                    name = toy.name,
                    category = toy.category,
                    price = toy.price,
                    rating = toy.rating,
                    artisan = toy.artisan,
                    image = toy.image
                )
            } else {
                database.toyDao().insert(toy.toEntity())
            }
        }
    }

    MaterialTheme {
        CompositionLocalProvider(LocalKannadaEnabled provides kannadaEnabled) {
        Surface(color = PrideColors.Background, modifier = Modifier.fillMaxSize()) {
            when (screen) {
                Screen.Splash -> SplashScreen(onContinue = { screen = Screen.Role })
                Screen.Role -> RoleScreen(
                    onCustomer = { screen = Screen.Bazaar },
                    onArtisan = { screen = Screen.ArtisanDashboard }
                )

                Screen.Bazaar -> CustomerScaffold(
                    current = Screen.Bazaar,
                    onNavigate = { screen = it },
                    onBasket = { screen = Screen.Saved }
                ) { padding ->
                    BazaarScreen(
                        padding = padding,
                        toys = toys,
                        savedToyNames = savedToyNames,
                        onToggleSaved = { toy ->
                            savedToyNames = if (toy.name in savedToyNames) {
                                savedToyNames - toy.name
                            } else {
                                savedToyNames + toy.name
                            }
                        },
                        onToyClick = {
                            selectedToy = it
                            screen = Screen.Product
                        },
                        onVerifyToy = { screen = Screen.VerifyToy },
                        onHowMade = { screen = Screen.HowMade },
                        onMeetMaker = { screen = Screen.MakerMap }
                    )
                }
                Screen.VerifyToy -> CustomerScaffold(Screen.VerifyToy, { screen = it }) {
                    VerifyToyScreen(
                        padding = it,
                        onBack = { screen = Screen.Bazaar }
                    )
                }
                Screen.HowMade -> CustomerScaffold(Screen.HowMade, { screen = it }) {
                    HowMadeScreen(
                        padding = it,
                        onBack = { screen = Screen.Bazaar }
                    )
                }
                Screen.MakerMap -> CustomerScaffold(Screen.MakerMap, { screen = it }) { MakerMapScreen(it) }
                Screen.Saved -> CustomerScaffold(Screen.Saved, { screen = it }) {
                    SavedScreen(
                        padding = it,
                        toys = savedToys,
                        onToyClick = { toy ->
                            selectedToy = toy
                            screen = Screen.Product
                        },
                        onToggleSaved = { toy -> savedToyNames = savedToyNames - toy.name }
                    )
                }
                Screen.Account -> CustomerScaffold(Screen.Account, { screen = it }) {
                    AccountScreen(
                        padding = it,
                        savedCount = savedToys.size,
                        kannadaEnabled = kannadaEnabled,
                        onKannadaChange = { kannadaEnabled = it },
                        onWishlist = { screen = Screen.Saved }
                    )
                }
                Screen.Product -> ProductScreen(
                    toy = selectedToy,
                    isSaved = selectedToy.name in savedToyNames,
                    onToggleSaved = {
                        savedToyNames = if (selectedToy.name in savedToyNames) {
                            savedToyNames - selectedToy.name
                        } else {
                            savedToyNames + selectedToy.name
                        }
                    },
                    onBack = { screen = Screen.Bazaar }
                )
                Screen.ArtisanProfile -> ArtisanProfileScreen(toys, onBack = { screen = Screen.Bazaar })
                Screen.ArtisanDashboard -> ArtisanDashboardScreen(
                    toys = toys,
                    onAddToy = { screen = Screen.AddToy },
                    onShop = { screen = Screen.Bazaar },
                    onProfile = { screen = Screen.Account }
                )
                Screen.AddToy -> AddToyScreen(
                    onBack = { screen = Screen.ArtisanDashboard },
                    onSave = { toy ->
                        scope.launch {
                            database.toyDao().insert(toy.toEntity())
                            screen = Screen.ArtisanDashboard
                        }
                    }
                )
            }
        }
        }
    }
}

@Composable
private fun SplashScreen(onContinue: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(PrideColors.PrimaryContainer, PrideColors.Primary, Color(0xFF7E090F))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                ToyImage(
                    toy = seedToys[2],
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )
            }
            Spacer(Modifier.height(24.dp))
            Text("Channapatna", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.ExtraBold)
            Text("NAMMA PRIDE", color = PrideColors.Yellow, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(44.dp))
            Button(
                onClick = onContinue,
                colors = ButtonDefaults.buttonColors(containerColor = PrideColors.Yellow, contentColor = PrideColors.Text),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Get Started", fontWeight = FontWeight.Bold)
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.padding(start = 8.dp))
            }
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = Color.White.copy(alpha = 0.75f))
                Text(" Authentic Heritage Craft", color = Color.White.copy(alpha = 0.75f), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun RoleScreen(onCustomer: () -> Unit, onArtisan: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.craft_banner),
            contentDescription = "Craft banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(24.dp))
        Text("Welcome to Namma Pride", color = PrideColors.Primary, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
        Text("Who are you today?", color = PrideColors.Muted, fontSize = 18.sp)
        Spacer(Modifier.height(24.dp))
        RoleCard("Artisan / Seller", "I make toys", Icons.Default.PrecisionManufacturing, true, onArtisan)
        Spacer(Modifier.height(12.dp))
        RoleCard("Toy Lover / Customer", "I want to buy toys", Icons.Default.ShoppingBasket, false, onCustomer)
        Spacer(Modifier.height(28.dp))
        VerifiedChip("Authentic Channapatna Heritage")
        Spacer(Modifier.height(28.dp))
        Button(
            onClick = onCustomer,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrideColors.Primary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(" Continue ", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
private fun RoleCard(title: String, subtitle: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, if (selected) PrideColors.Primary else Color.Transparent),
        colors = CardDefaults.cardColors(containerColor = PrideColors.Surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(if (selected) PrideColors.SurfaceHigh else PrideColors.SurfaceLow),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = if (selected) PrideColors.Primary else PrideColors.Secondary)
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrideColors.Text)
                Text(subtitle, color = PrideColors.Muted)
            }
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .border(2.dp, if (selected) PrideColors.Primary else PrideColors.Muted, CircleShape)
                    .padding(5.dp)
                    .background(if (selected) PrideColors.Primary else Color.Transparent, CircleShape)
            )
        }
    }
}

@Composable
private fun OtpScreen(mobileNumber: String, onVerified: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onBack, modifier = Modifier.align(Alignment.Start)) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Spacer(Modifier.height(40.dp))
        Box(Modifier.size(88.dp).clip(CircleShape).background(PrideColors.SurfaceHigh), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Verified, contentDescription = null, tint = PrideColors.Primary, modifier = Modifier.size(44.dp))
        }
        Spacer(Modifier.height(24.dp))
        Text("Verify your mobile", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = PrideColors.Primary)
        Text(
            text = if (mobileNumber.isBlank()) {
                "Enter the 4 digit OTP sent to your number"
            } else {
                "Enter the 4 digit OTP sent to +91 $mobileNumber"
            },
            color = PrideColors.Muted
        )
        Spacer(Modifier.height(28.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(4) { index ->
                Box(
                    Modifier
                        .size(58.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(PrideColors.Surface)
                        .border(1.dp, PrideColors.SurfaceHigh, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (index < 3) "${index + 2}" else "", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(Modifier.height(36.dp))
        Button(
            onClick = onVerified,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrideColors.Primary),
            shape = RoundedCornerShape(16.dp)
        ) { Text("Verify and Open Bazaar", fontWeight = FontWeight.Bold) }
        TextButton(onClick = {}) { Text("Resend OTP", color = PrideColors.Primary) }
    }
}

@Composable
private fun CustomerScaffold(
    current: Screen,
    onNavigate: (Screen) -> Unit,
    onBasket: () -> Unit = { onNavigate(Screen.Saved) },
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = PrideColors.Background,
        bottomBar = {
            NavigationBar(containerColor = PrideColors.SurfaceLow, modifier = Modifier.navigationBarsPadding()) {
                NavItem(current, Screen.Bazaar, uiText("Home", "ಮುಖಪುಟ"), Icons.Default.Home, onNavigate)
                NavItem(current, Screen.MakerMap, uiText("Makers", "ಕಾರಿಗರು"), Icons.Default.Storefront, onNavigate)
                NavItem(current, Screen.Saved, uiText("Saved", "ಉಳಿಸಿದವು"), Icons.Default.Favorite, onNavigate)
                NavItem(current, Screen.Account, uiText("Account", "ಖಾತೆ"), Icons.Default.Person, onNavigate)
            }
        },
        floatingActionButton = {
            if (current == Screen.Bazaar) {
                FloatingActionButton(onClick = onBasket, containerColor = PrideColors.Primary, contentColor = Color.White) {
                    Icon(Icons.Default.ShoppingBasket, contentDescription = "Basket")
                }
            }
        },
        content = content
    )
}

@Composable
private fun RowScope.NavItem(current: Screen, target: Screen, label: String, icon: ImageVector, onNavigate: (Screen) -> Unit) {
    NavigationBarItem(
        selected = current == target,
        onClick = { onNavigate(target) },
        icon = { Icon(icon, contentDescription = label) },
        label = { Text(label) }
    )
}

@Composable
private fun BazaarScreen(
    padding: PaddingValues,
    toys: List<Toy>,
    savedToyNames: Set<String>,
    onToggleSaved: (Toy) -> Unit,
    onToyClick: (Toy) -> Unit,
    onVerifyToy: () -> Unit,
    onHowMade: () -> Unit,
    onMeetMaker: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = remember(toys) {
        listOf("All") + toys.map { it.category }.distinct().sorted()
    }
    val focusManager = LocalFocusManager.current
    val normalizedSearch = searchText.trim()
    val filteredToys = toys.filter {
        val matchesSearch = normalizedSearch.isBlank() ||
            it.name.contains(normalizedSearch, ignoreCase = true) ||
            it.category.contains(normalizedSearch, ignoreCase = true) ||
            it.artisan.contains(normalizedSearch, ignoreCase = true)
        val matchesCategory = selectedCategory == "All" ||
            it.category.equals(selectedCategory, ignoreCase = true)
        matchesSearch && matchesCategory
    }
    val toyRows = filteredToys.chunked(2)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .statusBarsPadding(),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 100.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(uiText("Channapatna Bazaar", "ಚನ್ನಪಟ್ಟಣ ಬಜಾರ್"), fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = PrideColors.Primary)
                    Text(uiText("Authentic GI-tagged toys", "ನೈಜ ಜಿಐ ಟ್ಯಾಗ್ ಆಟಿಕೆಗಳು"), color = PrideColors.Muted, fontWeight = FontWeight.SemiBold)
                }
                IconButton(onClick = { focusManager.clearFocus() }) { Icon(Icons.Default.Search, contentDescription = uiText("Search", "ಹುಡುಕಿ"), tint = PrideColors.Primary) }
            }
        }
        item {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text(uiText("Search handcrafted toys...", "ಕೈಮಗ್ಗದ ಆಟಿಕೆಗಳನ್ನು ಹುಡುಕಿ...")) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                shape = RoundedCornerShape(16.dp)
            )
        }
        item {
            UserFlowActions(
                onVerifyToy = onVerifyToy,
                onHowMade = onHowMade,
                onMeetMaker = onMeetMaker
            )
        }
        item {
            CategoryChips(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )
        }
        items(toyRows.size) { rowIndex ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                toyRows[rowIndex].forEach { toy ->
                    ToyCard(
                        toy = toy,
                        isSaved = toy.name in savedToyNames,
                        onToggleSaved = { onToggleSaved(toy) },
                        onClick = { onToyClick(toy) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (toyRows[rowIndex].size == 1) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
        if (filteredToys.isEmpty()) {
            item {
                Text(
                    text = uiText("No toys found for \"$normalizedSearch\"", "\"$normalizedSearch\" ಗೆ ಆಟಿಕೆಗಳು ಸಿಗಲಿಲ್ಲ"),
                    color = PrideColors.Muted,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoryChips(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        categories.forEach { label ->
            val selected = selectedCategory == label
            Surface(
                modifier = Modifier.clickable { onCategorySelected(label) },
                shape = CircleShape,
                color = if (selected) PrideColors.Primary else PrideColors.SurfaceHigh,
                border = if (selected) null else BorderStroke(1.dp, Color(0xFFE4BEB9))
            ) {
                Text(
                    text = categoryText(label),
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 9.dp),
                    color = if (selected) Color.White else PrideColors.Muted,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun UserFlowActions(onVerifyToy: () -> Unit, onHowMade: () -> Unit, onMeetMaker: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(uiText("App Usage", "ಆಪ್ ಬಳಕೆ"), color = PrideColors.Text, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FlowActionCard(
                title = uiText("Verify My Toy", "ನನ್ನ ಆಟಿಕೆ ಪರಿಶೀಲಿಸಿ"),
                subtitle = uiText("Check 6 digit ID", "6 ಅಂಕಿಯ ಐಡಿ ಪರಿಶೀಲಿಸಿ"),
                icon = Icons.Default.Verified,
                modifier = Modifier.weight(1f),
                onClick = onVerifyToy
            )
            FlowActionCard(
                title = uiText("How It's Made", "ಹೇಗೆ ತಯಾರಿಸುತ್ತಾರೆ"),
                subtitle = uiText("Hale wood and lac", "ಹಾಲೆ ಮರ ಮತ್ತು ಲಾಕ್"),
                icon = Icons.Default.MenuBook,
                modifier = Modifier.weight(1f),
                onClick = onHowMade
            )
        }
        FlowActionCard(
            title = uiText("Meet the Maker", "ಕಾರಿಗರನ್ನು ಭೇಟಿ ಮಾಡಿ"),
            subtitle = uiText("Visit Channapatna workshops near you", "ಚನ್ನಪಟ್ಟಣ ಕಾರ್ಯಾಗಾರಗಳನ್ನು ನೋಡಿ"),
            icon = Icons.Default.Storefront,
            modifier = Modifier.fillMaxWidth(),
            onClick = onMeetMaker
        )
    }
}

@Composable
private fun ImpactGoalsCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PrideColors.SurfaceHigh)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Impact Goals", color = PrideColors.Primary, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            ImpactGoal("Protecting GI Tags", "Safeguard authentic Geographical Indication products.")
            ImpactGoal("Artisan Recognition", "Give the maker a name, face and workshop identity.")
            ImpactGoal("Sustainable Play", "Promote natural, non-toxic toys over plastic ones.")
        }
    }
}

@Composable
private fun ImpactGoal(title: String, body: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(Icons.Default.Verified, contentDescription = null, tint = PrideColors.Green, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Column {
            Text(title, color = PrideColors.Text, fontWeight = FontWeight.Bold)
            Text(body, color = PrideColors.Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun FlowActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PrideColors.Surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(PrideColors.SurfaceHigh),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = PrideColors.Primary, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.ExtraBold, color = PrideColors.Text, maxLines = 1)
                Text(subtitle, color = PrideColors.Muted, fontSize = 12.sp, maxLines = 2)
            }
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = PrideColors.Primary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun ToyCard(
    toy: Toy,
    isSaved: Boolean = false,
    onToggleSaved: () -> Unit = {},
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PrideColors.Surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box {
            ToyImage(
                toy = toy,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            VerifiedChip("GI", modifier = Modifier.padding(8.dp))
        }
        Column(Modifier.padding(12.dp)) {
            Text(toy.name, fontWeight = FontWeight.Bold, color = PrideColors.Text, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = PrideColors.Yellow, modifier = Modifier.size(16.dp))
                Text(toy.rating, color = PrideColors.Muted, fontSize = 12.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(toy.price, color = PrideColors.Primary, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
                Box(
                    Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(if (isSaved) PrideColors.Primary else PrideColors.SurfaceHigh)
                        .clickable(onClick = onToggleSaved),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = if (isSaved) "Remove from saved" else "Save toy",
                        tint = if (isSaved) Color.White else PrideColors.Primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Text("${uiText("Artisan", "ಕಾರಿಗರ")}: ${toy.artisan}", color = PrideColors.Muted, fontSize = 11.sp, maxLines = 1)
        }
    }
}

@Composable
private fun ToyImage(toy: Toy, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageId = remember(toy.image) {
        context.resources.getIdentifier(
            toy.image,
            "drawable",
            context.packageName
        )
    }

    if (imageId != 0) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = toy.name,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else if (toy.image.startsWith("http")) {
        AsyncImage(
            model = toy.image,
            contentDescription = toy.name,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = modifier.background(
                Brush.linearGradient(
                    listOf(
                        PrideColors.Yellow,
                        PrideColors.PrimaryContainer,
                        PrideColors.Teal
                    )
                )
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Star,
                contentDescription = toy.name,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}



@Composable
private fun VerifiedChip(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(PrideColors.Green)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Verified, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text(text, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun FeatureBanner(onStoryClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = PrideColors.Yellow),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Featured Maker", color = PrideColors.Secondary, fontWeight = FontWeight.Bold)
                Text("Master Raghu V.", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = PrideColors.Text)
                Text("40 years of turning ivory wood into vibrant childhood memories.", color = PrideColors.Text)
                Spacer(Modifier.height(12.dp))
                Button(onClick = onStoryClick, colors = ButtonDefaults.buttonColors(containerColor = PrideColors.Text), shape = RoundedCornerShape(12.dp)) {
                    Text("Read Story", color = PrideColors.Yellow)
                }
            }
            Box(Modifier.size(80.dp).clip(RoundedCornerShape(16.dp)).background(Color.White.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.PrecisionManufacturing, contentDescription = null, tint = PrideColors.Secondary, modifier = Modifier.size(42.dp))
            }
        }
    }
}

@Composable
private fun ProductScreen(
    toy: Toy,
    isSaved: Boolean,
    onToggleSaved: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = PrideColors.Background,
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.95f))
                    .navigationBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f).height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrideColors.Primary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null)
                    Text(uiText(" Call", " ಕರೆ"))
                }
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1.5f).height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null)
                    Text(uiText(" Chat with Artisan", " ಕಾರಿಗರರ ಜೊತೆ ಚಾಟ್"))
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding()),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Box {
                    ToyImage(
                        toy = toy,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(420.dp)
                    )
                    Row(
                        Modifier
                            .statusBarsPadding()
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RoundIconButton(Icons.Default.ArrowBack, "Back", onBack)
                        RoundIconButton(
                            Icons.Default.Favorite,
                            if (isSaved) "Remove from saved" else "Save",
                            onToggleSaved,
                            if (isSaved) PrideColors.Primary else Color.Black.copy(alpha = 0.32f)
                        )
                    }
                    Row(
                        Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .background(PrideColors.Green)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = Color.White)
                        Text(uiText(" Verified GI Certified", " ಜಿಐ ಪ್ರಮಾಣಿತ"), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            item {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    ProductInfoCard(toy)
                    StoryCard()
                    ReviewCard("Ananya K.", "The finish is incredible. Smooth, safe and brighter than the photos.", 5)
                    ReviewCard("Vikram S.", "Authentic craftsmanship. You can tell this was made with care.", 4)
                }
            }
        }
    }
}

@Composable
private fun RoundIconButton(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit,
    backgroundColor: Color = Color.Black.copy(alpha = 0.32f)
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(backgroundColor)
    ) {
        Icon(icon, contentDescription = description, tint = Color.White)
    }
}

@Composable
private fun ProductInfoCard(toy: Toy) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.Surface), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row {
                Column(Modifier.weight(1f)) {
                    Text(toy.name, color = PrideColors.Primary, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    Text(categoryText(toy.category), color = PrideColors.Muted, fontWeight = FontWeight.SemiBold)
                }
                Text(toy.price, color = PrideColors.Secondary, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SpecItem(Icons.Default.Forest, uiText("Wood Type", "ಮರದ ಪ್ರಕಾರ"), uiText("Hale Wood", "ಹಾಲೆ ಮರ"), Modifier.weight(1f))
                SpecItem(Icons.Default.Person, uiText("Artisan", "ಕಾರಿಗರ"), toy.artisan, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SpecItem(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = PrideColors.Primary)
        Spacer(Modifier.width(8.dp))
        Column {
            Text(label.uppercase(), color = PrideColors.Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(value, color = PrideColors.Text, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StoryCard() {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFDDB5))) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MenuBook, contentDescription = null, tint = PrideColors.Secondary)
                Text(uiText(" Story of the Wood", " ಮರದ ಕಥೆ"), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                uiText(
                    "Each curve began as sustainably sourced Hale Wood. The artisan turns the block on a lathe and bonds organic lacquer from natural dyes into a mirror-like finish. This toy carries the 200-year-old Channapatna craft into a modern home.",
                    "ಪ್ರತಿ ಆಕಾರವೂ ಹಾಲೆ ಮರದಿಂದ ಆರಂಭವಾಗುತ್ತದೆ. ಕಾರಿಗರು ಮರವನ್ನು ಲೇತ್ ಮೇಲೆ ತಿರುಗಿಸಿ, ನೈಸರ್ಗಿಕ ಬಣ್ಣದ ಲಾಕ್ ಬಳಸಿ ಮಿನುಗುವ ಹೊಳಪು ಕೊಡುತ್ತಾರೆ. ಈ ಆಟಿಕೆ ಚನ್ನಪಟ್ಟಣದ 200 ವರ್ಷಗಳ ಕೈಗಾರಿಕೆಯನ್ನು ಮನೆಗೆ ತರುತ್ತದೆ."
                ),
                color = PrideColors.Text,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun ReviewCard(name: String, body: String, stars: Int) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.SurfaceLow)) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(name, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                repeat(5) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = if (it < stars) PrideColors.Yellow else Color.LightGray, modifier = Modifier.size(16.dp))
                }
            }
            Text(body, color = PrideColors.Muted)
        }
    }
}

@Composable
private fun VerifyToyScreen(padding: PaddingValues, onBack: () -> Unit) {
    var toyId by remember { mutableStateOf("") }
    var searchedId by remember { mutableStateOf("") }
    var firebaseResult by remember { mutableStateOf<ToyVerification?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val cleanId = toyId.filter { it.isDigit() }.take(6)
    val matchedToy = firebaseResult ?: toyVerifications.firstOrNull { it.toyId == searchedId }
    val canVerify = cleanId.length == 6

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .statusBarsPadding(),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrideColors.Primary)
            }
        }
        item {
            SectionTitle(uiText("Verify My Toy", "ನನ್ನ ಆಟಿಕೆ ಪರಿಶೀಲಿಸಿ"), uiText("Enter the 6 digit toy ID to confirm the artisan and origin", "ಕಾರಿಗರ ಮತ್ತು ಮೂಲವನ್ನು ತಿಳಿಯಲು 6 ಅಂಕಿಯ ಆಟಿಕೆ ಐಡಿ ನಮೂದಿಸಿ"))
        }
        item {
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.Surface)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = cleanId,
                        onValueChange = { toyId = it.filter { char -> char.isDigit() }.take(6) },
                        label = { Text(uiText("6 digit toy ID", "6 ಅಂಕಿಯ ಆಟಿಕೆ ಐಡಿ")) },
                        placeholder = { Text("284901") },
                        leadingIcon = { Icon(Icons.Default.Verified, contentDescription = null, tint = PrideColors.Primary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )
                    Button(
                        onClick = {
                            searchedId = cleanId
                            firebaseResult = null
                            errorMessage = ""
                            isLoading = true
                            firestore.collection("toy_verifications")
                                .document(cleanId)
                                .get()
                                .addOnSuccessListener { document ->
                                    isLoading = false
                                    if (document.exists()) {
                                        firebaseResult = ToyVerification(
                                            toyId = cleanId,
                                            toyName = document.getString("toyName").orEmpty(),
                                            artisanName = document.getString("artisanName").orEmpty(),
                                            workshop = document.getString("workshop").orEmpty(),
                                            artisanImageUrl = document.getString("artisanImageUrl").orEmpty()
                                        )
                                    } else {
                                        errorMessage = "No record found for #$cleanId"
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    isLoading = false
                                    errorMessage = exception.message ?: "Verification failed"
                                }
                        },
                        enabled = canVerify,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrideColors.Primary,
                            disabledContainerColor = PrideColors.Primary.copy(alpha = 0.35f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(if (isLoading) uiText("Checking...", "ಪರಿಶೀಲಿಸಲಾಗುತ್ತಿದೆ...") else uiText("Verify Toy", "ಆಟಿಕೆ ಪರಿಶೀಲಿಸಿ"), fontWeight = FontWeight.Bold)
                    }
                    Text(
                        uiText("Try sample IDs: 284901, 184502, 736214, 519873, 660125", "ಮಾದರಿ ಐಡಿಗಳು: 284901, 184502, 736214, 519873, 660125"),
                        color = PrideColors.Muted,
                        fontSize = 12.sp
                    )
                }
            }
        }
        if (matchedToy != null) {
            item {
                VerifiedToyResult(matchedToy)
            }
        } else if (searchedId.length == 6 && !isLoading) {
            item {
                InvalidToyResult(searchedId, errorMessage)
            }
        }
    }
}

@Composable
private fun VerifiedToyResult(result: ToyVerification) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.Surface), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(118.dp)
                    .clip(CircleShape)
                    .background(PrideColors.SurfaceHigh),
                contentAlignment = Alignment.Center
            ) {
                val context = LocalContext.current
                val artisanImageId = remember(result.artisanImageUrl) {
                    context.resources.getIdentifier(result.artisanImageUrl, "drawable", context.packageName)
                }
                if (result.artisanImageUrl.startsWith("http")) {
                    AsyncImage(
                        model = result.artisanImageUrl,
                        contentDescription = result.artisanName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (artisanImageId != 0) {
                    Image(
                        painter = painterResource(id = artisanImageId),
                        contentDescription = result.artisanName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.Person, contentDescription = null, tint = PrideColors.Primary, modifier = Modifier.size(58.dp))
                }
            }
            Spacer(Modifier.height(14.dp))
            VerifiedChip(uiText("Authentic Channapatna Toy", "ನೈಜ ಚನ್ನಪಟ್ಟಣ ಆಟಿಕೆ"))
            Spacer(Modifier.height(12.dp))
            Text("${uiText("Toy ID", "ಆಟಿಕೆ ಐಡಿ")} #${result.toyId}", color = PrideColors.Muted, fontWeight = FontWeight.SemiBold)
            Text(result.artisanName, color = PrideColors.Primary, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text("${result.toyName} - ${result.workshop}", color = PrideColors.Muted)
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SpecItem(Icons.Default.Forest, uiText("Wood", "ಮರ"), uiText("Hale Wood", "ಹಾಲೆ ಮರ"), Modifier.weight(1f))
                SpecItem(Icons.Default.Verified, uiText("Finish", "ಪೂರ್ಣತೆ"), uiText("Natural Lac", "ನೈಸರ್ಗಿಕ ಲಾಕ್"), Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun InvalidToyResult(toyId: String, message: String) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.SurfaceHigh)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, contentDescription = null, tint = PrideColors.Primary, modifier = Modifier.size(34.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(uiText("No match found for #$toyId", "#$toyId ಗೆ ಹೊಂದಾಣಿಕೆ ಸಿಗಲಿಲ್ಲ"), color = PrideColors.Primary, fontWeight = FontWeight.ExtraBold)
                Text(message.ifBlank { uiText("Check the 6 digit ID printed on the toy tag and try again.", "ಆಟಿಕೆ ಟ್ಯಾಗ್‌ನಲ್ಲಿರುವ 6 ಅಂಕಿಯ ಐಡಿ ಪರಿಶೀಲಿಸಿ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ.") }, color = PrideColors.Muted)
            }
        }
    }
}

@Composable
private fun HowMadeScreen(padding: PaddingValues, onBack: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .statusBarsPadding(),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrideColors.Primary)
            }
        }
        item { SectionTitle(uiText("How It's Made", "ಹೇಗೆ ತಯಾರಿಸುತ್ತಾರೆ"), uiText("The hale wood and lac process behind Channapatna toys", "ಚನ್ನಪಟ್ಟಣ ಆಟಿಕೆಗಳ ಹಾಲೆ ಮರ ಮತ್ತು ಲಾಕ್ ಪ್ರಕ್ರಿಯೆ")) }
        item {
            ProcessCard(
                step = "1",
                title = uiText("Hale Wood Selection", "ಹಾಲೆ ಮರದ ಆಯ್ಕೆ"),
                body = uiText("Artisans use soft, workable hale wood, traditionally called Aale Mara. The wood is cut, seasoned and prepared so it can be turned smoothly on the lathe.", "ಕಾರಿಗರು ಮೃದುವಾದ ಹಾಲೆ ಮರವನ್ನು ಆಯ್ಕೆ ಮಾಡುತ್ತಾರೆ. ಮರವನ್ನು ಕತ್ತರಿಸಿ ಒಣಗಿಸಿ ಲೇತ್ ಮೇಲೆ ಸುಲಭವಾಗಿ ತಿರುಗುವಂತೆ ಸಿದ್ಧಪಡಿಸುತ್ತಾರೆ.")
            )
        }
        item {
            ProcessCard(
                step = "2",
                title = uiText("Turning on the Lathe", "ಲೇತ್ ಮೇಲೆ ತಿರುಗಿಸುವುದು"),
                body = uiText("The wooden block is shaped by hand while it spins. Rounded toy forms, rings and curves are created using simple tools and steady pressure.", "ಮರದ ತುಂಡು ತಿರುಗುವಾಗ ಕೈಯಿಂದ ಆಕಾರ ಕೊಡುತ್ತಾರೆ. ಸರಳ ಸಾಧನಗಳಿಂದ ವೃತ್ತಾಕಾರ, ಉಂಗುರಗಳು ಮತ್ತು ವಕ್ರತೆಗಳನ್ನು ರಚಿಸುತ್ತಾರೆ.")
            )
        }
        item {
            ProcessCard(
                step = "3",
                title = uiText("Natural Lac Color", "ನೈಸರ್ಗಿಕ ಲಾಕ್ ಬಣ್ಣ"),
                body = uiText("Lac, a natural resin, is mixed with safe color pigments. Friction from the spinning wood warms the lac and bonds it to the toy surface.", "ನೈಸರ್ಗಿಕ ರೆಸಿನ್ ಆದ ಲಾಕ್ ಅನ್ನು ಸುರಕ್ಷಿತ ಬಣ್ಣಗಳೊಂದಿಗೆ ಮಿಶ್ರಣ ಮಾಡುತ್ತಾರೆ. ತಿರುಗುವ ಮರದ ಉಷ್ಣದಿಂದ ಬಣ್ಣ ಮೇಲ್ಮೈಗೆ ಅಂಟುತ್ತದೆ.")
            )
        }
        item {
            ProcessCard(
                step = "4",
                title = uiText("Polish and Safety Check", "ಪಾಲಿಷ್ ಮತ್ತು ಸುರಕ್ಷತಾ ಪರಿಶೀಲನೆ"),
                body = uiText("The finished toy is polished for a glossy surface, checked for smooth edges, and marked as authentic before it reaches the buyer.", "ಆಟಿಕೆಗೆ ಹೊಳಪು ಕೊಡಲಾಗುತ್ತದೆ, ಅಂಚುಗಳು ಮೃದುವಾಗಿದೆಯೇ ಎಂದು ಪರಿಶೀಲಿಸಿ ನಂತರ ನೈಜ ಉತ್ಪನ್ನವೆಂದು ಗುರುತಿಸಲಾಗುತ್ತದೆ.")
            )
        }
    }
}

@Composable
private fun ProcessCard(step: String, title: String, body: String) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.Surface)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(PrideColors.Primary),
                contentAlignment = Alignment.Center
            ) {
                Text(step, color = Color.White, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = PrideColors.Primary, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(4.dp))
                Text(body, color = PrideColors.Muted, lineHeight = 21.sp)
            }
        }
    }
}

@Composable
private fun MakerMapScreen(padding: PaddingValues) {
    val workshops = listOf(
        Workshop("Raghu V. Workshop", "2.4 km away", "Open today, 10 AM - 5 PM", "Rocking horses and spinning tops", 12.6510, 77.2058),
        Workshop("Meera Bai Lacquer Studio", "3.1 km away", "Visits by appointment", "Nesting dolls and festival sets", 12.6535, 77.2092),
        Workshop("Lakshmi Learning Toys", "4.2 km away", "Open weekends", "Puzzles and educational toys", 12.6473, 77.2124),
        Workshop("Kumar Heritage Turning Unit", "5.6 km away", "Open today, 11 AM - 4 PM", "Traditional lathe demos", 12.6458, 77.2017),
        Workshop("Namma Gombe Collective", "6.3 km away", "Open daily, 9 AM - 6 PM", "GI-tagged toy catalog and craft visits", 12.6504, 77.2158)
    )
    val uriHandler = LocalUriHandler.current

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).statusBarsPadding(),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { SectionTitle(uiText("Meet the Maker", "ಕಾರಿಗರನ್ನು ಭೇಟಿ ಮಾಡಿ"), uiText("A directory of Channapatna workshops you can visit", "ಭೇಟಿ ನೀಡಬಹುದಾದ ಚನ್ನಪಟ್ಟಣ ಕಾರ್ಯಾಗಾರಗಳ ಪಟ್ಟಿ")) }
        items(workshops.size) { index ->
            MakerRow(workshops[index], onClick = { uriHandler.openUri(workshops[index].mapsUrl()) })
        }
    }
}

private data class Workshop(
    val name: String,
    val distance: String,
    val visitInfo: String,
    val specialty: String,
    val latitude: Double,
    val longitude: Double
) {
    fun mapsUrl(): String {
        return "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
    }
}

@Composable
private fun MakerRow(workshop: Workshop, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PrideColors.Surface)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Box(Modifier.size(54.dp).clip(CircleShape).background(PrideColors.SurfaceHigh), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Storefront, contentDescription = null, tint = PrideColors.Primary)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(workshop.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(workshop.specialty, color = PrideColors.Text, fontSize = 13.sp)
                Text("${workshop.distance} - ${workshop.visitInfo}", color = PrideColors.Muted, fontSize = 13.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = PrideColors.Teal, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${workshop.latitude}, ${workshop.longitude}",
                        color = PrideColors.Teal,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = PrideColors.Primary)
        }
    }
}

@Composable
private fun SavedScreen(
    padding: PaddingValues,
    toys: List<Toy>,
    onToyClick: (Toy) -> Unit,
    onToggleSaved: (Toy) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).statusBarsPadding(),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { SectionTitle(uiText("Saved Toys", "ಉಳಿಸಿದ ಆಟಿಕೆಗಳು"), uiText("Your wishlist of handcrafted pieces", "ನಿಮ್ಮ ಕೈಮಗ್ಗದ ಆಟಿಕೆಗಳ ಆಸೆಪಟ್ಟಿ")) }
        if (toys.isEmpty()) {
            item {
                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.SurfaceLow)) {
                    Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(uiText("No saved toys yet", "ಇನ್ನೂ ಉಳಿಸಿದ ಆಟಿಕೆಗಳಿಲ್ಲ"), color = PrideColors.Text, fontWeight = FontWeight.ExtraBold)
                        Text(uiText("Tap the heart on any toy to add it back here.", "ಯಾವುದೇ ಆಟಿಕೆಯಲ್ಲಿ ಹೃದಯವನ್ನು ಒತ್ತಿ ಇಲ್ಲಿ ಸೇರಿಸಿ."), color = PrideColors.Muted)
                    }
                }
            }
        }
        items(toys.size) { index ->
            val toy = toys[index]
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onToyClick(toy) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrideColors.Surface)
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    ToyImage(toy, modifier = Modifier.size(82.dp).clip(RoundedCornerShape(14.dp)))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(toy.name, fontWeight = FontWeight.Bold)
                        Text(toy.artisan, color = PrideColors.Muted)
                        Text(toy.price, color = PrideColors.Primary, fontWeight = FontWeight.ExtraBold)
                    }
                    IconButton(onClick = { onToggleSaved(toy) }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Remove from saved", tint = PrideColors.Primary)
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountScreen(
    padding: PaddingValues,
    savedCount: Int,
    kannadaEnabled: Boolean,
    onKannadaChange: (Boolean) -> Unit,
    onWishlist: () -> Unit
) {
    var displayName by remember { mutableStateOf("Koyal M") }
    var isEditingProfile by remember { mutableStateOf(false) }
    var showOrders by remember { mutableStateOf(false) }
    var showPreferences by remember { mutableStateOf(false) }
    var alertsEnabled by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).statusBarsPadding(),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { SectionTitle(uiText("Profile Settings", "ಪ್ರೊಫೈಲ್ ಸೆಟ್ಟಿಂಗ್‌ಗಳು"), uiText("Manage your Namma Pride account", "ನಿಮ್ಮ ನಮ್ಮ ಪ್ರೈಡ್ ಖಾತೆಯನ್ನು ನಿರ್ವಹಿಸಿ")) }
        item {
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.Surface)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(68.dp).clip(CircleShape).background(PrideColors.SurfaceHigh), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = PrideColors.Primary, modifier = Modifier.size(34.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(displayName, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                        Text(uiText("Channapatna craft supporter", "ಚನ್ನಪಟ್ಟಣ ಕೈಗಾರಿಕೆ ಬೆಂಬಲಿಗ"), color = PrideColors.Muted)
                    }
                    IconButton(onClick = { isEditingProfile = !isEditingProfile }) {
                        Icon(Icons.Default.Edit, contentDescription = uiText("Edit profile", "ಪ್ರೊಫೈಲ್ ತಿದ್ದು"), tint = PrideColors.Primary)
                    }
                }
            }
        }
        if (isEditingProfile) {
            item {
                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.Surface)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = displayName,
                            onValueChange = { displayName = it },
                            label = { Text(uiText("Name", "ಹೆಸರು")) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                        Button(
                            onClick = { isEditingProfile = false },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrideColors.Primary),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(uiText("Save Profile", "ಪ್ರೊಫೈಲ್ ಉಳಿಸಿ"))
                        }
                    }
                }
            }
        }
        item { SettingRow(Icons.Default.Inventory, uiText("Orders", "ಆರ್ಡರ್‌ಗಳು"), uiText("Track recent purchases", "ಇತ್ತೀಚಿನ ಖರೀದಿಗಳನ್ನು ನೋಡಿ"), onClick = { showOrders = !showOrders }) }
        if (showOrders) {
            item {
                InfoPanel(
                    title = uiText("Recent Orders", "ಇತ್ತೀಚಿನ ಆರ್ಡರ್‌ಗಳು"),
                    body = uiText("Classic Rocking Horse - Delivered\nNesting Heritage Dolls - Processing", "ಕ್ಲಾಸಿಕ್ ರಾಕಿಂಗ್ ಕುದುರೆ - ತಲುಪಿಸಲಾಗಿದೆ\nನೆಸ್ಟಿಂಗ್ ಹೆರಿಟೇಜ್ ಗೊಂಬೆಗಳು - ಪ್ರಕ್ರಿಯೆಯಲ್ಲಿದೆ")
                )
            }
        }
        item { SettingRow(Icons.Default.Favorite, uiText("Wishlist", "ಆಸೆಪಟ್ಟಿ"), uiText("$savedCount saved toys and makers", "$savedCount ಉಳಿಸಿದ ಆಟಿಕೆಗಳು ಮತ್ತು ಕಾರಿಗರು"), onClick = onWishlist) }
        item { SettingRow(Icons.Default.Settings, uiText("Preferences", "ಆಯ್ಕೆಗಳು"), uiText("Language, alerts and privacy", "ಭಾಷೆ, ಎಚ್ಚರಿಕೆಗಳು ಮತ್ತು ಗೌಪ್ಯತೆ"), onClick = { showPreferences = !showPreferences }) }
        if (showPreferences) {
            item {
                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.Surface)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        PreferenceSwitch(uiText("Kannada language", "ಕನ್ನಡ ಭಾಷೆ"), kannadaEnabled, onKannadaChange)
                        PreferenceSwitch(uiText("Order and artisan alerts", "ಆರ್ಡರ್ ಮತ್ತು ಕಾರಿಗರ ಎಚ್ಚರಿಕೆಗಳು"), alertsEnabled, { alertsEnabled = it })
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingRow(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PrideColors.SurfaceLow)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = PrideColors.Primary)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(subtitle, color = PrideColors.Muted)
            }
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = PrideColors.Muted)
        }
    }
}

@Composable
private fun InfoPanel(title: String, body: String) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.Surface)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, color = PrideColors.Primary, fontWeight = FontWeight.ExtraBold)
            Text(body, color = PrideColors.Muted, lineHeight = 22.sp)
        }
    }
}

@Composable
private fun PreferenceSwitch(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = PrideColors.Text)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArtisanProfileScreen(toys: List<Toy>, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artisan Heritage") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrideColors.Background)
            )
        },
        containerColor = PrideColors.Background
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.Surface)) {
                    Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.size(110.dp).clip(CircleShape).background(PrideColors.SurfaceHigh), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.PrecisionManufacturing, contentDescription = null, tint = PrideColors.Primary, modifier = Modifier.size(56.dp))
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("Master Raghu V.", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = PrideColors.Primary)
                        Text("Fourth-generation Channapatna artisan", color = PrideColors.Muted)
                        Spacer(Modifier.height(12.dp))
                        VerifiedChip("GI Certified Maker")
                    }
                }
            }
            item { StoryCard() }
            item { SectionTitle("Workshop Highlights", "Hand-turned wood, organic lacquer and child-safe finishing") }
            items(toys.take(3).size) { index -> ToyCard(toys[index], onClick = {}) }
        }
    }
}

@Composable
private fun ArtisanDashboardScreen(toys: List<Toy>, onAddToy: () -> Unit, onShop: () -> Unit, onProfile: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Artisan Dashboard", color = PrideColors.Primary, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Manage your toys, orders and profile", color = PrideColors.Muted)
                }
                IconButton(onClick = onProfile) { Icon(Icons.Default.Person, contentDescription = "Profile") }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("12", "Toys", Icons.Default.Inventory, Modifier.weight(1f))
                StatCard("38", "Orders", Icons.Default.ShoppingBasket, Modifier.weight(1f))
            }
        }
        item {
            Button(
                onClick = onAddToy,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrideColors.Primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text(" Add New Toy", fontWeight = FontWeight.Bold)
            }
        }
        item { SectionTitle("Live Listings", "Products visible to customers") }
        items(toys.size) { index -> DashboardToyRow(toys[index]) }
        item {
            OutlinedButton(onClick = onShop, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Default.Storefront, contentDescription = null)
                Text(" Preview Customer Bazaar")
            }
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, icon: ImageVector, modifier: Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.Surface)) {
        Column(Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = PrideColors.Primary)
            Text(value, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Text(label, color = PrideColors.Muted)
        }
    }
}

@Composable
private fun DashboardToyRow(toy: Toy) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = PrideColors.Surface)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            ToyImage(toy, modifier = Modifier.size(70.dp).clip(RoundedCornerShape(12.dp)))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(toy.name, fontWeight = FontWeight.Bold)
                Text("${toy.price} - Rating ${toy.rating}", color = PrideColors.Muted)
            }
            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = PrideColors.Primary)
            Spacer(Modifier.width(10.dp))
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = PrideColors.Muted)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddToyScreen(onBack: () -> Unit, onSave: (Toy) -> Unit) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var woodType by remember { mutableStateOf("") }
    var story by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Toy") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrideColors.Background)
            )
        },
        containerColor = PrideColors.Background
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(Modifier.fillMaxWidth().height(170.dp).clip(RoundedCornerShape(18.dp)).background(PrideColors.SurfaceHigh), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = PrideColors.Primary, modifier = Modifier.size(42.dp))
                    Text("Upload toy photos", color = PrideColors.Primary, fontWeight = FontWeight.Bold)
                }
            }
            FormField("Toy name", name, { name = it }, "Classic Rocking Horse")
            FormField("Category", category, { category = it }, "Traditional Toys & Games")
            FormField("Price", price, { price = it }, "1249")
            FormField("Wood type", woodType, { woodType = it }, "Hale Wood")
            FormField("Story", story, { story = it }, "Describe the wood, dye, process and heritage", minLines = 4)
            Button(
                onClick = {
                    val cleanName = name.ifBlank { "New Channapatna Toy" }
                    val cleanCategory = category.ifBlank { "Traditional Toys" }
                    val cleanPrice = price.ifBlank { "0" }
                    onSave(
                        Toy(
                            name = cleanName,
                            category = cleanCategory,
                            price = "Rs. $cleanPrice",
                            rating = "New",
                            artisan = "Your Workshop",
                            image = ""
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrideColors.Primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Dashboard, contentDescription = null)
                Text(" Save Toy")
            }
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        minLines = minLines,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp)
    )
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Column {
        Text(title, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = PrideColors.Primary)
        Text(subtitle, color = PrideColors.Muted)
    }
}
