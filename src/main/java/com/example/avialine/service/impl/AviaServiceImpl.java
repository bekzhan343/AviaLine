package com.example.avialine.service.impl;

import com.example.avialine.dto.AirportDTO;
import com.example.avialine.dto.CityDTO;
import com.example.avialine.dto.OrderDTO;
import com.example.avialine.dto.PrivacyPoliceDTO;
import com.example.avialine.dto.request.*;
import com.example.avialine.dto.response.*;
import com.example.avialine.enums.*;
import com.example.avialine.exception.*;
import com.example.avialine.mapper.DTOMapper;
import com.example.avialine.model.entity.*;
import com.example.avialine.repo.*;
import com.example.avialine.security.util.SecurityUtil;
import com.example.avialine.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class AviaServiceImpl implements AviaService {

    private final CountryRepo countryRepo;
    private final PrivacyPoliceRepo privacyPoliceRepo;
    private final DTOMapper dtoMapper;
    private final FlightScheduleRepo flightScheduleRepo;
    private final TariffRepo tariffRepo;
    private static final String company = "AT";
    private final CityRepo cityRepo;
    private final BookingRepo bookingRepo;
    private final UserService userService;
    private final BookingSegmentService bookingSegmentService;
    private final BookingService bookingService;
    private final PassengerService passengerService;
    private final OrderService orderService;
    private final DocRepo docRepo;

    @Override
    public Set<SearchParamsResponse> getCountryDetail() {

        List<Country> countries = countryRepo.findCountryWithAirports();

        return countries.stream().map(
                country -> {
                    List<CityDTO> cityDTOS = country.getCities().stream().map(
                            city -> {
                                List<AirportDTO> airportDTOS = city.getAirports().stream().map(
                                        airport -> {
                                            return new AirportDTO(
                                                    airport.getId(),
                                                    airport.getName(),
                                                    airport.getCode()
                                            );
                                        }
                                ).toList();

                                return new CityDTO(
                                        city.getId(),
                                        city.getName(),
                                        city.getCode(),
                                        airportDTOS
                                );
                            }
                    ).toList();

                    return SearchParamsResponse
                            .builder()
                            .id(country.getId())
                            .name(country.getName())
                            .codeName(country.getCode())
                            .image(country.getImage())
                            .cities(cityDTOS)
                            .build();
                }
        ).collect(Collectors.toSet());
    }

    @Override
    public List<PrivacyPoliceDTO> getAllPrivacyPolices() {
        return privacyPoliceRepo
                .findAll()
                .stream()
                .map(dtoMapper::toPrivacyPoliceDTO)
                .toList();
    }

    @Override
    public String billPoints() {
        int random = 100000 + new Random().nextInt(90000);

        return String.valueOf(random);
    }

    @Override
    public String billStatic() {
        int num = 354646;

        return String.valueOf(num);
    }


    @Override
    public ScheduleResponse getSchedule(DepArrRequest request) {

        List<FlightSchedule> schedule = flightScheduleRepo.findUpcomingFlights(request.getDeparture(), request.getArrival());

        List<ScheduleResponse.Result> results = new ArrayList<>();


            for (FlightSchedule fs : schedule) {


                ScheduleResponse.Result result = ScheduleResponse
                        .Result
                        .builder()
                        .company(company)
                        .num(fs.getFlight().getFlightNumber())
                        .operatingCompany(null)
                        .operatingFlight(null)
                        .origin(fs.getFlight().getDepartureCode())
                        .destination(fs.getFlight().getArrivalCode())
                        .departureTime(fs.getFlight().getDepartureTime().toString())
                        .arrivalTime(fs.getFlight().getArrivalTime().toString())
                        .flightTime(convertTime(fs.getFlight().getFlightMinutes()))
                        .airplane(fs.getFlight().getAirplane())
                        .serviceType(fs.getFlight().getServiceType())
                        .origTerm(fs.getFlight().getOriginTerminal())
                        .destTerm(fs.getFlight().getDestinationTerminal())
                        .originType("airport")
                        .originCity(cityRepo.findCityByCode(fs.getFlight().getDepartureCode()).getName())
                        .destinationType("airport")
                        .destinationCity(cityRepo.findCityByCode(fs.getFlight().getArrivalCode()).getName())
                        .classes(
                                ScheduleResponse
                                        .Class
                                        .builder()
                                        .first(fs.getAvailFirst())
                                        .econom(fs.getAvailEconom())
                                        .business(fs.getAvailBusiness())
                                        .build())
                        .build();

                results.add(result);
            }

        return ScheduleResponse.builder()
                .count(results.size())
                .nextPagePath("")
                .previousPagePath("")
                .current(1)
                .results(results)
                .build();
    }

    @Transactional
    @Override
    public PNRResponse booking(BookingRequest request) {

        Authentication auth = SecurityUtil.requireAuthentication();
        User user = userService.getActiveUserByPhone(auth.getName());

        Booking booking = bookingService.createBooking(request, user);

        List<BookingSegment> bookingSegments = bookingSegmentService.createBookingSegments(booking, request.getSegments());
        booking.setBookingSegments(bookingSegments);

        List<Passenger> passengers = passengerService.createPassenger(booking, request, bookingSegments);
        booking.setPassengers(passengers);

        bookingRepo.save(booking);

        orderService.createOrder(booking);

        return new PNRResponse(booking.getPnrNumber(), booking.getStatus().toString());
    }

    @Override
    public BookingInfoResponse detailBooking(RegnumSurnameRequest request) {

        Booking booking = bookingService.getBookingBySurnameAndPnr(request.getSurname(), request.getRegnum());
        List<Passenger> passengers = booking.getPassengers();
        List<BookingSegment> bookingSegments = booking.getBookingSegments();

        boolean moreInfo = request.isMoreInfo();
        boolean commonStatus = request.isAddCommonStatus();

        BookingInfoResponse response = BookingInfoResponse
                .builder()
                .surname(request.getSurname())
                .regnum(booking.getPnrNumber())
                .build();

        if (moreInfo){
            response.setCurrency(booking.getCurrency().toString());
            response.setEmail(booking.getEmail());
            response.setPhone(booking.getPhoneNumber());
            response.setPassengers(passengers.stream().map(dtoMapper::toPassengerDetail).toList());
            response.setSegments(bookingSegments.stream().map(dtoMapper::toSegmentsDetail).toList());
        }

        if (commonStatus){
            response.setCreatedAt(booking.getCreatedAt());
            response.setUpdatedAt(booking.getUpdatedAt());
        }

        return response;
    }

    @Override
    public List<OrderDTO> getAllOrders() {

        Authentication auth = SecurityUtil.requireAuthentication();

        User user = userService.getActiveUserByPhone(auth.getName());

        List<Booking> bookings = bookingService.getByUser(user);

        List<Order> orders = orderService.getAllOrders(bookings);

        List<BookingSegment> segments = bookingSegmentService.getBookingSegments(bookings);

        List<Passenger> passengers = passengerService.getPassengers(bookings);

        return orders.stream().map(
                order -> {

                    Booking booking = order.getBooking();

                    List<OrderDTO.SegmentShort> segmentShorts = segments.stream()
                            .filter(s -> s.getBooking().equals(booking))
                            .map(
                                    bookingSegment -> {

                                        LocalDateTime departure = LocalDateTime.of(
                                                bookingSegment.getSchedule().getDate(), bookingSegment.getSchedule().getFlight().getDepartureTime());

                                        LocalDateTime arrival = departure.plusMinutes(bookingSegment.getSchedule().getFlight().getFlightMinutes());

                                        return OrderDTO.SegmentShort.builder()
                                                .flight(bookingSegment.getFlight())
                                                .company(bookingSegment.getCompany())
                                                .duration(convertTime(bookingSegment.getSchedule().getFlight().getFlightMinutes()))
                                                .departure(bookingSegment.getDeparture())
                                                .arrival(bookingSegment.getArrival())
                                                .departureDate(bookingSegment.getSchedule().getDate().toString())
                                                .departureTime(bookingSegment.getSchedule().getFlight().getDepartureTime().toString())
                                                .arrivalDate(arrival.toLocalDate().toString())
                                                .arrivalTime(bookingSegment.getSchedule().getFlight().getArrivalTime().toString())
                                                .build();
                                    }
                            ).toList();


                    List<OrderDTO.PassengerShort> passengerShorts = passengers.stream()
                            .filter(s -> s.getBooking().equals(booking))
                            .map(
                                    passenger -> {
                                        return OrderDTO.PassengerShort.builder()
                                                .id(passenger.getId())
                                                .firstName(passenger.getFirstname())
                                                .lastName(passenger.getLastname())
                                                .surname(passenger.getSurname())
                                                .category(passenger.getCategory().toString())
                                                .sex(passenger.getSex().toString())
                                                .birthdate(passenger.getBirthdate().toString())
                                                .docCountry(passenger.getDocCountry())
                                                .doc(passenger.getDoc())
                                                .pspexpire(passenger.getPspexpire().toString())
                                                .docCode(passenger.getDocCode())
                                                .build();
                                    }
                            ).toList();
                    return OrderDTO
                            .builder()
                            .orderId(order.getId())
                            .regnum(order.getRegnum())
                            .email(order.getBooking().getEmail())
                            .status(order.getStatus().toString())
                            .price(order.getPrice().toString())
                            .currency(order.getCurrency().toString())
                            .segments(segmentShorts)
                            .passengers(passengerShorts)
                            .passengersCount(passengerShorts.size())
                            .build();
                }
        ).toList();

    }

    @Override
    public OrderDTO getOrderById(Integer id) {

        Order order = orderService.getOrderById(id);



        List<OrderDTO.SegmentShort> segments = order.getBooking().getBookingSegments().stream().map(
                bookingSegment -> {

                    LocalDateTime depTime = LocalDateTime.of(bookingSegment.getSchedule().getDate(), bookingSegment.getSchedule().getFlight().getDepartureTime());

                    LocalDateTime arrTime = depTime.plusMinutes(bookingSegment.getSchedule().getFlight().getFlightMinutes());

                    return OrderDTO.SegmentShort.builder()
                        .flight(bookingSegment.getFlight())
                        .company(bookingSegment.getCompany())
                        .duration(bookingSegment.getSchedule().getFlight().getFlightMinutes().toString())
                        .departure(bookingSegment.getDeparture())
                        .arrival(bookingSegment.getArrival())
                        .departureDate(depTime.toLocalDate().toString())
                        .departureTime(depTime.toLocalTime().toString())
                        .arrivalDate(arrTime.toLocalDate().toString())
                        .arrivalTime(arrTime.toLocalTime().toString())
                        .build();
                }
        ).toList();

        List<OrderDTO.PassengerShort> passengers = order.getBooking().getPassengers().stream().map(
                passenger -> {
                    return OrderDTO.PassengerShort.builder()
                            .id(passenger.getId())
                            .firstName(passenger.getFirstname())
                            .lastName(passenger.getLastname())
                            .surname(passenger.getSurname())
                            .category(passenger.getCategory().toString())
                            .sex(passenger.getSex().toString())
                            .birthdate(passenger.getBirthdate().toString())
                            .docCountry(passenger.getDocCountry())
                            .doc(passenger.getDoc())
                            .pspexpire(passenger.getPspexpire().toString())
                            .docCode(passenger.getDocCode())
                            .build();
                }
        ).toList();

        return OrderDTO.builder()
                .orderId(order.getId())
                .regnum(order.getRegnum())
                .email(order.getBooking().getEmail())
                .status(order.getStatus().toString())
                .price(order.getPrice().toString())
                .currency(order.getCurrency().toString())
                .segments(segments)
                .passengers(passengers)
                .passengersCount(passengers.size())
                .build();
    }

    @Override
    public OrderStatusResponse getOrderStatus(String regnum) {

        Order order = orderService.getOrderByRegnum(regnum);

        return new OrderStatusResponse(order.getId(), order.getStatus().toString());
    }

    @Override
    public PNRResponse addInfant(AddInfantRequest request) {
        Map<String,List<String>> errors = new HashMap<>();

        Passenger parent = passengerService.getPassengerById(request.getParentPassId());

        long age = Period.between(parent.getBirthdate(), LocalDate.now()).getYears();

        if (parent.getBirthdate().plusYears(18).isAfter(LocalDate.now())){
            errors.put("noAvailAdtAge", List.of(ApiErrorMessage.UNAVAILABLE_ADT_AGE_MESSAGE.getMessage(age)));
        }

        if (request.getBirthdate().plusYears(2).isBefore(LocalDate.now())) {
            errors.put("age", List.of(ApiErrorMessage.INF_AGE_ERROR_MESSAGE.getMessage()));
        }

        if (!request.getDoc().equals(request.getDocCode())){
            errors.put("doc", List.of(ApiErrorMessage.DOC_DOCCODE_DOES_NOT_MATCH_MESSAGE.getMessage()));
        }
        if (!errors.isEmpty()){
            throw new ValidationException("errors", errors);
        }

        Doc doc = docRepo.findByCode(request.getDocCode())
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.DOC_NOT_AVAILABLE_MESSAGE.getMessage()));


        Booking booking = bookingService.getBookingByRegnum(request.getRegnum());

        Passenger passenger = passengerService.addInfant(request, booking, request.getNationality());

        return new PNRResponse(booking.getPnrNumber(), BookingStatus.CREATED.getStatus());
    }

    @Override
    public SearchTicketResponse searchTicket(SearchTicketRequest request) {

        List<SearchTicketResponse.Variant> variants = new ArrayList<>();

        SearchTicketRequest.SearchSegment segment = request.getSegments().getFirst();

        if (segment.getDate().isBefore(LocalDate.now())){
            throw new PastDateException(ApiErrorMessage.DATE_IN_PAST_MESSAGE.getMessage());
        }

        List<FlightSchedule> flightSchedules = flightScheduleRepo.findAvailableFlights(
                segment.getDeparture(),
                segment.getArrival(),
                segment.getDate(),
                segment.getDirect()
        );


        for (FlightSchedule schedule : flightSchedules) {


            Flight flight = schedule.getFlight();

            List<SearchTicketResponse.PassengersPrice> passengersPrices = new ArrayList<>();
            BigDecimal variantTotalPrice = BigDecimal.ZERO;

            for (SearchTicketRequest.Passenger passenger : request.getPassengers()) {

                    PaxCode paxCode = passenger.getCode();

                if (passenger.getAge() > paxCode.getMax() ||
                    passenger.getAge() < paxCode.getMin()
                ){
                    throw new IllegalArgumentException(ApiErrorMessage.INVALID_PASSENGER_AGE_MESSAGE.getMessage(passenger.getAge()));
                }

                Tariff tariff = tariffRepo.findTariff(flight.getId(),
                        paxCode,
                        request.getCurrency()
                        ).orElseThrow(
                                () -> new IllegalArgumentException(
                                        ApiErrorMessage
                                                .TARIFF_NOT_FOUND_MESSAGE
                                                .getMessage()));

                BigDecimal total = tariff.getFare().add(tariff.getTaxes())
                                .multiply(BigDecimal.valueOf(passenger.getCount()));

                variantTotalPrice = variantTotalPrice.add(total);
                passengersPrices.add(
                        SearchTicketResponse.PassengersPrice.builder()
                                .paxCode(paxCode.getName())
                                .count(passenger.getCount())
                                .fare(tariff.getFare())
                                .taxes(tariff.getTaxes())
                                .total(total)
                                .build()
                );
            }

            SearchTicketRequest.AnswerParams params = request.getAnswerParams();

            SearchTicketResponse.Variant variant = SearchTicketResponse.Variant
                    .builder()
                    .flightNumber(flight.getFlightNumber())
                    .carrier(flight.getCarrier())
                    .airplane(flight.getAirplane())
                    .date(schedule.getDate())
                    .direct(schedule.getDirect())
                    .departure(SearchTicketResponse.DepartureArrival
                            .builder()
                            .code(flight.getDepartureCode())
                            .time(flight.getDepartureTime())
                            .build())
                    .arrival(SearchTicketResponse.DepartureArrival
                            .builder()
                            .code(flight.getArrivalCode())
                            .time(flight.getArrivalTime())
                            .build()

                    )
                    .flightTime(Boolean.TRUE.equals(params.getShowFlightTime())
                    ? convertTime(flight.getFlightMinutes()) : null)
                    .meals(Boolean.TRUE.equals(params.getShowMeals())
                    ? flight.getMeals() : null
                    )
                    .variantTotal(Boolean.TRUE.equals(params.getShowVariantTotal())
                    ? variantTotalPrice : null)
                    .available(Boolean.TRUE.equals(params.getShowAvailable())
                    ? schedule.getAvailable() : null
                    )
                    .bagNormFull(Boolean.TRUE.equals(params.getShowBagNormalFull())
                    ? flight.getBagNormKg() : null
                    )
                    .passengers(passengersPrices)
                    .currency(request.getCurrency().getSymbol())
                    .build();


            variants.add(variant);
        }

        return SearchTicketResponse.builder()
                .variants(variants)
                .build();
    }

    private String convertTime(Integer minutes) {
        int hours = minutes / 60;
        int minute = minutes % 60;

        return String.format("%02d:%02d", hours, minute);
    }

}
