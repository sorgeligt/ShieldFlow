package com.sorgeligt.shieldflow.core.time

public interface TimeProviderImplScope {
    public val TimeProviderImplScope.systemTimeProvider: TimeProvider get() = SystemTimeProvider
}
