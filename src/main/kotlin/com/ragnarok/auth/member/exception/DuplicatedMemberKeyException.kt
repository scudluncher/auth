package com.ragnarok.auth.member.exception

open class DuplicatedMemberKeyException(val type: String) : RuntimeException() {}

class AlreadyUsedEmailException : DuplicatedMemberKeyException("이메일")

class AlreadyUsedNickNameException : DuplicatedMemberKeyException("별명")

class AlreadyUsedPhoneNumberException : DuplicatedMemberKeyException("전화번호")
