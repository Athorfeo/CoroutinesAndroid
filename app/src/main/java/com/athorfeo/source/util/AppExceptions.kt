package com.athorfeo.source.util

import java.net.UnknownHostException

fun Exception.getCode(forceCode: Int? = null): Int{
    var code = AppCode.DEFAULT

    when(this){
        is UnknownHostException -> { code = AppCode.NO_INTERNET }
        is QueryDatabaseException -> { code = AppCode.QUERY_DATABASE }
    }

    return forceCode ?: code
}

class QueryDatabaseException(message: String): Exception(message)