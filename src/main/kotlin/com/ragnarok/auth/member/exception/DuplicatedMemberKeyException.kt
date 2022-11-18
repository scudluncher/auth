package com.ragnarok.auth.member.exception

open class DuplicatedMemberKeyException : RuntimeException()

class AlreadyUsedEmailException : DuplicatedMemberKeyException()

class AlreadyUsedNickNameException : DuplicatedMemberKeyException()

class AlreadyUsedPhoneNumberException : DuplicatedMemberKeyException()
