package com.solidcode.library.mapper;

import com.solidcode.library.dto.BookDetails;
import com.solidcode.library.repository.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDetails toDTO(Book book);

    Book toEntity(BookDetails bookDTO);
}
