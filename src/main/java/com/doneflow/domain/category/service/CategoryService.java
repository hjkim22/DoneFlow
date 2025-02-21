package com.doneflow.domain.category.service;

import com.doneflow.common.exception.CustomException;
import com.doneflow.common.exception.ErrorCode;
import com.doneflow.domain.category.dto.CategoryRequestDto;
import com.doneflow.domain.category.entity.Category;
import com.doneflow.domain.category.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  // 카테고리 생성
  public Category createCategory(CategoryRequestDto requestDto) {
    if (categoryRepository.findByName(requestDto.getName()).isPresent()) {
      throw new CustomException(ErrorCode.CATEGORY_ALREADY_EXISTS);
    }
    Category category = Category.builder()
        .name(requestDto.getName())
        .build();
    return categoryRepository.save(category);
  }

  // 전체 카테고리 목록 조회
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  // 카테고리 조회 (ID)
  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
  }

  // 카테고리 조회 (카테고리명)
  public Category getCategoryByName(String name) {
    return categoryRepository.findByName(name)
        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
  }

  // 카테고리 수정
  public Category updateCategory(Long id, String name) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

    if (!category.getName().equals(name) && categoryRepository.findByName(name).isPresent()) {
      throw new CustomException(ErrorCode.CATEGORY_ALREADY_EXISTS);
    }

    category.updateName(name);
    return categoryRepository.save(category);
  }

  // 카테고리 삭제
  public void deleteCategory(Long id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

    if ("미분류".equals(category.getName())) {
      throw new CustomException(ErrorCode.CANNOT_DELETE_DEFAULT_CATEGORY);
    }

    categoryRepository.deleteById(category.getId());
  }
}
