package br.com.crewcontrolapi.repositories;

import br.com.crewcontrolapi.domain.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role != 'ADMINISTRATOR'")
    Page<User> findNonAdminUsers(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role = 'COLLABORATOR' AND u.team.id = :teamId")
    Page<User> findCollaboratorsByTeamId(@Param("teamId") Long teamId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Page<User> findByUserId(@Param("userId") Long userId, Pageable pageable);

}