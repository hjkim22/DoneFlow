package com.doneflow.domain.category.controller;

import com.doneflow.domain.category.dto.CategoryRequestDto;
import com.doneflow.domain.category.entity.Category;
import com.doneflow.domain.category.repository.CategoryRepository;
import com.doneflow.domain.category.service.CategoryService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

  private final CategoryService categoryService;
  private final CategoryRepository categoryRepository;

  // 카테고리 생성
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Category createCategory(@Valid @RequestBody CategoryRequestDto requestDto) {
    return categoryService.createCategory(requestDto);
  }

  // 전체 카테고리 목록 조회
  @GetMapping
  public List<Category> getAllCategories() {
    return categoryService.getAllCategories();
  }

  // 카테고리 조회 (ID)
  @GetMapping("/{id}")
  public Category getCategoryById(@PathVariable("id") Long id) {
    return categoryService.getCategoryById(id);
  }

  // 카테고리 조회 (카테고리명)
  @GetMapping("/search")
  public Category getCategoryByName(@RequestParam(name = "name") String name) {
    return categoryService.getCategoryByName(name);
  }

  // 카테고리 수정
  @PutMapping("/{id}")
  public Category updateCategory(
      @PathVariable("id") Long id, @Valid @RequestBody CategoryRequestDto requestDto) {
    return categoryService.updateCategory(id, requestDto.getName());
  }

  // 카테고리 삭제
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCategory(@PathVariable("id") Long id) {
    categoryService.deleteCategory(id);
  }

  @PostConstruct
  public void initDefaultCategory() {
    if (categoryRepository.findByName("미분류").isEmpty()) {
      categoryRepository.save(new Category(null, "미분류"));
    }
  }
}
