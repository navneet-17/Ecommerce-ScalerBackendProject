package dev.navneet.productservice.repositories;

public interface CustomQueries {
    String FIND_ALL_BY_TITLE = "select * from product join product_orders " +
            "on product.id = product_orders.product_id where title = :naman";

    String GET_ALL_PRODUCT_BY_CATEGORY = "Select p.* from products p left join categories c on p.category_id = c.id where c.name=:categoryName";
    String GET_ALL_PRODUCT_CATEGORY = "Select distinct c.name from products p left join categories c on p.category_id = c.id";

    String FIND_ALL_PRODUCT = "SELECT DISTINCT p FROM products p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.price";
}