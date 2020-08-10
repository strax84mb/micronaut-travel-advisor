package com.mn.travel.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "route")
@Table(name = "route", indexes = {
        @Index(name = "route_source_index", columnList = "source_id"),
        @Index(name = "route_destination_index", columnList = "destination_id"),
        @Index(name = "route_path_index", columnList = "source_id,destination_id"),
        @Index(name = "route_unique_index", columnList = "source_id,destination_id,airline_id", unique = true)
})
public class Route {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 3)
    private String airline;

    @Column(name = "airline_id", nullable = false)
    private Long airlineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Airport source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    private Airport destination;

    @Column(nullable = false)
    private Double price;
}
