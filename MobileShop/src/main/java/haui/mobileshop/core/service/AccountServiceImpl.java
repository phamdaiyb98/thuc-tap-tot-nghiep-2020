package haui.mobileshop.core.service;

import haui.mobileshop.global.entity.Account;
import haui.mobileshop.global.repository.AccountRepository;
import haui.mobileshop.global.service.AccountService;
import haui.mobileshop.global.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void create(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void update(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void delete(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    @Override
    public Account findUserByUsernameAndEncryptedPassword(String username, String password) {
        return accountRepository.findUserByUsernameAndEncryptedPassword(username, MD5.encode(password));
    }

    @Override
    public Account findCustomerByUsernameAndEncryptedPassword(String username, String password) {
        return accountRepository.findCustomerByUsernameAndEncryptedPassword(username, MD5.encode(password));
    }

    @Override
    public Account findSocialAccountInfo(String id) {
        return accountRepository.getSocialAccountInfo(id);
    }

    @Override
    public int countTotalUser() {
        return accountRepository.findAllUser().size();
    }

    @Override
    public void changeAdminPassword(String password) {
        accountRepository.changeAdminPassword(password);
    }

    @Override
    public void changeUserPassword(Long accountId, String password) {
        accountRepository.changeUserPassword(password, accountId);
    }

    @Override
    public boolean checkAdminPassword(String oldPassword) {
        String adminPass = accountRepository.getAdminPassword();
        return adminPass.equals(oldPassword);
    }

}
