package msadaka.repositories;


import msadaka.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findUserByUsername(String username);
    User findByEmailOrUsername(String email,String username);
}