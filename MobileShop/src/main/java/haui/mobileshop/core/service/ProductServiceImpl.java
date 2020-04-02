package haui.mobileshop.core.service;

import haui.mobileshop.apriori.entity.ConfidenceItemSet;
import haui.mobileshop.apriori.entity.RawData;
import haui.mobileshop.global.entity.Apriori;
import haui.mobileshop.global.entity.Product;
import haui.mobileshop.global.repository.ProductRepository;
import haui.mobileshop.global.service.AprioriService;
import haui.mobileshop.global.service.CartService;
import haui.mobileshop.global.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    // methods
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AprioriService aprioriService;

    @Autowired
    private CartService cartService;

    private haui.mobileshop.apriori.Apriori apriori;

    //properties
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAll(String type, String manufacturer, String os) {
        if ("".equals(type)) {
            if ("".equals(manufacturer)) {
                if ("".equals(os)) {
                    return productRepository.findAll();
                } else { // filter os
                    return productRepository.findAllOs(os);
                }
            } else { // filter manufacturer
                if ("".equals(os)) {
                    return productRepository.findAllManufacturer(manufacturer);
                } else { // filter os
                    return productRepository.findAllOsAndManufacturer(manufacturer, os);
                }
            }
        } else { // filter type
            if ("".equals(manufacturer)) {
                if ("".equals(os)) {
                    return productRepository.findAllType(type);
                } else { // filter os
                    return productRepository.findAllOsAndType(type, os);
                }
            } else { // filter manufacturer
                if ("".equals(os)) {
                    return productRepository.findAllManufacturerAndType(type, manufacturer);
                } else { // filter os
                    return productRepository.findAll(type, manufacturer, os);
                }
            }
        }
    }

    @Override
    public List<Product> findAllSameBill(String fullName, String email, String phone, String addressReceive, Date createDate) {
        return productRepository.findAllSameBill(fullName, email, phone, addressReceive, createDate);
    }

    @Override
    public Product findOne(Long productId) {
        return productRepository.getOne(productId);
    }

    @Override
    public Set<String> getAllType(List<Product> products) {
        Set<String> result = new HashSet<>();
        products.forEach(product -> {
            if (!"".equals(product.getType()) && product.getType() != null)
                result.add(product.getType());
        });
        return result;
    }

    @Override
    public Set<String> getAllManufacturer(List<Product> products) {
        Set<String> result = new HashSet<>();
        products.forEach(product -> {
            if (!"".equals(product.getManufacturer()) && product.getManufacturer() != null)
                result.add(product.getManufacturer());
        });
        return result;
    }

    @Override
    public Set<String> getAllOs(List<Product> products) {
        Set<String> result = new HashSet<>();
        products.forEach(product -> {
            if (!"".equals(product.getOs()) && product.getOs() != null)
                result.add(product.getOs());
        });
        return result;
    }

    @Override
    public void insert(List<Product> products) {
        products.forEach(product -> {
            productRepository.save(product);
        });
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Page<Product> getProduct(Pageable pageable, String type, String manufacturer, String os) {
        if ("".equals(type)) {
            if ("".equals(manufacturer)) {
                if ("".equals(os)) {
                    return productRepository.findAll(pageable);
                } else { // filter os
                    return productRepository.findAllOsPage(pageable, os);
                }
            } else { // filter manufacturer
                if ("".equals(os)) {
                    return productRepository.findAllManufacturerPage(pageable, manufacturer);
                } else { // filter os
                    return productRepository.findAllOsAndManufacturerPage(pageable, manufacturer, os);
                }
            }
        } else { // filter type
            if ("".equals(manufacturer)) {
                if ("".equals(os)) {
                    return productRepository.findAllTypePage(pageable, type);
                } else { // filter os
                    return productRepository.findAllOsAndTypePage(pageable, type, os);
                }
            } else { // filter manufacturer
                if ("".equals(os)) {
                    return productRepository.findAllManufacturerAndTypePage(pageable, type, manufacturer);
                } else { // filter os
                    return productRepository.findAllPage(pageable, type, manufacturer, os);
                }
            }
        }
    }

    @Override
    public List<Product> getRelativeProduct(Long productId) {
        Apriori apriori = aprioriService.getApriori();
        Product product = findOne(productId);
        Set<ConfidenceItemSet> setConfidence = getApriori(apriori.getMinSupport(), apriori.getMinConfidence(), product.getName());
        List<Product> relativeProducts = new ArrayList<>();
        setConfidence.forEach(confidenceItemSet -> {
            confidenceItemSet.getItemSetY().forEach(s -> {
                Product relativeProduct = getProductByName(s);
                if (!filterDuplicateProduct(relativeProducts, relativeProduct)) {
                    relativeProducts.add(relativeProduct);
                }
            });
        });
        return relativeProducts;
    }

    private boolean filterDuplicateProduct(List<Product> relativeProducts, Product relativeProduct) {
        return relativeProducts.contains(relativeProduct);
    }

    private Product getProductByName(String name) {
        return productRepository.getProductByName(name);
    }

    private Set<ConfidenceItemSet> getApriori(float minSupp, float minConf, String productName) {
        Set<Long> listIdTransaction = cartService.getAllIdTransaction();
        List<RawData> listRawData = new ArrayList<>();
        for (Long idTransaction : listIdTransaction) {
            RawData rawData = new RawData();
            rawData.setIdTracsaction(idTransaction);
            rawData.setItems(cartService.getAllItemSet(idTransaction));
            listRawData.add(rawData);
        }
        apriori = new haui.mobileshop.apriori.Apriori();
        apriori.setData(minSupp, minConf, listIdTransaction.size(), listRawData);
        Set<String> itemSetX = new HashSet<>();
        itemSetX.add(productName);
        return apriori.getAllConfidenceItemSetFiltered(itemSetX);
    }

}
