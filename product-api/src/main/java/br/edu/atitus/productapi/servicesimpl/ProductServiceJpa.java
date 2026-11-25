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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

@Service
public class ProductServiceJpa implements ProductService {
    private static final String ACCENTED_CHARACTERS = "áàâãäéèêëíìîïóòôõöúùûüç";
    private static final String UNACCENTED_CHARACTERS = "aaaaaeeeeiiiiooooouuuuc";
    private final ProductRepository repository;
    private final KitSelectionStrategy<ProductEntity> kitSelectionStrategy;

    public ProductServiceJpa(ProductRepository repository, KitSelectionStrategy<ProductEntity> kitSelectionStrategy) {
        this.repository = repository;
        this.kitSelectionStrategy = kitSelectionStrategy;
    }

    @Override
    public Page<ProductResponse> findAll(String category, ProductType type, String search, Pageable pageable) {
        Specification<ProductEntity> spec = (root, query, cb) -> cb.conjunction();
        if (category != null && !category.isBlank()) {
            String normalizedCategory = normalizeText(category);
            spec = spec.and((root, query, cb) -> cb.equal(
                    normalizeDatabaseText(cb, root.<String>get("category")), normalizedCategory));
        }
        if (type != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        if (search != null && !search.isBlank()) {
            String term = "%" + normalizeText(search) + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(normalizeDatabaseText(cb, root.<String>get("title")), term),
                    cb.like(normalizeDatabaseText(cb, root.<String>get("author")), term)));
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
        validateBookCategoryChange(entity, null, entity.getCategory());
        repository.delete(entity);
    }

    @Override
    public List<ProductEntity> findKitBooks(Long kitId, int quantity, Set<Long> excludedBookIds) {
        ProductEntity kit = getEntity(kitId);
        if (kit.getType() != ProductType.KIT) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O produto informado não é um kit");
        List<ProductEntity> candidates = findByCategoryAndType(kit.getCategory(), ProductType.BOOK);
        if (quantity < 1 || kit.getBookCount() == null || quantity > Integer.MAX_VALUE / kit.getBookCount())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade do kit inválida");
        int required = kit.getBookCount() * quantity;
        if (candidates.size() < required)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não há livros suficientes nesta categoria para montar o kit");

        Set<Long> excluded = excludedBookIds == null ? Set.of() : excludedBookIds;
        List<ProductEntity> available = candidates.stream()
                .filter(book -> !excluded.contains(book.getId()))
                .toList();
        int freshCount = Math.min(required, available.size());
        List<ProductEntity> selected = new java.util.ArrayList<>(kitSelectionStrategy.select(available, freshCount));
        if (selected.size() < required) {
            Set<Long> selectedIds = selected.stream().map(ProductEntity::getId).collect(java.util.stream.Collectors.toSet());
            List<ProductEntity> fallback = candidates.stream()
                    .filter(book -> !selectedIds.contains(book.getId()))
                    .toList();
            selected.addAll(kitSelectionStrategy.select(fallback, required - selected.size()));
        }
        return selected;
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
        if (request.type() == ProductType.KIT
                && (request.bookCount() == null || request.bookCount() < 1 || request.bookCount() > 5))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "bookCount é obrigatório para kits e deve estar entre 1 e 5");
        if (request.currency() != null && !"BRL".equalsIgnoreCase(request.currency().trim()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A moeda do catálogo deve ser BRL");
        if (request.image() != null && !request.image().isBlank()
                && (!request.image().startsWith("/images/") || request.image().contains("..") || request.image().contains("\\")))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A imagem deve ser enviada pelo endpoint de upload");
        String category = request.category().trim();
        validateBookCategoryChange(entity, request.type(), category);
        if (request.type() == ProductType.KIT) {
            long bookCount = countByCategoryAndType(category, ProductType.BOOK);
            if (entity.getId() != null && entity.getType() == ProductType.BOOK
                    && normalizeText(entity.getCategory()).equals(normalizeText(category))) {
                bookCount--;
            }
            if (bookCount < request.bookCount())
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "A categoria não possui livros suficientes para a quantidade escolhida no kit");
        }
        entity.setTitle(request.type() == ProductType.KIT ? "Kit " + category : request.title().trim());
        entity.setAuthor(request.type() == ProductType.BOOK ? request.author().trim() : null);
        entity.setDescription(request.description() == null ? null : request.description().trim());
        entity.setCategory(category);
        entity.setType(request.type());
        entity.setBookCount(request.type() == ProductType.KIT ? request.bookCount() : null);
        entity.setPrice(request.price());
        entity.setCurrency("BRL");
        entity.setImage(request.image() == null || request.image().isBlank() ? null : request.image().trim());
        return entity;
    }

    private void validateBookCategoryChange(ProductEntity entity, ProductType newType, String newCategory) {
        if (entity.getId() == null || entity.getType() != ProductType.BOOK) return;
        boolean leavesCurrentCategory = newType != ProductType.BOOK
                || !normalizeText(entity.getCategory()).equals(normalizeText(newCategory));
        int requiredBooks = findByCategoryAndType(entity.getCategory(), ProductType.KIT).stream()
                .map(ProductEntity::getBookCount)
                .filter(java.util.Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0);
        long remainingBooks = countByCategoryAndType(entity.getCategory(), ProductType.BOOK) - 1;
        if (leavesCurrentCategory && requiredBooks > 0 && remainingBooks < requiredBooks) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A categoria precisa manter livros suficientes para os kits ativos");
        }
    }

    private List<ProductEntity> findByCategoryAndType(String category, ProductType type) {
        return repository.findAll(categoryAndType(category, type), Sort.by("id").ascending());
    }

    private long countByCategoryAndType(String category, ProductType type) {
        return repository.count(categoryAndType(category, type));
    }

    private Specification<ProductEntity> categoryAndType(String category, ProductType type) {
        String normalizedCategory = normalizeText(category);
        return (root, query, cb) -> cb.and(
                cb.equal(normalizeDatabaseText(cb, root.<String>get("category")), normalizedCategory),
                cb.equal(root.get("type"), type));
    }

    private static Expression<String> normalizeDatabaseText(CriteriaBuilder cb, Expression<String> expression) {
        return cb.function("translate", String.class, cb.lower(expression),
                cb.literal(ACCENTED_CHARACTERS), cb.literal(UNACCENTED_CHARACTERS));
    }

    private static String normalizeText(String value) {
        String decomposed = Normalizer.normalize(value.trim(), Normalizer.Form.NFD);
        return Normalizer.normalize(decomposed.replaceAll("\\p{M}+", ""), Normalizer.Form.NFC)
                .toLowerCase(Locale.ROOT);
    }
}
