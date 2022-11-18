package com.ragnarok.auth.verification.exception

open class VerificationConfirmFailException :RuntimeException()

class CodeNotMatchedException : VerificationConfirmFailException()

class CodeExpiredException : VerificationConfirmFailException()
