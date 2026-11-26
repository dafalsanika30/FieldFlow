package com.feildflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feildflow.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long>{
    List<Team> findByActiveTrue();

    boolean existsByTeamName(String teamName);
}
