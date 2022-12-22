package com.exp.junitmockito.controllertest;

import com.exp.junitmockito.controller.BookController;
import com.exp.junitmockito.entity.Book;
import com.exp.junitmockito.repositrory.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest
{
    /* To mock the methods from Repository Layer we use MOCKITO Framework...
     - used to mocking object
     - RunWith - this will ensure that we are using Mockito to run our class
     */

    private MockMvc mockMvc;

    // we need Object Mapper to convert JASOn into String and wise-versa
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private BookRepository repository;

    // the Class which will accept the Mock and is Inject
    @InjectMocks
    private BookController bookController;

    // writing some test data
    Book b1 = new Book(100L,"TaxPay","a book is tax guide",4);
    Book b2 = new Book(200L,"Golden","A book!",3);
    Book b3 = new Book(300L,"Stranger","an book with strangers",5);

    // Before every test we need to set Up our class
    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void getAllRecords_success() throws Exception
    {
        List<Book> records = new ArrayList<>(Arrays.asList(b1,b2,b3));
        Mockito.when(repository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/book/allbooksrecords")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)));
    }

    @Test
    public void getBookById_success() throws Exception
    {
        Mockito.when(repository.findById(b2.getBookId())).thenReturn(java.util.Optional.of(b2));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/book/bookbyid/200")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.bookName", is("Eject")));
    }

    @Test
    public void createBookRecord_success() throws Exception
    {
        Book book = Book.builder()
                .bookId(400L)
                .bookName("Atomic Habbits")
                .summary("the habbits for good")
                .ratings(3)
                .build();

        Mockito.when(repository.save(book)).thenReturn(book);

        String content = objectWriter.writeValueAsString(book);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/book/savebook")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.bookName", is("Atomic Habbits")));
    }

    @Test
    public void updateBookRecord_success() throws Exception
    {
        Book updatedBook = Book.builder()
                .bookId(200L)
                .bookName("Golden Men")
                .summary("This book"")
                .ratings(5)
                .build();

        Mockito.when(repository.findById(b2.getBookId())).thenReturn(java.util.Optional.of(b2));
        Mockito.when(repository.save(updatedBook)).thenReturn(updatedBook);

        // this for converting Jason into String
        String updatedContent = objectWriter.writeValueAsString(updatedBook);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/book/updatebook/200")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.bookName", is("Golden Men")))
                .andExpect(jsonPath("$.summary", is("This book!")))
                .andExpect(jsonPath("$.ratings", is(5)));
    }

    // TTD approach first Test case and Then create An API
    @Test
    public void deleteByBookById() throws Exception
    {
        Mockito.when(repository.findById(b1.getBookId())).thenReturn(java.util.Optional.of(b2));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/book/delete/200")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
