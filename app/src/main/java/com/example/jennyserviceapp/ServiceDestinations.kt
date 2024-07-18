package com.example.jennyserviceapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Approval
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.LibraryAddCheck
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector


interface ServiceDestinations {
    val icon: ImageVector
    val route: String
}


object HomeUpload : ServiceDestinations {
    override val icon: ImageVector = Icons.Default.CloudUpload
    override val route: String = "upload products"
}

object UploadFeed : ServiceDestinations {
    override val icon: ImageVector = Icons.Default.VideoLibrary
    override val route: String = "upload videos"
}

object Orders : ServiceDestinations {
    override val icon: ImageVector = Icons.Default.LibraryAddCheck
    override val route: String = "customer orders"
}

object Message : ServiceDestinations {
    override val icon: ImageVector = Icons.AutoMirrored.Filled.Message
    override val route: String = "messages"
}

object SendNotification : ServiceDestinations {
    override val icon: ImageVector = Icons.Default.Notifications
    override val route: String = "send notification"
}


object Promotions : ServiceDestinations {
    override val icon: ImageVector = Icons.Default.Approval
    override val route: String = "promotions"
}

object Accounts : ServiceDestinations {
    override val icon: ImageVector = Icons.Default.AccountCircle
    override val route: String = "Account"
}

object Login : ServiceDestinations {
    override val icon: ImageVector = Icons.Default.AccountCircle
    override val route: String = "login"
}

object Splash : ServiceDestinations {
    override val icon: ImageVector = Icons.Default.Downloading
    override val route: String = "splash"
}

val serviceTabColumnBar = listOf(Orders, Message, HomeUpload, UploadFeed, SendNotification, Promotions, Accounts)