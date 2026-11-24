package br.edu.atitus.productapi.servicesimpl;

import br.edu.atitus.productapi.dtos.ProductRequest;
import br.edu.atitus.productapi.dtos.ProductResponse;
import br.edu.atitus.productapi.entities.ProductEntity;
import br.edu.atitus.productapi.entities.ProductType;
import br.edu.atitus.productapi.repositories.ProductRepository;
import br.edu.atitus.productapi.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.TreeMap;

@Service
public class ProductServiceJpa implements ProductService {
    private final ProductRepository repository;
    private final KitSelectionStrategy<ProductEntity> kitSelectionStrategy;

    public ProductServiceJpa(ProductRepository repository, KitSelectionStrategy<ProductEntity> kitSelectionStrategy) {
        this.repository = repository;
        this.kitSelectionStrategy = kitSelectionStrategy;
    }

    @Override
    public Page<ProductResponse> findAll(String category, ProductType type, String search, Pageable pageable) {
        Specification<ProductEntity> spec = (root, query, cb) -> cb.conjunction();
        if (category != null && !category.isBlank())
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("category")), category.trim().toLowerCase()));
        if (type != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        if (search != null && !search.isBlank()) {
            String term = "%" + search.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("title")), term),
                    cb.like(cb.lower(root.get("author")), term)));
        }
        Pageable sorted = pageable.getSort().isUnsorted()
                ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("title").ascending())
                : pageable;
        return repository.findAll(spec, sorted).map(ProductResponse::fromEntity);
    }

    @Override
    public List<String> findCategories() {
        TreeMap<String, String> categories = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        repository.findDistinctCategories().forEach(category -> categories.putIfAbsent(category, category));
        return List.copyOf(categories.values());
    }

    @Override
    public ProductResponse findById(Long id) {
        return ProductResponse.fromEntity(getEntity(id));
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        return ProductResponse.fromEntity(repository.save(toEntity(new ProductEntity(), request)));
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        return ProductResponse.fromEntity(repository.save(toEntity(getEntity(id), request)));
    }

    @Override
    public ProductResponse updateImage(Long id, String imagePath) {
        ProductEntity product = getEntity(id);
        product.setImage(imagePath);
        return ProductResponse.fromEntity(repository.save(product));
    }

    @Override
    public void delete(Long id) {
        ProductEntity entity = getEntity(id);
        repository.delete(entity);
    }

    @Override
    public List<ProductEntity> findKitBooks(Long kitId, int quantity) {
        ProductEntity kit = getEntity(kitId);
        if (kit.getType() != ProductType.KIT) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O produto informado não é um kit");
        List<ProductEntity> candidates = repository.findByCategoryIgnoreCaseAndType(kit.getCategory(), ProductType.BOOK);
        if (quantity < 1 || kit.getBookCount() == null || quantity > Integer.MAX_VALUE / kit.getBookCount())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade do kit inválida");
        int required = kit.getBookCount() * quantity;
        if (candidates.size() < required)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não há livros suficientes nesta categoria para montar o kit");
        return kitSelectionStrategy.select(candidates, required);
    }

    private ProductEntity getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    private ProductEntity toEntity(ProductEntity entity, ProductRequest request) {
        if (request.type() == ProductType.BOOK && (request.title() == null || request.title().isBlank()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Título é obrigatório para livros");
        if (request.type() == ProductType.BOOK && (request.author() == null || request.author().isBlank()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autor é obrigatório para livros");
        if (request.type() == ProductType.BOOK && request.bookCount() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bookCount deve ser nulo para livros");
        if (request.currency() != null && !"BRL".equalsIgnoreCase(request.currency().trim()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A moeda do catálogo deve ser BRL");
        if (request.image() != null && !request.image().isBlank()
                && (!request.image().startsWith("/images/") || request.image().contains("..") || request.image().contains("\\")))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A imagem deve ser enviada pelo endpoint de upload");
        String category = request.category().trim();
        entity.setTitle(request.type() == ProductType.KIT ? "Kit " + category : request.title().trim());
        entity.setAuthor(request.type() == ProductType.BOOK ? request.author().trim() : null);
        entity.setDescription(request.description() == null ? null : request.description().trim());
        entity.setCategory(category);
        entity.setType(request.type());
        entity.setBookCount(request.type() == ProductType.KIT ? 3 : null);
        entity.setPrice(request.price());
        entity.setCurrency("BRL");
        entity.setImage(request.image() == null || request.image().isBlank() ? null : request.image().trim());
        return entity;
    }
}
