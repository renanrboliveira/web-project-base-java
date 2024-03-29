package br.com.kamaleon.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.kamaleon.model.Authorities;

/**
 * Created by @author Renan Oliveira on 08/05/14
 */
public interface IAuthoritiesDAO extends PagingAndSortingRepository<Authorities, Integer> {
}
