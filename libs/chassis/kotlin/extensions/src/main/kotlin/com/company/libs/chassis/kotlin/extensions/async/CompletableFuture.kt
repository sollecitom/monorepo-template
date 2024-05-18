package com.company.libs.chassis.kotlin.extensions.async

import java.util.concurrent.CompletableFuture

suspend fun CompletableFuture<Void>.await(): Unit = await().let { }