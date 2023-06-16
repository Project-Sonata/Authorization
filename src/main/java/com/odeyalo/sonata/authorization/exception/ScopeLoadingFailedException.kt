package com.odeyalo.sonata.authorization.exception

/**
 * Exception that can be used when scopes can't be loaded
 */
class ScopeLoadingFailedException : Exception {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}