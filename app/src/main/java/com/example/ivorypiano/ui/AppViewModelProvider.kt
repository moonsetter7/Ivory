package com.example.ivorypiano.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ivorypiano.IvoryPianoApplication
import com.example.ivorypiano.ui.session.HomeViewModel
import com.example.ivorypiano.ui.session.SessionEntryViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire IvoryPiano app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(ivoryPianoApplication().container.sessionsRepository)
        }
        // Initializer for SessionEntryViewModel
        initializer {
            SessionEntryViewModel(ivoryPianoApplication().container.sessionsRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [IvoryPianoApplication].
 */
fun CreationExtras.ivoryPianoApplication(): IvoryPianoApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as IvoryPianoApplication)
