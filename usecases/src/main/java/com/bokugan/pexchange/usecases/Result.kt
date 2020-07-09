package com.bokugan.pexchange.usecases

sealed class Result<out T>
class Success<out T>(val data: T) : Result<T>()
object Empty : Result<Nothing>()