package com.ragnarok.auth.common.infra

interface InfraEntity<T> {
    fun update(domainEntity: T)
    fun toDomainEntity(): T
}
