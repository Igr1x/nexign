package ru.varnavskii.nexign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.varnavskii.nexign.entity.CDREntity;

@Repository
public interface CDRRepository extends JpaRepository<CDREntity, Long> {
}
