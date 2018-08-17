package example.api.infra;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(SampleEntityNotFoundException.class)
	public final ResponseEntity<ServiceResponse<Void>> handleEntityNotFoundException(SampleEntityNotFoundException ex,
			WebRequest request) {
		return new ResponseEntity<>(new ServiceResponse<>(new ServiceMessage(MessageType.ERROR, ex.getMessage())),
				HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<>(
				new ServiceResponse<>(new ServiceMessage(MessageType.ERROR,
						"Validation Failed: " + ex.getBindingResult().getFieldErrors().toString())),
				HttpStatus.BAD_REQUEST);
	}
}