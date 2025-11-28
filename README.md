Below is a **Spring Web – Information Retrieval Practice** test based on your Spring Web reference material.

Each task is written in a **HackerRank-style**: students must implement or complete some Spring Web code.
👉 **Important (per your rule):** the **questions do not include the reference code**; they only describe what students must create.

At the end you’ll find **suggested solutions**.

---

## Exercise 1 – “Ping Service” (status + headers)

You are working on a health-check endpoint for a service.

### Task

Create a REST endpoint that:

* Listens to `GET /api/ping`
* Returns:

  * HTTP status **200 OK**
  * Header `Cache-Control: no-store`
  * Body: the plain text `pong`

### Requirements

* Expose a Spring Web controller method for this endpoint.
* The method must explicitly control **status**, **headers**, and **body** using the appropriate Spring HTTP abstraction.
* The response body must be exactly `pong` (lowercase, no extra spaces).

### Sample request

```text
GET /api/ping
```

### Sample response

```http
HTTP/1.1 200 OK
Cache-Control: no-store
Content-Type: text/plain;charset=UTF-8

pong
```

---

## Exercise 2 – “Personalized Greeting” (path variable + query param)

You must implement an endpoint that greets a user in different languages.

### Task

Create an endpoint:

* URL: `GET /hello/{name}`
* Optional query parameter: `lang`

  * If omitted, default language is `"en"`.

The endpoint must return:

* For `lang=pt`: `Olá, {name}!`
* For `lang=es`: `¡Hola, {name}!`
* For any other value or default: `Hello, {name}!`

### Requirements

* Use a **path variable** to read `name`.
* Use a **query parameter** to read `lang`, with a default value.
* Return a plain `String` as the response body.

### Example

Request:

```text
GET /hello/Edmilson?lang=pt
```

Response body:

```text
Olá, Edmilson!
```

---

## Exercise 3 – “User-Agent Inspector” (request header)

You are building a debugging endpoint to inspect the client’s HTTP headers.

### Task

Create an endpoint:

* URL: `GET /debug/user-agent`
* The endpoint must:

  * Read the `User-Agent` header from the incoming request.
  * If the header is present, return:

    * HTTP 200 OK
    * Body: `User-Agent: {value}`
  * If the header is missing, return:

    * HTTP 400 Bad Request
    * Body: `Missing User-Agent header`

### Requirements

* Use the appropriate Spring annotation to read **request headers**.
* Use a type that allows explicit control of HTTP status and body.

### Example

Request:

```text
GET /debug/user-agent
User-Agent: curl/7.81.0
```

Response body:

```text
User-Agent: curl/7.81.0
```

---

## Exercise 4 – “Paginated Search Parameters” (query params + default values)

You are implementing a generic search endpoint that supports pagination.

### Task

Create an endpoint:

* URL: `GET /search/items`
* Query parameters:

  * `page` (optional, default: `0`)
  * `size` (optional, default: `20`)

The endpoint must:

* Read both query parameters, applying defaults when values are missing.

* Validate:

  * `page` must be `>= 0`
  * `size` must be between `1` and `100` inclusive

* If parameters are valid, return HTTP 200 with body:

  ```text
  page={page}, size={size}
  ```

* If invalid, return HTTP 400 with body:

  ```text
  Invalid pagination parameters
  ```

### Requirements

* Use Spring annotations to bind query parameters to method arguments.
* Handle missing or invalid (e.g., negative) values in Java.

---

## Exercise 5 – “Create Product” (JSON body with @RequestBody)

You are designing a minimal endpoint to create products in memory.

### Task

1. Define a simple DTO class `ProductDto` with fields:

   * `Long id`
   * `String name`
   * `double price`
2. Implement an endpoint:

   * URL: `POST /products`
   * Request body: JSON representing a product **without id** (id will be generated on the server).
   * Behavior:

     * Generate a new id (e.g., using an in-memory counter).
     * Create a new `ProductDto` with the generated id and the received `name` and `price`.
     * Store it in an in-memory map keyed by id.
     * Return HTTP **201 Created** with the created product as JSON.

### Requirements

* Use `@RequestBody` to map the JSON to the DTO.
* Use `ResponseEntity<ProductDto>` (or equivalent) for the response.
* The same controller must also provide:

  * `GET /products/{id}` that:

    * Returns HTTP 200 + product JSON if found.
    * Returns HTTP 404 if not found.

---

## Exercise 6 – “User Registration Validation” (@Valid + BindingResult)

You are implementing user registration with validation rules.

### Task

1. Create a class `RegistrationRequest` with fields:

   * `String name`
   * `String email`
2. Add Bean Validation constraints:

   * `name`:

     * Not blank
     * Length between 2 and 50
   * `email`:

     * Not blank
     * Must be a valid email format
3. Implement an endpoint:

   * URL: `POST /register`
   * Request body: JSON mapped to `RegistrationRequest`.
   * Behavior:

     * If validation succeeds, return HTTP 200 OK with body:

       ```text
       Registered: {email}
       ```
     * If validation fails, return HTTP 400 Bad Request containing **some representation of the validation errors** (format is up to you: list of messages, etc.).

### Requirements

* Use `@Valid` on the request argument.
* Use the Spring object that captures validation errors.
* Do not perform manual if-null checks; rely on Bean Validation.

---

## Exercise 7 – “Global NotFound Error” (@ControllerAdvice + ProblemDetail)

You want to standardize the error response for resources not found.

### Task

