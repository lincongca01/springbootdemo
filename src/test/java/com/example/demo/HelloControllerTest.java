package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HelloControllerTest extends AbstractTest {
  private final String uri = "/api/hello";
  
  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @Test
  public void helloworld() throws Exception {
    MockHttpServletResponse res = getResponse(uri);
    String content = res.getContentAsString();
    int len = content.length();

    assertEquals(200, res.getStatus());
    assertTrue(content.endsWith("1"));
    
    content =getResponse(uri).getContentAsString();
    assertEquals(len, content.length());
    assertTrue(content.endsWith("2"));
  }

  @Test
  public void helloworldMultiThreaded() throws Exception {
    String content = getResponse(uri).getContentAsString();
    assertEquals("1", content.substring(content.length()-1));

    int poolsize = 2;
    ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(poolsize);
    CountDownLatch latch = new CountDownLatch(poolsize);
    for (int i = 0; i < poolsize; i++) {
        WORKER_THREAD_POOL.submit(() -> {
            IntStream.rangeClosed(1, 50).forEach(n -> {
                try {
                  getResponse(uri).getContentAsString();
                } catch (Exception e) {
                  Thread.currentThread().interrupt();
                }
            });
            latch.countDown();
        });
    }
    
    // wait for the latch to be decremented by the two remaining threads
    latch.await();

    content = getResponse(uri).getContentAsString();
    assertEquals("102", content.substring(content.length()-3));
  }

  private MockHttpServletResponse getResponse(String uri) throws Exception {
    return mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.TEXT_PLAIN_VALUE)).andReturn().getResponse();
  }
}