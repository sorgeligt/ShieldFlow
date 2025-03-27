package com.sorgeligt.shieldflow.network.adapters

public class RetrofitTelemetryNetworkEventsAdapter(
    private val retrofitContracts: List<RetrofitContract>,
    private val supportedAnnotations: List<Class<out Annotation>>,
) : TelemetryNetworkEventsAdapter {

    override fun getPaths(): List<String> {
        val paths = mutableListOf<String>()
        retrofitContracts.forEach { contract ->
            paths.addAll(
                getAnnotationValues(
                    clazz = contract.clazz,
                    supportedAnnotations = supportedAnnotations
                ).map { contract.basePrefix + it }
            )
        }

        return paths
    }

    private fun getAnnotationValues(
        clazz: Class<*>,
        supportedAnnotations: List<Class<out Annotation>>
    ): List<String> {
        val paths = mutableListOf<String>()
        clazz.declaredMethods.forEach { method ->
            supportedAnnotations.forEach { annotationClass ->
                val annotation = method.getAnnotation(annotationClass)
                if (annotation != null) {
                    val valueMethod = annotationClass.getDeclaredMethod("value")
                    valueMethod.isAccessible = true
                    val value = valueMethod.invoke(annotation) as? String
                    value?.let { paths.add(value) }
                }
            }
        }
        return paths
    }
}

public class RetrofitContract(
    public val clazz: Class<*>,
    public val basePrefix: String = "",
)
