package com.airbnb.mvrx.test

import com.airbnb.mvrx.mocking.MockBehavior
import com.airbnb.mvrx.mocking.MockableMavericks
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.rules.ExternalResource

/**
 * To use this in your test class, add:
 * ```
 * @get:Rule
 * val mvrxRule = MvRxTestRule()
 * ```
 */
class MvRxTestRule(
    /**
     * If true, any subscriptions made to a MvRx view model will NOT be made lifecycle aware.
     * This can make it easier to test subscriptions because you won't have to move the test targets to a
     * STARTED state before they can receive subscriptions.
     */
    setForceDisableLifecycleAwareObserver: Boolean = true,
    /**
     * If provided, MvRx mocking will be enabled via [MockableMavericks.initialize] and this will be set as
     * the mocking behavior. The default behavior simply puts the ViewModel in a configuration
     * where state changes happen synchronously, which is often necessary for tests.
     *
     * You can pass a "Scriptable" state store behavior to prevent state changes while forcing
     * your own state changes.
     *
     * If null is given then mock behavior is disabled via [MockableMavericks.initialize].
     */
    viewModelMockBehavior: MockBehavior? = MockBehavior(
        stateStoreBehavior = MockBehavior.StateStoreBehavior.Synchronous
    ),
    /**
     * Changes whether the [com.airbnb.mvrx.MavericksViewModelConfigFactory] is initialized with debug mode or not.
     * By default debug mode is not used so that viewmodels don't all have to run the debug checks
     * each time they are created for Unit tests. This also prevents the need for Robolectric,
     * since the debug checks use Android APIs.
     */
    debugMode: Boolean = false,
    /**
     * A custom coroutine dispatcher that will be set as Dispatchers.Main for testing purposes.
     */
    @Suppress("EXPERIMENTAL_API_USAGE")
    private val testDispatcher: CoroutineDispatcher = TestCoroutineDispatcher()
) : ExternalResource() {

    private val testLifecycleCallbacks: MvRxTestLifecycleCallbacks = MvRxTestLifecycleCallbacksImpl(
        setForceDisableLifecycleAwareObserver = setForceDisableLifecycleAwareObserver,
        viewModelMockBehavior = viewModelMockBehavior,
        debugMode = debugMode,
        testDispatcher = testDispatcher,
    )

    @ExperimentalCoroutinesApi
    override fun before() {
        testLifecycleCallbacks.before()
    }

    @ExperimentalCoroutinesApi
    override fun after() {
        testLifecycleCallbacks.after()
    }
}
