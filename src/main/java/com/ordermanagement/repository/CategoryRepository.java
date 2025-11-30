package com.ordermanagement.repository;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    boolean existsByCategoryNameAndStatus(String categoryName, Enum.Status active);

    @Query(value = "SELECT * FROM categories WHERE category_name = ?1 AND status = 1", nativeQuery = true)
    Optional<Category> findByCategoryNameAndStatus(String categoryName, Enum.Status active);

    @Query(value = "SELECT * FROM categories WHERE status = 1", nativeQuery = true)
    Page<Category> fetchAllCategories(String search, Pageable pageable);

    @Query(value = "SELECT * FROM categories WHERE status = 1", nativeQuery = true)
    List<Category> getAllCategories();

    Optional<Category> findByCategoryNameAndShipperOrganization(String categoryName, Organization shipperOrganization);


    Optional<Category> findByCategoryNameAndShipperOrganizationAndParentCategory(String categoryName, Organization shipperOrganization, Category parentCategory);

    @Query(
            value = "SELECT * FROM categories " +
                    "WHERE id <> :categoryId " +
                    "AND category_name = :categoryName " +
                    "AND status = 1",
            nativeQuery = true
    )
    Optional<Category> findByCategoryNameAndStatusExcludingId(
            @Param("categoryName") String categoryName,
            @Param("categoryId") Integer categoryId);


    @Query(
            value = "SELECT * FROM categories " +
                    "WHERE parent_id = :id AND status = 1",
            nativeQuery = true
    )
    Category findByParentCategory(@Param("id") Integer id);

}
