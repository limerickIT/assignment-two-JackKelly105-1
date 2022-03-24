/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Beer;

import Repository.BeerRepo;
import Service.BeerService;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Jack Kelly
 */
@RestController
@RequestMapping("/beer")
public class BeerRestController {

    @Autowired
    private BeerService beerService;

    @Autowired
    private BeerRepo beerRepo;

    @Operation(summary = "Get all beers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found beers",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Beers",
                content = @Content)})
    @GetMapping("")
    public List<Beer> getAll() {
        return beerService.findAll();
    }
}
