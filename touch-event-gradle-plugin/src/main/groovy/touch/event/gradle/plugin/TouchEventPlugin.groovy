package touch.event.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import touch.event.gradle.asm.utils.LogUtils

class TouchEventPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        LogUtils.println("==TouchEventPlugin gradle plugin==")

        println '----------- registering TouchEventTransform  -----------'
        TouchEventTransform transform = new TouchEventTransform()
        project.android.registerTransform(transform)
    }
}
