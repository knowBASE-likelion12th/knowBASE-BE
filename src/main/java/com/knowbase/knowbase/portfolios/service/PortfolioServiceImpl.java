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
import com.knowbase.knowbase.util.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService{
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final S3UploadService s3UploadService;

    //포트폴리오 작성
    @Override
    public ResponseEntity<CustomApiResponse<?>> createPortfolio(PortfolioCreateDto portfolioCreateDto, MultipartFile portfolioImg) {
        try {
            // 게시글 작성자가 DB에 존재하는지 확인
            Optional<User> findUser = userRepository.findById(portfolioCreateDto.getUserId());
            if (findUser.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다."));
            }

            // S3에 파일 업로드
            String portfolioImagePath = s3UploadService.saveFile(portfolioImg);

            Portfolio newPortfolio = portfolioCreateDto.toEntity(portfolioImagePath);
            newPortfolio.createPortfolio(findUser.get()); // 연관관계 설정
            portfolioRepository.save(newPortfolio);

            // 응답
            CustomApiResponse<PortfolioCreateDto> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "포트폴리오가 작성되었습니다.");
            return ResponseEntity.ok(res);
        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }


    //포트폴리오 수정
    @Override
    public ResponseEntity<CustomApiResponse<?>> updatePortfolio(Long portfolioId, PortfolioUpdateDto portfolioUpdateDto, MultipartFile portfolioImg) {
        try {
            // 1. 수정하려는 포트폴리오가 DB에 존재하는지 확인
            Optional<Portfolio> findPortfolio = portfolioRepository.findById(portfolioId);
            if (findPortfolio.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(CustomApiResponse.createFailWithout(
                                HttpStatus.BAD_REQUEST.value(),
                                "해당 포트폴리오는 존재하지 않습니다."));
            }

            // 2. 수정하려는 포트폴리오의 작성자와 수정 요청자가 같은지 확인
            Portfolio portfolio = findPortfolio.get();
            if (!portfolio.getUserId().getUserId().equals(portfolioUpdateDto.getUserId())) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(CustomApiResponse.createFailWithout(
                                HttpStatus.FORBIDDEN.value(),
                                "해당 포트폴리오의 작성자가 아닙니다."));
            }

            // 4. 새 이미지 업로드
            String newImagePath = s3UploadService.saveFile(portfolioImg);
            portfolio.changeImagePath(newImagePath);

            // 5. 저장
            portfolioRepository.save(portfolio);

            // 응답
            CustomApiResponse<PortfolioUpdateDto> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "포트폴리오가 수정되었습니다.");
            return ResponseEntity.ok(res);
        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }

    //특정 유저의 포트폴리오 조회(모든 유저가 조회 가능)
    @Override
    public ResponseEntity<CustomApiResponse<?>> getPortfolio(Long userId) {
        try {
            // 조회하려는 유저가 DB에 존재하는지 확인
            Optional<User> findUser = userRepository.findById(userId);
            if (findUser.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다."));
            }

            // 해당 유저의 모든 포트폴리오 조회
            List<Portfolio> findPortfolio = portfolioRepository.findByUserId(findUser.get());

            // 응답 DTO 생성
            List<PortfolioListDto.PortfolioDto> portfolioResponse = new ArrayList<>();
            for(Portfolio portfolio : findPortfolio){
                PortfolioListDto.PortfolioDto portfolioDto = PortfolioListDto.PortfolioDto.builder()
                        .portfolioId(portfolio.getPortfolioId())
                        .portfolioImagePath(portfolio.getPortfolioImagePath())
                        .build();
                portfolioResponse.add(portfolioDto);
            }

            // 응답
            PortfolioListDto.SearchPortfoliosRes searchPortfoliosRes = new PortfolioListDto.SearchPortfoliosRes(portfolioResponse);
            CustomApiResponse<PortfolioListDto.SearchPortfoliosRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchPortfoliosRes,"포트폴리오가 조회되었습니다.");
            return ResponseEntity.ok(res);
        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }

    //포트폴리오 상세 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getPortfolioDetail(Long portfolioId) {
        try {
            // 조회하려는 포트폴리오가 DB에 존재하는지 확인
            Optional<Portfolio> findPortfolio = portfolioRepository.findById(portfolioId);
            if (findPortfolio.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(
                                HttpStatus.NOT_FOUND.value(),
                                "존재하지 않는 포트폴리오입니다."));
            }

            // 응답 DTO 생성
            PortfolioListDto.PortfolioDto portfolioResponse = PortfolioListDto.PortfolioDto.builder()
                    .portfolioId(findPortfolio.get().getPortfolioId())
                    .portfolioImagePath(findPortfolio.get().getPortfolioImagePath())
                    .build();

            // 응답
            CustomApiResponse<PortfolioListDto.PortfolioDto> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), portfolioResponse, "포트폴리오 조회 성공");
            return ResponseEntity.ok(res);
        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }

    @Override
    public ResponseEntity<CustomApiResponse<?>> deletePortfolio(PortfolioDeleteDto portfolioDeleteDto) {
        try {
            // 삭제하려는 포트폴리오가 DB에 존재하는지 확인
            Optional<Portfolio> findPortfolio = portfolioRepository.findById(portfolioDeleteDto.getPortfolioId());
            if (findPortfolio.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(CustomApiResponse.createFailWithout(
                                HttpStatus.BAD_REQUEST.value(),
                                "해당 포트폴리오는 존재하지 않습니다."));
            }

            Portfolio portfolio = findPortfolio.get();

            // 포트폴리오 사용자와 현재 접속한 사용자가 같은지 확인
            if (!portfolio.getUserId().getUserId().equals(portfolioDeleteDto.getUserId())) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(CustomApiResponse.createFailWithout(
                                HttpStatus.FORBIDDEN.value(),
                                "해당 포트폴리오의 작성자가 아닙니다."));
            }

            // 3. 기존 이미지 삭제
            String originalFilename = portfolio.getPortfolioImagePath();
            s3UploadService.deleteImage(originalFilename);

            // 4. 포트폴리오 삭제
            portfolioRepository.delete(portfolio);

            // 응답
            CustomApiResponse<?> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "포트폴리오가 삭제되었습니다.");
            return ResponseEntity.ok(res);
        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }

}
