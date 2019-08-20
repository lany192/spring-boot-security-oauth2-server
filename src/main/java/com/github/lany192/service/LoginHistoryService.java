package com.github.lany192.service;

import com.github.lany192.domain.JsonObjects;
import com.github.lany192.domain.LoginHistory;

public interface LoginHistoryService extends CommonServiceInterface<LoginHistory> {
    JsonObjects<LoginHistory> listByUsername(String username, int pageNum,
                                             int pageSize,
                                             String sortField,
                                             String sortOrder);

    void asyncCreate(LoginHistory loginHistory);

}
