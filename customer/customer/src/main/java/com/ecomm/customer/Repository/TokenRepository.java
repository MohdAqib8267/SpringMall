package com.ecomm.customer.Repository;

import com.ecomm.customer.Modal.Token;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {
    @Query("""
            select t from Token t
            inner join Customer c
            on t.customer.id = c.id
            where t.customer.id=:id and (t.revoked=false and t.expired=false)
            """)
    List<Token> findAllValidTokensByUser(int id);

    Optional<Token> findByAccessToken(String token);
}
