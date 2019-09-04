package com.athorfeo.source.utility

inline fun <T: Any, reified A: Annotation> T.findAnnotation(property: String) : A?{
    return try {
        val field = this::class.java.getDeclaredField(property)
        field.getAnnotation(A::class.java)
    }catch (exception: Exception){
        null
    }
}