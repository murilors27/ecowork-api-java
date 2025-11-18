package com.ecowork.repository;

import com.ecowork.models.MetaSustentavel;
import com.ecowork.models.Empresa;
import com.ecowork.models.enums.StatusMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetaSustentavelRepository extends JpaRepository<MetaSustentavel, Long> {

    List<MetaSustentavel> findByEmpresa(Empresa empresa);

    List<MetaSustentavel> findByStatus(StatusMeta status);
}