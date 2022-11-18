package com.ragnarok.auth.verification.exception

import java.time.LocalDateTime

class AlreadyVerifiedException(val verificationExpiredTime: LocalDateTime) : RuntimeException()
