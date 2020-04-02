package haui.mobileshop.global.repository;

import haui.mobileshop.global.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select e from Account e where e.username = :username and e.password = :password and e.role = true")
    Account findUserByUsernameAndEncryptedPassword(@Param("username") String username, @Param("password") String password);

    @Query("select e from Account e where e.username = :username and e.password = :password and e.role = false")
    Account findCustomerByUsernameAndEncryptedPassword(@Param("username") String username, @Param("password") String password);

    @Query("select a from Account a where a.role = false")
    List<Account> findAllUser();

    @Transactional
    @Modifying
    @Query("update Account a set a.password = :password where a.role = true")
    void changeAdminPassword(@Param("password") String password);

    @Transactional
    @Modifying
    @Query("update Account a set a.password = :password where a.accountId = :accountId")
    void changeUserPassword(@Param("password") String password, @Param("accountId") Long accountId);

    @Query("select e.password from Account e where  e.role = true")
    String getAdminPassword();
}