1. Create a custom unchecked exception `ResourceNotFoundException` that receives a message in the constructor.
2. Update your `GET /products/{id}` (from Exercise 5) to:

   * Throw `ResourceNotFoundException` if the product is not found, instead of returning 404 directly.
3. Create a global exception handler that:

   * Catches `ResourceNotFoundException`.
   * Returns HTTP 404 with a standard **Problem Detail** JSON:

     * Status: `404`
     * Title: `"Resource not found"`
     * Detail: exception message.

### Requirements

* Use `@ControllerAdvice` and `@ExceptionHandler`.
* Use the modern Spring Web structure for problem details.
* You must not duplicate the try/catch logic in each controller method.

---

## Exercise 8 – “File Upload Metadata” (Multipart file)

You need to implement a file upload endpoint that returns metadata of the uploaded file.

### Task

Create an endpoint:

* URL: `POST /upload`
* Expected request:

  * `multipart/form-data` with a part named `file`.
* Behavior:

  * Read the uploaded file as a multipart argument.
  * Extract:

    * Original filename
    * Size in bytes
  * Return HTTP 200 OK with body:

    ```text
    filename={filename}, size={size}
    ```

### Requirements

* Use the Spring Web multipart abstraction for the file parameter.
* Use the appropriate annotation to bind the multipart field.
* You **do not** need to store the file; just read metadata.

---

# Suggested Solutions

> These are **one possible implementation** for each exercise. Method names and package names can vary as long as behavior is correct.

---

## Solution 1 – Ping Service

```java
// src/main/java/com/example/demo/web/PingController.java
package com.example.demo.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PingController {

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body("pong");
    }
}
```

---

## Solution 2 – Personalized Greeting

```java
// src/main/java/com/example/demo/web/GreetingController.java
package com.example.demo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping("/hello/{name}")
    public String greet(
            @PathVariable String name,
            @RequestParam(name = "lang", defaultValue = "en") String lang) {

        return switch (lang) {
            case "pt" -> "Olá, " + name + "!";
            case "es" -> "¡Hola, " + name + "!";
            default   -> "Hello, " + name + "!";
        };
    }
}
```

---

## Solution 3 – User-Agent Inspector

```java
// src/main/java/com/example/demo/web/HeaderDebugController.java
package com.example.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeaderDebugController {

    @GetMapping("/debug/user-agent")
    public ResponseEntity<String> userAgent(
            @RequestHeader(value = "User-Agent", required = false) String userAgent) {

        if (userAgent == null || userAgent.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Missing User-Agent header");
        }

        return ResponseEntity.ok("User-Agent: " + userAgent);
    }
}
```

---

## Solution 4 – Paginated Search Parameters

```java
// src/main/java/com/example/demo/web/SearchController.java
package com.example.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @GetMapping("/search/items")
    public ResponseEntity<String> search(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {

        boolean valid =
                page >= 0 &&
                size >= 1 &&
                size <= 100;

        if (!valid) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid pagination parameters");
        }

        String body = "page=%d, size=%d".formatted(page, size);
        return ResponseEntity.ok(body);
    }
}
```

---

## Solution 5 – Create Product

```java
// src/main/java/com/example/demo/web/ProductDto.java
package com.example.demo.web;

public record ProductDto(Long id, String name, double price) {}
```

```java
// src/main/java/com/example/demo/web/ProductController.java
package com.example.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final Map<Long, ProductDto> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto input) {
        long id = sequence.getAndIncrement();
        ProductDto created = new ProductDto(id, input.name(), input.price());
        store.put(id, created);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        ProductDto product = store.get(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }
}
```

---

## Solution 6 – User Registration Validation

```java
// src/main/java/com/example/demo/web/RegistrationRequest.java
package com.example.demo.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Email
    private String email;

    // getters & setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

```java
// src/main/java/com/example/demo/web/RegistrationController.java
package com.example.demo.web;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    @PostMapping
    public ResponseEntity<?> register(
            @Valid @RequestBody RegistrationRequest request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Simple format: concatenate error messages
            String errors = bindingResult.getAllErrors()
                    .stream()
                    .map(e -> e.getDefaultMessage())
                    .reduce((a, b) -> a + "; " + b)
                    .orElse("Validation error");

            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.ok("Registered: " + request.getEmail());
    }
}
```

---

## Solution 7 – Global NotFound Error

```java
// src/main/java/com/example/demo/web/ResourceNotFoundException.java
package com.example.demo.web;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

Update `ProductController`:

```java
// Only the GET method needs to change
@GetMapping("/{id}")
public ProductDto findById(@PathVariable Long id) {
    ProductDto product = store.get(id);
    if (product == null) {
        throw new ResourceNotFoundException("Product %d not found".formatted(id));
    }
    return product;
}
```

Global exception handler:

```java
// src/main/java/com/example/demo/web/GlobalExceptionHandler.java
package com.example.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Resource not found");
        pd.setDetail(ex.getMessage());
        return pd;
    }
}
```

---

## Solution 8 – File Upload Metadata

```java
// src/main/java/com/example/demo/web/FileUploadController.java
package com.example.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file)
            throws IOException {

        String filename = file.getOriginalFilename();
        long size = file.getSize();

        String body = "filename=%s, size=%d".formatted(filename, size);
        return ResponseEntity.ok(body);
    }
}
```

---

If you’d like, I can now **turn these into a .md test file** (with sections for each exercise and solutions) or add **JPA-based retrieval exercises** on top, using the same Spring Web patterns.
