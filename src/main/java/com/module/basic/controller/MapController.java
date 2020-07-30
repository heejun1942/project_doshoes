package com.module.basic.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.module.basic.model.Point;
import com.module.basic.repository.PointRepository;

@Controller
public class MapController {
	@Autowired
	PointRepository pointRepository;

	@GetMapping("/map")
	public String map(Model model) {
		return "map/map";
	}

	@GetMapping("/map/getPoint")
	@ResponseBody
	public List<Point> getPoint(double latitude, double longitude) {
		List<Point> list = pointRepository.findAll();
		List<Point> Comlist = new ArrayList<>();
		
		double[][] lank;
		lank = new double[list.size()][3];
		
		for (int i = 0; i < list.size(); i++) {
			double dist = distance(latitude, longitude, list.get(i).getLatitude(), list.get(i).getLongitude());
			lank[i][0] = dist;
			lank[i][1] = list.get(i).getLatitude();
			lank[i][2] = list.get(i).getLongitude();
		} // for

		for (int i = 0; i < lank.length; i++) {
			for (int j = i + 1; j < lank.length; j++) {
				if (lank[i][0] > lank[j][0]) {
					double[] temp = lank[i];
					lank[i] = lank[j];
					lank[j] = temp;
				} // if
			} // inner for
		} // outer for

		// 정렬된 배열값과 비교하여 동일하면 리스트 반환할 예정
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < list.size(); j++) {
				if (lank[i][1] == list.get(j).getLatitude()) {
					if (lank[i][2] == list.get(j).getLongitude()) {
						Comlist.add(list.get(j));
					} // if
				} // if
			} // for
		} // for

		return Comlist;
	}

	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return dist;
	}

	private double deg2rad(double deg) {
		return deg * Math.PI / 180.0;
	}

	private double rad2deg(double rad) {
		return rad * 180 / Math.PI;
	}
}
