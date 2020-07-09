package com.bokugan.pexchange.usecases

sealed class Result<out T>
class Success<T>(val data: T) : Result<T>()
class Failure(val code: Int = -1): Result<Nothing>()
object Empty : Result<Nothing>()