package com.module.basic.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.module.basic.model.Booking;
import com.module.basic.model.Client;
import com.module.basic.repository.BookingRepository;
import com.module.basic.repository.ClientRepository;
import com.module.basic.repository.PointRepository;

@Controller
public class BookingController {
	@Autowired
	ClientRepository clientRepository;

	@Autowired
	BookingRepository bookingRepository;

	@Autowired
	PointRepository pointRepository;

	@Autowired
	HttpSession session;

	@GetMapping("/")
	public String booking(Model model) {

		return "index";
	}

	@PostMapping("/reservation/post")
	public String postBooking(@ModelAttribute Client client, @ModelAttribute Booking booking,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		if (session.getAttribute("user_info") == null) {
			out.println("<script> alert('로그인이 필요합니다!'); location.href='/signin'; </script>");
			out.flush();
		}
		// 오늘 날짜
		LocalDate today = LocalDate.now();
		int day = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth();

		// booking DB에 저장
		Client user = (Client) session.getAttribute("user_info");
		booking.userId = user.getEmail();
		booking.time = client.bookingTime;
		booking.day = day;

		bookingRepository.save(booking);

		return "redirect:/mypage";
	}

	@GetMapping("/reservation/store/{storeCode}")
	public String page(@PathVariable("storeCode") long storeCode, Model model) {

		// 오늘 날짜
		LocalDate today = LocalDate.now();
		int day = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth();

		// DB 데이터 가져오기
		List<Integer> booking = bookingRepository.findByStoreCodeAndDay(storeCode, day);
		model.addAttribute("booking", booking);

		// 현재 시간 보내기
		LocalTime nowTime = LocalTime.now();
		int nowHour = nowTime.getHour();
		model.addAttribute("nowHour", nowHour);
		return "reservation/store";
	}

}
