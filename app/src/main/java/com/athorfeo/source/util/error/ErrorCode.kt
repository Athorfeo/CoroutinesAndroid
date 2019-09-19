package com.athorfeo.source.util.error

import java.net.UnknownHostException

/**
 * Codigos de errores
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class ErrorCode {
    companion object{
        /* System */
        const val DEFAULT = 2000
        const val INTERNET = 2001
        const val QUERY_DATABASE = 2002

        /* Data */
        const val RESPONSE_EMPTY = 2003
        const val DATA_EMPTY = 2004
    }
}

fun Exception.getCode(forceCode: Int? = null): Int{
    var code = ErrorCode.DEFAULT

    when(this){
        is UnknownHostException -> { code = ErrorCode.INTERNET }
        is QueryDatabaseException -> { code = ErrorCode.QUERY_DATABASE }
    }

    return forceCode ?: code
}