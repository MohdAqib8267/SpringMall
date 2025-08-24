package com.ecomm.product_service.productRepository;

import com.ecomm.product_service.modal.ShoppingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShoppingRecordRepository extends JpaRepository<ShoppingRecord,Integer> {

    void deleteAllByOrderId(UUID orderId);
}
