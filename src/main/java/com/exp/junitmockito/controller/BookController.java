package com.exp.junitmockito.controller;

import com.exp.junitmockito.entity.Book;
import com.exp.junitmockito.exception.BookIdNotFoundException;
import com.exp.junitmockito.repositrory.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/book")
public class BookController
{
    @Autowired
    private BookRepository repository;

    @GetMapping("/allbooksrecords")
    public List<Book> getAllBookRecord()
    {
        return repository.findAll();
    }

    @GetMapping("/bookbyid/{bookId}")
    public Book getBookById(@PathVariable ( value = "bookId") Long bookId){
         Optional<Book> book = repository.findById(bookId);
         Book bookById = book.get();
         return  bookById;
    }

    @PostMapping(value = "/savebook")
    public Book saveBookRecord(@RequestBody @Validated Book book)
    {
        return repository.save(book);
    }

    @PutMapping(value = "/updatebook/{bookId}")
    public Book updateBook(@RequestBody @Validated Book book, @PathVariable ("bookId") Long bookId)
            throws BookIdNotFoundException
    {
        Optional<Book> bookById = repository.findById(bookId);
        if (bookById.isPresent()){
            Book updateById = repository.save(book);
            return updateById;
        }else {
            throw new BookIdNotFoundException();
        }
    }

    /* TODO: write /delete api during test */

    @DeleteMapping("/delete/{bookId}")
    public void deleteBookRecordById(@PathVariable(value = "bookId") Long bookId) throws BookIdNotFoundException
    {
        if(!repository.findById(bookId).isPresent())
        {
            throw new BookIdNotFoundException();
        }else {
            repository.deleteById(bookId);
        }
    }
}
