package com.example.demo;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

  private static final String RESPONSE_STRING_FORMAT = "Hello from '%s': %d";

  /**
   * Counter to help us see the lifecycle
   */
  private AtomicLong incrementer = new AtomicLong(0);

  @GetMapping
  public String helloworld(){
    return String.format(
        RESPONSE_STRING_FORMAT,
        System.getenv().getOrDefault("HOSTNAME", "unknown"),
        incrementer.incrementAndGet());
  }
}
