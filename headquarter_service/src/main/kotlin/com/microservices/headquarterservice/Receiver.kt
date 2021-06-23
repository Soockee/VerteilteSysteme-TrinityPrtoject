package com.microservices.headquarterservice

import org.springframework.stereotype.Component;
import org.slf4j.Logger
import org.slf4j.LoggerFactory
@Component
class Receiver {
  companion object {
    val logger = LoggerFactory.getLogger(Receiver::class.java)
  }
  fun receiveMessage(message: String) {
    logger.warn("Received <" + message + ">");
  }
}