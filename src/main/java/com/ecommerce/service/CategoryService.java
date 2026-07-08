package com.ecommerce.service;
import com.ecommerce.dto.request.CategoryRequest;import com.ecommerce.dto.response.CategoryResponse;import com.ecommerce.exception.*;import com.ecommerce.model.Category;import com.ecommerce.repository.CategoryRepository;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Service;import java.text.Normalizer;import java.util.*;import java.util.stream.Collectors;
@Service @RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private String toSlug(String name){return Normalizer.normalize(name,Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9\\s]","").trim().toLowerCase().replaceAll("\\s+","-");}
    private CategoryResponse toResponse(Category c){
        return CategoryResponse.builder().id(c.getId()).name(c.getName()).slug(c.getSlug()).description(c.getDescription()).parentCategoryId(c.getParentCategory()!=null?c.getParentCategory().getId():null).parentCategoryName(c.getParentCategory()!=null?c.getParentCategory().getName():null).subCategories(c.getSubCategories().stream().map(this::toResponse).collect(Collectors.toList())).createdAt(c.getCreatedAt()).build();
    }
    public List<CategoryResponse> getAllCategories(){return categoryRepository.findByParentCategoryIsNull().stream().map(this::toResponse).collect(Collectors.toList());}
    public CategoryResponse getCategoryById(Long id){return toResponse(categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not found")));}
    public CategoryResponse createCategory(CategoryRequest req){
        Category cat=Category.builder().name(req.getName()).slug(toSlug(req.getName())).description(req.getDescription()).build();
        if(req.getParentCategoryId()!=null) cat.setParentCategory(categoryRepository.findById(req.getParentCategoryId()).orElseThrow(()->new ResourceNotFoundException("Parent category not found")));
        return toResponse(categoryRepository.save(cat));
    }
    public CategoryResponse updateCategory(Long id,CategoryRequest req){
        Category cat=categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not found"));
        cat.setName(req.getName());cat.setSlug(toSlug(req.getName()));cat.setDescription(req.getDescription());
        if(req.getParentCategoryId()!=null) cat.setParentCategory(categoryRepository.findById(req.getParentCategoryId()).orElseThrow(()->new ResourceNotFoundException("Parent not found")));
        else cat.setParentCategory(null);
        return toResponse(categoryRepository.save(cat));
    }
    public void deleteCategory(Long id){if(!categoryRepository.existsById(id)) throw new ResourceNotFoundException("Category not found");categoryRepository.deleteById(id);}
}
