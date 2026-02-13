package kz.aitu.booksapp.domain

/** Thrown when an action requires an authenticated Firebase user. */
class AuthRequiredException : Exception("Please login to use this feature")
