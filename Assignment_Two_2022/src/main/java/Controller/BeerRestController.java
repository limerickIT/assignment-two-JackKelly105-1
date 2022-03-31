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
 * @author Jack Kelly http://localhost:8888/explorer/index.html#
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
            throw new ApiRequestException("Oops cannot find selected property");
        }
    }
}
