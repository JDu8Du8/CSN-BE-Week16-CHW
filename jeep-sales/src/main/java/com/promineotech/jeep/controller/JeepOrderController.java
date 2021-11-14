package com.promineotech.jeep.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.Order;
import com.promineotech.jeep.entity.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RequestMapping("/orders")
@Validated
public interface JeepOrderController {
//@formatter:off
 @Operation(
     summary = "Orders a Jeep",
     description = "Creates an order for a Jeep.",
     responses = {
         @ApiResponse(responseCode = "200", 
             description = "Jeep Order created.", 
             content = @Content(
                 mediaType = "application/json",
             schema = @Schema(implementation = Order.class))),
         @ApiResponse(responseCode = "400", 
         description = "Request Parameters Invalid", 
             content = @Content(mediaType = "application/json")),
         @ApiResponse(responseCode = "404", 
         description = "No Jeep Component Found", 
             content = @Content(mediaType = "application/json")),
         @ApiResponse(responseCode = "500", 
         description = "An unplanned error occurred", 
             content = @Content(mediaType = "application/json"))
     },
     parameters = {
         @Parameter(name = "orderRequest", 
             required = true,
             description = "Order as JSON")
     }
     )
 // @formatter:on
 @PostMapping
 @ResponseStatus(code = HttpStatus.CREATED)
  Order createOrder(@RequestBody OrderRequest orderRequest);
}
