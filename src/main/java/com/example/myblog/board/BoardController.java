package com.example.myblog.board;

import com.example.myblog.user.User;
import com.example.myblog.util.PageDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardNativeRepository boardNativeRepository;
    private final BoardPersistRepository boardPersistRepository;
    private final HttpSession session;

    // http://localhost/board/save-form
    @GetMapping("/board/save-form")
    public String saveForm() {

        return "/board/save-form";
    }

    @PostMapping("/board/save")
    public String saveProc(BoardRequest.SaveDTO saveDTO) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        if(sessionUser == null) {
            return "redirect:user/login-form";
        }

        saveDTO.validate();

        boardPersistRepository.save(saveDTO.toEntity(sessionUser));
        return "redirect:/";
    }

    @GetMapping({"/", "index"})
    public String list(Model model, @RequestParam(defaultValue = "0") int page) {
        // 1. 한 페이지에 보여줄 게시글 수
        int size = 5;

        // 2. 해당 페이지의 데이터 가져오기 (Repository에 findAll(page, size)가 구현되어 있어야 함)
        List<Board> boardList = boardPersistRepository.findAll(page, size);

        // 3. 전체 게시글 수 및 전체 페이지 수 계산
        Long totalCount = boardPersistRepository.count(); // 전체 레코드 수
        int totalPages = (int) Math.ceil((double) totalCount / size);

        // 4. 화면에 뿌릴 페이지 번호 DTO 리스트 생성
        List<PageDTO> pageNumbers = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            // (실제데이터값, 화면표시값, 활성화여부)
            pageNumbers.add(new PageDTO(i, i + 1, i == page));
        }

        // 5. 모델에 데이터 담기
        model.addAttribute("boardList", boardList);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("prevPage", page - 1);
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("first", page == 0);
        model.addAttribute("last", page >= totalPages - 1);

        return "/board/list";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Integer id, Model model) {
        Board board = boardPersistRepository.findById(id);
        model.addAttribute("board", board);

        return "/board/detail";
    }

    @PostMapping("/board/{id}/delete")
    public String deleteProc(@PathVariable(name="id")Integer id) {
        boardPersistRepository.deleteById(id);
        return "redirect:/";
    }


    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name ="id") Integer id, Model model) {
        Board board = boardPersistRepository.findById(id);
        model.addAttribute("board",board);
        return "/board/update-form";
    }

    @PostMapping("/board/{id}/update")
    public String updateProc(@PathVariable(name = "id") Integer id,
                             BoardRequest.UpdateDTO updateDTO){


        updateDTO.validate();

        boardPersistRepository.updateById(id, updateDTO);
        return "redirect:/board/" + id;
    }
}
