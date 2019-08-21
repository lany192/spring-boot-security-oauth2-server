package com.github.lany192.service.impl;

import com.github.dozermapper.core.Mapper;
import com.github.lany192.entity.LoginHistory;
import com.github.lany192.repository.LoginHistoryRepository;
import com.github.lany192.exception.AlreadyExistsException;
import com.github.lany192.domain.JsonObjects;
import com.github.lany192.service.LoginHistoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginHistoryServiceImpl implements LoginHistoryService {
    @Autowired
    LoginHistoryRepository loginHistoryRepository;

    @Autowired
    Mapper dozerMapper;

    @Override
    public JsonObjects<com.github.lany192.domain.LoginHistory> listByUsername(String username, int pageNum, int pageSize, String sortField, String sortOrder) {
        JsonObjects<com.github.lany192.domain.LoginHistory> jsonObjects = new JsonObjects<>();
        Sort sort;
        if (StringUtils.equalsIgnoreCase(sortOrder, "asc")) {
            sort = new Sort(Sort.Direction.ASC, sortField);
        } else {
            sort = new Sort(Sort.Direction.DESC, sortField);
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<LoginHistory> page = loginHistoryRepository.findByUsername(username, pageable);
        if (page.getContent() != null && page.getContent().size() > 0) {
            jsonObjects.setRecordsTotal(page.getTotalElements());
            jsonObjects.setRecordsFiltered(page.getTotalElements());
            page.getContent().forEach(u -> jsonObjects.getData().add(dozerMapper.map(u, com.github.lany192.domain.LoginHistory.class)));
        }
        return jsonObjects;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void asyncCreate(com.github.lany192.domain.LoginHistory loginHistory) throws AlreadyExistsException {
        LoginHistory entity = dozerMapper.map(loginHistory, LoginHistory.class);
        loginHistoryRepository.save(entity);
    }
}
