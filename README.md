# Library Manager

## Overview
Library Manager is a Spring Boot application that provides a RESTful API for managing a library's book inventory.

## Endpoints

### Books
#### Add a Book
- **URL:** `/api/books`
- **Method:** `POST`
- **Headers:**
  - `Authorization: Bearer <jwt-token>`
- **Request Body:**
  ```json
  {
    "isbn": "1234567890",
    "title": "Book Title",
    "author": "Author Name",
    "publicationYear": 2023,
    "availableCopies": 5
  }
  ```
- **Response:** `201 Created`

#### Remove a Book
- **URL:** `/api/books/{isbn}`
- **Method:** `DELETE`
- **Headers:**
  - `Authorization: Bearer <jwt-token>`
- **Response:** `200 OK`

#### Find a Book by ISBN
- **URL:** `/api/books/{isbn}`
- **Method:** `GET`
- **Headers:**
  - `Authorization: Bearer <jwt-token>`
- **Response:**
  ```json
  {
    "isbn": "1234567890",
    "title": "Book Title",
    "author": "Author Name",
    "publicationYear": 2023,
    "availableCopies": 5
  }
  ```

#### Find Books by Author
- **URL:** `/api/books/author/{author}`
- **Method:** `GET`
- **Headers:**
  - `Authorization: Bearer <jwt-token>`
- **Response:**
  ```json
  [
    {
      "isbn": "1234567890",
      "title": "Book Title",
      "author": "Author Name",
      "publicationYear": 2023,
      "availableCopies": 5
    }
  ]
  ```

#### Borrow a Book
- **URL:** `/api/books/borrow/{isbn}`
- **Method:** `POST`
- **Headers:**
  - `Authorization: Bearer <jwt-token>`
- **Response:** `200 OK`

#### Return a Book
- **URL:** `/api/books/return/{isbn}`
- **Method:** `POST`
- **Headers:**
  - `Authorization: Bearer <jwt-token>`
- **Response:** `200 OK`

## Rate Limiting
The application implements rate limiting to prevent abuse. Each client IP is allowed a maximum of 5 requests per hour. If the rate limit is exceeded, the server responds with a `406 Not Acceptable` status code.

## Example Rate Limiting Response
- **Status:** `406 Not Acceptable`
- **Body:**
  ```json
  {
    "message": "Rate limit exceeded. Try again later."
  }
  ```

## Running the Application
1. Clone the repository.
2. Navigate to the project directory.
3. Run `mvn spring-boot:run`.

## License
Koray Kaya - SolidCode Ltd
```