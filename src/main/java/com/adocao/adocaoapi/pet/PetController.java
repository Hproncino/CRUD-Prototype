package com.adocao.adocaoapi.pet;

import java.net.URI;
import java.util.List;

import com.adocao.adocaoapi.pet.dto.PetCreateRequest;
import com.adocao.adocaoapi.pet.dto.PetResponse;
import com.adocao.adocaoapi.pet.dto.PetUpdateRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetRepository repo;

    public PetController(PetRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<PetResponse> list() {
        return repo.findAll().stream().map(PetController::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> get(@PathVariable Long id) {
        return repo.findById(id).map(PetController::toResponse).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PetResponse> create(@Valid @RequestBody PetCreateRequest req, UriComponentsBuilder uri) {
        Pet saved = repo.save(new Pet(req.name()));
        URI location = uri.path("/api/pets/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> update(@PathVariable Long id, @Valid @RequestBody PetUpdateRequest req) {
        return repo.findById(id).map(p -> {
            p.setName(req.name());
            return ResponseEntity.ok(toResponse(repo.save(p)));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private static PetResponse toResponse(Pet p) {
        return new PetResponse(p.getId(), p.getName(), p.getCreatedAt());
    }
}

