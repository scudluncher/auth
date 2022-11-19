package com.ragnarok.auth.verification.value

object TimeLimit {
    // 전화번호 제출 시, 인증코드 입력 기한 3분
    const val CODE_EXPIRED: Long = 3

    // 인증코드 제출 시, 10분간 회원가입 / 비밀번호 재설정
    const val VALID_VERIFICATION_EXPIRED: Long = 10

    // 최초 인증 요청 이후 15분 후 삭제
    const val TTL_MINUTES = 15L
}
