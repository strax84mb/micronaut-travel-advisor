package com.mn.travel.util.path;

import com.mn.travel.entity.Route;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class RouteTreeNode {

    private RouteTreeNode parent;
    private Set<RouteTreeNode> children = new HashSet<>();
    private Long routeId;
    private Long startId;
    private Long destinationId;
    private Double sumPrice;

    public RouteTreeNode(Long startingPoint) {
        parent = null;
        routeId = null;
        startId = null;
        destinationId = startingPoint;
        sumPrice = 0.0;
    }

    public RouteTreeNode(RouteTreeNode parent, Route route) {
        this.parent = parent;
        routeId = route.getId();
        startId = route.getSource().getId();
        destinationId = route.getDestination().getId();
        sumPrice = route.getPrice() + parent.getSumPrice();
    }

    public List<Long> getAllStops() {
        RouteTreeNode temp = this;
        List<Long> result = new ArrayList<>();
        while(temp != null) {
            result.add(temp.getDestinationId());
            temp = temp.getParent();
        }
        return result;
    }

    public List<Long> getAllFlights() {
        RouteTreeNode temp = this;
        List<Long> result = new ArrayList<>();
        while(temp != null && temp.getRouteId() != null) {
            result.add(0, temp.getRouteId());
            temp = temp.getParent();
        }
        return result;
    }

    public void destroy() {
        for (RouteTreeNode child : children) {
            child.destroy();
        }
        children.clear();
        parent = null;
    }
}
