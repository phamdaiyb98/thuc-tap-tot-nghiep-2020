package haui.mobileshop.global.repository;

import haui.mobileshop.global.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("select b.fullName, b.email,b.phone,b.addressReceive,b.createdDate,b.status from Bill b " +
            "group by b.fullName, b.email,b.phone,b.addressReceive,b.createdDate,b.status")
    List<Object[]> findAllGroupBy();

    @Query(value = "select b.[full_name], b.email,b.phone,b.[address_receive],b.[created_date],b.status " +
            "from [MobileShop].[dbo].[bill] b group by b.[full_name], b.email,b.phone,b.[address_receive],b.[created_date],b.status " +
            "having b.[created_date] = GETDATE()", nativeQuery = true)
    List<Object[]> findAllGroupByToday();

    @Transactional
    @Modifying
    @Query("update Bill b set b.status = 1 where b.status = 0 and b.phone = :phone and b.fullName = :fullName " +
            "and b.email = :email and b.createdDate = :createdDate and b.addressReceive = :addressReceive")
    void receiveBill(@Param("fullName") String fullName, @Param("email") String email, @Param("phone") String phone,
                     @Param("addressReceive") String addressReceive, @Param("createdDate") Date createDate);

    @Transactional
    @Modifying
    @Query("delete from Bill b where b.status = 0 and b.phone = :phone and b.fullName = :fullName " +
            "and b.email = :email and b.createdDate = :createdDate and b.addressReceive = :addressReceive")
    void removeBill(@Param("fullName") String fullName, @Param("email") String email, @Param("phone") String phone,
                    @Param("addressReceive") String addressReceive, @Param("createdDate") Date createDate);
}
