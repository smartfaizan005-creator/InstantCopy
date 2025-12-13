package com.kmpbootstrap.shared

import platform.UIKit.UIDevice
import platform.UIKit.UIApplication
import platform.Foundation.NSBundle
import kotlinx.coroutines.*
import platform.Foundation.NSProcessInfo

/**
 * iOS implementation of PlatformService
 */
class IOSPlatformService : PlatformService {
    
    override val platformName: String = "iOS"
    override val platformVersion: String = UIDevice.currentDevice.systemVersion
    override val isDebugBuild: Boolean = NSBundle.mainBundle.infoDictionary?.get("Debug") != null
    
    override suspend fun getSystemInfo(): SystemInfo = withContext(Dispatchers.Main) {
        val device = UIDevice.currentDevice
        val processInfo = NSProcessInfo.processInfo
        val bundle = NSBundle.mainBundle
        
        SystemInfo(
            deviceModel = device.model,
            osVersion = device.systemVersion,
            appVersion = bundle.infoDictionary?.get("CFBundleShortVersionString") as? String ?: "1.0",
            availableMemory = processInfo.physicalMemory.toLong(),
            isLowMemoryDevice = processInfo.isLowPowerModeEnabled
        )
    }
    
    override suspend fun performAction(action: PlatformAction): PlatformResult = withContext(Dispatchers.Main) {
        return@withContext when (action) {
            PlatformAction.GetDeviceInfo -> {
                PlatformResult.Success(getSystemInfo())
            }
            PlatformAction.CheckStorageSpace -> {
                // Simplified storage check - in real app would check actual storage
                PlatformResult.Success(mapOf("available" to true, "message" to "Storage check completed"))
            }
            PlatformAction.ClearCache -> {
                // Simplified cache clearing - in real app would clear actual caches
                PlatformResult.Success(mapOf("message" to "Cache cleared"))
            }
            is PlatformAction.ShowToast -> {
                // iOS doesn't have built-in toast, could use UIAlertController in real implementation
                PlatformResult.Success(mapOf("message" to "Toast shown"))
            }
        }
    }
}

/**
 * iOS implementation of PlatformServiceRepository
 */
class IOSPlatformServiceRepository : PlatformServiceRepository {
    private val service by lazy { IOSPlatformService() }
    
    override suspend fun getService(): PlatformService = service
    
    override suspend fun executeAction(action: PlatformAction): PlatformResult {
        return service.performAction(action)
    }
}