package com.liuxe.iword.http.gson

import java.io.IOException

/**
 *Created by Liuxe on 2022/3/19 21:03
 *desc : code != 200 处理
 */
class ApiException(
 var code: Int = -1,
 var msg: String
):IOException()