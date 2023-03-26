package touch.event.gradle.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.io.FileUtils
import groovy.io.FileType
import touch.event.gradle.asm.visitor.ITargetMethodMatchedListener
import touch.event.gradle.asm.matcher.ClassInfo
import touch.event.gradle.asm.utils.LogUtils
import touch.event.gradle.asm.utils.PluginUtils
import touch.event.gradle.asm.visitor.TouchEventClassNode
import touch.event.gradle.asm.visitor.TouchEventClassVisitor
import org.gradle.api.internal.artifacts.transform.TransformException
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

class TouchEventTransform extends Transform {
    private static final String TAG = "Transform"

    /**
     * 定义自定义Transform的名称
     * @return
     */
    @Override
    String getName() {
        return "TouchEvent"
    }

    /**
     * 设置 LifeCycleTransform 接收的文件类型
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * 规定自定义 Transform 检索的范围
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        LogUtils.println(TAG + " getScopes start")
        // TransformManager.PROJECT_ONLY 配合每个子module下都应用插件，则可以遍历所有module下的class。
        return TransformManager.PROJECT_ONLY

        // 获取全部的ScopeType：无效，不会遍历任何模块的Class
//        Set<QualifiedContent.ScopeType> scopes = new ImmutableSet.Builder<QualifiedContent.ScopeType>()
//                .addAll(TransformManager.SCOPE_FULL_WITH_FEATURES)
//                .add(InternalScope.LOCAL_DEPS)
//                .build()
//        for (QualifiedContent.ScopeType type : scopes) {
//            LogUtils.println(TAG + " getScopes type: " + type.name() + "-" + type.value)
//        }
//        LogUtils.println(TAG + " getScopes end")
//        return scopes

        // app模块下，使用此配置，可以遍历app模块下的class，无法遍历子module下的Class
//        return TransformManager.SCOPE_FULL_PROJECT

        // app模块下，使用此配置，无法遍历任何class
//        return TransformManager.SCOPE_FULL_LIBRARY_WITH_LOCAL_JARS

        // app模块下，使用此配置，无法遍历任何class
//        return TransformManager.SCOPE_FULL_PROJECT_WITH_LOCAL_JARS

        // app模块下，使用此配置，无法遍历任何class
//        return TransformManager.SCOPE_FULL_WITH_FEATURES
    }

    /**
     * Transform 是否支持增量编译
     * @return
     */
    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        LogUtils.println(TAG + " transform")
        // 拿到所有的class文件， 获取输入项进行遍历
        def transformInputs = transformInvocation.inputs
        //获取输出目录
        def transformOutputProvider = transformInvocation.outputProvider
        transformInputs.each { TransformInput transformInput ->
            // 遍历 jar 包
            transformInput.jarInputs.each { JarInput jarInput ->
                // 直接将 jar 包 copy 到输出目录
                File dest = transformOutputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }
            // 遍历目录
            // 遍历directoryInputs(文件夹中的class文件) directoryInputs代表着以源码方式参与项目编译的所有目录结构及其目录下的源码文件
            // 比如我们手写的类以及R.class、BuildConfig.class以及MainActivity.class等
            transformInput.directoryInputs.each { DirectoryInput directoryInput ->
                // 获取目录里面的 class 文件
                File dir = directoryInput.file
                if (dir) {
                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File file ->
                        LogUtils.println(TAG + " transform start file: " + file.name)

                        // 第一次调用。如果没有目标方法，会添加目标方法，但是该目标方法不会走visit逻辑。
                        def matchDispatchMethod = transformClassFile(file)

                        // class中没有目标方法时，才会进行第二次调用。本次调用中，刚才添加的方法也会走visit逻辑。
                        if (!matchDispatchMethod) {
                            transformClassFile(file)
                        }

                        LogUtils.println(TAG + " transform end file: " + file.name)
                    }
                }
                // 将 Directory 的文件 copy 到输出目录。处理完输入文件后把输出传给下一个文件
                File dest = transformOutputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }
    }

    boolean transformClassFile(File file) {
        LogUtils.println(TAG + " transformClassFile start file: " + file.name)
        def result = false
        // 对class文件进行读取与解析
        def classReader = new ClassReader(file.bytes)
        // 将class文件内容写入到ClassWriter中
        def classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)

        // TouchEventClassNode 中判断是否有目标方法，在listener中同步返回结果。
        def listener = new ITargetMethodMatchedListener() {

            @Override
            void onMatch(boolean matched, ClassInfo classInfo) {
                result = matched
                if (!matched) {
                    // class中没有目标方法，增加对应方法默认实现的字节码
                    PluginUtils.genDispatchTouchEvent(classWriter, classInfo)
                }
            }
        }
        def classNode = new TouchEventClassNode(listener)

        // 使用 TouchEventClassVisitor 去读取内容
        def classVisitor = new TouchEventClassVisitor(classNode)

        // 开始读取，依次调用 ClassVisitor、 ClassNode 接口的各个方法
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)

        classNode.accept(classWriter)

        // 获取修改后的内容，toByteArray方法会将最终修改的字节码以 byte 数组形式返回。
        def bytes = classWriter.toByteArray()
        // 覆盖之前的文件。通过文件流写入方式覆盖掉原先的内容，实现class文件的改写。
        def outputStream = new FileOutputStream(file.path)
        outputStream.write(bytes)
        outputStream.close()

        LogUtils.println(TAG + " transformClassFile end result: " + result)
        return result
    }
}
