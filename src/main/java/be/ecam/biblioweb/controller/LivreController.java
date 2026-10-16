package be.ecam.biblioweb.controller;

import be.ecam.biblioweb.dto.LivreRequest;
import be.ecam.biblioweb.model.Livre;
import be.ecam.biblioweb.service.LivreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/livres")
public class LivreController {

    private final LivreService livreService;

    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }

    @GetMapping
    public List<Livre> getAll() {
        return livreService.getAllLivres();
    }

    @GetMapping("/{id}")
    public Livre getOne(@PathVariable UUID id) {
        return livreService.getLivre(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Livre create(@RequestBody @Valid LivreRequest request) {
        return livreService.addLivre(request.title(), request.author());
    }

    @PutMapping("/{id}")
    public Livre update(@PathVariable UUID id, @RequestBody @Valid LivreRequest request) {
        return livreService.updateLivre(id, request.title(), request.author());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        livreService.deleteLivre(id);
    }

    @PostMapping("/{id}/borrow")
    public Livre borrow(@PathVariable UUID id) {
        return livreService.borrow(id);
    }

    @PostMapping("/{id}/return")
    public Livre giveBack(@PathVariable UUID id) {
        return livreService.giveBack(id);
    }
}
