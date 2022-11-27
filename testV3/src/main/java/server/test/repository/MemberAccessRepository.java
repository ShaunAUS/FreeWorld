package server.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.test.auth.entity.MemberAccess;

@Repository
public interface MemberAccessRepository extends JpaRepository<MemberAccess, Long> {

}

