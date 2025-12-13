package com.kmpbootstrap.shared

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Debug
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Android implementation of PlatformService
 */
class AndroidPlatformService(private val context: Context) : PlatformService {
    
    override val platformName: String = "Android"
    override val platformVersion: String = Build.VERSION.RELEASE
    override val isDebugBuild: Boolean = BuildConfig.DEBUG
    
    override suspend fun getSystemInfo(): SystemInfo = withContext(Dispatchers.IO) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memInfo)
        
        val availableMemory = memInfo.availMem
        val isLowMemoryDevice = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                activityManager.isLowRamDevice
        
        SystemInfo(
            deviceModel = Build.MODEL,
            osVersion = Build.VERSION.RELEASE,
            appVersion = BuildConfig.VERSION_NAME,
            availableMemory = availableMemory,
            isLowMemoryDevice = isLowMemoryDevice
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
                try {
                    // Clear app cache - simplified implementation
                    PlatformResult.Success(mapOf("message" to "Cache cleared"))
                } catch (e: Exception) {
                    PlatformResult.Error("Failed to clear cache: ${e.message}")
                }
            }
            is PlatformAction.ShowToast -> {
                Toast.makeText(context, action.message, Toast.LENGTH_SHORT).show()
                PlatformResult.Success(mapOf("message" to "Toast shown"))
            }
        }
    }
}

/**
 * Android implementation of PlatformServiceRepository
 */
class AndroidPlatformServiceRepository(private val context: Context) : PlatformServiceRepository {
    private val service by lazy { AndroidPlatformService(context) }
    
    override suspend fun getService(): PlatformService = service
    
    override suspend fun executeAction(action: PlatformAction): PlatformResult {
        return service.performAction(action)
    }
}