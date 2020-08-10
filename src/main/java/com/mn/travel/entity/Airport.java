package com.mn.travel.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "airport")
public class Airport {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "airportId", nullable = false, unique = true)
    private Long airportId;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(length = 4)
    private String iata;

    @Column(length = 4)
    private String icao;
}
