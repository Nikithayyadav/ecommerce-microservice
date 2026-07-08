package com.ecommerce.service;
import com.ecommerce.dto.request.ProductRequest;import com.ecommerce.dto.response.ProductResponse;import com.ecommerce.exception.*;import com.ecommerce.model.*;import com.ecommerce.repository.*;import lombok.RequiredArgsConstructor;import org.springframework.data.domain.*;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import java.util.List;import java.util.stream.Collectors;
@Service @RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository; private final CategoryRepository categoryRepository; private final ReviewRepository reviewRepository;
    public ProductResponse toResponse(Product p){
        Double avg=reviewRepository.getAverageRatingByProductId(p.getId());
        Long count=reviewRepository.getReviewCountByProductId(p.getId());
        List<String> imgs=p.getImages().stream().sorted((a,b)->Boolean.compare(b.getIsPrimary(),a.getIsPrimary())).map(ProductImage::getImageUrl).collect(Collectors.toList());
        return ProductResponse.builder().id(p.getId()).name(p.getName()).description(p.getDescription()).price(p.getPrice()).discountPrice(p.getDiscountPrice()).stock(p.getStock()).categoryId(p.getCategory()!=null?p.getCategory().getId():null).categoryName(p.getCategory()!=null?p.getCategory().getName():null).imageUrls(imgs).averageRating(avg!=null?avg:0.0).reviewCount(count!=null?count:0L).createdAt(p.getCreatedAt()).build();
    }
    public Page<ProductResponse> getAllProducts(Pageable pageable){return productRepository.findAll(pageable).map(this::toResponse);}
    public ProductResponse getProductById(Long id){return toResponse(productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found")));}
    public Page<ProductResponse> getProductsByCategory(Long catId,Pageable pageable){return productRepository.findByCategoryId(catId,pageable).map(this::toResponse);}
    @Transactional public ProductResponse createProduct(ProductRequest req){
        Product p=Product.builder().name(req.getName()).description(req.getDescription()).price(req.getPrice()).discountPrice(req.getDiscountPrice()).stock(req.getStock()).build();
        if(req.getCategoryId()!=null) p.setCategory(categoryRepository.findById(req.getCategoryId()).orElseThrow(()->new ResourceNotFoundException("Category not found")));
        if(req.getImageUrls()!=null) req.getImageUrls().forEach(url->{boolean primary=p.getImages().isEmpty();p.getImages().add(ProductImage.builder().product(p).imageUrl(url).isPrimary(primary).build());});
        return toResponse(productRepository.save(p));
    }
    @Transactional public ProductResponse updateProduct(Long id,ProductRequest req){
        Product p=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found"));
        p.setName(req.getName());p.setDescription(req.getDescription());p.setPrice(req.getPrice());p.setDiscountPrice(req.getDiscountPrice());p.setStock(req.getStock());
        if(req.getCategoryId()!=null) p.setCategory(categoryRepository.findById(req.getCategoryId()).orElseThrow(()->new ResourceNotFoundException("Category not found")));
        if(req.getImageUrls()!=null){p.getImages().clear();req.getImageUrls().forEach(url->{boolean primary=p.getImages().isEmpty();p.getImages().add(ProductImage.builder().product(p).imageUrl(url).isPrimary(primary).build());});}
        return toResponse(productRepository.save(p));
    }
    public void deleteProduct(Long id){if(!productRepository.existsById(id)) throw new ResourceNotFoundException("Product not found");productRepository.deleteById(id);}
}
