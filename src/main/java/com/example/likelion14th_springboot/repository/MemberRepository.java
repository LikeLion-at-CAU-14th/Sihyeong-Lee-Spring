package com.example.likelion14th_springboot.repository;

import com.example.likelion14th_springboot.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>  {
        // ?? 아기사자가 채우기
        Optional<Member> findByEmail(String email);

        }