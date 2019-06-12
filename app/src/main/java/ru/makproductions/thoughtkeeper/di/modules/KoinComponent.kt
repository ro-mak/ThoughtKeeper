package ru.makproductions.thoughtkeeper.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.provider.FirestoreProvider
import ru.makproductions.thoughtkeeper.model.provider.RemoteDataProvider
import ru.makproductions.thoughtkeeper.viewmodel.main.MainViewModel
import ru.makproductions.thoughtkeeper.viewmodel.note.NoteViewModel
import ru.makproductions.thoughtkeeper.viewmodel.splash.SplashViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirestoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { NoteRepo(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}