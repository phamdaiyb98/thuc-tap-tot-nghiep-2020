package haui.mobileshop.core.service;

import haui.mobileshop.global.dto.BillDTO;
import haui.mobileshop.global.entity.Bill;
import haui.mobileshop.global.entity.Product;
import haui.mobileshop.global.repository.BillRepository;
import haui.mobileshop.global.repository.ProductRepository;
import haui.mobileshop.global.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<BillDTO> findAll() {
        List<BillDTO> billDTOList = new ArrayList<>();
        billRepository.findAllGroupBy().forEach(objects -> {
            billDTOList.add(new BillDTO(String.valueOf(objects[0]), String.valueOf(objects[1]),
                    String.valueOf(objects[2]), String.valueOf(objects[3]), (Date) objects[4], (int) objects[5]));
        });
        return billDTOList;
    }

    @Override
    public int countBillPerToday() {
        return billRepository.findAllGroupByToday().size();
    }

    @Override
    public void save(Bill bill) {
        billRepository.save(bill);
    }

    @Override
    public void receiveBill(String fullName, String email, String phone, String addressReceive, Date createDate) {
        billRepository.receiveBill(fullName, email, phone, addressReceive, createDate);
    }

    @Override
    public void removeBill(String fullName, String email, String phone, String addressReceive, Date createDate) {
        List<Product> products = productRepository.findAllSameBill(fullName, email, phone, addressReceive, createDate);
        productRepository.deleteAll(products);// xoa cac san pham lien quan
    }
}
