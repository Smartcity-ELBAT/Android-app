package be.henallux.ig3.smartcity.elbatapp.service.mappers;

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

    public List<Reservation> mapToReservation(List<ReservationDto> reservationDto){

        return reservationDto == null ? null :
                reservationDto
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
                                e.printStackTrace();
                            }
                            return null;
                        }
                ).collect(Collectors.toCollection(ArrayList::new));
    }
}
