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
        const val DEFAULT = 0
        const val INTERNET = 1

        /* Data */
        const val DATA_EMPTY = 1000
    }
}

fun Exception.getCode(): Int{
    var code = ErrorCode.DEFAULT

    when(this){
        is UnknownHostException -> { code = ErrorCode.INTERNET
        }
    }

    return code
}