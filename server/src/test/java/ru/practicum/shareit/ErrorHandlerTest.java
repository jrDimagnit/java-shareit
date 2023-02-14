package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.error.ErrorHandler;
import ru.practicum.shareit.error.ErrorResponse;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.error.NotOwnerException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ErrorHandlerTest {

    private ErrorHandler handler = new ErrorHandler();

    @Test
    public void handlerNotFoundExceptionTest() {
        NotFoundException e = new NotFoundException("error");
        ErrorResponse errorResponse = handler.handlerNotFoundException(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getError(), e.getMessage());
    }

    @Test
    public void handleNotOwnerExceptionTest() {
        NotOwnerException e = new NotOwnerException("error");
        ErrorResponse errorResponse = handler.handleNotOwnerException(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getError(), e.getMessage());
    }

    @Test
    public void handlerThrowableTest() {
        Throwable e = new Throwable("error");
        ErrorResponse errorResponse = handler.handlerThrowable(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getError(), e.getMessage());
    }
}
