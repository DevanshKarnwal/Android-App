package com.example.musicappui

import androidx.annotation.DrawableRes

data class Lib(@DrawableRes val icon : Int , val name : String)
val libraries = listOf<Lib>(
    Lib(R.drawable.ic_subscribe,"Playlist"),
    Lib(R.drawable.baseline_mic_none_24,"Artists"),
    Lib(R.drawable.baseline_album_24,"Album"),
    Lib(R.drawable.ic_music_player_green,"Songs"),
    Lib(R.drawable.baseline_playlist_play_24,"Genre")
)
