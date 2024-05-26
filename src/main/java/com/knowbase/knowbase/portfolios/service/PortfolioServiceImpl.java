package com.knowbase.knowbase.portfolios.service;

import com.knowbase.knowbase.domain.Portfolio;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.portfolios.dto.PortfolioCreateDto;
import com.knowbase.knowbase.portfolios.dto.PortfolioDeleteDto;
import com.knowbase.knowbase.portfolios.dto.PortfolioListDto;
import com.knowbase.knowbase.portfolios.dto.PortfolioUpdateDto;
import com.knowbase.knowbase.portfolios.repository.PortfolioRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class PortfolioServiceImpl implements PortfolioService{
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    //포트폴리오 작성
    @Override
    public ResponseEntity<CustomApiResponse<?>> createPortfolio(PortfolioCreateDto.Req portfolioCreateDto) {

        //게시글 작성자가 DB에 존재하는지 확인
        Optional<User> findUser = userRepository.findById(portfolioCreateDto.getUserId());
        if(findUser.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                            "해당 유저는 존재하지 않습니다."));
        }
        Portfolio newPortfolio = portfolioCreateDto.toEntity();
        newPortfolio.createPortfolio(findUser.get()); //연관관계 설정
        Portfolio savedPortfolio = portfolioRepository.save(newPortfolio);

        //응답 dto
        PortfolioCreateDto.CreatePortfolio createdPortfolioResponse = new PortfolioCreateDto.CreatePortfolio(savedPortfolio.getPortfolioId(),savedPortfolio.getCreateAt());
        //응답
        CustomApiResponse<PortfolioCreateDto.CreatePortfolio> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), createdPortfolioResponse,"포트폴리오가 작성되었습니다.");
        return ResponseEntity.ok(res);
    }

    //포트폴리오 수정
    @Override
    public ResponseEntity<CustomApiResponse<?>> updatePortfolio(Long portfolioId, PortfolioUpdateDto.Req portfolioUpdateDto) {
        //1.수정하려는 포트폴리오가 DB에 존재하는지 확인
        Optional<Portfolio> findPortfolio = portfolioRepository.findById(portfolioId);
        if (findPortfolio.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.BAD_REQUEST.value(),
                            "해당 포트폴리오는 존재하지 않습니다."));
        }

        //2.수정하려는 포트폴리오의 작성자와 수정 요청자가 같은지 확인
        if (!findPortfolio.get().getUserId().getUserId().equals(portfolioUpdateDto.getUserId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.FORBIDDEN.value(),
                            "해당 포트폴리오의 작성자가 아닙니다."));
        }

        //3.수정
        Portfolio portfolio = findPortfolio.get();
        portfolio.changeImagePath(portfolioUpdateDto.getPortfolioImagePath());

        //응답 DTO 생성
        PortfolioUpdateDto.UpdatePortfolio updatedPortfolioResponse = PortfolioUpdateDto.UpdatePortfolio.builder()
                .updatedAt(portfolio.getUpdateAt())
                .build();

        //응답
        CustomApiResponse<PortfolioUpdateDto.UpdatePortfolio> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), updatedPortfolioResponse, "포트폴리오가 수정되었습니다.");
        return ResponseEntity.ok(res);
    }

    //특정 유저의 모든 포트폴리오 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getPortfolio(Long userId) {
        //1.조회하려는 유저가 DB에 존재하는지 확인
        Optional<User> findUser = userRepository.findById(userId);

        //2.해당 유저의 모든 포트폴리오 조회
        List<Portfolio> findPortfolio = portfolioRepository.findByUserId(findUser.get());

        //응답 DTO 생성
        List<PortfolioListDto.PortfolioDto> portfolioResponse = new ArrayList<>();
        for(Portfolio portfolio : findPortfolio){
            PortfolioListDto.PortfolioDto portfolioDto = PortfolioListDto.PortfolioDto.builder()
                    .portfolioId(portfolio.getPortfolioId())
                    .portfolioImagePath(portfolio.getPortfolioImagePath())
                    .updatedAt(portfolio.getUpdateAt())
                    .build();
            portfolioResponse.add(portfolioDto);
        }

        //응답
        PortfolioListDto.SearchPortfoliosRes searchPortfoliosRes = new PortfolioListDto.SearchPortfoliosRes(portfolioResponse);
        CustomApiResponse<PortfolioListDto.SearchPortfoliosRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchPortfoliosRes,"포트폴리오가 조회되었습니다.");
        return ResponseEntity.ok(res);
    }

    //특정 포트폴리오 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getPortfolioDetail(Long portfolioId) {
        //1.조회하려는 포트폴리오가 DB에 존재하는지 확인
        Optional<Portfolio> findPortfolio = portfolioRepository.findById(portfolioId);
        if (findPortfolio.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.BAD_REQUEST.value(),
                            "해당 포트폴리오는 존재하지 않습니다."));
        }

        //응답 DTO 생성
        PortfolioListDto.PortfolioDto portfolioResponse = PortfolioListDto.PortfolioDto.builder()
                .portfolioId(findPortfolio.get().getPortfolioId())
                .portfolioImagePath(findPortfolio.get().getPortfolioImagePath())
                .updatedAt(findPortfolio.get().getUpdateAt())
                .build();

        //응답
        CustomApiResponse<PortfolioListDto.PortfolioDto> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), portfolioResponse, "포트폴리오 조회 성공");
        return ResponseEntity.ok(res);
    }

    //포트폴리오 삭제
    @Override
    public ResponseEntity<CustomApiResponse<?>> deletePortfolio(PortfolioDeleteDto portfolioDeleteDto) {
        //1.삭제하려는 포트폴리오가 DB에 존재하는지 확인
        Optional<Portfolio> findPortfolio = portfolioRepository.findById(portfolioDeleteDto.getPortfolioId());
        if (findPortfolio.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.BAD_REQUEST.value(),
                            "해당 포트폴리오는 존재하지 않습니다."));
        }

        //포트폴리오 사용자와 현재 접속한 사용자가 같은지 확인
        if (!findPortfolio.get().getUserId().getUserId().equals(portfolioDeleteDto.getUserId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.FORBIDDEN.value(),
                            "해당 포트폴리오의 작성자가 아닙니다."));
        }

        //3.삭제
        portfolioRepository.delete(findPortfolio.get());

        //응답
        CustomApiResponse<?> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "포트폴리오가 삭제되었습니다.");
        return ResponseEntity.ok(res);
    }
}
