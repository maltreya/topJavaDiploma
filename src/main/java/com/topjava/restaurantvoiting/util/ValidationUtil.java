package com.topjava.restaurantvoiting.util;

import com.topjava.restaurantvoiting.HasId;
import com.topjava.restaurantvoiting.util.exception.ErrorType;
import com.topjava.restaurantvoiting.util.exception.IllegalRequestDataException;
import com.topjava.restaurantvoiting.util.exception.OutOfTimeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.data.repository.CrudRepository;
import org.webjars.NotFoundException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@UtilityClass
public class ValidationUtil {
    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new(id=null)");
        }
    }

    public static <T, ID> void checkExisted(CrudRepository<T, ID> repository, ID id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }

    public static <T> List<T> checkExisted(List<T> objects, int id) {
        if (objects.isEmpty()) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
        return objects;
    }

    public static <T> List<T> checkExisted(List<T> objects, LocalDate date) {
        if (objects.isEmpty()) {
            throw new NotFoundException("Entities from this date " + date + "not found");
        }
        return objects;
    }

    public static <T> List<T> checkExisted(List<T> objects, LocalDate date, int id) {
        if (objects.isEmpty()) {
            throw new NotFoundException("Entities from this id=" + id +
                    "and this date " + date + "not found");
        }
        return objects;
    }

    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    public static void votingTimeVerification(Clock clock) {
        if (LocalTime.now(clock).isAfter(LocalTime.of(11, 0, 0))) {
            throw new OutOfTimeException(
                    "Now " + LocalTime.now(clock).format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                    + ", sorry, voting time expired. You could vote till 11:00:00");
        }
    }

    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static String getMessage(Throwable t) {
        return t.getLocalizedMessage() != null ? t.getLocalizedMessage() : t.getClass().getName();
    }

    public static Throwable logAndGetRootCause(Logger log, HttpServletRequest req, Exception e,
                                               boolean logStackTrace, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logStackTrace) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return rootCause;
    }


}
