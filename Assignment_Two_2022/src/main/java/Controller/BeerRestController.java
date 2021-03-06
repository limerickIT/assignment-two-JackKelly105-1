/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Beer;
import Model.Brewery;
import Model.Category;
import Model.Style;
import Repository.BeerRepo;
import Repository.CategoryRepo;
import Repository.StyleRepo;
import Repository.BreweryRepo;
import Service.BeerService;
import Service.CategoryService;
import Service.StyleService;
import Service.BreweryService;
import com.google.zxing.WriterException;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
 * http://localhost:8888/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/
 * http://localhost:8888/explorer/index.html#uri=/
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

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private StyleService styleService;

    @Autowired
    private StyleRepo styleRepo;

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
                    linkTo(methodOn(BeerRestController.class).getAll()).withRel("http://localhost:8888/allbeers"));
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
    @GetMapping("allbeers")
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
                            schema = @Schema(implementation = Brewery.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Brewerys not found",
                content = @Content)})
    @GetMapping("/Brewerys/{id}")
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
                            schema = @Schema(implementation = Brewery.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Brewerys not found",
                content = @Content)})
    @GetMapping("allBrewerys")
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
    @GetMapping("count")
    public long getCount() {
        return beerService.count();
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

    @Operation(summary = "Get Image for a given beer thumbs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the beer",
                content = {
                    @Content(mediaType = "application/jpeg",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Beer not found",
                content = @Content)})
    @GetMapping(
            value = "getbeerthumbs/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] getImageWithMediaType(@PathVariable long id) throws IOException {
        try {
            Beer a = beerService.findOne(id);
            String value = "";  
            if(id <=4)
                value = ("/static/assets/images/thumbs/" + a.getId() + ".jpg");
            else
                value = ("/static/assets/images/thumbs/noimage.jpg");
            InputStream in = getClass()
                    .getResourceAsStream(value);
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            throw new ApiRequestException("No beer found");
        }
    }

    @Operation(summary = "Get Image for a given beer large")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the beer",
                content = {
                    @Content(mediaType = "application/jpeg",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Beer not found",
                content = @Content)})
    @GetMapping(
            value = "getbeerlarge/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] getImageWithMediaTypelarge(@PathVariable long id) throws IOException {
        try {
            Beer a = beerService.findOne(id);
            String value = "";  
            if(id <=4)
                value = ("/static/assets/images/large/" + a.getId() + ".jpg");
           else
                value = ("/static/assets/images/large/noimage.jpg");
             InputStream in = getClass()
                    .getResourceAsStream(value);
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            throw new ApiRequestException("No beer found");
        }
    }
   //Zip by ID
    @Operation(summary = "Returns zipped file of images related to beer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the beer",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "beer not found",
                content = @Content)})
    @RequestMapping(value = "/zip/{id}", produces = "application/zip")
    public byte[] zipFiles(HttpServletResponse response, @PathVariable Long id) throws IOException {

        Beer beer = beerService.findOne(id);
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"images.zip\"");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

        ArrayList<File> files = new ArrayList<>();
        if(id <=4)
        files.add(new File("src/main/resources/static/assets/images/large/" +beer.getId() + ".jpg"));
        else
        files.add(new File("src/main/resources/static/assets/images/large/noimage.jpg"));
        

        for (File file : files) {

            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);

            IOUtils.copy(fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.closeEntry();
        }

        if (zipOutputStream != null) {
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
        }
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    //Zip all beers
    @Operation(summary = "Returns zipped file of all images")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the beer",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "zip not found",
                content = @Content)})
    @RequestMapping(value = "zip", produces = "application/zip")
    public byte[] zipFilestwo(HttpServletResponse response) throws IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"images.zip\"");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

        ArrayList<File> files = new ArrayList<>();
        files.add(new File("src/main/resources/static/assets/images/large/1.jpg"));
        files.add(new File("src/main/resources/static/assets/images/large/2.jpg"));
        files.add(new File("src/main/resources/static/assets/images/large/3.jpg"));
        files.add(new File("src/main/resources/static/assets/images/large/4.jpg"));
        files.add(new File("src/main/resources/static/assets/images/large/noimage.jpg"));
        
        for (File file : files) {

            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);

            IOUtils.copy(fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.closeEntry();
        }

        if (zipOutputStream != null) {
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
        }
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    
    @Operation(summary = "Returns beer Brochure in PDF format")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the beer",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Beer.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "beer not found",
                content = @Content)})
    @GetMapping(value = "/pdf/{id}",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity customersReport(@PathVariable Long id) throws IOException {
        try {
            Beer beer = beerService.findOne(id);
            Brewery a = breweryService.findOne(id);
//            Category c = categoryService.findOne(id);
//            Style b = styleService.findOne(id);
            ByteArrayInputStream bis = PDFGenerator.customerPDFReport(beer, a);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=beer.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        } catch (Exception e) {
            throw new ApiRequestException("Inalid input, no beer found");
        }
    }

    @Operation(summary = "Returns QR Code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "QR CODE",
                content = {
                    @Content(mediaType = "application/png",
                            schema = @Schema(implementation = Brewery.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "beer not found",
                content = @Content)})
    @PostMapping(value = "/zxing/qrcode/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> zxingQRCode(@PathVariable long id) throws Exception {
        try {
            Brewery brewery = new Brewery();
            brewery = breweryService.findOne(id);
            long l = brewery.getId();
            Brewery barcode = new Brewery();
            barcode = breweryService.findOne(l);

            return successResponse(QRCode.generatedQRCode(barcode));
        } catch (Exception e) {
            throw new ApiRequestException("QR Generation failure, check parameters");
        }
    }

    private ResponseEntity<BufferedImage> successResponse(BufferedImage image) {
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
