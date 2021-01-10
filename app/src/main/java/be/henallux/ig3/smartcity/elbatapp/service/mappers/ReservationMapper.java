package be.henallux.ig3.smartcity.elbatapp.service.mappers;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import be.henallux.ig3.smartcity.elbatapp.data.model.Reservation;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.ReservationDto;

public class ReservationMapper {
    private static ReservationMapper instance = null;

    public static ReservationMapper getInstance() {
        if(instance == null)
            instance = new ReservationMapper();

        return instance;
    }

    public List<Reservation> mapToReservations(List<ReservationDto> reservationsDto){

        return reservationsDto == null ? null :
                reservationsDto
                .stream()
                .map(reservation ->
                        {
                            try {
                                return new Reservation(reservation.getPersonId(),
                                        reservation.getDateTimeReserved(),
                                        reservation.getNbCustomers(),
                                        reservation.getAdditionalInfo(),
                                        reservation.getCancelled(),
                                        reservation.getOutside(),
                                        reservation.getEstablishmentName()
                                );
                            } catch (ParseException e) {
                                Log.e("MapToReservations - Parsing error", e.getLocalizedMessage());
                            }
                            return null;
                        }
                ).collect(Collectors.toCollection(ArrayList::new));
    }

    public ReservationDto mapToReservationDto(Reservation reservation) {
        return reservation == null ? null :
                new ReservationDto(
                        reservation.getPersonId(),
                        (String) DateFormat.format("yyyy-MM-dd HH:mm:ss", reservation.getDateTimeReserved()),
                        reservation.getNbCustomers(),
                        reservation.getAdditionalInfo(),
                        reservation.getCancelled(),
                        reservation.getOutside(),
                        reservation.getEstablishmentId(),
                        reservation.getEstablishmentName(),
                        reservation.getTableId()
                );
    }
}
