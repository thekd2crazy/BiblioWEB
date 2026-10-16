package be.ecam.biblioweb.service;

import be.ecam.biblioweb.model.Livre;

import java.util.List;
import java.util.UUID;

public interface LivreService {

    Livre addLivre(String title, String author);

    void deleteLivre(UUID id);

    Livre updateLivre(UUID id, String title, String author);

    Livre getLivre(UUID id);

    List<Livre> getAllLivres();

    Livre borrow(UUID id);

    Livre giveBack(UUID id);
}
