package com.kmpbootstrap.shared

/**
 * Platform service interface defining available platform capabilities
 */
interface PlatformService {
    val platformName: String
    val platformVersion: String
    val isDebugBuild: Boolean
    
    suspend fun getSystemInfo(): SystemInfo
    suspend fun performAction(action: PlatformAction): PlatformResult
}

/**
 * Data class containing system information
 */
data class SystemInfo(
    val deviceModel: String,
    val osVersion: String,
    val appVersion: String,
    val availableMemory: Long,
    val isLowMemoryDevice: Boolean
)

/**
 * Sealed class representing platform actions
 */
sealed class PlatformAction {
    object GetDeviceInfo : PlatformAction()
    object CheckStorageSpace : PlatformAction()
    object ClearCache : PlatformAction()
    data class ShowToast(val message: String) : PlatformAction()
}

/**
 * Sealed class representing platform action results
 */
sealed class PlatformResult {
    data class Success(val data: Any) : PlatformResult()
    data class Error(val message: String) : PlatformResult()
    object NotSupported : PlatformResult()
}

/**
 * Platform service repository interface
 */
interface PlatformServiceRepository {
    suspend fun getService(): PlatformService
    suspend fun executeAction(action: PlatformAction): PlatformResult
}

/**
 * Platform service use case for business logic
 */
class PlatformServiceUseCase(private val repository: PlatformServiceRepository) {
    suspend fun getSystemInfo(): SystemInfo {
        val service = repository.getService()
        return service.getSystemInfo()
    }
    
    suspend fun checkStorage(): PlatformResult {
        return repository.executeAction(PlatformAction.CheckStorageSpace)
    }
    
    suspend fun clearCache(): PlatformResult {
        return repository.executeAction(PlatformAction.ClearCache)
    }
    
    suspend fun showMessage(message: String): PlatformResult {
        return repository.executeAction(PlatformAction.ShowToast(message))
    }
    
    suspend fun isDebugBuild(): Boolean {
        val service = repository.getService()
        return service.isDebugBuild
    }
}