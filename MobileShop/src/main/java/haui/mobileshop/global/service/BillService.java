package haui.mobileshop.global.service;

import haui.mobileshop.global.dto.BillDTO;
import haui.mobileshop.global.entity.Bill;

import java.util.Date;
import java.util.List;

public interface BillService {

    List<BillDTO> findAll();

    int countBillPerToday();

    void save(Bill bill);

    void receiveBill(String fullName, String email, String phone, String addressReceive, Date createDate);

    void removeBill(String fullName, String email, String phone, String addressReceive, Date createDate);

}
