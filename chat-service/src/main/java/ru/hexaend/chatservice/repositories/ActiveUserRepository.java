//package ru.hexaend.chatservice.repositories;
//
//
//import org.springframework.data.repository.CrudRepository;
//import ru.hexaend.chatservice.models.ActiveUser;
//
//import java.util.Optional;
//
//public interface ActiveUserRepository extends CrudRepository<ActiveUser, String> {
//
//    Optional<ActiveUser> findByUserId(String userId);
//
//    void deleteByUserId(String userId);
//
//    void deleteAllByUserId(String userId);
//}
