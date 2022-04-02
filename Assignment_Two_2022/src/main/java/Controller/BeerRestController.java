/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Beer;
import Model.Brewery;

import Repository.BeerRepo;
import Repository.BreweryRepo;
import Service.BeerService;
import Service.BreweryService;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpHeaders;

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
    
    @Autowired
    private BreweryRepo breweryRepo;

    @Autowired
    private BreweryService breweryService;

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

    @Operation(summary = "Get a beer by its id (HATEOAS)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the Beer",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Beer not found",
                content = @Content)})
    @GetMapping("/beer/{id}")
    EntityModel<Optional<Beer>> one(@PathVariable Long id) {

        Optional<Beer> b = beerRepo.findById(id);

        try {
            return EntityModel.of(b, //
                    linkTo(methodOn(BeerRestController.class).one(id)).withSelfRel(),
                    linkTo(methodOn(BeerRestController.class).getAll()).withRel("http://localhost:8888/beer/allbeers"));
        } catch (Exception e) {
            throw new ApiRequestException("Oops cannot find selected beer");
        }
    }

    @Operation(summary = "will display the beer name, beer description, and brewery.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found beer",
                content = {
                    @Content(mediaType = "string",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Beer not found",
                content = @Content)})
    @GetMapping("details/{id}")
    public String getOneDetail(@PathVariable long id) {
        try {
            Beer b = beerService.findOne(id);

            long x = (long) b.getBrewery_id();
            Brewery a = breweryService.findOne(x);

            String r = "Name = " + b.getName() + " Description = " + b.getDescription() + " Brewery = " + a.getName();
            return r;
        } catch (Exception e) {
            throw new ApiRequestException("Oops caannot find selected beer");
        }
    }

    @Operation(summary = "Get all beers (HATEOAS)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found beers",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Beers not found",
                content = @Content)})
    @GetMapping("/allbeers")
    CollectionModel<EntityModel<Beer>> all() {
        try {
            List<EntityModel<Beer>> b = beerService.findAll().stream()
                    .map(B -> EntityModel.of(B,
                    linkTo(methodOn(BeerRestController.class).one(B.getId())).withSelfRel(),
                    linkTo(methodOn(BeerRestController.class).all()).withRel("http://localhost:8888/beer/details/" + B.getId())))
                    .collect(Collectors.toList());

            return CollectionModel.of(b, linkTo(methodOn(BeerRestController.class).all()).withSelfRel());
        } catch (Exception e) {
            throw new ApiRequestException("Oops cannot find all beers");
        }
    }

    @Operation(summary = "Get a Brewerys by its id (HATEOAS)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the Brewerys",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Brewerys not found",
                content = @Content)})
    @GetMapping("/brewerys/{id}")
    EntityModel<Optional<Brewery>> oneBrewerys(@PathVariable Long id) {

        Optional<Brewery> b = breweryRepo.findById(id);

        try {
            return EntityModel.of(b, //
                    linkTo(methodOn(BeerRestController.class).one(id)).withSelfRel(),
                    linkTo(methodOn(BeerRestController.class).getAll()).withRel("http://localhost:8888/brewerys/allbrewerys"));
        } catch (Exception e) {
            throw new ApiRequestException("Oops cannot find selected brewerys");
        }
    }

    @Operation(summary = "Get all Brewerys (HATEOAS)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found Brewerys",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Brewerys not found",
                content = @Content)})
    @GetMapping("/allBrewerys")
    CollectionModel<EntityModel<Brewery>> allBrewery() {
        try {
            List<EntityModel<Brewery>> b = breweryService.findAll().stream()
                    .map(B -> EntityModel.of(B,
                    linkTo(methodOn(BeerRestController.class).oneBrewerys(B.getId())).withSelfRel(),
                    linkTo(methodOn(BeerRestController.class).allBrewery()).withRel("http://localhost:8888/brewery/details/" + B.getId())))
                    .collect(Collectors.toList());

            return CollectionModel.of(b, linkTo(methodOn(BeerRestController.class).allBrewery()).withSelfRel());
        } catch (Exception e) {
            throw new ApiRequestException("Oops cannot find all beers");
        }
    }

    @Operation(summary = "Returns total number of beer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found total beers",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Beers not found",
                content = @Content)})
    @GetMapping("/count")
    public long getCount() {
        return beerService.count();
    }

    @Operation(summary = "Deletes selected beer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deleted",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Beer not found",
                content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        try {
            beerService.deleteByID(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            throw new ApiRequestException("Beer never existed, invalid URL");
        }
    }

    @Operation(summary = "Add a new beer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Beer added",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content),})
    @PostMapping("")
    public ResponseEntity add(@RequestBody Beer a) {
        try {
            beerService.saveBeer(a);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ApiRequestException("Add Failure, input may be incorrect");
        }
    }

    @Operation(summary = "Edit selected beer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Beer has been Edited",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Beer not found",
                content = @Content)})
    @PutMapping("")
    public ResponseEntity edit(@RequestBody Beer a) {
        try {
            beerService.saveBeer(a);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            throw new ApiRequestException("Edit failure, potential incorrect input");
        }
    }

    @Operation(summary = "Get a beer by its id (XML FORMAT)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the beer",
                content = {
                    @Content(mediaType = "application/xml",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Beer not found",
                content = @Content)})
    @GetMapping("xml/{id}")
    @Produces(MediaType.APPLICATION_XML_VALUE)
    public Beer getoneXMLList(@PathVariable long id) {
        try {
            return beerService.findOne(id);
        } catch (Exception e) {
            throw new ApiRequestException("Oops cannot find selected beer");
        }
    }

}
