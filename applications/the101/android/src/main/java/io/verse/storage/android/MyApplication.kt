package io.verse.storage.android

import io.tagd.android.app.TagdApplication
import io.tagd.android.app.loadingstate.AppLoadingStateHandler
import io.tagd.android.app.loadingstate.AppLoadingStepDispatcher

class MyApplication : TagdApplication() {

    override fun newLoadingStateHandler(
        dispatcher: AppLoadingStepDispatcher<out TagdApplication>
    ): AppLoadingStateHandler {

        return MyAppLoadingStateHandler(this, dispatcher)
    }
}