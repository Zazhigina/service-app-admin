package igc.mirror.exception.handler;

import igc.mirror.exception.common.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class EntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalEntityStateException.class)
    ResponseEntity<Object> handleIllegalEntityState(IllegalEntityStateException ex, HttpServletRequest request) {
        if (ex.getErrors() != null) {
            List<ExceptionInfo> exceptions = ex.getErrors().getFieldErrors()
                    .stream()
                    .map(error -> new EntityExceptionInfo(error.getDefaultMessage(), null, ex.getObjectId(), ex.getEntityClass(),
                            error.getField()))
                    .peek(e -> e.setPublicErrorInfo(request, HttpStatus.BAD_REQUEST))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(exceptions, HttpStatus.BAD_REQUEST);

        } else {
            ExceptionInfo exceptionInfo = ex.getExceptionInfo();
            exceptionInfo.setPublicErrorInfo(request, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(exceptionInfo, HttpStatus.BAD_REQUEST);
        }
    }


    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ExceptionInfo> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        ExceptionInfo exceptionInfo = ex.getExceptionInfo();
        exceptionInfo.setPublicErrorInfo(request, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(exceptionInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotSavedException.class)
    ResponseEntity<ExceptionInfo> handleEntityNotSaved(EntityNotSavedException ex, HttpServletRequest request) {
        ExceptionInfo exceptionInfo = ex.getExceptionInfo();
        exceptionInfo.setPublicErrorInfo(request, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(exceptionInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoadFileException.class)
    ResponseEntity<ExceptionInfo> handleLoadFile(LoadFileException ex, HttpServletRequest request) {
        ExceptionInfo exceptionInfo = new ExceptionInfo(ex.getMessage(), "");
        exceptionInfo.setPublicErrorInfo(request, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(exceptionInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ExceptionInfo> handleMaxUploadSizeExceed(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        ExceptionInfo exceptionInfo =
                new ExceptionInfo("Размер файла превышает " + ex.getMaxUploadSize() + "."
                        , "Загрузите файл меньшего размера.");
        exceptionInfo.setPublicErrorInfo(request, HttpStatus.BAD_REQUEST, ex.getCause());
        return new ResponseEntity<>(exceptionInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ExceptionInfo> handleDataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {
        ExceptionInfo exceptionInfo = new ExceptionInfo(e.getCause().getMessage(), "");
        exceptionInfo.setPublicErrorInfo(request, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(exceptionInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionInfo> handleConstraintValidation(ConstraintViolationException e, HttpServletRequest request) {
        ExceptionInfo exceptionInfo = new ExceptionInfo("Ошибка валидации", null);
        exceptionInfo.setPublicErrorInfo(request, HttpStatus.BAD_REQUEST);
        List<DetailExceptionInfo> detailExceptions = e.getConstraintViolations()
                .stream()
                .map(violation -> new DetailExceptionInfo(violation.getMessage(), violation.getPropertyPath().toString()))
                .collect(Collectors.toList());
        exceptionInfo.setDetails(detailExceptions);

        return new ResponseEntity<>(exceptionInfo, HttpStatus.BAD_REQUEST);
    }

//    /**
//     * @param e       the exception
//     * @param headers the headers to be written to the response
//     * @param status  the selected response status
//     * @param request the current request
//     * @return TODO: проверить возможно стандартный метод из класса ResponseEntityExceptionHandler подойдет
//     * или убрать, если не будем использовать валидацию параметров методов контролллера
//     */
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
//                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
//        List<ExceptionInfo> exceptions = e.getBindingResult().getFieldErrors()
//                .stream()
//                .map(error -> new EntityExceptionInfo(error.getDefaultMessage(), null, null, null,
//                        error.getField()))
//                .peek(ex -> ex.setPublicErrorInfo(null, HttpStatus.BAD_REQUEST))
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(EntityDuplicatedException.class)
    ResponseEntity<ExceptionInfo> handleEntityNotFound(EntityDuplicatedException ex, HttpServletRequest request) {
        ExceptionInfo exceptionInfo = ex.getExceptionInfo();
        exceptionInfo.setPublicErrorInfo(request, HttpStatus.CONFLICT);
        return new ResponseEntity<>(exceptionInfo, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RemoteServiceCallException.class)
    public ResponseEntity<Object> handleRemoteServiceCall(RemoteServiceCallException ex, HttpServletRequest request) {
        ExceptionInfo exceptionInfo = ex.getExceptionInfo();
        exceptionInfo.setPublicErrorInfo(request, ex.getRemoteStatus());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", exceptionInfo.getTimestamp());
        body.put("status", exceptionInfo.getStatus());
        body.put("error", exceptionInfo.getError());
        body.put("path",  exceptionInfo.getPath());
        body.put("cause", exceptionInfo.getCause());
        body.put("remoteUrl", ex.getRemoteUrl());
        body.put("remoteStatus", ex.getRemoteStatus());
        body.put("originalMessage", ex.getOriginalMessage());

        return new ResponseEntity<>(body, ex.getRemoteStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionInfo> handleAccessDeniedException(Exception ex, HttpServletRequest request) {
        ExceptionInfo exceptionInfo = new ExceptionInfo("Нет полномочий на выполнение операции", null);
        exceptionInfo.setPublicErrorInfo(request, HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(exceptionInfo, HttpStatus.FORBIDDEN);
    }

}
