package com.github.lany192.service.impl;

import com.github.dozermapper.core.Mapper;
import com.github.lany192.exception.EntityNotFoundException;
import com.github.lany192.entity.OauthClient;
import com.github.lany192.repository.OauthClientRepository;
import com.github.lany192.exception.AlreadyExistsException;
import com.github.lany192.domain.JsonObjects;
import com.github.lany192.service.OauthClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OauthClientServiceImpl implements OauthClientService {

    @Autowired
    OauthClientRepository oauthClientRepository;

    @Autowired
    Mapper dozerMapper;

    @Override
    public com.github.lany192.domain.OauthClient findByClientId(String clientId) {
        OauthClient oauthClient = oauthClientRepository.findByClientId(clientId);
        if (oauthClient != null) {
            return dozerMapper.map(oauthClient, com.github.lany192.domain.OauthClient.class);
        } else {
            return null;
        }
    }

    @Override
    public JsonObjects<com.github.lany192.domain.OauthClient> list(int pageNum, int pageSize, String sortField, String sortOrder) {
        JsonObjects<com.github.lany192.domain.OauthClient> jsonObjects = new JsonObjects<>();
        Sort sort;
        if (StringUtils.equalsIgnoreCase(sortOrder, "asc")) {
            sort = new Sort(Sort.Direction.ASC, sortField);
        } else {
            sort = new Sort(Sort.Direction.DESC, sortField);
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<OauthClient> page = oauthClientRepository.findAll(pageable);
        if (page.getContent() != null && page.getContent().size() > 0) {
            jsonObjects.setRecordsTotal(page.getTotalElements());
            jsonObjects.setRecordsFiltered(page.getTotalElements());
            page.getContent().forEach(u -> jsonObjects.getData().add(dozerMapper.map(u, com.github.lany192.domain.OauthClient.class)));
        }
        return jsonObjects;
    }

    @Override
    public com.github.lany192.domain.OauthClient create(com.github.lany192.domain.OauthClient oauthClient) throws AlreadyExistsException {
        OauthClient exist = oauthClientRepository.findByClientId(oauthClient.getClientId());
        if (exist != null) {
            throw new AlreadyExistsException(oauthClient.getClientId() + " already exists!");
        }
        OauthClient oauthClientEntity = dozerMapper.map(oauthClient, OauthClient.class);
        oauthClientRepository.save(oauthClientEntity);
        return dozerMapper.map(oauthClientEntity, com.github.lany192.domain.OauthClient.class);
    }

    @Override
    public com.github.lany192.domain.OauthClient retrieveById(long id) throws EntityNotFoundException {
        Optional<OauthClient> entityOptional = oauthClientRepository.findById(id);
        return dozerMapper.map(entityOptional.orElseThrow(EntityNotFoundException::new), com.github.lany192.domain.OauthClient.class);
    }

    @Override
    public com.github.lany192.domain.OauthClient updateById(com.github.lany192.domain.OauthClient oauthClient) throws EntityNotFoundException {
        Optional<OauthClient> entityOptional = oauthClientRepository.findById(Long.parseLong(oauthClient.getId()));
        OauthClient e = entityOptional.orElseThrow(EntityNotFoundException::new);
        if (StringUtils.isNotEmpty(oauthClient.getClientSecret())) {
            e.setClientSecret(oauthClient.getClientSecret());
        }
        e.setAuthorities(oauthClient.getAuthorities());
        e.setScope(oauthClient.getScope());
        e.setAuthorizedGrantTypes(oauthClient.getAuthorizedGrantTypes());
        e.setWebServerRedirectUri(oauthClient.getWebServerRedirectUri());

        if (StringUtils.isNotEmpty(oauthClient.getRemarks())) {
            e.setRemarks(oauthClient.getRemarks());
        }

        oauthClientRepository.save(e);
        return dozerMapper.map(e, com.github.lany192.domain.OauthClient.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecordStatus(long id, int recordStatus) {
        Optional<OauthClient> entityOptional = oauthClientRepository.findById(id);
        OauthClient e = entityOptional.orElseThrow(EntityNotFoundException::new);
        e.setRecordStatus(recordStatus);
        oauthClientRepository.save(e);
    }
}
