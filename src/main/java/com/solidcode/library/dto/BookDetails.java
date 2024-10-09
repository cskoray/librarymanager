package com.solidcode.library.dto;

import com.solidcode.library.validator.ValidISBN;
import com.solidcode.library.validator.ValidPublicationYear;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDetails {

    @ValidISBN
    private String isbn;

    @Size(min = 3, message = "Title must be at least 3 characters long")
    private String title;

    @Size(min = 3, message = "Author must be at least 3 characters long")
    private String author;

    @ValidPublicationYear
    private int publicationYear;

    @Min(value = 0, message = "Available copies cannot be negative")
    private int availableCopies;
}
