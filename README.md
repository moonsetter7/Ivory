<div align="center">

<img src="https://files.catbox.moe/74wxy5.png" width="100px"/>

# **Ivory**
Ivory is a free piano practice session tracker app designed to help users record their musical journey. Through a simple, and elegant interface and features like a session stopwatch, users can easily log their daily piano progress.

<img src="https://raw.githubusercontent.com/moonsetter7/Ivory/refs/heads/master/notes_bg.png" width = "200px"/>

</div>

<div align="left">
  
## Features
- Practice stopwatch for accurate session duration tracking
- Detailed session logging including piece name, composer, tempo, and measures practiced
- Retrospective session editing
- Organization of practice history by piece, composer, and month, simplifying log navigation
- Search interface filtering session logs real-time by piece name and composer
- Multi-user profile management supporting multiple user profiles with individualized practice histories and session persistence
- Secure Login/Registration system implementing Jetpack Security ensuring user credentials are never stored in plain text
- Dark mode support
- Offline database integration using ROOM SQLite for CRUD operations on user profiles and practice sessions
- Real-time UI synchronization through lifecycle-aware state collection 



## Architecture
Ivory uses an MVVM Architecture heavily based on [Android Basics with Compose's Inventory App](https://github.com/google-developer-training/basic-android-kotlin-compose-training-inventory-app/tree/main). The UI and Data Layers follow the pattern of [Unidirectional Data Flow (UDF)](https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state?continue=https://developer.android.com/courses/pathways/android-basics-compose-unit-4-pathway-1#codelab-https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state#5) to decouple Composables that display state from code that stores/changes state.

### UI Layer (Jetpack Compose)

- Screens like `SessionEditScreen.kt` hoist state to ViewModels. ViewModels in turn manage the UiState and UI logic, while Composable functions receive the state as arguments and communicate user events back through callbacks. This separates the visual representation from the business logic and state management. 
- `IvoryPianoNavHost` centralizes navigation, managing screen transitions.

### Data Layer (Room + Repository)
- `PianoSession` and `User` entities define the database tables.
- The various repositories (`SessionsRepository.kt`, `UserSessionRepository.kt`, `UsersRepository.kt`) abstract the sources of data from the rest of the app, and decouple the UI logic from the data sources.
- The offline repositories use Room's DAOs to ensure all practice data persists locally.
- The data is exposed to the rest of the app as `Flows`, linking the database to the UI, where any change like adding a new practice session is automatically pushed through the repository and displayed by the UI without needing a refresh.
- `AppContainer.kt` manages instantiating the repositories and database, providing ViewModels with the repositories they need through `AppViewModelProvider.kt`.

### Security
- In addition to the UI and Data Layers, Ivory also implements the Jetpack Security library to provide encryption of sensitive session information.
- `SecurityManager.kt` stores active session IDs and user data in `EncryptedSharedPreferences`.
- `SecurityManager.kt` also ensures passwords are never stored in plain text, as seen in this code snippet using a one-way hash via the SHA-256 algorithm:
```
fun hashPassword(password: String): String {  
    val bytes = password.toByteArray()  
    val md = MessageDigest.getInstance("SHA-256")  
    val digest = md.digest(bytes)  
    return digest.fold("") { str, it -> str + "%02x".format(it) }  
}
```

## Demo

<div align="center">
  <a href="https://www.youtube.com/watch?v=ntvOG9OxXiU" target="_blank">
    <img src="https://files.catbox.moe/49u013.png" alt="Demonstration Video" width="600" />
  </a>
  <p><b>Note:</b> Click the thumbnail above to view the demonstration video in a new tab.</p>
</div>



<details>

<summary><b>Screenshots</b></summary>

<br>

<table>
  <tr>
    <td align="center"><img src="https://files.catbox.moe/ozuehy.png" width="200"/><br><sub>Register</sub></td>
    <td align="center"><img src="https://files.catbox.moe/dlyl1w.png" width="200"/><br><sub>Login</sub></td>
    <td align="center"><img src="https://files.catbox.moe/gjb9ao.png" width="200"/><br><sub>Home</sub></td>
  </tr>
  <tr>
    <td align="center"><img src="https://files.catbox.moe/6u3r5w.png" width="200"/><br><sub>Aggregation by Composer</sub></td>
    <td align="center"><img src="https://files.catbox.moe/5pogpb.png" width="200"/><br><sub>Search</sub></td>
    <td align="center"><img src="https://files.catbox.moe/mnhzfj.png" width="200"/><br><sub>New Entry</sub></td>
  </tr>
  <tr>
    <td align="center"><img src="https://files.catbox.moe/8jx9vu.png" width="200"/><br><sub>Entry Details</sub></td>
    <td align="center"><img src="https://files.catbox.moe/fc8gxo.png" width="200"/><br><sub>Edit Entry</sub></td>
    <td align="center"><img src="https://files.catbox.moe/7sw2bl.png" width="200"/><br><sub>Profile</sub></td>
  </tr>
  <tr>
    <td align="center"><img src="https://files.catbox.moe/2rzk5h.png" width="200"/><br><sub>Dark Mode</sub></td>
  </tr>
</table>

</details>


## Implementation Reference Table
### UI Layer 

| Component | File Path | Function |
| :--- | :--- | :--- |
| Navigation Graph | `ui/navigation/IvoryPianoNavGraph.kt` | Provides the navigation graph, navHost, and all destinations + their handling |
| Home Screen | `ui/session/HomeViewModel.kt` | Aggregation and search logic |
| Practice Timer | `ui/session/SessionEntryViewModel.kt` | Timer logic, handles millisecond state |
| User Entry | `ui/user/UserEntryViewModel.kt` | registration + input validation (usernames, emails etc.) |
| Login Logic | `ui/user/LoginViewModel.kt` | authentication, works with SecurityManager to verify credentials |

### Data Layer 

| Component | File Path| Function|
| :--- | :--- | :--- |
| Session Entity | `data/PianoSession.kt` | Room DB table for practice logs (BPM, piece name, measures etc) |
| User Entity | `data/User.kt` | user profile tabe, stores hashed passwords |
| Practice Repository | `data/OfflineSessionsRepository.kt` | Locally persisting single-source-of-truth data for all practice sessions |
| User Repository | `data/OfflineUsersRepository.kt` | Locally persisting single-source-of-truth data for all users |
| Database| `data/SessionDatabase.kt` | main room database class, DAOs and singleton pattern |

### Security & Infrastructure

| Component | File Path | Function|
| :--- | :--- | :--- |
| Security Manager | `data/SecurityManager.kt` | SHA-256 password hashing + AES-256 encryption of `EncryptedSharedPreferences` (`secure_user_prefs`)|
| Dependency Injection | `data/AppContainer.kt` | instantiates repositories and singletons |
| ViewModel Factory | `ui/AppViewModelProvider.kt` | provides initializers for all ViewModels, and its required repositories + security manager |
| Application Class | `IvoryPianoApplication.kt` | initializes the AppContainer |

</div>
