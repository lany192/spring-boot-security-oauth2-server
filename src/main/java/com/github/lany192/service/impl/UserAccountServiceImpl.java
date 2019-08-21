package com.github.lany192.service.impl;

import com.github.dozermapper.core.Mapper;
import com.github.lany192.exception.EntityNotFoundException;
import com.github.lany192.entity.RoleEntity;
import com.github.lany192.entity.Account;
import com.github.lany192.repository.RoleRepository;
import com.github.lany192.repository.AccountRepository;
import com.github.lany192.utils.DateUtil;
import com.github.lany192.exception.AlreadyExistsException;
import com.github.lany192.domain.JsonObjects;
import com.github.lany192.domain.UserAccount;
import com.github.lany192.service.UserAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    Mapper dozerMapper;

    @Value("${signin.failure.max:5}")
    private int failureMax;

    @Override
    public JsonObjects<UserAccount> listByUsername(String username, int pageNum, int pageSize, String sortField, String sortOrder) {
        JsonObjects<UserAccount> jsonObjects = new JsonObjects<>();
        Sort sort;
        if (StringUtils.equalsIgnoreCase(sortOrder, "asc")) {
            sort = new Sort(Sort.Direction.ASC, sortField);
        } else {
            sort = new Sort(Sort.Direction.DESC, sortField);
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<Account> page;
        if (StringUtils.isBlank(username)) {
            page = accountRepository.findAll(pageable);
        } else {
            page = accountRepository.findByUsernameLike(username + "%", pageable);
        }
        if (page.getContent() != null && page.getContent().size() > 0) {
            jsonObjects.setRecordsTotal(page.getTotalElements());
            jsonObjects.setRecordsFiltered(page.getTotalElements());
            page.getContent().forEach(u -> jsonObjects.getData().add(dozerMapper.map(u, UserAccount.class)));
        }
        return jsonObjects;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAccount create(UserAccount userAccount) throws AlreadyExistsException {
        Account exist = accountRepository.findByUsername(userAccount.getUsername());
        if (exist != null) {
            throw new AlreadyExistsException(userAccount.getUsername() + " already exists!");
        }
        Account account = dozerMapper.map(userAccount, Account.class);
        account.getRoles().clear();
        if (userAccount.getRoles() != null && userAccount.getRoles().size() > 0) {
            userAccount.getRoles().forEach(e -> {
                RoleEntity roleEntity = roleRepository.findByRoleName(e.getRoleName());
                if (roleEntity != null) {
                    account.getRoles().add(roleEntity);
                }
            });
        }
        accountRepository.save(account);
        return dozerMapper.map(account, UserAccount.class);
    }

    @Override
    public UserAccount retrieveById(long id) throws EntityNotFoundException {
        Optional<Account> entityOptional = accountRepository.findById(id);
        return dozerMapper.map(entityOptional.orElseThrow(EntityNotFoundException::new), UserAccount.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAccount updateById(UserAccount userAccount) throws EntityNotFoundException {
        Optional<Account> entityOptional = accountRepository.findById(Long.parseLong(userAccount.getId()));
        Account e = entityOptional.orElseThrow(EntityNotFoundException::new);
        if (StringUtils.isNotEmpty(userAccount.getPassword())) {
            e.setPassword(userAccount.getPassword());
        }
        e.setNickName(userAccount.getNickName());
        e.setBirthday(userAccount.getBirthday());
        e.setMobile(userAccount.getMobile());
        e.setProvince(userAccount.getProvince());
        e.setCity(userAccount.getCity());
        e.setAddress(userAccount.getAddress());
        e.setAvatarUrl(userAccount.getAvatarUrl());
        e.setEmail(userAccount.getEmail());

        accountRepository.save(e);
        return dozerMapper.map(e, UserAccount.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecordStatus(long id, int recordStatus) {
        Optional<Account> entityOptional = accountRepository.findById(id);
        Account e = entityOptional.orElseThrow(EntityNotFoundException::new);
        e.setRecordStatus(recordStatus);
        accountRepository.save(e);
    }

    @Override
    public UserAccount findByUsername(String username) throws EntityNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account != null) {
            return dozerMapper.map(account, UserAccount.class);
        } else {
            throw new EntityNotFoundException(username + " not found!");
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void loginSuccess(String username) throws EntityNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account != null) {
            account.setFailureCount(0);
            account.setFailureTime(null);
            accountRepository.save(account);
        } else {
            throw new EntityNotFoundException(username + " not found!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void loginFailure(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account != null) {
            if (account.getFailureTime() == null) {
                account.setFailureCount(1);
            } else {
                if (DateUtil.beforeToday(account.getFailureTime())) {
                    account.setFailureCount(0);
                } else {
                    account.setFailureCount(account.getFailureCount() + 1);
                }
            }
            account.setFailureTime(new Date());
            if (account.getFailureCount() >= failureMax && account.getRecordStatus() >= 0) {
                account.setRecordStatus(-1);
            }
            accountRepository.save(account);
        }
    }
}
