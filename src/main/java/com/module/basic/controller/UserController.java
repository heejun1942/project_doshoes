package com.module.basic.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.module.basic.model.Booking;
import com.module.basic.model.Client;
import com.module.basic.repository.BookingRepository;
import com.module.basic.repository.ClientRepository;

@Controller
public class UserController {
	@Autowired
	ClientRepository ClientRepository;
	@Autowired
	BookingRepository BookingRepository;

	@Autowired
	HttpSession session;

	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}

	@PostMapping("/signup")
	public String signupPost(@ModelAttribute Client user) {
		ClientRepository.save(user);
		return "/signin";
	}

	@GetMapping("/signin")
	public String signin() {
		return "signin";
	}

	@PostMapping("/signin")
	public String signinPost(@ModelAttribute Client user, HttpServletResponse response) throws IOException {
		Client dbUser = ClientRepository.findByEmailAndPwd(user.getEmail(), user.getPwd());

		if (dbUser != null) {
			session.setAttribute("user_info", dbUser);
			return "redirect:/";
		} else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script> alert('로그인 정보가 틀립니다.'); location.href='/signin'; </script>");
			out.flush();
			return "redirect:/signin";
		}
	}

	@GetMapping("/signout")
	public String signout() {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/mypage")
	public String mypage(Model model) {
		Client user = (Client) session.getAttribute("user_info");

		if ((Client) session.getAttribute("user_info") == null) {
			return "signin";
		} // 로그인이 안되어 있으면 로그인 페이지로
		else {
			 List<Booking> bookingList = BookingRepository.findbyUserId(user.getEmail());
			model.addAttribute("bookingList", bookingList);// thymeleaf방식으로 값 넘겨주기
			return "mypage";
		}
	}

	@GetMapping("/mypage/getBooking")
	@ResponseBody
	public List<Booking> getBooking(Model model) {
		Client user = (Client) session.getAttribute("user_info");
		List<Booking> dbBooking = BookingRepository.findAll();
		List<Booking> result = new ArrayList<>();

		if (user == null) {

		} // 로그인이 안되어 있으면 로그인 페이지로
		else {
			for (int i = 0; i < dbBooking.size(); i++) {
				if (user.getEmail() == dbBooking.get(i).getUserId()) {
					result.add(dbBooking.get(i));
					System.out.println(result + "##################");
				} // if
				else {
					System.out.println(i + " 번째 Booking DB 검색중..");
				}
			} // for
		} // 로그인이 되어 있으면

		return result;// ajax방식으로 넘겨주려고 리턴
	}// getBooking

}