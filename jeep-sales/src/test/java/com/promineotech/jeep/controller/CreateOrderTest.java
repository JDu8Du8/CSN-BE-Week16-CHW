package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Order;
import com.promineotech.jeep.entity.OrderRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {
    "classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, 
    config = @SqlConfig(encoding = "utf-8"))

@Slf4j
public class CreateOrderTest {

  @LocalServerPort
  protected int serverPort;
  
  @Autowired
  @Getter
  public TestRestTemplate restTemplate;
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  @Test
  void testCreateOrderReturnsSuccess201() {
    String body = createOrderBody();
    String uri = String.format("http://localhost:%d/orders", serverPort);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);
    
    ResponseEntity<Order> response = restTemplate.exchange(uri,
        HttpMethod.POST, bodyEntity, Order.class);
    log.debug("RESPONSE = {}", response);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();

    Order order = response.getBody();
    assertThat(order.getCustomer().getCustomerId()).isEqualTo("ATTAWAY_HECKTOR");
    assertThat(order.getModel().getModelId()).isEqualTo(JeepModel.GLADIATOR);
    assertThat(order.getModel().getTrimLevel()).isEqualTo("Sport");
    assertThat(order.getModel().getNumDoors()).isEqualTo(4);
    assertThat(order.getColor().getColorId()).isEqualTo("EXT_NACHO");
    assertThat(order.getEngine().getEngineId()).isEqualTo("3_6_GAS");
    assertThat(order.getTire().getTireId()).isEqualTo("265_MICHELIN");
    assertThat(order.getOptions()).hasSize(7);
    }
  
  protected String createOrderBody() {
    // @formatter:off
    return "{\n"
        + "   \"customer\":\"ATTAWAY_HECKTOR\",\n"
        + "   \"model\":\"GLADIATOR\",\n"
        + "   \"trim\":\"Sport\",\n"
        + "   \"doors\":4,\n"
        + "   \"color\":\"EXT_NACHO\",\n"
        + "   \"engine\":\"3_6_GAS\",\n"
        + "   \"tire\":\"265_MICHELIN\",\n"
        + "   \"options\":[\n"
        + "      \"DOOR_QUAD_4\",\n"
        + "      \"EXT_AEV_LIFT\",\n"
        + "      \"EXT_WARN_WINCH\",\n"
        + "      \"EXT_WARN_BUMPER_FRONT\",\n"
        + "      \"EXT_WARN_BUMPER_REAR\",\n"
        + "      \"EXT_ARB_COMPRESSOR\",\n"
        + "      \"TOP_MOPAR_TOP_MESH\"\n"
        + "   ]\n"
        + "}";
      //@formatter:on
  }
  /*
  @Test
  void testDb() {
    int numrows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "models");
    System.out.println("num=" + numrows);
  }
*/
}
