package com.yeonghoon.heemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HeemoApiApplication

fun main(args: Array<String>) {
    runApplication<HeemoApiApplication>(*args)
}
