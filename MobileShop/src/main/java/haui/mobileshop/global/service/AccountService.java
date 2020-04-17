package haui.mobileshop.global.service;

import haui.mobileshop.global.entity.Account;

public interface AccountService {

    void create(Account account);

    void update(Account account);

    void delete(Long accountId);

    Account findUserByUsernameAndEncryptedPassword(String username, String password);

    Account findCustomerByUsernameAndEncryptedPassword(String username, String password);

    Account findSocialAccountInfo(String id);

    int countTotalUser();

    void changeAdminPassword(String password);

    void changeUserPassword(Long accountId, String password);

    boolean checkAdminPassword(String oldPassword);
}
