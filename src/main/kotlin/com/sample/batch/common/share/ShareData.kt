package com.sample.batch.common.share

import com.sample.batch.common.property.ShareKey
import org.springframework.stereotype.Component


@Component
class ShareData {
    private var shareMap: MutableMap<String, Any?> = mutableMapOf()

    fun putData(key: ShareKey, data: Any?) {
        this.shareMap[key.name] = data
    }

    fun getData(key: ShareKey): Any? {
        return this.shareMap[key.name]
    }
}