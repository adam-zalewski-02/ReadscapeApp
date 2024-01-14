package com.example.model

import com.example.model.book.Volume

data class FollowableVolume (
    val volume: Volume,
    val isFollowed: Boolean,
)