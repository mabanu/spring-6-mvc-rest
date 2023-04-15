package mvcrest.spring6mvcrest.service;

import mvcrest.spring6mvcrest.model.Beer;

import java.util.UUID;

public interface BeerService {
    public Beer getBeerById(UUID id);
}
