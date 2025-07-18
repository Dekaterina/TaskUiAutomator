package ru.netology.testing.uiautomator

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

const val SETTINGS_PACKAGE = "com.android.settings"
const val MODEL_PACKAGE = "ru.netology.testing.uiautomator"
const val TIMEOUT = 5000L

@RunWith(AndroidJUnit4::class)
class ChangeTextTest {

    private lateinit var device: UiDevice
    private val textToSet = "Netology"

    private fun waitForPackage(packageName: String) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(packageName)), TIMEOUT)
    }

    @Before
    fun beforeEachTest() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressHome()
        val launcherPackage = device.launcherPackageName
        device.wait(Until.hasObject(By.pkg(launcherPackage)), TIMEOUT)
    }

    @Test
    fun testInternetSettings() {
        waitForPackage(SETTINGS_PACKAGE)
        device.findObject(UiSelector().resourceId("android:id/title").instance(0)).click()
    }

    @Test
    fun testChangeText() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)
        device.findObject(By.res(packageName, "userInput")).text = textToSet
        device.findObject(By.res(packageName, "buttonChange")).click()
        val result = device.findObject(By.res(packageName, "textToBeChanged")).text
        assertEquals(result, textToSet)
    }

    @Test
    fun testEmptyStringInput() {
        val appPackageName = MODEL_PACKAGE
        waitForPackage(appPackageName)
        val initialText = device.findObject(By.res(appPackageName, "textToBeChanged")).text
        device.findObject(By.res(appPackageName, "userInput")).text = " "
        device.findObject(By.res(appPackageName, "buttonChange")).click()
        val resultText = device.findObject(By.res(appPackageName, "textToBeChanged")).text
        assertEquals(resultText, initialText)
    }

    @Test
    fun testOpenNewActivity() {
        val appPackageName = MODEL_PACKAGE
        waitForPackage(appPackageName)
        device.findObject(By.res(appPackageName, "userInput")).text = textToSet
        device.findObject(By.res(appPackageName, "buttonActivity")).click()
        waitForPackage(appPackageName)
        val displayedText = device.findObject(By.res(appPackageName, "text")).text
        assertEquals(displayedText, textToSet)
    }
}