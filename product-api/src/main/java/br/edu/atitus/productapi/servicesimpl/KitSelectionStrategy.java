package br.edu.atitus.productapi.servicesimpl;

import java.util.List;

public interface KitSelectionStrategy<T> {
    List<T> select(List<T> candidates, int quantity);
}
