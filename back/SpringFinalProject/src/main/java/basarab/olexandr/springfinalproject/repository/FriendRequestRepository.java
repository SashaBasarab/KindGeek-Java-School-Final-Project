package basarab.olexandr.springfinalproject.repository;

import basarab.olexandr.springfinalproject.entity.FriendRequest;
import basarab.olexandr.springfinalproject.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> , JpaSpecificationExecutor<FriendRequest> {

    List<FriendRequest> findAllByUserSenderIdOrUserReceiverId(Long userSenderId, Long userReceiverId);

    @Query("select fr from FriendRequest fr where (fr.userSender = :user and fr.userReceiver = :friend) " +
            "or (fr.userReceiver = :user and fr.userSender = :friend)")
    Optional<FriendRequest> findFriendRequestByUserSenderIdAndUserReceiverId(@Param("user") User user, @Param("friend") User friend);

//    @Query("select fr.id from FriendRequest fr where fr.userReceiver = :user and fr.userSender = :friend")
//    Long acceptOrDenyFriendRequest(@Param("user") User user, @Param("friend") User friend);

    @Query("select fr from FriendRequest fr join fr.userSender us join fr.userReceiver ur where ur.id = :receiverId and us.id = :friendId")
    Optional<FriendRequest> findFriendRequestByReceiverIdAndSenderId(@Param("receiverId") Long receiverId, @Param("friendId") Long friendId);

    //?
    @Query("select fr from FriendRequest fr where fr.accepted = true and (fr.userReceiver.id = ?1 or fr.userSender.id = ?1) " +
            "and (fr.userReceiver.username like ?2 or fr.userSender.username like ?2)")
    List<FriendRequest> findAllByFriendUsername(@Param("userId") Long id ,@Param("friendUsername") String friendUsername, Pageable pageable);

}
