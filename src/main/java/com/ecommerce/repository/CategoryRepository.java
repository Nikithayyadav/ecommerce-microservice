package com.ecommerce.repository;
import com.ecommerce.model.Category;import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;
public interface CategoryRepository extends JpaRepository<Category,Long> { List<Category> findByParentCategoryIsNull(); Optional<Category> findBySlug(String slug); }
