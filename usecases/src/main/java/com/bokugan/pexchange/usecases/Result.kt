package com.bokugan.pexchange.usecases

sealed class Result<out T>
class Success<T>(val data: T) : Result<T>()
object Empty : Result<Nothing>()