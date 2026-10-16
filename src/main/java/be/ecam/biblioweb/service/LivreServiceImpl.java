package be.ecam.biblioweb.service;

import be.ecam.biblioweb.exception.ResourceNotFoundException;
import be.ecam.biblioweb.model.Livre;
import be.ecam.biblioweb.model.Status;
import be.ecam.biblioweb.repository.LivreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LivreServiceImpl implements LivreService {

    private final LivreRepository livreRepository;

    public LivreServiceImpl(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    @Override
    public Livre addLivre(String title, String author) {
        return livreRepository.save(new Livre(title, author));
    }

    @Override
    public void deleteLivre(UUID id) {
        livreRepository.delete(getLivre(id));
    }

    @Override
    public Livre updateLivre(UUID id, String title, String author) {
        Livre livre = getLivre(id);
        livre.setTitle(title);
        livre.setAuthor(author);
        return livreRepository.save(livre);
    }

    @Override
    @Transactional(readOnly = true)
    public Livre getLivre(UUID id) {
        return livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre introuvable : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livre> getAllLivres() {
        return livreRepository.findAll();
    }

    @Override
    public Livre borrow(UUID id) {
        Livre livre = getLivre(id);
        if (livre.getStatus() == Status.NOT_AVAILABLE) {
            throw new IllegalStateException("Le livre " + livre.getTitle() + " est deja emprunte");
        }
        livre.setStatus(Status.NOT_AVAILABLE);
        return livreRepository.save(livre);
    }

    @Override
    public Livre giveBack(UUID id) {
        Livre livre = getLivre(id);
        livre.setStatus(Status.AVAILABLE);
        return livreRepository.save(livre);
    }
}
