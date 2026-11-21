package br.edu.atitus.productapi.servicesimpl;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomKitSelectionStrategy<T> implements KitSelectionStrategy<T> {
    @Override
    public List<T> select(List<T> candidates, int quantity) {
        if (quantity < 0 || quantity > candidates.size()) throw new IllegalArgumentException("Quantidade de livros indisponível");
        // Partial Fisher-Yates: O(n) para copiar a lista, depois O(k) para sortear k livros sem repetição.
        List<T> shuffled = new ArrayList<>(candidates);
        for (int i = 0; i < quantity; i++) {
            int selected = ThreadLocalRandom.current().nextInt(i, shuffled.size());
            T value = shuffled.get(i);
            shuffled.set(i, shuffled.get(selected));
            shuffled.set(selected, value);
        }
        return new ArrayList<>(shuffled.subList(0, quantity));
    }
}
