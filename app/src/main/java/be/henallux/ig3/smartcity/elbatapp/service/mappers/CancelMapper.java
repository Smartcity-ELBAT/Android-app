package be.henallux.ig3.smartcity.elbatapp.service.mappers;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.CancelDto;

public class CancelMapper {
    private static CancelMapper instance;

    static {
        instance = null;
    }

    public static CancelMapper getInstance() {
        if (instance == null)
            instance = new CancelMapper();

        return instance;
    }

    public CancelDto mapToCancelDto(Integer personId, GregorianCalendar dateTimeReserved) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return new CancelDto(personId, format.format(dateTimeReserved.getTime()));
    }
}
