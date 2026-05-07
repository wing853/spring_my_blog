package com.example.myblog.board;

import com.example.myblog._core.errors.Exception403;
import com.example.myblog._core.errors.Exception404;
import com.example.myblog.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service // IoC 처리
@RequiredArgsConstructor // DI 처리
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 저장
    @Transactional
    public void save(BoardRequest.SaveDTO saveDTO, User sessionUser) {
        log.info("게시글 저장 서비스 시작 - 제목 : {}, 작성자 : {}",
                saveDTO.getTitle(), sessionUser.getUsername());
        Board board = saveDTO.toEntity(sessionUser);

        Board savedBoardEntity = boardRepository.save(board);
        log.info("게시글 저장 완료 - ID : {}, 제목 : {}",
                savedBoardEntity.getId(), savedBoardEntity.getTitle());
    }

    // 게시글 목록 조회
    public List<BoardResponse.ListDTO> findAll() {
        log.info("게시글 목록 조회 서비스");
        List<Board> boardList = boardRepository.findAllJoinUser();
        log.info("게시글 목록 조회 완료 - 총 : {}", boardList.size());
        return boardList.stream()
                .map(BoardResponse.ListDTO::new)
                .collect(Collectors.toList());
    }

    // 게시글 상세 보기
    public BoardResponse.DetailDTO findById(Integer id) {
        log.info("게시글 상세 조회 서비스");
        Board boardEntity = boardRepository.findByIdJoinUser(id).orElseThrow(() -> {
            log.warn("게시글 조회 실패 - ID: {}", id);
            return new Exception404("해당하는 게시글을 찾을 수 없습니다");
        });

        log.info("게시글 조회 완료 - 제목: {}, 작성자: {}",
                boardEntity.getTitle(), boardEntity.getUser().getUsername());
        return new BoardResponse.DetailDTO(boardEntity);
    }

    /**
     * 게시글 수정 화면 요청(인가 처리)
     *
     * @param id          (Board PK)
     * @param sessionUser (로그인한 사용자 정보)
     * @return Board
     */
    public BoardResponse.DetailDTO findByIdAndCheckOwner(Integer id, User sessionUser) {
        log.info("게시글 수정 화면 조회 서비스");
        BoardResponse.DetailDTO detailDTO = findById(id);
        if(!detailDTO.getUserId().equals(sessionUser.getId())) {
            throw new Exception403("권한 없음");
        }
        log.info("게시글 수정 조회 완료 - 제목: {}, 작성자: {}",
                detailDTO.getTitle(), detailDTO.getUsername());
        return detailDTO;
    }

    // 게시글 수정
    @Transactional
    public void updateById(Integer id, BoardRequest.UpdateDTO updateDTO, User sessionUser) {
        log.info("게시글 수정 서비스");
        Board boardEntity = boardRepository.findByIdJoinUser(id).orElseThrow(() -> {
            throw new Exception404("해당 게시글을 찾을 수 없습니다");
        });

        boardEntity.update(updateDTO);

        log.info("게시글 수정 완료 - ID : {}, 새 제목: {}",
                boardEntity.getId(), boardEntity.getTitle());
    }

    // 게시글 삭제 (권한 체크 포함)
    @Transactional
    public void deleteById(Integer id, User sessionUser) {

        log.info("게시글 삭제 서비스");
        Board boardEntity = boardRepository.findById(id).orElseThrow(
                () -> new Exception404("게시글을 찾을 수 없습니다"));
        boardEntity.isOwner(sessionUser.getId());
        boardRepository.deleteById(id);
        log.info("게시글 삭제 완료 - ID : {}", id);
    }


}


