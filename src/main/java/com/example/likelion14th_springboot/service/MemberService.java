package com.example.likelion14th_springboot.service;

import com.example.likelion14th_springboot.domain.Member;
import com.example.likelion14th_springboot.dto.request.JoinRequestDto;
import com.example.likelion14th_springboot.exception.DuplicateNameException;
import com.example.likelion14th_springboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        // ?? 아기사자가 채우기
        return memberRepository.findAll();
    }

    public Member getByEmail(String email) {
        // ?? 아기사자가 채우기
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 회원이 없습니다."));
    }

    public Page<Member> getMembersByPage(int page, int size) {
        // ?? 아기사자가 채우기
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return memberRepository.findAll(pageable);
    }

    //나이가 20이상인 member 조회, 이름을 기준으로 오름차순 정렬된 페이징 결과
    public Page<Member> getAdultMembersSortedByName(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return memberRepository.findByAgeGreaterThanEqualOrderByNameAsc(20, pageable);
    }

    public List<Member> getMembersByNamePrefix(String prefix) {
        return memberRepository.findByNameStartingWith(prefix);
    }

    // 비밀번호 인코더 DI(생성자 주입)
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(JoinRequestDto joinRequestDto) {
        // 해당 name이 이미 존재하는 경우
        if (memberRepository.existsByName(joinRequestDto.getName())) {
            throw new DuplicateNameException("이미 사용 중인 이름입니다.");
        }

        // 유저 객체 생성
        Member member = joinRequestDto.toEntity(bCryptPasswordEncoder);

        // 유저 정보 저장
        memberRepository.save(member);
    }
}