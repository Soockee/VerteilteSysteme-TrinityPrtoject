package com.microservices.headquarterservice

import org.springframework.stereotype.Component;

@Component
class Receiver {
  fun receiveMessage(message: String) {
    System.out.println("Received <" + message + ">");
  }
}