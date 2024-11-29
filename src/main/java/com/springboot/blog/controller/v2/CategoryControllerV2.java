package com.springboot.blog.controller.v2;

import com.springboot.blog.controller.CategoryController;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category Management", description = "Endpoints for managing categories (v2 API)")
@RestController
@RequestMapping("/api/v2/categories")
public class CategoryControllerV2 {

    private final CategoryController categoryControllerV1;
    private final CategoryService categoryService;

    public CategoryControllerV2(CategoryController categoryControllerV1, CategoryService categoryService) {
        this.categoryControllerV1 = categoryControllerV1;
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get category by ID", description = "Fetches a category by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        return categoryControllerV1.getCategory(id);
    }

    @Operation(summary = "Add a new category", description = "Creates a new category in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "403", description = "User does not have the required permissions")
    })

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryControllerV1.addCategory(categoryDto);
    }

    @Operation(summary = "Partially update a category", description = "Updates specified fields of a category.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "403", description = "User does not have the required permissions")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> patchCategory(@RequestBody CategoryDto categoryDto,
                                                     @PathVariable Long id) {
        CategoryDto updatedCategory = categoryService.partialUpdateCategory(categoryDto, id);
        return ResponseEntity.ok(updatedCategory);
    }

    @Operation(summary = "Update category by ID", description = "Replaces an existing category with a new one.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "403", description = "User does not have the required permissions")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,
                                                      @PathVariable Long id) {
        return categoryControllerV1.updateCategory(categoryDto, id);
    }

    @Operation(summary = "Get all categories", description = "Fetches all categories in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return categoryControllerV1.getCategories();
    }

    @Operation(summary = "Delete category by ID", description = "Deletes a category from the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "403", description = "User does not have the required permissions")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        return categoryControllerV1.deleteCategory(id);
    }
}
