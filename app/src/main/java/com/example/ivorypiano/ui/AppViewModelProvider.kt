package com.example.ivorypiano.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ivorypiano.IvoryPianoApplication
import com.example.ivorypiano.ui.session.HomeViewModel
import com.example.ivorypiano.ui.session.SessionDetailsViewModel
import com.example.ivorypiano.ui.session.SessionEditViewModel
import com.example.ivorypiano.ui.session.SessionEntryViewModel
import com.example.ivorypiano.ui.user.LoginViewModel
import com.example.ivorypiano.ui.user.ProfileViewModel
import com.example.ivorypiano.ui.user.UserEntryViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire IvoryPiano app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                ivoryPianoApplication().container.sessionsRepository,
                ivoryPianoApplication().container.userSessionRepository
            )
        }
        // Initializer for SessionEntryViewModel
        initializer {
            SessionEntryViewModel(
                ivoryPianoApplication().container.sessionsRepository,
                ivoryPianoApplication().container.userSessionRepository
            )
        }
        // Initializer for SessionDetailsViewModel
        initializer {
            SessionDetailsViewModel(
                this.createSavedStateHandle(),
                ivoryPianoApplication().container.sessionsRepository,
                ivoryPianoApplication().container.userSessionRepository
            )
        }
        // Initializer for SessionEditViewModel
        initializer {
            SessionEditViewModel(
                this.createSavedStateHandle(),
                ivoryPianoApplication().container.sessionsRepository,
                ivoryPianoApplication().container.userSessionRepository
            )
        }
        // Initializer for UserEntryViewModel
        initializer {
            UserEntryViewModel(
                ivoryPianoApplication().container.usersRepository,
                ivoryPianoApplication().container.userSessionRepository,
                ivoryPianoApplication().container.securityManager
            )
        }
        // Initializer for LoginViewModel
        initializer {
            LoginViewModel(
                ivoryPianoApplication().container.usersRepository,
                ivoryPianoApplication().container.userSessionRepository,
                ivoryPianoApplication().container.securityManager
            )
        }
        // Initializer for ProfileViewModel
        initializer {
            ProfileViewModel(
                ivoryPianoApplication().container.usersRepository,
                ivoryPianoApplication().container.sessionsRepository,
                ivoryPianoApplication().container.userSessionRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [IvoryPianoApplication].
 */
fun CreationExtras.ivoryPianoApplication(): IvoryPianoApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as IvoryPianoApplication)
