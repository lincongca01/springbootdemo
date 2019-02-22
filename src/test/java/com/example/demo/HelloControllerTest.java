package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class HelloControllerTest extends AbstractTest {
  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @Test
  public void helloworld() throws Exception {
    String uri = "/api/hello";
    MockHttpServletResponse res = getResponse(uri);

    int status = res.getStatus();
    assertEquals(200, status);
    String content = res.getContentAsString();
    assertTrue(content.length() < 40);
    assertTrue(content.endsWith("1"));
    
    content =getResponse(uri).getContentAsString();
    assertTrue(content.length() < 40);
    assertTrue(content.endsWith("2"));
  }

  private MockHttpServletResponse getResponse(String uri) throws Exception {
    return mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.TEXT_PLAIN_VALUE)).andReturn().getResponse();
  }

  public static void main(String[] args) {
    System.getenv().keySet().stream().sorted().forEach(e -> System.out.println(e));;
  }
}