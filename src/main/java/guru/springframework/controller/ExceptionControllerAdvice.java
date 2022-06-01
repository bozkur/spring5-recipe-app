package guru.springframework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView numberFormatException(NumberFormatException nfe) {
        log.error("Number format exception is occurred:", nfe);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/400error");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        modelAndView.addObject("exception", nfe);
        return modelAndView;
    }
}
