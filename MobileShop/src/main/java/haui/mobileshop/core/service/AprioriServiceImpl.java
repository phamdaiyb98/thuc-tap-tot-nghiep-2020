package haui.mobileshop.core.service;

import haui.mobileshop.global.entity.Apriori;
import haui.mobileshop.global.repository.AprioriRepository;
import haui.mobileshop.global.service.AprioriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AprioriServiceImpl implements AprioriService {

    @Autowired
    private AprioriRepository aprioriRepository;

    @Override
    public Apriori getApriori() {
        if (aprioriRepository.findAll().isEmpty()) {
            return null;
        }
        return aprioriRepository.findAll().get(0);
    }

    @Override
    public void saveApriori(Apriori apriori) {
        aprioriRepository.save(apriori);
    }
}
