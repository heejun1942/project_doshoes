package com.module.basic.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.module.basic.model.Board;
import com.module.basic.model.Client;
import com.module.basic.repository.BoardRepository;

@Controller
public class BoardController {
	@Autowired
	BoardRepository boardRepository;

	@Autowired
	HttpSession session;

	@GetMapping("/board/write")
	public String boardWrite() {
		return "board/write";
	}

	@PostMapping("/board/write")
	public String boardWritePost(@ModelAttribute Board board) {
		Client user = (Client) session.getAttribute("user_info");
		String userId = user.getEmail();
		board.setUserId(userId);
		board.setCreDate(new Date());

		boardRepository.save(board);
		return "redirect:/board";
	}

	@GetMapping("/board")
	public String board(Model model) {
		List<Board> list = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("list", list);
		return "board/list";

	}

	@GetMapping("/board/{id}")
	public String boardView(Model model, @PathVariable("id") long id) {
		Optional<Board> data = boardRepository.findById(id);
		Board board = data.get();
		model.addAttribute("board", board);
		return "board/view";
	}

	@GetMapping("/board/update/{id}")
	public String boardUpdate(Model model, @PathVariable("id") long id) {

		Optional<Board> data = boardRepository.findById(id);
		Board board = data.get();

		Client user = (Client) session.getAttribute("user_info");
		if (user.getEmail().equals(board.getUserId())) {

			model.addAttribute("board", board);
			return "board/update";
		} else {
			return "not_user";
		}

	}

	@PostMapping("/board/update/{id}")
	public String boardUpdatePost(@ModelAttribute Board board, @PathVariable("id") long id) {
		Client user = (Client) session.getAttribute("user_info");

		String userId = user.getEmail();
		board.setUserId(userId);
		board.setId(id);
		boardRepository.save(board);
		return "redirect:/board";
	}

	@GetMapping("/board/delete/{id}")
	public String boardDelete(@PathVariable("id") long id) {
		// 1. 로그인된 사용자의 정보 알아내기
		Client user = (Client) session.getAttribute("user_info");
		// 2. 게시물 작성자의 정보 알아내기
		Optional<Board> opt = boardRepository.findById(id);
		Board board = opt.get();
		if (user.getEmail().equals(board.getUserId())) {
			boardRepository.deleteById(id);
		} else {
			// 사용자가 일치하지 않음
			return "not_user";
		}

		return "redirect:/board";
	}
}