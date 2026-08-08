package com.example.likelion14th_springboot.service;

import com.example.likelion14th_springboot.domain.Member;
import com.example.likelion14th_springboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> getAllMembers(){
        // ?? 아기사자가 채우기
        return memberRepository.findAll();
    }

    public Member getByEmail(String email){
        // ?? 아기사자가 채우기
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 회원이 없습니다."));
    }

    public Page<Member> getMembersByPage(int page, int size){
        // ?? 아기사자가 채우기
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return memberRepository.findAll(pageable);
    }
    //나이가 20이상인 member 조회, 이름을 기준으로 오름차순 정렬된 페이징 결과
    public Page<Member> getAdultMembersSortedByName(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return memberRepository.findByAgeGreaterThanEqualOrderByNameAsc(20, pageable);
    }

    public List<Member> getMembersByNamePrefix(String prefix) {
        return memberRepository.findByNameStartingWith(prefix);
    }
}